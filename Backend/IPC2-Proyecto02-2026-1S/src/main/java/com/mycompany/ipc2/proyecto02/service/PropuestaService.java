/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dto.EnviarPropuestaDTO;
import com.mycompany.ipc2.proyecto02.model.Propuesta;

public interface PropuestaService {
    Propuesta enviarPropuesta(Integer idFreelancer, EnviarPropuestaDTO dto) throws Exception;
}