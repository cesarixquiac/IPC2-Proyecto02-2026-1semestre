/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author cesar
 */
public interface CrudDAO<T, ID> {
    
    T crear(T entidad);
    
    Optional<T> obtenerPorId(ID id);
    
    List<T> obtenerTodos();
    
    boolean actualizar(T entidad);
    
    boolean eliminar(ID id);
}
