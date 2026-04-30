/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dao.EntregaDao;
import com.mycompany.ipc2.proyecto02.dao.impl.EntregaDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.CrearEntregaDTO;
import com.mycompany.ipc2.proyecto02.dto.EvaluarEntregaDTO;
import com.mycompany.ipc2.proyecto02.model.Entrega;
import com.mycompany.ipc2.proyecto02.service.EntregaService;

public class EntregaServiceImpl implements EntregaService {

    private final EntregaDao entregaDao;

    public EntregaServiceImpl() {
        this.entregaDao = new EntregaDaoImpl();
    }

    @Override
    public Entrega subirEntrega(Integer idFreelancer, CrearEntregaDTO dto) throws Exception {
        if (dto.getUrlArchivo() == null || dto.getUrlArchivo().isEmpty()) {
            throw new Exception("El enlace del archivo es obligatorio.");
        }

        Entrega entrega = new Entrega();
        entrega.setIdContrato(dto.getIdContrato());
        entrega.setUrlArchivo(dto.getUrlArchivo());
        entrega.setDescripcion(dto.getDescripcion());

        Entrega creada = entregaDao.crear(entrega);
        if (creada == null) {
            throw new Exception("Error al registrar la entrega.");
        }
        return creada;
    }

    @Override
    public void evaluarEntrega(Integer idCliente, EvaluarEntregaDTO dto) throws Exception {
        if (!"APROBADA".equals(dto.getEstado()) && !"RECHAZADA".equals(dto.getEstado())) {
            throw new Exception("El estado debe ser APROBADA o RECHAZADA.");
        }
        
        boolean exito = entregaDao.evaluarEntregaYProcesarPago(idCliente, dto.getIdEntrega(), dto.getEstado());
        if (!exito) {
            throw new Exception("Error al procesar la evaluación.");
        }
    }
}
