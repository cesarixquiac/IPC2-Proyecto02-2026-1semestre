/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.controller;

import com.google.gson.Gson;
import com.mycompany.ipc2.proyecto02.dao.impl.ClienteDaoImpl;
import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilClienteDTO;
import com.mycompany.ipc2.proyecto02.dto.RecargaDTO;
import com.mycompany.ipc2.proyecto02.model.Cliente;
import com.mycompany.ipc2.proyecto02.service.ClienteService;
import com.mycompany.ipc2.proyecto02.service.impl.ClienteServiceImpl;

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
@WebServlet(name = "ClienteServlet", urlPatterns = {"/api/clientes/*"})
public class ClienteServlet extends HttpServlet {

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
            out.println("<title>Servlet ClienteServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ClienteServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private ClienteService clienteService;
    private ClienteDaoImpl clienteDaoImpl;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.clienteService = new ClienteServiceImpl();
        this.clienteDaoImpl = new ClienteDaoImpl();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        // Creamos una sub-ruta para verificar si existe
        if (pathInfo != null && pathInfo.contains("/perfil/existe")) {
            verificarSiExistePerfil(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta de cliente GET no encontrada.\"}");
        }
    }

    private void verificarSiExistePerfil(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            
         
            boolean existe = clienteDaoImpl.existePerfil(idUsuario);

            if (existe) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"existe\": true}");
            } else {
                // Si no existe, mandamos error 404
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"El perfil del cliente aún no ha sido completado.\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

// Manejar null y variaciones de la ruta
       
        if (pathInfo != null && pathInfo.contains("/perfil")) {
            manejarCompletarPerfil(req, resp);
        } else if (pathInfo != null && pathInfo.contains("/recarga")) {
            manejarRecarga(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta de cliente no encontrada.\"}");
        }
    }

    private void manejarCompletarPerfil(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1. Extraer datos de la petición (inyectados por JwtAuthFilter)
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            String rol = (String) req.getAttribute("rol");

            // Validación de seguridad adicional
            if (!"CLIENTE".equals(rol)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Acceso denegado. Solo cuentas de CLIENTE pueden realizar esta acción.\"}");
                return;
            }

            // 2. Leer JSON
            CompletarPerfilClienteDTO dto = gson.fromJson(req.getReader(), CompletarPerfilClienteDTO.class);

            // 3. Ejecutar Lógica
            Cliente clienteCreado = clienteService.completarPerfil(idUsuario, dto);

            // 4. Responder
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(clienteCreado));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    private void manejarRecarga(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            String rol = (String) req.getAttribute("rol");

            if (!"CLIENTE".equals(rol)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Solo los clientes pueden recargar saldo.\"}");
                return;
            }

            RecargaDTO dto = gson.fromJson(req.getReader(), RecargaDTO.class);
            clienteService.realizarRecarga(idUsuario, dto.getMonto());

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"mensaje\": \"Recarga de Q" + dto.getMonto() + " procesada exitosamente.\"}");

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
