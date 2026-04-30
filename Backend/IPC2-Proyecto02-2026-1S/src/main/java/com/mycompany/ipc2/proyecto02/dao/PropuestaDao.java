/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

import com.mycompany.ipc2.proyecto02.model.Propuesta;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author cesar
 */
public interface PropuestaDao extends CrudDAO<Propuesta, Integer> {
    boolean freelancerYaAplico(Integer idProyecto, Integer idFreelancer);
    boolean cumpleAlMenosUnaHabilidad(Integer idProyecto, Integer idFreelancer);
    // Un arreglo de Objetos para retornar [PresupuestoMaximo, EstadoProyecto] de forma rápida
    Object[] obtenerDatosValidacionProyecto(Integer idProyecto); 
}
