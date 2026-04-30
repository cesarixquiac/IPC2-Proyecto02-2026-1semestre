/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dao.ContratoDao;
import com.mycompany.ipc2.proyecto02.dao.impl.ContratoDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.GenerarContratoDTO;
import com.mycompany.ipc2.proyecto02.model.Contrato;
import com.mycompany.ipc2.proyecto02.service.ContratoService;

public class ContratoServiceImpl implements ContratoService {

    private final ContratoDao contratoDao;

    public ContratoServiceImpl() {
        this.contratoDao = new ContratoDaoImpl();
    }

    @Override
    public Contrato generarContrato(Integer idCliente, GenerarContratoDTO dto) throws Exception {
        if (dto.getIdPropuesta() == null) {
            throw new Exception("El ID de la propuesta es requerido.");
        }
        // Toda la lógica compleja y validaciones ya se ejecutan de forma atómica en el DAO
        return contratoDao.aceptarPropuestaYGenerarContrato(idCliente, dto.getIdPropuesta());
    }
}