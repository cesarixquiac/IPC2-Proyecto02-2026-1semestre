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
import java.util.List;
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

    // Métodos heredados de CrudDao (obligatorios, aunque no los usemos ahora)
    @Override public Optional<Freelancer> obtenerPorId(Integer id) { return Optional.empty(); }
    @Override public List<Freelancer> obtenerTodos() { return null; }
    @Override public boolean actualizar(Freelancer entidad) { return false; }
    @Override public boolean eliminar(Integer id) { return false; }
}