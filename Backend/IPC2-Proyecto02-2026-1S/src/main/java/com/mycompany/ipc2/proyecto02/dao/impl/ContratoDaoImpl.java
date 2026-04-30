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
import com.mycompany.ipc2.proyecto02.dao.ContratoDao;
import com.mycompany.ipc2.proyecto02.model.Contrato;

import java.sql.*;
import java.util.List;
import java.util.Optional;


public class ContratoDaoImpl implements ContratoDao {

    @Override
    public Contrato aceptarPropuestaYGenerarContrato(Integer idCliente, Integer idPropuesta) throws Exception {
        String sqlValidacion = "SELECT p.id_proyecto, p.monto_ofertado, pr.id_cliente, pr.estado, c.saldo_disponible " +
                               "FROM Propuesta p " +
                               "JOIN Proyecto pr ON p.id_proyecto = pr.id_proyecto " +
                               "JOIN Cliente c ON pr.id_cliente = c.id_cliente " +
                               "WHERE p.id_propuesta = ?";
                               
        String sqlComision = "SELECT porcentaje FROM Historial_Comision WHERE fecha_fin IS NULL LIMIT 1";
        String sqlUpdateCliente = "UPDATE Cliente SET saldo_disponible = saldo_disponible - ? WHERE id_cliente = ?";
        String sqlUpdateProyecto = "UPDATE Proyecto SET estado = 'EN_PROGRESO' WHERE id_proyecto = ?";
        String sqlUpdatePropuesta = "UPDATE Propuesta SET estado = 'ACEPTADA' WHERE id_propuesta = ?";
        String sqlInsertContrato = "INSERT INTO Contrato (id_propuesta, monto_bloqueado, porcentaje_comision_aplicado) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // INICIO DE LA TRANSACCIÓN

            // 1. Validar datos
            int idProyecto;
            double montoOfertado;
            double saldoDisponible;
            try (PreparedStatement stmtVal = conn.prepareStatement(sqlValidacion)) {
                stmtVal.setInt(1, idPropuesta);
                try (ResultSet rs = stmtVal.executeQuery()) {
                    if (!rs.next()) throw new Exception("La propuesta no existe.");
                    
                    if (rs.getInt("id_cliente") != idCliente) throw new Exception("Este proyecto no le pertenece.");
                    if (!"ABIERTO".equals(rs.getString("estado"))) throw new Exception("El proyecto ya no está abierto.");
                    
                    idProyecto = rs.getInt("id_proyecto");
                    montoOfertado = rs.getDouble("monto_ofertado");
                    saldoDisponible = rs.getDouble("saldo_disponible");
                }
            }

            if (saldoDisponible < montoOfertado) {
                throw new Exception("Saldo insuficiente. Necesita Q" + montoOfertado + " pero dispone de Q" + saldoDisponible);
            }

            // 2. Obtener Comisión Actual
            double comisionActual = 0.0;
            try (PreparedStatement stmtCom = conn.prepareStatement(sqlComision);
                 ResultSet rsCom = stmtCom.executeQuery()) {
                if (rsCom.next()) {
                    comisionActual = rsCom.getDouble("porcentaje");
                } else {
                    throw new Exception("Error de configuración: No hay comisión activa en la plataforma.");
                }
            }

            // 3. Descontar Saldo
            try (PreparedStatement stmtCli = conn.prepareStatement(sqlUpdateCliente)) {
                stmtCli.setDouble(1, montoOfertado);
                stmtCli.setInt(2, idCliente);
                stmtCli.executeUpdate();
            }

            // 4. Actualizar Estados
            try (PreparedStatement stmtProy = conn.prepareStatement(sqlUpdateProyecto)) {
                stmtProy.setInt(1, idProyecto);
                stmtProy.executeUpdate();
            }
            try (PreparedStatement stmtProp = conn.prepareStatement(sqlUpdatePropuesta)) {
                stmtProp.setInt(1, idPropuesta);
                stmtProp.executeUpdate();
            }

            // 5. Crear Contrato
            Contrato contrato = new Contrato();
            try (PreparedStatement stmtContr = conn.prepareStatement(sqlInsertContrato, Statement.RETURN_GENERATED_KEYS)) {
                stmtContr.setInt(1, idPropuesta);
                stmtContr.setDouble(2, montoOfertado);
                stmtContr.setDouble(3, comisionActual);
                stmtContr.executeUpdate();
                
                try (ResultSet rsKeys = stmtContr.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        contrato.setIdContrato(rsKeys.getInt(1));
                        contrato.setIdPropuesta(idPropuesta);
                        contrato.setMontoBloqueado(montoOfertado);
                        contrato.setPorcentajeComisionAplicado(comisionActual);
                    }
                }
            }

            conn.commit(); // CONFIRMAR TRANSACCIÓN
            return contrato;

        } catch (Exception e) {
            if (conn != null) conn.rollback(); // DESHACER TODO SI HAY ERROR
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override public Contrato crear(Contrato entidad) { return null; }
    @Override public Optional<Contrato> obtenerPorId(Integer id) { return Optional.empty(); }
    @Override public List<Contrato> obtenerTodos() { return null; }
    @Override public boolean actualizar(Contrato entidad) { return false; }
    @Override public boolean eliminar(Integer id) { return false; }
}
