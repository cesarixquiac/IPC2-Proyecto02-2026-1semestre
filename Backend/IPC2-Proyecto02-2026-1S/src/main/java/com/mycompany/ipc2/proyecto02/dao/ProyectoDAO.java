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

    Map<String, Object> obtenerDetalleEntrega(int idProyecto) throws Exception;

    void aprobarEntrega(int idProyecto, int estrellas, String comentario) throws Exception;

    void rechazarEntrega(int idProyecto, String motivo) throws Exception;

    void cancelarContrato(int idProyecto, String motivo, int idCliente) throws Exception;

    void rechazarPropuesta(int idPropuesta) throws Exception;

    void editarProyectoAbierto(int idProyecto, String titulo, String descripcion, double presupuesto, String fechaLimite) throws Exception;

    void eliminarProyectoAbierto(int idProyecto) throws Exception;

    List<Map<String, Object>> obtenerCatalogoHabilidades() throws Exception;

    List<Map<String, Object>> obtenerProyectosDisponibles(int idFreelancer) throws Exception;

    List<Map<String, Object>> obtenerContratosActivosFreelancer(int idFreelancer) throws Exception;

}
