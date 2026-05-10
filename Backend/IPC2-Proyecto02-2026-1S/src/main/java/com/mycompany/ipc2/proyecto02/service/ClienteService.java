/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilClienteDTO;
import com.mycompany.ipc2.proyecto02.model.Cliente;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public interface ClienteService {
    Cliente completarPerfil(Integer idUsuario, CompletarPerfilClienteDTO dto) throws Exception;
    void realizarRecarga(Integer idCliente, Double monto) throws Exception;
    Cliente obtenerPerfil(Integer idCliente) throws Exception;
    
    List<Map<String, Object>> obtenerHistorialRecargas(int idCliente) throws Exception;
    
    List<Map<String, Object>> reporteHistorialProyectos(int idCliente, String fechaInicio, String fechaFin) throws Exception;
    List<Map<String, Object>> reporteHistorialRecargas(int idCliente) throws Exception;
    List<Map<String, Object>> reporteGastoPorCategoria(int idCliente, String fechaInicio, String fechaFin) throws Exception;
    
    boolean solicitarCategoria(int idUsuario, String nombre, String descripcion) throws Exception;
}
