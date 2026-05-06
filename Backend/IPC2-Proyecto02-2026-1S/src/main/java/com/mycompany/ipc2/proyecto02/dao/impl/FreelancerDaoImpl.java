/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao.impl;
import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dao.FreelancerDao;
import com.mycompany.ipc2.proyecto02.model.Freelancer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author cesar
 */

public class FreelancerDaoImpl implements FreelancerDao {

    @Override
    public Freelancer crear(Freelancer freelancer) {
        String sql = "INSERT INTO Freelancer (id_freelancer, biografia, nivel_experiencia, tarifa_hora, saldo_acumulado) VALUES (?, ?, ?, ?, 0.00)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, freelancer.getIdFreelancer());
            stmt.setString(2, freelancer.getBiografia());
            stmt.setString(3, freelancer.getNivelExperiencia());
            stmt.setDouble(4, freelancer.getTarifaHora());
            
            if (stmt.executeUpdate() > 0) {
                freelancer.setSaldoAcumulado(0.00);
                return freelancer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existePerfil(Integer idFreelancer) {
        String sql = "SELECT 1 FROM Freelancer WHERE id_freelancer = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFreelancer);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean asignarHabilidades(Integer idFreelancer, List<Integer> habilidades) {
        // Utilizamos un "Batch" para insertar múltiples habilidades en una sola transacción eficiente
        String sql = "INSERT INTO Freelancer_Habilidad (id_freelancer, id_habilidad) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (Integer idHabilidad : habilidades) {
                stmt.setInt(1, idFreelancer);
                stmt.setInt(2, idHabilidad);
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean tienePerfilCompleto(int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM freelancer WHERE id_freelancer = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si devuelve algo, es true (ya tiene perfil)
            }
        }
    }
    
    @Override
    public double obtenerSaldo(int idFreelancer) throws Exception {
        double saldo = 0.00;
        // Seleccionamos la columna de tu base de datos (verifica si se llama saldo o saldo_acumulado)
        String sql = "SELECT saldo_acumulado FROM freelancer WHERE id_freelancer = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    saldo = rs.getDouble("saldo_acumulado");
                }
            }
        }
        return saldo;
    }
    
    @Override
    public List<Map<String, Object>> obtenerHistorialGanancias(int idFreelancer) throws Exception {
        List<Map<String, Object>> historial = new ArrayList<>();
        
        // Buscamos los proyectos finalizados de este freelancer, 
        // restándole la comisión para que vea su ganancia neta.
        String sql = 
            "SELECT p.titulo, c.monto_bloqueado, c.porcentaje_comision_aplicado, " +
            "       p.fecha_limite AS fecha_pago " +
            "FROM proyecto p " +
            "JOIN propuesta pr ON p.id_proyecto = pr.id_proyecto " +
            "JOIN contrato c ON pr.id_propuesta = c.id_propuesta " +
            "WHERE pr.id_freelancer = ? " +
            "AND pr.estado = 'ACEPTADA' " +
            "AND p.estado = 'COMPLETADO' " +
            "ORDER BY p.fecha_limite DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> ganancia = new HashMap<>();
                    ganancia.put("proyecto", rs.getString("titulo"));
                    
                    double montoBruto = rs.getDouble("monto_bloqueado");
                    double comision = rs.getDouble("porcentaje_comision_aplicado");
                    double montoNeto = montoBruto - (montoBruto * (comision / 100.0));
                    
                    ganancia.put("montoBruto", montoBruto);
                    ganancia.put("montoNeto", montoNeto); // Lo que realmente le llegó
                    ganancia.put("fecha", rs.getDate("fecha_pago").toString());
                    historial.add(ganancia);
                }
            }
        }
        return historial;
    }
    
    @Override
    public List<Map<String, Object>> obtenerReporteContratosCompletados(int idFreelancer, String fechaInicio, String fechaFin) throws Exception {
        List<Map<String, Object>> reporte = new ArrayList<>();
        
        String sql = 
            "SELECT u.nombre_completo AS cliente, p.titulo AS proyecto, " +
            "       (c.monto_bloqueado - (c.monto_bloqueado * (c.porcentaje_comision_aplicado / 100))) AS monto_recibido, " +
            "       cal.estrellas, cal.fecha_calificacion " +
            "FROM contrato c " +
            "JOIN propuesta pr ON c.id_propuesta = pr.id_propuesta " +
            "JOIN proyecto p ON pr.id_proyecto = p.id_proyecto " +
            "JOIN usuario u ON p.id_cliente = u.id_usuario " +
            "JOIN calificacion cal ON c.id_contrato = cal.id_contrato " +
            "WHERE pr.id_freelancer = ? " +
            "  AND p.estado = 'COMPLETADO' " +
            "  AND DATE(cal.fecha_calificacion) BETWEEN ? AND ? " +
            "ORDER BY cal.fecha_calificacion DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            stmt.setString(2, fechaInicio); // Formato esperado: "YYYY-MM-DD"
            stmt.setString(3, fechaFin);    // Formato esperado: "YYYY-MM-DD"
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("cliente", rs.getString("cliente"));
                    fila.put("proyecto", rs.getString("proyecto"));
                    fila.put("monto_recibido", rs.getDouble("monto_recibido"));
                    fila.put("calificacion", rs.getInt("estrellas"));
                    fila.put("fecha", rs.getString("fecha_calificacion"));
                    reporte.add(fila);
                }
            }
        }
        return reporte;
    }
    
    @Override
    public List<Map<String, Object>> obtenerReporteTopCategorias(int idFreelancer) throws Exception {
        List<Map<String, Object>> reporte = new ArrayList<>();
        
        String sql = 
            "SELECT cat.nombre AS categoria, " +
            "       COUNT(c.id_contrato) AS cantidad_contratos, " +
            "       SUM(c.monto_bloqueado - (c.monto_bloqueado * (c.porcentaje_comision_aplicado / 100))) AS total_ingresos " +
            "FROM categoria cat " +
            "JOIN proyecto p ON cat.id_categoria = p.id_categoria " +
            "JOIN propuesta pr ON p.id_proyecto = p.id_proyecto " +
            "JOIN contrato c ON pr.id_propuesta = c.id_propuesta " +
            "WHERE pr.id_freelancer = ? " +
            "  AND pr.estado = 'ACEPTADA' " +
            "GROUP BY cat.id_categoria, cat.nombre " +
            "ORDER BY cantidad_contratos DESC, total_ingresos DESC " +
            "LIMIT 5";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("categoria", rs.getString("categoria"));
                    fila.put("cantidad_contratos", rs.getInt("cantidad_contratos"));
                    fila.put("total_ingresos", rs.getDouble("total_ingresos"));
                    reporte.add(fila);
                }
            }
        }
        return reporte;
    }
    
    @Override
    public List<Map<String, Object>> obtenerReportePropuestasEnviadas(int idFreelancer, String fechaInicio, String fechaFin) throws Exception {
        List<Map<String, Object>> reporte = new ArrayList<>();
        
        String sql = 
            "SELECT p.titulo AS proyecto, pr.monto_ofertado, pr.estado, pr.fecha_envio " +
            "FROM propuesta pr " +
            "JOIN proyecto p ON pr.id_proyecto = p.id_proyecto " +
            "WHERE pr.id_freelancer = ? " +
            "  AND DATE(pr.fecha_envio) BETWEEN ? AND ? " +
            "ORDER BY pr.fecha_envio DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            stmt.setString(2, fechaInicio); // Formato esperado: "YYYY-MM-DD"
            stmt.setString(3, fechaFin);    // Formato esperado: "YYYY-MM-DD"
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("proyecto", rs.getString("proyecto"));
                    fila.put("monto_ofertado", rs.getDouble("monto_ofertado"));
                    fila.put("estado", rs.getString("estado"));
                    fila.put("fecha", rs.getString("fecha_envio"));
                    reporte.add(fila);
                }
            }
        }
        return reporte;
    }
    
    @Override
    public boolean solicitarNuevaHabilidad(int idFreelancer, String nombre, String descripcion) throws Exception {
        String sql = "INSERT INTO solicitud_catalogo (id_usuario_solicitante, tipo_solicitud, nombre_sugerido, descripcion, estado) " +
                     "VALUES (?, 'HABILIDAD', ?, ?, 'PENDIENTE')";
                     
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se insertó correctamente
        }
    }
    

    // Métodos heredados de CrudDao (obligatorios, aunque no los usemos ahora)
    @Override public Optional<Freelancer> obtenerPorId(Integer id) { return Optional.empty(); }
    @Override public List<Freelancer> obtenerTodos() { return null; }
    @Override public boolean actualizar(Freelancer entidad) { return false; }
    @Override public boolean eliminar(Integer id) { return false; }
}