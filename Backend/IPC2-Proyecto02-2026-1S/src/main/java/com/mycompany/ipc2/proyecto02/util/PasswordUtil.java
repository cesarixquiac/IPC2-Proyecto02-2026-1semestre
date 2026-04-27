/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto02.util;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author cesar
 */
public class PasswordUtil {
    
    // Aplica un "salt" de factor 12 para aumentar la resistencia a ataques de fuerza bruta
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Compara la contraseña en texto plano enviada en el login contra el hash almacenado en la BD
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
}
