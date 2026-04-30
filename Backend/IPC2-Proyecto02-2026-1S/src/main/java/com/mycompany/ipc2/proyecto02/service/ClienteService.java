/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilClienteDTO;
import com.mycompany.ipc2.proyecto02.model.Cliente;

/**
 *
 * @author cesar
 */
public interface ClienteService {
    Cliente completarPerfil(Integer idUsuario, CompletarPerfilClienteDTO dto) throws Exception;
    void realizarRecarga(Integer idCliente, Double monto) throws Exception;
}
