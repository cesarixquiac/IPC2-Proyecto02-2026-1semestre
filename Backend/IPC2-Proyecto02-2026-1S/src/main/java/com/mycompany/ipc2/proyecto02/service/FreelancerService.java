/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilFreelancerDTO;
import com.mycompany.ipc2.proyecto02.model.Freelancer;

public interface FreelancerService {
    Freelancer completarPerfil(Integer idUsuario, CompletarPerfilFreelancerDTO dto) throws Exception;
}
