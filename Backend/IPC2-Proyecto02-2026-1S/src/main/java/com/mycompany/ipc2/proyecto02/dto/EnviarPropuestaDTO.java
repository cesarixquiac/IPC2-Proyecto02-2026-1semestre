/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dto;

/**
 *
 * @author cesar
 */
public class EnviarPropuestaDTO {
    private Integer idProyecto;
    private Double montoOfertado;
    private Integer plazoDias;
    private String cartaPresentacion;

    public Integer getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Integer idProyecto) { this.idProyecto = idProyecto; }
    public Double getMontoOfertado() { return montoOfertado; }
    public void setMontoOfertado(Double montoOfertado) { this.montoOfertado = montoOfertado; }
    public Integer getPlazoDias() { return plazoDias; }
    public void setPlazoDias(Integer plazoDias) { this.plazoDias = plazoDias; }
    public String getCartaPresentacion() { return cartaPresentacion; }
    public void setCartaPresentacion(String cartaPresentacion) { this.cartaPresentacion = cartaPresentacion; }
}