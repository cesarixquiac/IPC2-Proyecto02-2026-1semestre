/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

import com.mycompany.ipc2.proyecto02.model.Freelancer;
import java.util.List;

/**
 *
 * @author cesar
 */
public interface FreelancerDao extends CrudDAO<Freelancer, Integer> {
    boolean existePerfil(Integer idFreelancer);
    boolean asignarHabilidades(Integer idFreelancer, List<Integer> habilidades);
}