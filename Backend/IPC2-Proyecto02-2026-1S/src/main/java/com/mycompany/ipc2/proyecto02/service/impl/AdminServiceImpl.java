/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

/**
 *
 * @author cesar
 */
import com.mycompany.ipc2.proyecto02.dao.AdminDAO;
import com.mycompany.ipc2.proyecto02.dao.impl.AdminDaoImpl;
import com.mycompany.ipc2.proyecto02.service.AdminService;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService {
    
    private AdminDAO adminDao;

    public AdminServiceImpl() {
        this.adminDao = new AdminDaoImpl();
    }

    @Override
    public List<Map<String, Object>> obtenerSolicitudesPendientes() throws Exception {
        return adminDao.obtenerSolicitudesPendientes();
    }

    @Override
    public boolean procesarSolicitud(int idSolicitud, String estadoAccion, Integer idCategoriaDestino) throws Exception {
        return adminDao.procesarSolicitud(idSolicitud, estadoAccion, idCategoriaDestino);
    }
    
    @Override
    public Double obtenerComisionActual() throws Exception {
        return adminDao.obtenerComisionActual();
    }

    @Override
    public boolean actualizarComision(double nuevoPorcentaje) throws Exception {
        return adminDao.actualizarComision(nuevoPorcentaje);
    }
    
    @Override
    public List<Map<String, Object>> obtenerCategoriasAdmin() throws Exception {
        return adminDao.obtenerCategoriasAdmin();
    }

    @Override
    public boolean crearCategoria(String nombre, String descripcion) throws Exception {
        return adminDao.crearCategoria(nombre, descripcion);
    }

    @Override
    public boolean editarCategoria(int idCategoria, String nombre, String descripcion) throws Exception {
        return adminDao.editarCategoria(idCategoria, nombre, descripcion);
    }

    @Override
    public boolean cambiarEstadoCategoria(int idCategoria, String nuevoEstado) throws Exception {
        return adminDao.cambiarEstadoCategoria(idCategoria, nuevoEstado);
    }
    
    // --- MÉTODOS DE REPORTES ---

    @Override
    public List<Map<String, Object>> reporteHistorialComisiones() throws Exception {
        return adminDao.reporteHistorialComisiones();
    }

    @Override
    public List<Map<String, Object>> reporteTopFreelancers(String fechaInicio, String fechaFin) throws Exception {
        return adminDao.reporteTopFreelancers(fechaInicio, fechaFin);
    }

    @Override
    public List<Map<String, Object>> reporteTopCategorias(String fechaInicio, String fechaFin) throws Exception {
        return adminDao.reporteTopCategorias(fechaInicio, fechaFin);
    }

    @Override
    public Map<String, Object> reporteIngresosPlataforma(String fechaInicio, String fechaFin) throws Exception {
        return adminDao.reporteIngresosPlataforma(fechaInicio, fechaFin);
    }
    
    // --- MÉTODOS DE HABILIDADES ---
    @Override
    public List<Map<String, Object>> obtenerTodasHabilidadesAdmin() throws Exception {
        return adminDao.obtenerTodasHabilidadesAdmin();
    }

    @Override
    public boolean crearHabilidad(int idCategoria, String nombre) throws Exception {
        return adminDao.crearHabilidad(idCategoria, nombre);
    }

    @Override
    public boolean editarHabilidad(int idHabilidad, int idCategoria, String nombre) throws Exception {
        return adminDao.editarHabilidad(idHabilidad, idCategoria, nombre);
    }

    @Override
    public boolean cambiarEstadoHabilidad(int idHabilidad, String nuevoEstado) throws Exception {
        return adminDao.cambiarEstadoHabilidad(idHabilidad, nuevoEstado);
    }
}
