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
public class CompletarPerfilFreelancerDTO {
    
    private String biografia;
    private String nivelExperiencia; // 'JUNIOR', 'SEMI_SENIOR', 'SENIOR'
    private Double tarifaHora;
    private List<Integer> habilidades; // Recibiremos un arreglo de IDs, ej: [1, 2, 4]

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(String nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }

    public Double getTarifaHora() { return tarifaHora; }
    public void setTarifaHora(Double tarifaHora) { this.tarifaHora = tarifaHora; }

    public List<Integer> getHabilidades() { return habilidades; }
    public void setHabilidades(List<Integer> habilidades) { this.habilidades = habilidades; }
    
}
