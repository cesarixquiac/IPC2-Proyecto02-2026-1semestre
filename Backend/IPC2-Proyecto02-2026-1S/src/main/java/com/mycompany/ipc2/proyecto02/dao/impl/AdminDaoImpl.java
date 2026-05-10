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
import com.mycompany.ipc2.proyecto02.dao.AdminDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDaoImpl implements AdminDAO {

    @Override
    public List<Map<String, Object>> obtenerSolicitudesPendientes() throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT s.id_solicitud, s.tipo_solicitud, s.nombre_sugerido, s.descripcion, s.estado, u.nombre_completo AS solicitante " +
                     "FROM solicitud_catalogo s " +
                     "JOIN usuario u ON s.id_usuario_solicitante = u.id_usuario " +
                     "WHERE s.estado = 'PENDIENTE' " +
                     "ORDER BY s.id_solicitud ASC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id_solicitud", rs.getInt("id_solicitud"));
                fila.put("tipo_solicitud", rs.getString("tipo_solicitud"));
                fila.put("nombre_sugerido", rs.getString("nombre_sugerido"));
                fila.put("descripcion", rs.getString("descripcion"));
                fila.put("estado", rs.getString("estado"));
                fila.put("solicitante", rs.getString("solicitante"));
                lista.add(fila);
            }
        }
        return lista;
    }

    @Override
    public boolean procesarSolicitud(int idSolicitud, String estadoAccion, Integer idCategoriaDestino) throws Exception {
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciamos Transacción

            // 1. Obtener los datos de la solicitud
            String tipoSolicitud = "";
            String nombreSugerido = "";
            String descripcion = "";
            
            String sqlSelect = "SELECT tipo_solicitud, nombre_sugerido, descripcion FROM solicitud_catalogo WHERE id_solicitud = ?";
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                stmtSelect.setInt(1, idSolicitud);
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (rs.next()) {
                        tipoSolicitud = rs.getString("tipo_solicitud");
                        nombreSugerido = rs.getString("nombre_sugerido");
                        descripcion = rs.getString("descripcion");
                    } else {
                        return false; // No existe la solicitud
                    }
                }
            }

            // 2. Actualizar el estado a ACEPTADA o RECHAZADA
            String sqlUpdate = "UPDATE solicitud_catalogo SET estado = ? WHERE id_solicitud = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, estadoAccion);
                stmtUpdate.setInt(2, idSolicitud);
                stmtUpdate.executeUpdate();
            }

            // 3. Si fue ACEPTADA, insertamos en el catálogo oficial
            if ("ACEPTADA".equals(estadoAccion)) {
                if ("CATEGORIA".equals(tipoSolicitud)) {
                    String sqlInsertCat = "INSERT INTO categoria (nombre, descripcion, estado) VALUES (?, ?, 'ACTIVO')";
                    try (PreparedStatement stmtInsertCat = conn.prepareStatement(sqlInsertCat)) {
                        stmtInsertCat.setString(1, nombreSugerido);
                        stmtInsertCat.setString(2, descripcion);
                        stmtInsertCat.executeUpdate();
                    }
                } else if ("HABILIDAD".equals(tipoSolicitud)) {
                    if (idCategoriaDestino == null) {
                        throw new Exception("Para aceptar una habilidad se requiere indicar a qué categoría pertenece.");
                    }
                    String sqlInsertHab = "INSERT INTO habilidad (id_categoria, nombre, estado) VALUES (?, ?, 'ACTIVO')";
                    try (PreparedStatement stmtInsertHab = conn.prepareStatement(sqlInsertHab)) {
                        stmtInsertHab.setInt(1, idCategoriaDestino);
                        stmtInsertHab.setString(2, nombreSugerido);
                        stmtInsertHab.executeUpdate();
                    }
                }
            }

            conn.commit(); // Confirmamos los cambios
            return true;
            
        } catch (Exception e) {
            if (conn != null) conn.rollback(); // Si algo falla, revertimos todo
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    // --- MÉTODOS DE COMISIÓN ---

   @Override
    public Double obtenerComisionActual() throws Exception {
        // La comisión activa es la que NO tiene fecha de fin
        String sql = "SELECT porcentaje FROM historial_comision WHERE fecha_fin IS NULL LIMIT 1";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("porcentaje");
            }
            return 0.0; // Si la tabla está vacía, devuelve 0
        }
    }

    @Override
    public boolean actualizarComision(double nuevoPorcentaje) throws Exception {
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciamos transacción

            // 1. Apagamos la comisión actual poniéndole la fecha de hoy como fin
            String sqlUpdate = "UPDATE historial_comision SET fecha_fin = CURDATE() WHERE fecha_fin IS NULL";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.executeUpdate();
            }

            // 2. Insertamos la nueva comisión solo con fecha de inicio (fecha_fin queda NULL por defecto)
            String sqlInsert = "INSERT INTO historial_comision (porcentaje, fecha_inicio) VALUES (?, CURDATE())";
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setDouble(1, nuevoPorcentaje);
                stmtInsert.executeUpdate();
            }

            conn.commit(); // Confirmamos los cambios
            return true;
            
        } catch (Exception e) {
            if (conn != null) conn.rollback(); // Revertimos si hay error
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    // --- MÉTODOS DE GESTIÓN DE CATEGORÍAS ---

    @Override
    public List<Map<String, Object>> obtenerCategoriasAdmin() throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, estado FROM categoria ORDER BY id_categoria DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id_categoria", rs.getInt("id_categoria"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("descripcion", rs.getString("descripcion"));
                fila.put("estado", rs.getString("estado"));
                lista.add(fila);
            }
        }
        return lista;
    }

    @Override
    public boolean crearCategoria(String nombre, String descripcion) throws Exception {
        String sql = "INSERT INTO categoria (nombre, descripcion, estado) VALUES (?, ?, 'ACTIVO')";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean editarCategoria(int idCategoria, String nombre, String descripcion) throws Exception {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, idCategoria);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstadoCategoria(int idCategoria, String nuevoEstado) throws Exception {
        String sql = "UPDATE categoria SET estado = ? WHERE id_categoria = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idCategoria);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // --- MÉTODOS DE REPORTES ---

    @Override
    public List<Map<String, Object>> reporteHistorialComisiones() throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT porcentaje, fecha_inicio, fecha_fin FROM historial_comision ORDER BY fecha_inicio DESC";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("porcentaje", rs.getDouble("porcentaje"));
                fila.put("fecha_inicio", rs.getString("fecha_inicio"));
                fila.put("fecha_fin", rs.getString("fecha_fin") != null ? rs.getString("fecha_fin") : "Vigente");
                lista.add(fila);
            }
        }
        return lista;
    }

    @Override
    public List<Map<String, Object>> reporteTopFreelancers(String fechaInicio, String fechaFin) throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Cambiamos c.estado por p.estado (usando el estado de la propuesta)
        String sql = "SELECT u.nombre_completo, COUNT(c.id_contrato) AS cantidad_contratos, " +
                     "SUM(c.monto_bloqueado - (c.monto_bloqueado * c.porcentaje_comision_aplicado / 100)) AS total_generado, " +
                     "SUM(c.monto_bloqueado * c.porcentaje_comision_aplicado / 100) AS comision_plataforma " +
                     "FROM contrato c " +
                     "JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                     "JOIN usuario u ON p.id_freelancer = u.id_usuario " +
                     "WHERE p.estado = 'ACEPTADA' AND c.fecha_inicio BETWEEN ? AND ? " +
                     "GROUP BY u.id_usuario, u.nombre_completo " +
                     "ORDER BY total_generado DESC LIMIT 5";
                     
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("nombre_completo", rs.getString("nombre_completo"));
                    fila.put("cantidad_contratos", rs.getInt("cantidad_contratos"));
                    fila.put("total_generado", rs.getDouble("total_generado"));
                    fila.put("comision_plataforma", rs.getDouble("comision_plataforma"));
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    @Override
    public List<Map<String, Object>> reporteTopCategorias(String fechaInicio, String fechaFin) throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Cambiamos c.estado por p.estado
        String sql = "SELECT cat.nombre AS categoria, COUNT(c.id_contrato) AS cantidad_contratos, " +
                     "SUM(c.monto_bloqueado * c.porcentaje_comision_aplicado / 100) AS comisiones_generadas " +
                     "FROM contrato c " +
                     "JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                     "JOIN proyecto proy ON p.id_proyecto = proy.id_proyecto " +
                     "JOIN categoria cat ON proy.id_categoria = cat.id_categoria " +
                     "WHERE p.estado = 'ACEPTADA' AND c.fecha_inicio BETWEEN ? AND ? " +
                     "GROUP BY cat.id_categoria, cat.nombre " +
                     "ORDER BY comisiones_generadas DESC LIMIT 5";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("categoria", rs.getString("categoria"));
                    fila.put("cantidad_contratos", rs.getInt("cantidad_contratos"));
                    fila.put("comisiones_generadas", rs.getDouble("comisiones_generadas"));
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    @Override
    public Map<String, Object> reporteIngresosPlataforma(String fechaInicio, String fechaFin) throws Exception {
        Map<String, Object> resultado = new HashMap<>();
        // Agregamos el JOIN con propuesta para poder leer su estado
        String sql = "SELECT COUNT(c.id_contrato) AS cantidad_contratos, " +
                     "COALESCE(SUM(c.monto_bloqueado * c.porcentaje_comision_aplicado / 100), 0) AS total_comisiones " +
                     "FROM contrato c " +
                     "JOIN propuesta p ON c.id_propuesta = p.id_propuesta " +
                     "WHERE p.estado = 'ACEPTADA' AND c.fecha_inicio BETWEEN ? AND ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    resultado.put("cantidad_contratos", rs.getInt("cantidad_contratos"));
                    resultado.put("total_comisiones", rs.getDouble("total_comisiones"));
                }
            }
        }
        return resultado;
    }
    
    public List<Map<String, Object>> obtenerTodasHabilidadesAdmin() throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Hacemos un JOIN con categoría para mostrar el nombre de la categoría en la tabla de Angular
        String sql = "SELECT h.id_habilidad, h.id_categoria, c.nombre AS nombre_categoria, h.nombre, h.estado " +
                     "FROM habilidad h JOIN categoria c ON h.id_categoria = c.id_categoria ORDER BY h.id_habilidad DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("id_habilidad", rs.getInt("id_habilidad"));
                fila.put("id_categoria", rs.getInt("id_categoria"));
                fila.put("nombre_categoria", rs.getString("nombre_categoria"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("estado", rs.getString("estado"));
                lista.add(fila);
            }
        }
        return lista;
    }

    public boolean crearHabilidad(int idCategoria, String nombre) throws Exception {
        String sql = "INSERT INTO habilidad (id_categoria, nombre, estado) VALUES (?, ?, 'ACTIVO')";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.setString(2, nombre);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean editarHabilidad(int idHabilidad, int idCategoria, String nombre) throws Exception {
        String sql = "UPDATE habilidad SET id_categoria = ?, nombre = ? WHERE id_habilidad = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCategoria);
            stmt.setString(2, nombre);
            stmt.setInt(3, idHabilidad);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean cambiarEstadoHabilidad(int idHabilidad, String nuevoEstado) throws Exception {
        String sql = "UPDATE habilidad SET estado = ? WHERE id_habilidad = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idHabilidad);
            return stmt.executeUpdate() > 0;
        }
    }
    
    
}
