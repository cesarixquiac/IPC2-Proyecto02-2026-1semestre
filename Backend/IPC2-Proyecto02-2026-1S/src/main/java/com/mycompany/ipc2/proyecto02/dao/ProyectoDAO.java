/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.model.Proyecto;
import java.util.List;
import java.util.Map;

public interface ProyectoDAO extends CrudDAO<Proyecto, Integer> {
    Proyecto publicarProyectoConHabilidades(Proyecto proyecto, List<Integer> habilidades);
    List<Map<String, Object>> obtenerPropuestasPorProyecto(int idProyecto);
    void aceptarPropuesta(int idProyecto, int idPropuesta, int idCliente) throws Exception;
    
    void rechazarPropuesta(int idPropuesta) throws Exception;
    
}
