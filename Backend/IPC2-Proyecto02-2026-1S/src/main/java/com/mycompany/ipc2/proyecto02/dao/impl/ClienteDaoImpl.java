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
import java.util.List;
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

    // Métodos CrudDao restantes omitidos por brevedad
    @Override public Optional<Cliente> obtenerPorId(Integer id) { return Optional.empty(); }
    @Override public List<Cliente> obtenerTodos() { return null; }
    @Override public boolean actualizar(Cliente entidad) { return false; }
    @Override public boolean eliminar(Integer id) { return false; }
}
