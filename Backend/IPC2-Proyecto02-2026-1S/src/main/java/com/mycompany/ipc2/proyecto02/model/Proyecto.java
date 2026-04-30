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
public class Proyecto {
    private Integer idProyecto;
    private Integer idCliente;
    private Integer idCategoria;
    private String titulo;
    private String descripcion;
    private Double presupuestoMaximo;
    private LocalDate fechaLimite;
    private String estado;

    public Proyecto() {}

    // Getters y Setters
    public Integer getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Integer idProyecto) { this.idProyecto = idProyecto; }
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPresupuestoMaximo() { return presupuestoMaximo; }
    public void setPresupuestoMaximo(Double presupuestoMaximo) { this.presupuestoMaximo = presupuestoMaximo; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDate fechaLimite) { this.fechaLimite = fechaLimite; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
