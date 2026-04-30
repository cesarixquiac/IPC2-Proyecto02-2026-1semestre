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
import com.mycompany.ipc2.proyecto02.dto.CompletarPerfilFreelancerDTO;
import com.mycompany.ipc2.proyecto02.model.Freelancer;
import com.mycompany.ipc2.proyecto02.service.FreelancerService;
import com.mycompany.ipc2.proyecto02.service.impl.FreelancerServiceImpl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "FreelancerServlet", urlPatterns = {"/api/freelancers/*"})
public class FreelancerServlet extends HttpServlet {

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
            out.println("<title>Servlet FreelancerServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FreelancerServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private FreelancerService freelancerService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.freelancerService = new FreelancerServiceImpl();
        this.gson = new Gson();
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

        if (pathInfo != null && pathInfo.contains("/perfil")) {
            manejarCompletarPerfil(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Ruta de freelancer no encontrada.\"}");
        }
    }
    
    
    private void manejarCompletarPerfil(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer idUsuario = (Integer) req.getAttribute("idUsuario");
            String rol = (String) req.getAttribute("rol");

            if (!"FREELANCER".equals(rol)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"Acceso denegado. Solo cuentas de FREELANCER pueden realizar esta acción.\"}");
                return;
            }

            CompletarPerfilFreelancerDTO dto = gson.fromJson(req.getReader(), CompletarPerfilFreelancerDTO.class);
            Freelancer freelancerCreado = freelancerService.completarPerfil(idUsuario, dto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(freelancerCreado));

        } catch (Exception e) {
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
