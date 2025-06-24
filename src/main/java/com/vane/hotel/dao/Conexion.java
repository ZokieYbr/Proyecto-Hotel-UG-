package com.vane.hotel.dao;

import com.vane.hotel.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Connection instancia;

    private Conexion() {
        // Evitar instanciación
    }

    public static Connection conectar() {
        try {
            if (instancia == null || instancia.isClosed()) {
                String ruta = Config.get("ruta_db");
                String url = "jdbc:sqlite:" + ruta;
                instancia = DriverManager.getConnection(url);
                System.out.println("Conexión creada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
        return instancia;
    }

    public static void cerrar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                System.out.println("Conexión cerrada.");
            }
            instancia = null;
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
