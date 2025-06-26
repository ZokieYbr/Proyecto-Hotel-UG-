package com.vane.hotel.modelo;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportePrediccion {

    public static void mostrar(List<String> meses, List<Double> ocupacionReal, List<Double> ocupacionPredicha) {
        try {
            InputStream input = ReportePrediccion.class.getResourceAsStream("/com/vane/hotel/reportes/PrediccionOcupacion.jrxml");
            JasperReport reporte = JasperCompileManager.compileReport(input);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("meses", meses);
            parametros.put("ocupacion_real", ocupacionReal);
            parametros.put("ocupacion_predicha", ocupacionPredicha);

            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, new JREmptyDataSource());

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Predicción de Ocupación");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al generar el reporte de predicción:\n" + e.getMessage());
        }
    }

    public static void exportarPDF(List<String> meses, List<Double> ocupacionReal, List<Double> ocupacionPredicha, String ruta) {
        try {
            InputStream input = ReportePrediccion.class.getResourceAsStream("/com/vane/hotel/reportes/PrediccionOcupacion.jrxml");
            JasperReport reporte = JasperCompileManager.compileReport(input);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("meses", meses);
            parametros.put("ocupacion_real", ocupacionReal);
            parametros.put("ocupacion_predicha", ocupacionPredicha);

            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(print, ruta);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error al exportar el PDF de predicción:\n" + e.getMessage());
        }
    }
}
