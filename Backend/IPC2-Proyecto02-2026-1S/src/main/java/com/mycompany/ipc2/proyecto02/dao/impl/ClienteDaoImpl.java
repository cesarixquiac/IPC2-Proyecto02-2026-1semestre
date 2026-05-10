/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao.impl;
import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dao.ClienteDAO;
import com.mycompany.ipc2.proyecto02.model.Cliente;

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
public class ClienteDaoImpl implements ClienteDAO {

    @Override
    public Cliente crear(Cliente cliente) {
        String sql = "INSERT INTO Cliente (id_cliente, descripcion_empresa, sector_industria, sitio_web, saldo_disponible) VALUES (?, ?, ?, ?, 0.00)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliente.getIdCliente());
            stmt.setString(2, cliente.getDescripcionEmpresa());
            stmt.setString(3, cliente.getSectorIndustria());
            stmt.setString(4, cliente.getSitioWeb());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                cliente.setSaldoDisponible(0.00);
                return cliente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existePerfil(Integer idCliente) {
        String sql = "SELECT 1 FROM Cliente WHERE id_cliente = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean recargarSaldo(Integer idCliente, Double monto) {
        String sqlUpdateCliente = "UPDATE Cliente SET saldo_disponible = saldo_disponible + ? WHERE id_cliente = ?";
        String sqlInsertHistorial = "INSERT INTO Recarga_Saldo (id_cliente, monto) VALUES (?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción manual
            
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateCliente);
                 PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertHistorial)) {
                
                // 1. Actualizar saldo
                stmtUpdate.setDouble(1, monto);
                stmtUpdate.setInt(2, idCliente);
                int filasActualizadas = stmtUpdate.executeUpdate();
                
                if (filasActualizadas == 0) {
                    conn.rollback();
                    return false;
                }

                // 2. Registrar en historial
                stmtInsert.setInt(1, idCliente);
                stmtInsert.setDouble(2, monto);
                stmtInsert.executeUpdate();
                
                conn.commit(); // Confirmar transacción
                return true;
                
            } catch (SQLException ex) {
                conn.rollback(); // Revertir en caso de error en las consultas
                ex.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar comportamiento por defecto
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Cliente obtenerPerfil(Integer idCliente) {
        String sql = "SELECT id_cliente, descripcion_empresa, sector_industria, sitio_web, saldo_disponible FROM Cliente WHERE id_cliente = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setDescripcionEmpresa(rs.getString("descripcion_empresa"));
                    cliente.setSectorIndustria(rs.getString("sector_industria"));
                    cliente.setSitioWeb(rs.getString("sitio_web"));
                    cliente.setSaldoDisponible(rs.getDouble("saldo_disponible"));
                    return cliente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no lo encuentra
    }
    
    @Override
    public List<Map<String, Object>> obtenerHistorialRecargas(int idCliente) throws Exception {
        List<Map<String, Object>> historial = new ArrayList<>();
        
        String sql = "SELECT monto, fecha_recarga FROM recarga_saldo " +
                     "WHERE id_cliente = ? ORDER BY fecha_recarga DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> recarga = new HashMap<>();
                    recarga.put("monto", rs.getDouble("monto"));
                    recarga.put("fecha", rs.getDate("fecha_recarga").toString());
                    historial.add(recarga);
                }
            }
        }
        return historial;
    }
    
    // 1. Historial de proyectos (con estado, monto y freelancer)
    public List<Map<String, Object>> reporteHistorialProyectos(int idCliente, String fechaInicio, String fechaFin) throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        
        String sql = "SELECT p.titulo AS proyecto, prop.estado, c.monto_bloqueado AS monto, u.nombre_completo AS freelancer, c.fecha_inicio " +
                     "FROM proyecto p " +
                     "JOIN propuesta prop ON p.id_proyecto = prop.id_proyecto " +
                     "JOIN contrato c ON prop.id_propuesta = c.id_propuesta " +
                     "JOIN usuario u ON prop.id_freelancer = u.id_usuario " +
                     "WHERE p.id_cliente = ? AND c.fecha_inicio BETWEEN ? AND ? " +
                     "ORDER BY c.fecha_inicio DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("proyecto", rs.getString("proyecto"));
                    fila.put("estado", rs.getString("estado"));
                    fila.put("monto", rs.getDouble("monto"));
                    fila.put("freelancer", rs.getString("freelancer"));
                    fila.put("fecha_inicio", rs.getString("fecha_inicio"));
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    // 2. Historial de recargas (monto, fecha y hora)
    public List<Map<String, Object>> reporteHistorialRecargas(int idCliente) throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT monto, fecha_recarga FROM recarga_saldo WHERE id_cliente = ? ORDER BY fecha_recarga DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("monto", rs.getDouble("monto"));
                    fila.put("fecha_recarga", rs.getString("fecha_recarga")); // Trae fecha y hora por el DATETIME
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    // 3. Reporte de gasto por categoría
    public List<Map<String, Object>> reporteGastoPorCategoria(int idCliente, String fechaInicio, String fechaFin) throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Sumamos el monto de los contratos finalizados agrupados por categoría
        String sql = "SELECT cat.nombre AS categoria, SUM(c.monto_bloqueado) AS total_gastado " +
                     "FROM contrato c " +
                     "JOIN propuesta prop ON c.id_propuesta = prop.id_propuesta " +
                     "JOIN proyecto p ON prop.id_proyecto = p.id_proyecto " +
                     "JOIN categoria cat ON p.id_categoria = cat.id_categoria " +
                     "WHERE p.id_cliente = ? AND prop.estado = 'ACEPTADA' AND c.fecha_inicio BETWEEN ? AND ? " +
                     "GROUP BY cat.id_categoria, cat.nombre " +
                     "ORDER BY total_gastado DESC";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, fechaInicio);
            stmt.setString(3, fechaFin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("categoria", rs.getString("categoria"));
                    fila.put("total_gastado", rs.getDouble("total_gastado"));
                    lista.add(fila);
                }
            }
        }
        return lista;
    }
    
    public boolean solicitarCategoria(int idUsuario, String nombre, String descripcion) throws Exception {
        String sql = "INSERT INTO solicitud_catalogo (id_usuario_solicitante, tipo_solicitud, nombre_sugerido, descripcion, estado) VALUES (?, 'CATEGORIA', ?, ?, 'PENDIENTE')";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setString(2, nombre);
            stmt.setString(3, descripcion);
            return stmt.executeUpdate() > 0;
        }
    }

    // Métodos CrudDao restantes omitidos por brevedad
    @Override public Optional<Cliente> obtenerPorId(Integer id) { return Optional.empty(); }
    @Override public List<Cliente> obtenerTodos() { return null; }
    @Override public boolean actualizar(Cliente entidad) { return false; }
    @Override public boolean eliminar(Integer id) { return false; }
}
