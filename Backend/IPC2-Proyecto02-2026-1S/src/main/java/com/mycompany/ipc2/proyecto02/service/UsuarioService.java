/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service;

import com.mycompany.ipc2.proyecto02.dto.AuthResponseDTO;
import com.mycompany.ipc2.proyecto02.dto.LoginRequestDTO;
import com.mycompany.ipc2.proyecto02.model.Usuario;

/**
 *
 * @author cesar
 */
public interface UsuarioService {
    
    Usuario registrarUsuario(Usuario nuevoUsuario) throws Exception;
    
    AuthResponseDTO autenticarUsuario(LoginRequestDTO loginDto) throws Exception;
    
}
