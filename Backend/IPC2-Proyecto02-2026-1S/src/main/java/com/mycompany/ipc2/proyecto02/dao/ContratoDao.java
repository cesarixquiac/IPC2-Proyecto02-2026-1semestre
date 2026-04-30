/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.model.Contrato;
import java.util.List;
import java.util.Optional;

public interface ContratoDao extends CrudDAO<Contrato, Integer> {
    Contrato aceptarPropuestaYGenerarContrato(Integer idCliente, Integer idPropuesta) throws Exception;
}
