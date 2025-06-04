package com.vane.hotel.controlador;

import com.vane.hotel.dao.Conexion;
import com.vane.hotel.modelo.Habitacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CHabitacion {

    private ObservableList<Habitacion> listaHabitaciones = FXCollections.observableArrayList();

    public ObservableList<Habitacion> getHabitaciones() {
        listaHabitaciones.clear();
        String sql = "SELECT * FROM habitaciones";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Habitacion habitacion = new Habitacion(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getString("tipo"),
                        rs.getDouble("precio_noche"),
                        rs.getString("estado")
                );
                listaHabitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaHabitaciones;
    }

    public boolean registrarHabitacion(Habitacion habitacion) {
        String sql = "INSERT INTO habitaciones(numero, tipo, precio_noche, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, habitacion.getNumero());
            pstmt.setString(2, habitacion.getTipo());
            pstmt.setDouble(3, habitacion.getprecio_noche());
            pstmt.setString(4, habitacion.getEstado());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarHabitacion(Habitacion habitacion) {
        String sql = "UPDATE habitaciones SET numero=?, tipo=?, precio_noche=?, estado=? WHERE id=?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, habitacion.getNumero());
            pstmt.setString(2, habitacion.getTipo());
            pstmt.setDouble(3, habitacion.getprecio_noche());
            pstmt.setString(4, habitacion.getEstado());
            pstmt.setInt(5, habitacion.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarHabitacion(int idHabitacion) {
        String sql = "DELETE FROM habitaciones WHERE id=?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idHabitacion);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificarDisponibilidad(int habitacionId, java.sql.Date fechaEntrada, java.sql.Date fechaSalida) {
        String sql = "SELECT COUNT(*) FROM reservaciones WHERE habitacion_id = ? AND ((fecha_entrada <= ? AND fecha_salida >= ?) OR (fecha_entrada <= ? AND fecha_salida >= ?))";
        try (Connection conn = Conexion.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, habitacionId);
            pstmt.setDate(2, fechaEntrada);
            pstmt.setDate(3, fechaEntrada);
            pstmt.setDate(4, fechaSalida);
            pstmt.setDate(5, fechaSalida);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Devuelve true si no hay conflictos
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

