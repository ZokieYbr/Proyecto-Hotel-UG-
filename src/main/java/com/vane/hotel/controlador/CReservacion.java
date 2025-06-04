package com.vane.hotel.controlador;

import com.vane.hotel.dao.Conexion;
import com.vane.hotel.modelo.Reservacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CReservacion {

    public ObservableList<Reservacion> getReservaciones() {
        ObservableList<Reservacion> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reservaciones";
        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String fechaEntradaStr = rs.getString("fecha_entrada");
                String fechaSalidaStr = rs.getString("fecha_salida");
                java.sql.Date fechaEntrada = null;
                java.sql.Date fechaSalida = null;
                try {
                    if (fechaEntradaStr != null && !fechaEntradaStr.isEmpty()) {
                        if (fechaEntradaStr.matches("^\\d+$")) {
                            fechaEntrada = new java.sql.Date(Long.parseLong(fechaEntradaStr));
                        } else {
                            fechaEntrada = java.sql.Date.valueOf(fechaEntradaStr);
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Fecha de entrada inválida: '" + fechaEntradaStr + "' en reservación ID: " + rs.getInt("id"));
                    continue;
                }
                try {
                    if (fechaSalidaStr != null && !fechaSalidaStr.isEmpty()) {
                        if (fechaSalidaStr.matches("^\\d+$")) {
                            fechaSalida = new java.sql.Date(Long.parseLong(fechaSalidaStr));
                        } else {
                            fechaSalida = java.sql.Date.valueOf(fechaSalidaStr);
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Fecha de salida inválida: '" + fechaSalidaStr + "' en reservación ID: " + rs.getInt("id"));
                    continue;
                }
                lista.add(new Reservacion(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("habitacion_id"),
                        fechaEntrada,
                        fechaSalida,
                        rs.getString("estado")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean registrarReservacion(Reservacion r) {
        String sql = "INSERT INTO reservaciones (cliente_id, habitacion_id, fecha_entrada, fecha_salida, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getCliente_id());
            ps.setInt(2, r.getHabitacion_id());
            ps.setDate(3, new java.sql.Date(r.getFecha_entrada().getTime()));
            ps.setDate(4, new java.sql.Date(r.getFecha_salida().getTime()));
            ps.setString(5, r.getEstado());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarReservacion(Reservacion r) {
        String sql = "UPDATE reservaciones SET cliente_id=?, habitacion_id=?, fecha_entrada=?, fecha_salida=?, estado=? WHERE id=?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getCliente_id());
            ps.setInt(2, r.getHabitacion_id());
            ps.setDate(3, new java.sql.Date(r.getFecha_entrada().getTime()));
            ps.setDate(4, new java.sql.Date(r.getFecha_salida().getTime()));
            ps.setString(5, r.getEstado());
            ps.setInt(6, r.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarReservacion(int id) {
        String sql = "DELETE FROM reservaciones WHERE id=?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

