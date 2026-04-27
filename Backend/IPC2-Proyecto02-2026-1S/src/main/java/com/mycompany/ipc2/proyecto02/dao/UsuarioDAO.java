/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

import com.mycompany.ipc2.proyecto02.model.Usuario;
import java.util.Optional;

/**
 *
 * @author cesar
 */
public interface UsuarioDAO {
    // Método crítico para autenticación
    Optional<Usuario> buscarPorUsername(String username);
    
    // Método para validación de unicidad en registros
    boolean existeEmailOCui(String email, String cui);

    public Usuario crear(Usuario nuevoUsuario);
}
