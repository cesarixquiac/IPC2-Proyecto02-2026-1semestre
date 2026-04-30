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
import com.mycompany.ipc2.proyecto02.dao.PropuestaDao;
import com.mycompany.ipc2.proyecto02.model.Propuesta;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PropuestaDaoImpl implements PropuestaDao {

    @Override
    public Propuesta crear(Propuesta propuesta) {
        String sql = "INSERT INTO Propuesta (id_proyecto, id_freelancer, monto_ofertado, plazo_dias, carta_presentacion, estado) VALUES (?, ?, ?, ?, ?, 'PENDIENTE')";

        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, propuesta.getIdProyecto());
            stmt.setInt(2, propuesta.getIdFreelancer());
            stmt.setDouble(3, propuesta.getMontoOfertado());
            stmt.setInt(4, propuesta.getPlazoDias());
            stmt.setString(5, propuesta.getCartaPresentacion());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        propuesta.setIdPropuesta(rs.getInt(1));
                        propuesta.setEstado("PENDIENTE");
                        return propuesta;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean freelancerYaAplico(Integer idProyecto, Integer idFreelancer) {
        String sql = "SELECT 1 FROM Propuesta WHERE id_proyecto = ? AND id_freelancer = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProyecto);
            stmt.setInt(2, idFreelancer);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cumpleAlMenosUnaHabilidad(Integer idProyecto, Integer idFreelancer) {
        // Cruce de tablas intermedias para verificar coincidencia de habilidades
        String sql = "SELECT 1 FROM Proyecto_Habilidad ph JOIN Freelancer_Habilidad fh ON ph.id_habilidad = fh.id_habilidad WHERE ph.id_proyecto = ? AND fh.id_freelancer = ? LIMIT 1";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProyecto);
            stmt.setInt(2, idFreelancer);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object[] obtenerDatosValidacionProyecto(Integer idProyecto) {
        String sql = "SELECT presupuesto_maximo, estado FROM Proyecto WHERE id_proyecto = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProyecto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{rs.getDouble("presupuesto_maximo"), rs.getString("estado")};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Métodos CrudDao restantes
    @Override
    public Optional<Propuesta> obtenerPorId(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Propuesta> obtenerTodos() {
        return null;
    }

    @Override
    public boolean actualizar(Propuesta entidad) {
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        return false;
    }
}
