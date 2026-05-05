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
import com.mycompany.ipc2.proyecto02.dao.EntregaDao;
import com.mycompany.ipc2.proyecto02.model.Entrega;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class EntregaDaoImpl implements EntregaDao {

    @Override
    public Entrega crear(Entrega entrega) {
        String sqlInsert = "INSERT INTO Entrega (id_contrato, url_archivo, descripcion, estado) VALUES (?, ?, ?, 'PENDIENTE')";
        
        // ACTUALIZADO: Buscamos el id_proyecto pasando por la tabla propuesta
        String sqlUpdate = "UPDATE proyecto SET estado = 'ENTREGA_PENDIENTE' "
                + "WHERE id_proyecto = (SELECT p.id_proyecto FROM propuesta p "
                + "JOIN contrato c ON p.id_propuesta = c.id_propuesta "
                + "WHERE c.id_contrato = ?)";

        // Declaramos ambos PreparedStatement en el try-with-resources
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {

            // 1. Preparamos el INSERT de la entrega
            stmtInsert.setInt(1, entrega.getIdContrato());
            stmtInsert.setString(2, entrega.getUrlArchivo());
            stmtInsert.setString(3, entrega.getDescripcion());

            // 2. Si el INSERT funciona...
            if (stmtInsert.executeUpdate() > 0) {
                try (ResultSet rs = stmtInsert.getGeneratedKeys()) {
                    if (rs.next()) {
                        entrega.setIdEntrega(rs.getInt(1));
                        entrega.setEstado("PENDIENTE");
                        
                        // 3. ...hacemos el UPDATE del estado del proyecto
                        stmtUpdate.setInt(1, entrega.getIdContrato());
                        stmtUpdate.executeUpdate();

                        // 4. Retornamos la entrega con todo listo y actualizado
                        return entrega;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean evaluarEntregaYProcesarPago(Integer idCliente, Integer idEntrega, String estadoEvaluacion) throws Exception {
        // Obtenemos toda la radiografía del proyecto con un solo JOIN
        String sqlInfo = "SELECT p.id_proyecto, p.id_cliente, pr.id_freelancer, c.monto_bloqueado, c.porcentaje_comision_aplicado "
                + "FROM Entrega e "
                + "JOIN Contrato c ON e.id_contrato = c.id_contrato "
                + "JOIN Propuesta pr ON c.id_propuesta = pr.id_propuesta "
                + "JOIN Proyecto p ON pr.id_proyecto = p.id_proyecto "
                + "WHERE e.id_entrega = ?";

        String sqlUpdateEntrega = "UPDATE Entrega SET estado = ? WHERE id_entrega = ?";
        String sqlUpdateProyecto = "UPDATE Proyecto SET estado = 'COMPLETADO' WHERE id_proyecto = ?";
        String sqlPagoFreelancer = "UPDATE Freelancer SET saldo_acumulado = saldo_acumulado + ? WHERE id_freelancer = ?";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar Transacción

            int idProyecto, idFreelancer, clienteDuenio;
            double montoBloqueado, porcentajeComision;

            try (PreparedStatement stmtInfo = conn.prepareStatement(sqlInfo)) {
                stmtInfo.setInt(1, idEntrega);
                try (ResultSet rs = stmtInfo.executeQuery()) {
                    if (!rs.next()) {
                        throw new Exception("La entrega no existe.");
                    }

                    clienteDuenio = rs.getInt("id_cliente");
                    if (clienteDuenio != idCliente) {
                        throw new Exception("No tienes permiso para evaluar esta entrega.");
                    }

                    idProyecto = rs.getInt("id_proyecto");
                    idFreelancer = rs.getInt("id_freelancer");
                    montoBloqueado = rs.getDouble("monto_bloqueado");
                    porcentajeComision = rs.getDouble("porcentaje_comision_aplicado");
                }
            }

            // 1. Actualizar estado de la entrega
            try (PreparedStatement stmtEnt = conn.prepareStatement(sqlUpdateEntrega)) {
                stmtEnt.setString(1, estadoEvaluacion);
                stmtEnt.setInt(2, idEntrega);
                stmtEnt.executeUpdate();
            }

            // 2. Si es APROBADA, procesar pago y finalizar proyecto
            if ("APROBADA".equals(estadoEvaluacion)) {

                try (PreparedStatement stmtProy = conn.prepareStatement(sqlUpdateProyecto)) {
                    stmtProy.setInt(1, idProyecto);
                    stmtProy.executeUpdate();
                }

                double comision = montoBloqueado * (porcentajeComision / 100);
                double pagoFinal = montoBloqueado - comision;

                try (PreparedStatement stmtPago = conn.prepareStatement(sqlPagoFreelancer)) {
                    stmtPago.setDouble(1, pagoFinal);
                    stmtPago.setInt(2, idFreelancer);
                    stmtPago.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // Métodos CrudDao restantes
    @Override
    public Optional<Entrega> obtenerPorId(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Entrega> obtenerTodos() {
        return null;
    }

    @Override
    public boolean actualizar(Entrega entidad) {
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        return false;
    }
}
