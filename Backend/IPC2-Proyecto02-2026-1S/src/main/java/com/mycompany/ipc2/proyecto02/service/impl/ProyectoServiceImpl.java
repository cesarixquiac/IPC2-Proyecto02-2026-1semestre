/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.service.impl;

import com.mycompany.ipc2.proyecto02.dao.ProyectoDAO;
import com.mycompany.ipc2.proyecto02.service.ProyectoService;
import com.mycompany.ipc2.proyecto02.dao.impl.ProyectoDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.PublicarProyectoDTO;
import com.mycompany.ipc2.proyecto02.model.Proyecto;
import com.mycompany.ipc2.proyecto02.service.ProyectoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cesar
 */
public class ProyectoServiceImpl implements ProyectoService {
    
    private final ProyectoDAO proyectoDao;

    public ProyectoServiceImpl() {
        this.proyectoDao = new ProyectoDaoImpl();
    }

    @Override
    public Proyecto publicarProyecto(Integer idCliente, PublicarProyectoDTO dto) throws Exception {
        
        if (dto.getPresupuestoMaximo() == null || dto.getPresupuestoMaximo() <= 0) {
            throw new Exception("El presupuesto debe ser mayor a cero.");
        }
        
        if (dto.getHabilidadesRequeridas() == null || dto.getHabilidadesRequeridas().isEmpty()) {
            throw new Exception("Debe especificar al menos una habilidad requerida para el proyecto.");
        }

        Proyecto proyecto = new Proyecto();
        proyecto.setIdCliente(idCliente);
        proyecto.setIdCategoria(dto.getIdCategoria());
        proyecto.setTitulo(dto.getTitulo());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setPresupuestoMaximo(dto.getPresupuestoMaximo());
        
        try {
            proyecto.setFechaLimite(LocalDate.parse(dto.getFechaLimite()));
        } catch (Exception e) {
            throw new Exception("Formato de fecha inválido. Debe ser YYYY-MM-DD.");
        }

        Proyecto proyectoPublicado = proyectoDao.publicarProyectoConHabilidades(proyecto, dto.getHabilidadesRequeridas());

        if (proyectoPublicado == null) {
            throw new Exception("Error interno al publicar el proyecto.");
        }

        return proyectoPublicado;
    }
    
    
    @Override
    public List<Map<String, Object>> obtenerPropuestasPorProyecto(int idProyecto) {
        return this.proyectoDao.obtenerPropuestasPorProyecto(idProyecto);
    }

    @Override
    public void aceptarPropuesta(int idProyecto, int idPropuesta, int idCliente) throws Exception {
        // Usamos 'proyectoDao' que es tu variable declarada arriba
        this.proyectoDao.aceptarPropuesta(idProyecto, idPropuesta, idCliente);
    }

    @Override
    public void rechazarPropuesta(int idPropuesta) throws Exception {
        // Usamos 'proyectoDao' en minúsculas y sin el "Impl" extra
        this.proyectoDao.rechazarPropuesta(idPropuesta);
    }
}

