/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

/**
 *
 * @author cesar
 */

import com.mycompany.ipc2.proyecto02.dao.PropuestaDao;
import com.mycompany.ipc2.proyecto02.dao.impl.PropuestaDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.EnviarPropuestaDTO;
import com.mycompany.ipc2.proyecto02.model.Propuesta;
import com.mycompany.ipc2.proyecto02.service.PropuestaService;

public class PropuestaServiceImpl implements PropuestaService {

    private final PropuestaDao propuestaDao;

    public PropuestaServiceImpl() {
        this.propuestaDao = new PropuestaDaoImpl();
    }

    @Override
    public Propuesta enviarPropuesta(Integer idFreelancer, EnviarPropuestaDTO dto) throws Exception {
        
        Object[] datosProyecto = propuestaDao.obtenerDatosValidacionProyecto(dto.getIdProyecto());
        if (datosProyecto == null) {
            throw new Exception("El proyecto especificado no existe.");
        }

        Double presupuestoMaximo = (Double) datosProyecto[0];
        String estadoProyecto = (String) datosProyecto[1];

        // Regla: El proyecto debe estar ABIERTO
        if (!"ABIERTO".equals(estadoProyecto)) {
            throw new Exception("No se pueden enviar propuestas a un proyecto que no se encuentra ABIERTO.");
        }

        // Regla: La oferta no puede superar el presupuesto máximo
        if (dto.getMontoOfertado() > presupuestoMaximo) {
            throw new Exception("El monto ofertado (Q" + dto.getMontoOfertado() + ") supera el presupuesto máximo del cliente (Q" + presupuestoMaximo + ").");
        }

        // Regla: No puede enviar más de una propuesta al mismo proyecto
        if (propuestaDao.freelancerYaAplico(dto.getIdProyecto(), idFreelancer)) {
            throw new Exception("Ya has enviado una propuesta para este proyecto.");
        }

        // Regla: Debe cumplir al menos una habilidad requerida
        if (!propuestaDao.cumpleAlMenosUnaHabilidad(dto.getIdProyecto(), idFreelancer)) {
            throw new Exception("No cumples con ninguna de las habilidades requeridas para este proyecto.");
        }

        Propuesta propuesta = new Propuesta();
        propuesta.setIdProyecto(dto.getIdProyecto());
        propuesta.setIdFreelancer(idFreelancer);
        propuesta.setMontoOfertado(dto.getMontoOfertado());
        propuesta.setPlazoDias(dto.getPlazoDias());
        propuesta.setCartaPresentacion(dto.getCartaPresentacion());

        Propuesta propuestaCreada = propuestaDao.crear(propuesta);
        if (propuestaCreada == null) {
            throw new Exception("Ocurrió un error interno al registrar la propuesta.");
        }

        return propuestaCreada;
    }
}
