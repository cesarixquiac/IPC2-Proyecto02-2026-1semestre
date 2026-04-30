/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.model;

import java.time.LocalDateTime;

/**
 *
 * @author cesar
 */
public class Contrato {
    private Integer idContrato;
    private Integer idPropuesta;
    private Double montoBloqueado;
    private Double porcentajeComisionAplicado;
    private LocalDateTime fechaInicio;
    private String motivoCancelacion;

    public Contrato() {}

    // Getters y Setters
    public Integer getIdContrato() { return idContrato; }
    public void setIdContrato(Integer idContrato) { this.idContrato = idContrato; }
    public Integer getIdPropuesta() { return idPropuesta; }
    public void setIdPropuesta(Integer idPropuesta) { this.idPropuesta = idPropuesta; }
    public Double getMontoBloqueado() { return montoBloqueado; }
    public void setMontoBloqueado(Double montoBloqueado) { this.montoBloqueado = montoBloqueado; }
    public Double getPorcentajeComisionAplicado() { return porcentajeComisionAplicado; }
    public void setPorcentajeComisionAplicado(Double porcentajeComisionAplicado) { this.porcentajeComisionAplicado = porcentajeComisionAplicado; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }
}