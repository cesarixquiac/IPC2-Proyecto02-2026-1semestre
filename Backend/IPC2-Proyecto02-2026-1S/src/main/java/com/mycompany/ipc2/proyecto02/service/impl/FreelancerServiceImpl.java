/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

import com.mycompany.ipc2.proyecto02.dao.FreelancerDao;
import com.mycompany.ipc2.proyecto02.dao.impl.FreelancerDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilFreelancerDTO;
import com.mycompany.ipc2.proyecto02.model.Freelancer;
import com.mycompany.ipc2.proyecto02.service.FreelancerService;

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
}