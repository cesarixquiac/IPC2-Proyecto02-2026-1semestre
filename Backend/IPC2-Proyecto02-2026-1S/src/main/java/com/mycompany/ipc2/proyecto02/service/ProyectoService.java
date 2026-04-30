/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dto.PublicarProyectoDTO;
import com.mycompany.ipc2.proyecto02.model.Proyecto;

public interface ProyectoService {
    Proyecto publicarProyecto(Integer idCliente, PublicarProyectoDTO dto) throws Exception;
}
