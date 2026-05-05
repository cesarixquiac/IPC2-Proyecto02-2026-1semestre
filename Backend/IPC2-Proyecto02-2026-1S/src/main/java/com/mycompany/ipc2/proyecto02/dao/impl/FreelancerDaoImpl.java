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

    // Métodos heredados de CrudDao (obligatorios, aunque no los usemos ahora)
    @Override public Optional<Freelancer> obtenerPorId(Integer id) { return Optional.empty(); }
    @Override public List<Freelancer> obtenerTodos() { return null; }
    @Override public boolean actualizar(Freelancer entidad) { return false; }
    @Override public boolean eliminar(Integer id) { return false; }
}