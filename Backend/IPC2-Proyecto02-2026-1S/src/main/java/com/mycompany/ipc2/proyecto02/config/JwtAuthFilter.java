/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.config;

import com.mycompany.ipc2.proyecto02.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Intercepta todas las llamadas a la API
@WebFilter("/api/*")
public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getPathInfo();
        String servletPath = req.getServletPath();

        // 1. Dejar pasar peticiones CORS (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Rutas públicas (Login y Registro) no requieren token
        if (servletPath.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Validar existencia del encabezado Authorization
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            responderError(res, HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado. Token no proporcionado o formato incorrecto.");
            return;
        }

        // 4. Extraer y validar el Token
        String token = authHeader.substring(7); // Quitar el prefijo "Bearer "

        try {
            Claims claims = JwtUtil.validateTokenAndGetClaims(token);

            // 5. Inyectar los datos del usuario en la petición para que los Servlets los usen
            req.setAttribute("username", claims.getSubject());
            req.setAttribute("rol", claims.get("rol", String.class));
            req.setAttribute("idUsuario", claims.get("idUsuario", Integer.class));

            // Continuar con la petición hacia el Servlet correspondiente
            chain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException e) {
            responderError(res, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado. Por favor, inicie sesión nuevamente.");
        }
    }

    private void responderError(HttpServletResponse res, int statusCode, String mensaje) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setStatus(statusCode);
        res.getWriter().write("{\"error\": \"" + mensaje + "\"}");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}