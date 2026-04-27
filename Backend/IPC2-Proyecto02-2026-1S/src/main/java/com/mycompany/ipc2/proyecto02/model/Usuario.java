/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.model;

import java.time.LocalDate;

/**
 *
 * @author cesar
 */
public class Usuario {

    private Integer idUsuario;
    private String nombreCompleto;
    private String username;
    private String password;
    private String email;
    private String telefono;
    private String direccion;
    private String cui;
    private LocalDate fechaNacimiento;
    private String rol; // 'ADMIN', 'CLIENTE', 'FREELANCER'
    private String estadoCuenta; // 'ACTIVO', 'INACTIVO'

    // Constructor vacío requerido para inicialización en frameworks/mappers
    public Usuario() {}

    // Constructor para registro (sin ID ni estado, ya que se generan/asignan por defecto)
    public Usuario(String nombreCompleto, String username, String password, String email, 
                   String telefono, String direccion, String cui, LocalDate fechaNacimiento, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.cui = cui;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
    }

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCui() { return cui; }
    public void setCui(String cui) { this.cui = cui; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
}