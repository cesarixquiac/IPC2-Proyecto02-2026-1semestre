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

        // Verificamos si la ruta pedida es "/disponibles"
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/disponibles")) {

            // Traemos solo los proyectos que estén disponibles (Asumiendo que el estado inicial es 'ABIERTO' o 'ACTIVO')
            String sql = "SELECT id_proyecto, titulo, descripcion, presupuesto_maximo, fecha_limite "
                    + "FROM proyecto WHERE estado = 'ABIERTO' ORDER BY fecha_publicacion DESC";

            List<Map<String, Object>> proyectos = new ArrayList<>();

            try (Connection conn = ConexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("id", rs.getInt("id_proyecto"));
                    p.put("titulo", rs.getString("titulo"));
                    p.put("descripcion", rs.getString("descripcion"));
                    // Nombres exactos que espera el frontend en Angular:
                    p.put("presupuestoMaximo", rs.getDouble("presupuesto_maximo"));
                    p.put("fechaLimite", rs.getString("fecha_limite"));

                    proyectos.add(p);
                }

                out.print(new Gson().toJson(proyectos));

            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error al obtener proyectos: " + e.getMessage());
                out.print(new Gson().toJson(errorResponse));
            } finally {
                out.flush();
            }

        } else if (pathInfo != null && pathInfo.equals("/mis-proyectos")) {

            // Sacamos el ID del cliente desde el Token JWT
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
            } finally {
                out.flush();
            }
        } else if (pathInfo != null && pathInfo.matches("/\\d+/propuestas")) {
            try {
                // Extraemos el número de la ruta (ej: de "/5/propuestas" sacamos el "5")
                String[] partes = pathInfo.split("/");
                int idProyecto = Integer.parseInt(partes[1]);

                // Aquí usamos el servicio que acabas de crear
                List<Map<String, Object>> propuestas = proyectoService.obtenerPropuestasPorProyecto(idProyecto);

                out.print(new Gson().toJson(propuestas));

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID de proyecto inválido.\"}");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error al obtener propuestas: " + e.getMessage() + "\"}");
            } finally {
                out.flush();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("[]");
            out.flush();
        }

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
        } // El "No encontrado" solo si no entró en ninguna de las anteriores
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
