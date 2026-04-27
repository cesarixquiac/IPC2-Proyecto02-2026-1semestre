/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.controller;

import com.google.gson.Gson;
import com.mycompany.ipc2.proyecto02.dto.AuthResponseDTO;
import com.mycompany.ipc2.proyecto02.dto.LoginRequestDTO;
import com.mycompany.ipc2.proyecto02.model.Usuario;
import com.mycompany.ipc2.proyecto02.service.impl.UsuarioServiceImpl;
import com.mycompany.ipc2.proyecto02.service.UsuarioService;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/api/auth/*"})
public class AuthServlet extends HttpServlet {

    private UsuarioService usuarioService;
    private Gson gson;
    
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
            out.println("<title>Servlet AuthServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    
    
   @Override
    public void init() throws ServletException {
        this.usuarioService = new UsuarioServiceImpl();
        
        // Configuramos Gson usando clases anónimas
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
        processRequest(request, response);
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
        // Configurar cabeceras de respuesta estándar para JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Obtener la ruta específica (ej. /login o /registro)
        String pathInfo = req.getPathInfo();

        if ("/login".equals(pathInfo)) {
            manejarLogin(req, resp);
        } else if ("/registro".equals(pathInfo)) {
            manejarRegistro(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta no encontrada\"}");
        }
    }
    
    private void manejarLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1. Leer el JSON del cuerpo de la petición
            LoginRequestDTO loginDto = gson.fromJson(req.getReader(), LoginRequestDTO.class);

            // 2. Ejecutar lógica de negocio
            AuthResponseDTO authResponse = usuarioService.autenticarUsuario(loginDto);

            // 3. Responder con éxito (200 OK)
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(authResponse));

        } catch (Exception e) {
            // Manejo de credenciales inválidas (401 Unauthorized)
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void manejarRegistro(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1. Deserializar el usuario completo para el registro
            Usuario nuevoUsuario = gson.fromJson(req.getReader(), Usuario.class);

            // 2. Ejecutar lógica de registro
            Usuario usuarioCreado = usuarioService.registrarUsuario(nuevoUsuario);

            // 3. Responder con éxito (201 Created)
            // Se debe evitar enviar la contraseña hasheada en la respuesta
            usuarioCreado.setPassword(null);
            
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(usuarioCreado));

        } catch (Exception e) {
            // Manejo de errores como correo duplicado (400 Bad Request)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
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

}
