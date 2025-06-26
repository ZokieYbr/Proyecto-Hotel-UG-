package com.vane.hotel.modelo;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import com.vane.hotel.dao.Conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReporteIngresos {

    public static void mostrar(int idReservacion) {
        try {
            InputStream input = ReporteIngresos.class.getResourceAsStream("/com/vane/hotel/reportes/Ingresos.jrxml");
            JasperReport reporte = JasperCompileManager.compileReport(input);

            Connection conn = Conexion.conectar();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id_reservacion", idReservacion);

            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conn);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Reporte de Ingresos");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al generar el reporte de ingresos:\n" + e.getMessage());
        }
    }

    public static void exportarPDF(int idReservacion, String ruta) {
        try {
            InputStream input = ReporteIngresos.class.getResourceAsStream("/com/vane/hotel/reportes/Ingresos.jrxml");
            JasperReport reporte = JasperCompileManager.compileReport(input);

            Connection conn = Conexion.conectar();

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id_reservacion", idReservacion);

            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conn);

            JasperExportManager.exportReportToPdfFile(print, ruta);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al exportar el PDF:\n" + e.getMessage());
        }
    }
}
