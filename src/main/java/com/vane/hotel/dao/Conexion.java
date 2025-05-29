package com.vane.hotel.dao;

import com.vane.hotel.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
    public static Connection conectar() {
        String ruta = Config.get("ruta_db");
        String url = "jdbc:sqlite:" + ruta;
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}