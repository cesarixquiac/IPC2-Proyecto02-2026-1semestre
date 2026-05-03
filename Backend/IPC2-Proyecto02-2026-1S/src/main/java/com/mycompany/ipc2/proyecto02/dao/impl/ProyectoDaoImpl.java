/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao.impl;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dao.ProyectoDAO;
import com.mycompany.ipc2.proyecto02.model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProyectoDaoImpl implements ProyectoDAO {

    @Override
    public Proyecto publicarProyectoConHabilidades(Proyecto proyecto, List<Integer> habilidades) {
        String sqlProyecto = "INSERT INTO Proyecto (id_cliente, id_categoria, titulo, descripcion, presupuesto_maximo, fecha_limite, estado) VALUES (?, ?, ?, ?, ?, ?, 'ABIERTO')";
        String sqlHabilidades = "INSERT INTO Proyecto_Habilidad (id_proyecto, id_habilidad) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement stmtProyecto = conn.prepareStatement(sqlProyecto, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtHabilidades = conn.prepareStatement(sqlHabilidades)) {

                // 1. Insertar el Proyecto
                stmtProyecto.setInt(1, proyecto.getIdCliente());
                stmtProyecto.setInt(2, proyecto.getIdCategoria());
                stmtProyecto.setString(3, proyecto.getTitulo());
                stmtProyecto.setString(4, proyecto.getDescripcion());
                stmtProyecto.setDouble(5, proyecto.getPresupuestoMaximo());
                stmtProyecto.setDate(6, Date.valueOf(proyecto.getFechaLimite()));

                int filasAfectadas = stmtProyecto.executeUpdate();
                if (filasAfectadas == 0) {
                    conn.rollback();
                    return null;
                }

                // Obtener el ID generado
                int idGenerado;
                try (ResultSet rs = stmtProyecto.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        proyecto.setIdProyecto(idGenerado);
                        proyecto.setEstado("ABIERTO");
                    } else {
                        conn.rollback();
                        return null;
                    }
                }

                // 2. Insertar las habilidades requeridas (Batch)
                for (Integer idHabilidad : habilidades) {
                    stmtHabilidades.setInt(1, idGenerado);
                    stmtHabilidades.setInt(2, idHabilidad);
                    stmtHabilidades.addBatch();
                }
                stmtHabilidades.executeBatch();

                conn.commit(); // Confirmar transacción
                return proyecto;

            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public List<Map<String, Object>> obtenerPropuestasPorProyecto(int idProyecto) {
        List<Map<String, Object>> propuestas = new ArrayList<>();
        
        // Agregamos p.plazo_dias y p.carta_presentacion a la consulta
        String sql = "SELECT p.id_propuesta, u.nombre_completo AS nombre_freelancer, " +
                     "p.monto_ofertado, p.plazo_dias, " +
                     "p.carta_presentacion, p.estado " +
                     "FROM propuesta p " +
                     "INNER JOIN usuario u ON p.id_freelancer = u.id_usuario " +
                     "INNER JOIN freelancer f ON p.id_freelancer = f.id_freelancer " +
                     "WHERE p.id_proyecto = ? " +
                     "ORDER BY p.monto_ofertado ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProyecto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> prop = new HashMap<>();
                    prop.put("idPropuesta", rs.getInt("id_propuesta"));
                    prop.put("nombreFreelancer", rs.getString("nombre_freelancer"));
                    prop.put("montoOfertado", rs.getDouble("monto_ofertado"));
                    prop.put("plazoDias", rs.getInt("plazo_dias")); // Ajustado al nombre real
                    prop.put("cartaPresentacion", rs.getString("carta_presentacion")); // <-- Nuevo
                    prop.put("estado", rs.getString("estado"));
                    
                    propuestas.add(prop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return propuestas;
    }
    
    
    @Override
    public void aceptarPropuesta(int idProyecto, int idPropuesta, int idCliente) throws Exception {
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciamos la transacción

            // 1. Obtener el monto de la propuesta
            String sqlMonto = "SELECT monto_ofertado FROM propuesta WHERE id_propuesta = ?";
            double montoOfertado = 0;
            try (PreparedStatement stmt = conn.prepareStatement(sqlMonto)) {
                stmt.setInt(1, idPropuesta);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) montoOfertado = rs.getDouble("monto_ofertado");
                    else throw new Exception("La propuesta no existe.");
                }
            }

            // 2. Verificar el saldo del cliente
            String sqlSaldo = "SELECT saldo_disponible FROM cliente WHERE id_cliente = ?";
            double saldoCliente = 0;
            try (PreparedStatement stmt = conn.prepareStatement(sqlSaldo)) {
                stmt.setInt(1, idCliente);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) saldoCliente = rs.getDouble("saldo_disponible");
                    else throw new Exception("Cliente no encontrado.");
                }
            }

            // 3. Validar si tiene dinero suficiente
            if (saldoCliente < montoOfertado) {
                throw new Exception("Saldo insuficiente. Por favor, recarga tu cuenta.");
            }

            // 4. Descontar el saldo al cliente
            String sqlRestar = "UPDATE cliente SET saldo_disponible = saldo_disponible - ? WHERE id_cliente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlRestar)) {
                stmt.setDouble(1, montoOfertado);
                stmt.setInt(2, idCliente);
                stmt.executeUpdate();
            }

            // 5. Cambiar el proyecto a EN_PROGRESO
            String sqlProy = "UPDATE proyecto SET estado = 'EN_PROGRESO' WHERE id_proyecto = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlProy)) {
                stmt.setInt(1, idProyecto);
                stmt.executeUpdate();
            }

            // 6. Marcar esta propuesta como ACEPTADA
            String sqlAceptar = "UPDATE propuesta SET estado = 'ACEPTADA' WHERE id_propuesta = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlAceptar)) {
                stmt.setInt(1, idPropuesta);
                stmt.executeUpdate();
            }

            // 7. Marcar las demás propuestas como RECHAZADAS
            String sqlRechazar = "UPDATE propuesta SET estado = 'RECHAZADA' WHERE id_proyecto = ? AND id_propuesta != ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlRechazar)) {
                stmt.setInt(1, idProyecto);
                stmt.setInt(2, idPropuesta);
                stmt.executeUpdate();
            }

            conn.commit(); // ¡Todo salió bien! Guardamos los cambios
        } catch (Exception e) {
            if (conn != null) conn.rollback(); // Si algo falla, revertimos TODO
            throw e; // Lanzamos el error para que el Servlet lo atrape
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public void rechazarPropuesta(int idPropuesta) throws Exception {
        String sql = "UPDATE propuesta SET estado = 'RECHAZADA' WHERE id_propuesta = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPropuesta);
            stmt.executeUpdate();
        }
    }

    // Métodos CrudDao restantes omitidos por brevedad
    @Override public Proyecto crear(Proyecto entidad) { return null; }
    @Override public Optional<Proyecto> obtenerPorId(Integer integer) { return Optional.empty(); }
    @Override public List<Proyecto> obtenerTodos() { return null; }
    @Override public boolean actualizar(Proyecto entidad) { return false; }
    @Override public boolean eliminar(Integer integer) { return false; }
}
