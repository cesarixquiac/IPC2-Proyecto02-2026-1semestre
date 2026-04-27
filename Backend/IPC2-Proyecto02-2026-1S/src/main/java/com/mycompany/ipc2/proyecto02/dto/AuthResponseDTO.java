/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dto;

/**
 *
 * @author cesar
 */
public class AuthResponseDTO {
    
    private String token;
    private Integer idUsuario;
    private String rol;
    private String mensaje;

    public AuthResponseDTO(String token, Integer idUsuario, String rol, String mensaje) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.mensaje = mensaje;
    }

    // Getters ( serializar a JSON en la respuesta)
    public String getToken() { return token; }
    public Integer getIdUsuario() { return idUsuario; }
    public String getRol() { return rol; }
    public String getMensaje() { return mensaje; }
    
}
