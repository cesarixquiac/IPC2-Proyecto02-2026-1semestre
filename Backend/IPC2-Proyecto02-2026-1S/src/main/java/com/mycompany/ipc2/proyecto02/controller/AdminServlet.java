/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.controller;

import com.google.gson.Gson;
import com.mycompany.ipc2.proyecto02.service.AdminService;
import com.mycompany.ipc2.proyecto02.service.impl.AdminServiceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cesar
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/api/admin/*"})
public class AdminServlet extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        this.adminService = new AdminServiceImpl();
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
            out.println("<title>Servlet AdminServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/solicitudes")) {
            try {
                List<Map<String, Object>> solicitudes = adminService.obtenerSolicitudesPendientes();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(solicitudes));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        } 
        else if (pathInfo != null && pathInfo.equals("/comision-actual")) {
            try {
                Double porcentaje = adminService.obtenerComisionActual();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"porcentaje\": " + porcentaje + "}");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        } 
        else if (pathInfo != null && pathInfo.equals("/categorias")) {
            try {
                List<Map<String, Object>> categorias = adminService.obtenerCategoriasAdmin();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(categorias));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        }// REPORTE 1: Historial de Comisiones
        else if (pathInfo != null && pathInfo.equals("/reportes/comisiones")) {
            try {
                List<Map<String, Object>> reporte = adminService.reporteHistorialComisiones();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(reporte));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        } 
        // REPORTE 2: Top Freelancers
        else if (pathInfo != null && pathInfo.equals("/reportes/top-freelancers")) {
            try {
                String inicio = req.getParameter("inicio");
                String fin = req.getParameter("fin");
                List<Map<String, Object>> reporte = adminService.reporteTopFreelancers(inicio, fin);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(reporte));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        } 
        // REPORTE 3: Top Categorías
        else if (pathInfo != null && pathInfo.equals("/reportes/top-categorias")) {
            try {
                String inicio = req.getParameter("inicio");
                String fin = req.getParameter("fin");
                List<Map<String, Object>> reporte = adminService.reporteTopCategorias(inicio, fin);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(reporte));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        } 
        // REPORTE 4: Ingresos de la Plataforma
        else if (pathInfo != null && pathInfo.equals("/reportes/ingresos")) {
            try {
                String inicio = req.getParameter("inicio");
                String fin = req.getParameter("fin");
                Map<String, Object> reporte = adminService.reporteIngresosPlataforma(inicio, fin);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(reporte));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        }else if (pathInfo != null && pathInfo.equals("/habilidades")) {
            try {
                List<Map<String, Object>> habilidades = adminService.obtenerTodasHabilidadesAdmin();
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print(new Gson().toJson(habilidades));
            } catch (Exception e) {
                resp.setStatus(500); out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
            out.flush();
        } 
        else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Ruta GET de admin no encontrada. Ruta recibida: " + pathInfo + "\"}");
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

        if (pathInfo != null && pathInfo.contains("/procesar-solicitud")) {
            try {
                // Leemos el JSON recibido
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = req.getReader().readLine()) != null) {
                    sb.append(line);
                }

                Map<String, Object> body = new Gson().fromJson(sb.toString(), Map.class);

                // Parseo a prueba de balas: convertimos a String, luego a Double y sacamos el entero
                int idSolicitud = Double.valueOf(body.get("idSolicitud").toString()).intValue();
                String accion = body.get("accion").toString();

                Integer idCategoria = null;
                if (body.get("idCategoria") != null) {
                    String catStr = body.get("idCategoria").toString();
                    if (!catStr.isEmpty() && !catStr.equals("null")) {
                        idCategoria = Double.valueOf(catStr).intValue();
                    }
                }

                boolean exito = adminService.procesarSolicitud(idSolicitud, accion, idCategoria);

                if (exito) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"mensaje\": \"Solicitud procesada correctamente\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"No se pudo procesar la solicitud\"}");
                }

            } catch (Exception e) {
                // ESTO IMPRIMIRÁ EL ERROR REAL EN LA CONSOLA DE NETBEANS
                System.out.println("====== ERROR EN ADMIN SERVLET ======");
                e.printStackTrace();
                System.out.println("====================================");

                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                // Le devolvemos un JSON válido a Angular para que no diga "undefined"
                out.print("{\"error\": \"Error interno en el servidor: " + e.getMessage() + "\"}");
            }
            out.flush();
        } else if (pathInfo != null && pathInfo.contains("/actualizar-comision")) {
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = req.getReader().readLine()) != null) {
                    sb.append(line);
                }

                Map<String, Object> body = new Gson().fromJson(sb.toString(), Map.class);

                // Parseo seguro del nuevo porcentaje
                double nuevoPorcentaje = 0.0;
                if (body.get("porcentaje") != null) {
                    nuevoPorcentaje = Double.parseDouble(body.get("porcentaje").toString());
                }

                if (nuevoPorcentaje <= 0 || nuevoPorcentaje >= 100) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"El porcentaje debe ser mayor a 0 y menor a 100\"}");
                    out.flush();
                    return;
                }

                boolean exito = adminService.actualizarComision(nuevoPorcentaje);

                if (exito) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"mensaje\": \"Porcentaje de comisión actualizado correctamente\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.print("{\"error\": \"No se pudo actualizar la comisión\"}");
                }

            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"Error interno: " + e.getMessage() + "\"}");
            }
            out.flush();
        }// CREAR CATEGORÍA
        else if (pathInfo != null && pathInfo.contains("/crear-categoria")) {
            try {
                Map<String, String> body = new Gson().fromJson(req.getReader(), Map.class);
                boolean exito = adminService.crearCategoria(body.get("nombre"), body.get("descripcion"));
                if (exito) {
                    out.print("{\"mensaje\": \"Categoría creada\"}");
                } else {
                    resp.setStatus(400);
                    out.print("{\"error\": \"Error al crear\"}");
                }
            } catch (Exception e) {
                resp.setStatus(500);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        } // EDITAR CATEGORÍA
        else if (pathInfo != null && pathInfo.contains("/editar-categoria")) {
            try {
                Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                int id = Double.valueOf(body.get("idCategoria").toString()).intValue();
                boolean exito = adminService.editarCategoria(id, (String) body.get("nombre"), (String) body.get("descripcion"));
                if (exito) {
                    out.print("{\"mensaje\": \"Categoría actualizada\"}");
                } else {
                    resp.setStatus(400);
                    out.print("{\"error\": \"Error al editar\"}");
                }
            } catch (Exception e) {
                resp.setStatus(500);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        } // CAMBIAR ESTADO CATEGORÍA
        else if (pathInfo != null && pathInfo.contains("/estado-categoria")) {
            try {
                Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                int id = Double.valueOf(body.get("idCategoria").toString()).intValue();
                boolean exito = adminService.cambiarEstadoCategoria(id, (String) body.get("estado"));
                if (exito) {
                    out.print("{\"mensaje\": \"Estado actualizado\"}");
                } else {
                    resp.setStatus(400);
                    out.print("{\"error\": \"Error al cambiar estado\"}");
                }
            } catch (Exception e) {
                resp.setStatus(500);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }// CREAR HABILIDAD
        else if (pathInfo != null && pathInfo.contains("/crear-habilidad")) {
            try {
                Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                int idCat = Double.valueOf(body.get("idCategoria").toString()).intValue();
                boolean exito = adminService.crearHabilidad(idCat, (String)body.get("nombre"));
                if (exito) out.print("{\"mensaje\": \"Habilidad creada\"}");
                else { resp.setStatus(400); out.print("{\"error\": \"Error al crear\"}"); }
            } catch (Exception e) { resp.setStatus(500); out.print("{\"error\": \"" + e.getMessage() + "\"}"); }
        }
        // EDITAR HABILIDAD
        else if (pathInfo != null && pathInfo.contains("/editar-habilidad")) {
            try {
                Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                int idHab = Double.valueOf(body.get("idHabilidad").toString()).intValue();
                int idCat = Double.valueOf(body.get("idCategoria").toString()).intValue();
                boolean exito = adminService.editarHabilidad(idHab, idCat, (String)body.get("nombre"));
                if (exito) out.print("{\"mensaje\": \"Habilidad actualizada\"}");
                else { resp.setStatus(400); out.print("{\"error\": \"Error al editar\"}"); }
            } catch (Exception e) { resp.setStatus(500); out.print("{\"error\": \"" + e.getMessage() + "\"}"); }
        }
        // CAMBIAR ESTADO HABILIDAD
        else if (pathInfo != null && pathInfo.contains("/estado-habilidad")) {
            try {
                Map<String, Object> body = new Gson().fromJson(req.getReader(), Map.class);
                int idHab = Double.valueOf(body.get("idHabilidad").toString()).intValue();
                boolean exito = adminService.cambiarEstadoHabilidad(idHab, (String)body.get("estado"));
                if (exito) out.print("{\"mensaje\": \"Estado de habilidad actualizado\"}");
                else { resp.setStatus(400); out.print("{\"error\": \"Error al cambiar estado\"}"); }
            } catch (Exception e) { resp.setStatus(500); out.print("{\"error\": \"" + e.getMessage() + "\"}"); }
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
