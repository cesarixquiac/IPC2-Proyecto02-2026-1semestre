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
import java.util.List;
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

    // Métodos CrudDao restantes omitidos por brevedad
    @Override public Proyecto crear(Proyecto entidad) { return null; }
    @Override public Optional<Proyecto> obtenerPorId(Integer integer) { return Optional.empty(); }
    @Override public List<Proyecto> obtenerTodos() { return null; }
    @Override public boolean actualizar(Proyecto entidad) { return false; }
    @Override public boolean eliminar(Integer integer) { return false; }
}
