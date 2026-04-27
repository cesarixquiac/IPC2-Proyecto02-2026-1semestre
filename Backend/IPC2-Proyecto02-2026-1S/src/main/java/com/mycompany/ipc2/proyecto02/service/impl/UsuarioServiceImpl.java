/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

import com.mycompany.ipc2.proyecto02.service.UsuarioService;
import com.mycompany.ipc2.proyecto02.dao.UsuarioDAO;
import com.mycompany.ipc2.proyecto02.dao.impl.UsuarioDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.AuthResponseDTO;
import com.mycompany.ipc2.proyecto02.dto.LoginRequestDTO;
import com.mycompany.ipc2.proyecto02.model.Usuario;
import com.mycompany.ipc2.proyecto02.util.JwtUtil;
import com.mycompany.ipc2.proyecto02.util.PasswordUtil;
import java.util.Optional;

/**
 *
 * @author cesar
 */
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDao;

    public UsuarioServiceImpl() {
        // En un entorno sin inyección de dependencias (Spring), instanciamos manualmente
        this.usuarioDao = new UsuarioDaoImpl();
    }

    @Override
    public Usuario registrarUsuario(Usuario nuevoUsuario) throws Exception {
        // 1. Validar unicidad de correo y CUI
        if (usuarioDao.existeEmailOCui(nuevoUsuario.getEmail(), nuevoUsuario.getCui())) {
            throw new Exception("El correo electrónico o CUI ya se encuentran registrados.");
        }

        // 2. Encriptar la contraseña antes de guardarla
        String hashedPassword = PasswordUtil.hashPassword(nuevoUsuario.getPassword());
        nuevoUsuario.setPassword(hashedPassword);

        // 3. Persistir en la base de datos
        Usuario usuarioCreado = usuarioDao.crear(nuevoUsuario);
        if (usuarioCreado == null) {
            throw new Exception("Error interno al intentar registrar el usuario en la base de datos.");
        }

        return usuarioCreado;
    }

    @Override
    public AuthResponseDTO autenticarUsuario(LoginRequestDTO loginDto) throws Exception {
        // 1. Buscar usuario por username
        Optional<Usuario> usuarioOpt = usuarioDao.buscarPorUsername(loginDto.getUsername());
        
        if (usuarioOpt.isEmpty()) {
            throw new Exception("Credenciales inválidas o cuenta inactiva.");
        }

        Usuario usuario = usuarioOpt.get();

        // 2. Verificar que la contraseña coincida con el hash
        if (!PasswordUtil.verifyPassword(loginDto.getPassword(), usuario.getPassword())) {
            throw new Exception("Credenciales inválidas.");
        }

        // 3. Generar JSON Web Token
        String token = JwtUtil.generateToken(usuario.getUsername(), usuario.getRol(), usuario.getIdUsuario());

        // 4. Retornar el DTO con los datos de sesión limpios
        return new AuthResponseDTO(token, usuario.getIdUsuario(), usuario.getRol(), "Autenticación exitosa");
    }
}
