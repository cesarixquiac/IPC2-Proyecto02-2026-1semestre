/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.mycompany.ipc2.proyecto02.config.ConexionDB;
import com.mycompany.ipc2.proyecto02.dto.PublicarProyectoDTO;
import com.mycompany.ipc2.proyecto02.model.Proyecto;
import com.mycompany.ipc2.proyecto02.service.ProyectoService;
import com.mycompany.ipc2.proyecto02.service.impl.ProyectoServiceImpl;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "ProyectoServlet", urlPatterns = {"/api/proyectos", "/api/proyectos/*"})
public class ProyectoServlet extends HttpServlet {

    private ProyectoService proyectoService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.proyectoService = new ProyectoServiceImpl();
        

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public com.google.gson.JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                        return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
                        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                })
                .create();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProyectoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProyectoServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/disponibles")) {
            try {
                // Obtenemos el ID del freelancer que está logueado
                Integer idFreelancer = (Integer) request.getAttribute("idUsuario"); 
                
                // Llamamos al servicio (que llama al DAO con la consulta avanzada)
                List<Map<String, Object>> proyectos = proyectoService.obtenerProyectosDisponibles(idFreelancer);
                
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(proyectos));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error al obtener proyectos: " + e.getMessage() + "\"}");
            }
        } else if (pathInfo != null && pathInfo.equals("/mis-proyectos")) {
            // ... (Tu código de /mis-proyectos se queda igual) ...
            Integer idCliente = (Integer) request.getAttribute("idUsuario");
            String sql = "SELECT id_proyecto, titulo, descripcion, presupuesto_maximo, estado, fecha_limite "
                    + "FROM proyecto WHERE id_cliente = ? ORDER BY fecha_publicacion DESC";
            List<Map<String, Object>> misProyectos = new ArrayList<>();
            try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idCliente);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> p = new HashMap<>();
                        p.put("id", rs.getInt("id_proyecto"));
                        p.put("titulo", rs.getString("titulo"));
                        p.put("descripcion", rs.getString("descripcion"));
                        p.put("presupuestoMaximo", rs.getDouble("presupuesto_maximo"));
                        p.put("estado", rs.getString("estado"));
                        p.put("fechaLimite", rs.getString("fecha_limite"));
                        misProyectos.add(p);
                    }
                }
                out.print(new Gson().toJson(misProyectos));
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error al obtener los proyectos: " + e.getMessage() + "\"}");
            }

        } else if (pathInfo != null && pathInfo.matches("/\\d+/propuestas")) {
         
            try {
                String[] partes = pathInfo.split("/");
                int idProyecto = Integer.parseInt(partes[1]);
                List<Map<String, Object>> propuestas = proyectoService.obtenerPropuestasPorProyecto(idProyecto);
                out.print(new Gson().toJson(propuestas));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID de proyecto inválido.\"}");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error al obtener propuestas: " + e.getMessage() + "\"}");
            }

        } 
        else if (pathInfo != null && pathInfo.matches("^/\\d+/entregas$")) {
            try {
                int idProyecto = Integer.parseInt(pathInfo.split("/")[1]);
                Map<String, Object> detalle = proyectoService.obtenerDetalleEntrega(idProyecto);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(detalle));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            
        } else if (pathInfo != null && pathInfo.equals("/mis-contratos")) {
            try {
                Integer idFreelancer = (Integer) request.getAttribute("idUsuario");
                List<Map<String, Object>> contratos = proyectoService.obtenerContratosActivosFreelancer(idFreelancer);
                
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(contratos));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("[]");
        }
        
        out.flush();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        // Separamos la ruta para poder evaluar partes numéricas
        String[] parts = pathInfo != null ? pathInfo.split("/") : new String[0];

        if (pathInfo != null && pathInfo.contains("/publicar")) {
            manejarPublicacion(req, resp);
            
        } else if (pathInfo != null && pathInfo.equals("/propuestas/aceptar")) {
            try {
                Integer idCliente = (Integer) req.getAttribute("idUsuario");
                Map<String, Object> datos = new Gson().fromJson(req.getReader(), Map.class);
                int idProyecto = ((Number) datos.get("idProyecto")).intValue();
                int idPropuesta = ((Number) datos.get("idPropuesta")).intValue();

                proyectoService.aceptarPropuesta(idProyecto, idPropuesta, idCliente);

                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Propuesta aceptada con éxito.\"}");
            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            
        } else if (pathInfo != null && pathInfo.equals("/propuestas/rechazar")) {
            try {
                Map<String, Object> datos = new Gson().fromJson(req.getReader(), Map.class);
                int idPropuesta = ((Number) datos.get("idPropuesta")).intValue();

                proyectoService.rechazarPropuesta(idPropuesta);

                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Propuesta rechazada.\"}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            
        } // RUTAS DE ENTREGAS UNIFICADAS AQUÍ:
        else if (parts.length == 4 && parts[2].equals("entregas")) {
            int idProyecto = Integer.parseInt(parts[1]);
            String accion = parts[3];

            try {
                if (accion.equals("aprobar")) {
                    // 1. Leemos el JSON que nos manda Angular con las estrellas y comentario
                    Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                    
                    // 2. Extraemos los valores (con cuidado de parsear el número correctamente)
                    int estrellas = 5; // Valor por defecto por si acaso
                    if (body.get("estrellas") != null) {
                        estrellas = ((Number) body.get("estrellas")).intValue();
                    }
                    
                    String comentario = "";
                    if (body.get("comentario") != null) {
                        comentario = (String) body.get("comentario");
                    }

                    // 3. Llamamos al servicio con los nuevos datos
                    proyectoService.aprobarEntrega(idProyecto, estrellas, comentario);
                    
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"mensaje\": \"Entrega aprobada exitosamente. Se procesó el pago y la calificación.\"}");

                } else if (accion.equals("rechazar")) {
                    Map<String, String> body = new Gson().fromJson(req.getReader(), Map.class);
                    String motivo = body.get("motivo");

                    proyectoService.rechazarEntrega(idProyecto, motivo);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"mensaje\": \"Entrega rechazada. Se notificará al freelancer.\"}");
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            
        } // RUTA DE CANCELAR UNIFICADA AQUÍ:
        else if (parts.length == 3 && parts[2].equals("cancelar")) {
            try {
                int idProyecto = Integer.parseInt(parts[1]);
                Integer idCliente = (Integer) req.getAttribute("idUsuario");

                Map<String, String> body = new Gson().fromJson(req.getReader(), Map.class);
                String motivo = body.get("motivo");

                proyectoService.cancelarContrato(idProyecto, motivo, idCliente);

                // ¡Corregido! Era resp.setStatus, no req.setStatus
                resp.setStatus(HttpServletResponse.SC_OK); 
                out.print("{\"mensaje\": \"Contrato cancelado y fondos devueltos exitosamente.\"}");
            } catch (Exception e) {
                // ¡Corregido! Era resp.setStatus, no req.setStatus
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            
        }else if (parts.length == 3 && parts[2].equals("editar")) {
            try {
                int idProyecto = Integer.parseInt(parts[1]);
                Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                
                String titulo = (String) body.get("titulo");
                String descripcion = (String) body.get("descripcion");
                double presupuesto = ((Number) body.get("presupuestoMaximo")).doubleValue();
                String fechaLimite = (String) body.get("fechaLimite");

                proyectoService.editarProyectoAbierto(idProyecto, titulo, descripcion, presupuesto, fechaLimite);

                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Proyecto editado exitosamente.\"}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        } 
        // Ruta: /api/proyectos/{id}/eliminar
        else if (parts.length == 3 && parts[2].equals("eliminar")) {
            try {
                int idProyecto = Integer.parseInt(parts[1]);
                proyectoService.eliminarProyectoAbierto(idProyecto);

                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"mensaje\": \"Proyecto eliminado exitosamente.\"}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
        else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Ruta de proyectos no encontrada.\"}");
        }

        out.flush();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void manejarPublicacion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            String rol = (String) req.getAttribute("rol");

            if (!"CLIENTE".equals(rol)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Solo los clientes pueden publicar proyectos.\"}");
                return;
            }

            PublicarProyectoDTO dto = gson.fromJson(req.getReader(), PublicarProyectoDTO.class);
            Proyecto proyectoCreado = proyectoService.publicarProyecto(idUsuario, dto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(proyectoCreado));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
