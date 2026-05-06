/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dao.FreelancerDao;
import com.mycompany.ipc2.proyecto02.dao.impl.FreelancerDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilFreelancerDTO;
import com.mycompany.ipc2.proyecto02.model.Freelancer;
import com.mycompany.ipc2.proyecto02.service.FreelancerService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public class FreelancerServiceImpl implements FreelancerService {

    private final FreelancerDao freelancerDao;

    public FreelancerServiceImpl() {
        this.freelancerDao = new FreelancerDaoImpl();
    }

    @Override
    public Freelancer completarPerfil(Integer idUsuario, CompletarPerfilFreelancerDTO dto) throws Exception {
        if (freelancerDao.existePerfil(idUsuario)) {
            throw new Exception("El perfil de freelancer ya ha sido completado anteriormente.");
        }

        if (dto.getHabilidades() == null || dto.getHabilidades().isEmpty()) {
            throw new Exception("Debe seleccionar al menos una habilidad del catálogo.");
        }

        Freelancer freelancer = new Freelancer();
        freelancer.setIdFreelancer(idUsuario);
        freelancer.setBiografia(dto.getBiografia());
        freelancer.setNivelExperiencia(dto.getNivelExperiencia());
        freelancer.setTarifaHora(dto.getTarifaHora());

        Freelancer perfilCreado = freelancerDao.crear(freelancer);
        
        if (perfilCreado == null) {
            throw new Exception("Error al guardar el perfil del freelancer.");
        }

        // Si el perfil se guardó, asignamos las habilidades
        boolean habilidadesAsignadas = freelancerDao.asignarHabilidades(idUsuario, dto.getHabilidades());
        
        if (!habilidadesAsignadas) {
            // En un sistema estricto, aquí haríamos un Rollback, pero para este alcance lanzamos la excepción
            throw new Exception("El perfil se creó, pero ocurrió un error al asignar las habilidades.");
        }

        return perfilCreado;
    }
    
    @Override
    public boolean tienePerfilCompleto(int idUsuario) throws Exception {
        // Aquí hacemos el puente: el Service simplemente le pasa la pelota al DAO
        return freelancerDao.tienePerfilCompleto(idUsuario);
    }
    
    @Override
    public double obtenerSaldo(int idFreelancer) throws Exception {
        double saldo = 0.00;
        // Seleccionamos la columna de tu base de datos (verifica si se llama saldo o saldo_acumulado)
        String sql = "SELECT saldo_acumulado FROM freelancer WHERE id_freelancer = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFreelancer);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    saldo = rs.getDouble("saldo_acumulado");
                }
            }
        }
        return saldo;
    }
    
    @Override
    public List<Map<String, Object>> obtenerHistorialGanancias(int idFreelancer) throws Exception {
        // El puente: le decimos al DAO que haga la consulta SQL y nos devuelva la lista
        return freelancerDao.obtenerHistorialGanancias(idFreelancer);
    }
    
    // --- MÉTODOS PARA REPORTES ---

    @Override
    public List<Map<String, Object>> obtenerReporteContratosCompletados(int idFreelancer, String fechaInicio, String fechaFin) throws Exception {
        return freelancerDao.obtenerReporteContratosCompletados(idFreelancer, fechaInicio, fechaFin);
    }

    @Override
    public List<Map<String, Object>> obtenerReporteTopCategorias(int idFreelancer) throws Exception {
        return freelancerDao.obtenerReporteTopCategorias(idFreelancer);
    }

    @Override
    public List<Map<String, Object>> obtenerReportePropuestasEnviadas(int idFreelancer, String fechaInicio, String fechaFin) throws Exception {
        return freelancerDao.obtenerReportePropuestasEnviadas(idFreelancer, fechaInicio, fechaFin);
    }
    
    @Override
    public boolean solicitarNuevaHabilidad(int idFreelancer, String nombre, String descripcion) throws Exception {
        return freelancerDao.solicitarNuevaHabilidad(idFreelancer, nombre, descripcion);
    }
}