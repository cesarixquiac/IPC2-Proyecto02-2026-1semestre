/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.model;

/**
 *
 * @author cesar
 */
public class Propuesta {
    private Integer idPropuesta;
    private Integer idProyecto;
    private Integer idFreelancer;
    private Double montoOfertado;
    private Integer plazoDias;
    private String cartaPresentacion;
    private String estado;

    public Propuesta() {}

    public Integer getIdPropuesta() { return idPropuesta; }
    public void setIdPropuesta(Integer idPropuesta) { this.idPropuesta = idPropuesta; }
    public Integer getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Integer idProyecto) { this.idProyecto = idProyecto; }
    public Integer getIdFreelancer() { return idFreelancer; }
    public void setIdFreelancer(Integer idFreelancer) { this.idFreelancer = idFreelancer; }
    public Double getMontoOfertado() { return montoOfertado; }
    public void setMontoOfertado(Double montoOfertado) { this.montoOfertado = montoOfertado; }
    public Integer getPlazoDias() { return plazoDias; }
    public void setPlazoDias(Integer plazoDias) { this.plazoDias = plazoDias; }
    public String getCartaPresentacion() { return cartaPresentacion; }
    public void setCartaPresentacion(String cartaPresentacion) { this.cartaPresentacion = cartaPresentacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
