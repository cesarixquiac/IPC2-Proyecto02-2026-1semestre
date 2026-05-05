/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

import com.mycompany.ipc2.proyecto02.model.Freelancer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public interface FreelancerDao extends CrudDAO<Freelancer, Integer> {
    boolean existePerfil(Integer idFreelancer);
    boolean asignarHabilidades(Integer idFreelancer, List<Integer> habilidades);
    boolean tienePerfilCompleto(int idUsuario) throws Exception;
   
    double obtenerSaldo(int idFreelancer) throws Exception;
    // Método para obtener el historial de ganancias de los proyectos completados
    List<Map<String, Object>> obtenerHistorialGanancias(int idFreelancer) throws Exception;
}