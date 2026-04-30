/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dto.CrearEntregaDTO;
import com.mycompany.ipc2.proyecto02.dto.EvaluarEntregaDTO;
import com.mycompany.ipc2.proyecto02.model.Entrega;

public interface EntregaService {
    Entrega subirEntrega(Integer idFreelancer, CrearEntregaDTO dto) throws Exception;
    void evaluarEntrega(Integer idCliente, EvaluarEntregaDTO dto) throws Exception;
}
