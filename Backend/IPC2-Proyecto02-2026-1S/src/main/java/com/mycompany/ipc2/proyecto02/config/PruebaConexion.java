/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.config;

/**
 *
 * @author cesar
 */


import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión a la base de datos ...");
        
        
        try (Connection conn = ConexionDB.getConnection()) {
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("¡Éxito! La conexión a la base de datos se ha establecido correctamente.");
                System.out.println("Catálogo actual: " + conn.getCatalog());
            } else {
                System.out.println(" La conexión falló o está cerrada.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al intentar conectar a la base de datos.");
            System.err.println("Código de estado SQL: " + e.getSQLState());
            System.err.println("Mensaje de error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}