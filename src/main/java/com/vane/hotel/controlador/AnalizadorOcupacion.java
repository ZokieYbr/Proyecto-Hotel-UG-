package com.vane.hotel.controlador;

import com.vane.hotel.Config;
import com.vane.hotel.dao.Conexion;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class AnalizadorOcupacion {

    public Map<String, Double> calcularOcupacionHistorica() {
        Map<String, Double> ocupacionMensual = new TreeMap<>();

        int años = Integer.parseInt(Config.get("años_prediccion"));
        LocalDate hoy = LocalDate.now();
        LocalDate fechaInicio = hoy.minusYears(años);

        String sql = "SELECT fecha_entrada, fecha_salida FROM reservaciones WHERE estado = 'finalizada' OR estado = 'activa'";
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String entradaStr = rs.getString("fecha_entrada");
                String salidaStr = rs.getString("fecha_salida");
                LocalDate entrada = parseFechaFlexible(entradaStr);
                LocalDate salida = parseFechaFlexible(salidaStr);

                if (!entrada.isBefore(fechaInicio)) {
                    while (!entrada.isAfter(salida.minusDays(1))) {
                        String claveMes = entrada.getYear() + "-" + String.format("%02d", entrada.getMonthValue());
                        ocupacionMensual.put(claveMes, ocupacionMensual.getOrDefault(claveMes, 0.0) + 1);
                        entrada = entrada.plusDays(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ocupacionMensual;
    }

    public double predecirOcupacion(String mesFuturo) {
        Map<String, Double> historial = calcularOcupacionHistorica();

        double suma = 0;
        int count = 0;
        for (String clave : historial.keySet()) {
            if (clave.endsWith(mesFuturo.substring(5))) {
                suma += historial.get(clave);
                count++;
            }
        }

        return count > 0 ? suma / count : 0.0;
    }

    private LocalDate parseFechaFlexible(String valor) {
        if (valor == null) return null;
        try {
            if (valor.matches("\\d+")) {
                long epoch = Long.parseLong(valor);
                return java.time.Instant.ofEpochMilli(epoch).atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            } else {
                return LocalDate.parse(valor);
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo parsear la fecha: " + valor, e);
        }
    }
}
