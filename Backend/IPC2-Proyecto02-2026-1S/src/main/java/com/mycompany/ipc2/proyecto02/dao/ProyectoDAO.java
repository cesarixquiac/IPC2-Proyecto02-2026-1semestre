/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.model.Proyecto;
import java.util.List;

public interface ProyectoDAO extends CrudDAO<Proyecto, Integer> {
    Proyecto publicarProyectoConHabilidades(Proyecto proyecto, List<Integer> habilidades);
}
