/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dto;

import java.util.List;

/**
 *
 * @author cesar
 */
public class PublicarProyectoDTO {
    private String titulo;
    private String descripcion;
    private Integer idCategoria;
    private Double presupuestoMaximo;
    private String fechaLimite; // Lo recibimos como String y lo parseamos
    private List<Integer> habilidadesRequeridas;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public Double getPresupuestoMaximo() { return presupuestoMaximo; }
    public void setPresupuestoMaximo(Double presupuestoMaximo) { this.presupuestoMaximo = presupuestoMaximo; }
    public String getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(String fechaLimite) { this.fechaLimite = fechaLimite; }
    public List<Integer> getHabilidadesRequeridas() { return habilidadesRequeridas; }
    public void setHabilidadesRequeridas(List<Integer> habilidadesRequeridas) { this.habilidadesRequeridas = habilidadesRequeridas; }
}