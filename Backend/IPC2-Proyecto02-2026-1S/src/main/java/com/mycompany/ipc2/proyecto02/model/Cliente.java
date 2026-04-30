/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.model;

/**
 *
 * @author cesar
 */
public class Cliente {

    private Integer idCliente; 
    private String descripcionEmpresa;
    private String sectorIndustria;
    private String sitioWeb;
    private Double saldoDisponible;

    public Cliente() {
    }

    public Cliente(Integer idCliente, String descripcionEmpresa, String sectorIndustria, String sitioWeb, Double saldoDisponible) {
        this.idCliente = idCliente;
        this.descripcionEmpresa = descripcionEmpresa;
        this.sectorIndustria = sectorIndustria;
        this.sitioWeb = sitioWeb;
        this.saldoDisponible = saldoDisponible;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getDescripcionEmpresa() {
        return descripcionEmpresa;
    }

    public void setDescripcionEmpresa(String descripcionEmpresa) {
        this.descripcionEmpresa = descripcionEmpresa;
    }

    public String getSectorIndustria() {
        return sectorIndustria;
    }

    public void setSectorIndustria(String sectorIndustria) {
        this.sectorIndustria = sectorIndustria;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public Double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(Double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
}
