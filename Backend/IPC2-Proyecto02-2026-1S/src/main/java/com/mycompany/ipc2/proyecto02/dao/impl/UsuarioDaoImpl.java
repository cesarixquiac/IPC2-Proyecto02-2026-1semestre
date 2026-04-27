/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao.impl;
import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dao.UsuarioDAO ;
import com.mycompany.ipc2.proyecto02.model.Usuario;

import java.sql.*;
import java.util.List;
import java.util.Optional;
        
/**
 *
 * @author cesar
 */
public class UsuarioDaoImpl implements UsuarioDAO {

    public Usuario crear(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nombre_completo, username, password, email, telefono, direccion, cui, fecha_nacimiento, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getNombreCompleto());
            stmt.setString(2, usuario.getUsername());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setString(6, usuario.getDireccion());
            stmt.setString(7, usuario.getCui());
            stmt.setDate(8, Date.valueOf(usuario.getFechaNacimiento()));
            stmt.setString(9, usuario.getRol());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setIdUsuario(generatedKeys.getInt(1));
                        usuario.setEstadoCuenta("ACTIVO");
                        return usuario;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: Implementar logger y manejo de excepciones personalizadas
        }
        return null;
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT * FROM Usuario WHERE username = ? AND estado_cuenta = 'ACTIVO'";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean existeEmailOCui(String email, String cui) {
        String sql = "SELECT 1 FROM Usuario WHERE email = ? OR cui = ? LIMIT 1";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, cui);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false por defecto; en producción manejar la excepción adecuadamente
    }

    // Métodos CrudDao restantes (obtenerPorId, obtenerTodos, actualizar, eliminar)

    public Optional<Usuario> obtenerPorId(Integer integer) { return Optional.empty(); }

    public List<Usuario> obtenerTodos() { return null; }

    public boolean actualizar(Usuario entidad) { return false; }

    public boolean eliminar(Integer integer) { return false; }

    // Utilidad interna para mapear el ResultSet al objeto Usuario
    private Usuario mapearResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setEmail(rs.getString("email"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setCui(rs.getString("cui"));
        u.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        u.setRol(rs.getString("rol"));
        u.setEstadoCuenta(rs.getString("estado_cuenta"));
        return u;
    }
}
