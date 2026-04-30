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
import com.mycompany.ipc2.proyecto02.dto.CrearEntregaDTO;
import com.mycompany.ipc2.proyecto02.dto.EvaluarEntregaDTO;
import com.mycompany.ipc2.proyecto02.model.Entrega;
import com.mycompany.ipc2.proyecto02.service.EntregaService;
import com.mycompany.ipc2.proyecto02.service.impl.EntregaServiceImpl;

/**
 *
 * @author cesar
 */
@WebServlet(name = "EntregaServlet", urlPatterns = {"/api/entregas/*"})
public class EntregaServlet extends HttpServlet {

    
    private EntregaService entregaService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.entregaService = new EntregaServiceImpl();
        this.gson = new Gson();
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
            out.println("<title>Servlet EntregaServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EntregaServlet at " + request.getContextPath() + "</h1>");
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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.contains("/subir")) {
            manejarSubida(req, resp);
        } else if (pathInfo != null && pathInfo.contains("/evaluar")) {
            manejarEvaluacion(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta de entregas no encontrada.\"}");
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
    
    
    
    private void manejarSubida(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            String rol = (String) req.getAttribute("rol");

            if (!"FREELANCER".equals(rol)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Solo los freelancers pueden subir entregas.\"}");
                return;
            }

            CrearEntregaDTO dto = gson.fromJson(req.getReader(), CrearEntregaDTO.class);
            Entrega entrega = entregaService.subirEntrega(idUsuario, dto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(entrega));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void manejarEvaluacion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            String rol = (String) req.getAttribute("rol");

            if (!"CLIENTE".equals(rol)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Solo los clientes pueden evaluar entregas.\"}");
                return;
            }

            EvaluarEntregaDTO dto = gson.fromJson(req.getReader(), EvaluarEntregaDTO.class);
            entregaService.evaluarEntrega(idUsuario, dto);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"mensaje\": \"La entrega ha sido " + dto.getEstado() + " exitosamente.\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
