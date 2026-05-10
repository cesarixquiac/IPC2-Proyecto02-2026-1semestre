/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public interface AdminDAO {
    List<Map<String, Object>> obtenerSolicitudesPendientes() throws Exception;
    
    // idCategoriaDestino solo se usa si se acepta una HABILIDAD
    boolean procesarSolicitud(int idSolicitud, String estadoAccion, Integer idCategoriaDestino) throws Exception;
    // --- MÉTODOS DE COMISIÓN ---
    Double obtenerComisionActual() throws Exception;
    boolean actualizarComision(double nuevoPorcentaje) throws Exception;
    // --- MÉTODOS DE GESTIÓN DE CATEGORÍAS ---
    List<Map<String, Object>> obtenerCategoriasAdmin() throws Exception;
    boolean crearCategoria(String nombre, String descripcion) throws Exception;
    boolean editarCategoria(int idCategoria, String nombre, String descripcion) throws Exception;
    boolean cambiarEstadoCategoria(int idCategoria, String nuevoEstado) throws Exception;
    
    // --- MÉTODOS DE REPORTES ---
    List<Map<String, Object>> reporteHistorialComisiones() throws Exception;
    List<Map<String, Object>> reporteTopFreelancers(String fechaInicio, String fechaFin) throws Exception;
    List<Map<String, Object>> reporteTopCategorias(String fechaInicio, String fechaFin) throws Exception;
    Map<String, Object> reporteIngresosPlataforma(String fechaInicio, String fechaFin) throws Exception;
    
    List<Map<String, Object>> obtenerTodasHabilidadesAdmin() throws Exception;
    boolean crearHabilidad(int idCategoria, String nombre) throws Exception;
    boolean editarHabilidad(int idHabilidad, int idCategoria, String nombre) throws Exception;
    boolean cambiarEstadoHabilidad(int idHabilidad, String nuevoEstado) throws Exception;
}
