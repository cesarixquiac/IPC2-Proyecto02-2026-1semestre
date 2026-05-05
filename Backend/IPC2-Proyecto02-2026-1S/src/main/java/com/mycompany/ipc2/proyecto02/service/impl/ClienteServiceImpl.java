/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

import com.mycompany.ipc2.proyecto02.dao.ClienteDAO;
import com.mycompany.ipc2.proyecto02.dao.impl.ClienteDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilClienteDTO;
import com.mycompany.ipc2.proyecto02.model.Cliente;
import com.mycompany.ipc2.proyecto02.service.ClienteService;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public class ClienteServiceImpl implements ClienteService {

    private final ClienteDAO clienteDao;

    public ClienteServiceImpl() {
        this.clienteDao = new ClienteDaoImpl();
    }

    @Override
    public Cliente completarPerfil(Integer idUsuario, CompletarPerfilClienteDTO dto) throws Exception {
        
        if (clienteDao.existePerfil(idUsuario)) {
            throw new Exception("El perfil de cliente ya ha sido completado anteriormente.");
        }

        Cliente cliente = new Cliente();
        cliente.setIdCliente(idUsuario); // El ID es el mismo de la tabla Usuario (1:1)
        cliente.setDescripcionEmpresa(dto.getDescripcionEmpresa());
        cliente.setSectorIndustria(dto.getSectorIndustria());
        cliente.setSitioWeb(dto.getSitioWeb());

        Cliente perfilCreado = clienteDao.crear(cliente);
        
        if (perfilCreado == null) {
            throw new Exception("Error al guardar el perfil del cliente.");
        }

        return perfilCreado;
    }
    
    @Override
    public void realizarRecarga(Integer idCliente, Double monto) throws Exception {
        if (monto == null || monto <= 0) {
            throw new Exception("El monto a recargar debe ser mayor a cero.");
        }
        
        boolean exito = clienteDao.recargarSaldo(idCliente, monto);
        if (!exito) {
            throw new Exception("Ocurrió un error al procesar la recarga. Intente nuevamente.");
        }
    }
    
    @Override
    public Cliente obtenerPerfil(Integer idCliente) throws Exception {
        Cliente perfil = clienteDao.obtenerPerfil(idCliente);
        if (perfil == null) {
            throw new Exception("No se encontró el perfil en la base de datos.");
        }
        return perfil;
    }
    
    @Override
    public List<Map<String, Object>> obtenerHistorialRecargas(int idCliente) throws Exception {
        // El puente: El service llama al método que acabas de crear en ClienteDaoImpl
        return clienteDao.obtenerHistorialRecargas(idCliente);
    }
    
    
}