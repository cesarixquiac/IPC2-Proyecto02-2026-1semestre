/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.dao;
import com.mycompany.ipc2.proyecto02.model.Cliente;
import java.util.List;
import java.util.Map;

public interface ClienteDAO extends CrudDAO<Cliente, Integer> {
    boolean existePerfil(Integer idCliente);
    boolean recargarSaldo(Integer idCliente, Double monto);
    Cliente obtenerPerfil(Integer idCliente);
    List<Map<String, Object>> obtenerHistorialRecargas(int idCliente) throws Exception;
}
