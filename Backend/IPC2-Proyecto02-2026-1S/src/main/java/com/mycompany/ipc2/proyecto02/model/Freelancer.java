/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.model;

/**
 *
 * @author cesar
 */

public class Freelancer {
    private Integer idFreelancer;
    private String biografia;
    private String nivelExperiencia;
    private Double tarifaHora;
    private Double saldoAcumulado;

    public Freelancer() {}

    public Integer getIdFreelancer() { return idFreelancer; }
    public void setIdFreelancer(Integer idFreelancer) { this.idFreelancer = idFreelancer; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(String nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }
    public Double getTarifaHora() { return tarifaHora; }
    public void setTarifaHora(Double tarifaHora) { this.tarifaHora = tarifaHora; }
    public Double getSaldoAcumulado() { return saldoAcumulado; }
    public void setSaldoAcumulado(Double saldoAcumulado) { this.saldoAcumulado = saldoAcumulado; }
}
