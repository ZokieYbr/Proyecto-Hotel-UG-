package com.vane.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javax.swing.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader menuLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
        Parent menuRoot = menuLoader.load();
        Scene menuScene = new Scene(menuRoot, 900, 500);
        stage.setTitle("Hotel 'Alterno'");
        stage.setScene(menuScene);
        stage.show();

        Button btnHabitaciones = (Button) menuScene.lookup("#btnHabitaciones");
        if (btnHabitaciones != null) {
            btnHabitaciones.setOnAction(event -> {
                try {
                    FXMLLoader habitacionesLoader = new FXMLLoader(HelloApplication.class.getResource("habitaciones.fxml"));
                    Parent habitacionesRoot = habitacionesLoader.load();
                    Scene habitacionesScene = new Scene(habitacionesRoot, 900, 500);
                    stage.setScene(habitacionesScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Button btnReservaciones = (Button) menuScene.lookup("#btnReservaciones");
        if (btnReservaciones != null) {
            btnReservaciones.setOnAction(event -> {
                try {
                    FXMLLoader reservacionesLoader = new FXMLLoader(HelloApplication.class.getResource("reservaciones.fxml"));
                    Parent reservacionesRoot = reservacionesLoader.load();
                    Scene reservacionesScene = new Scene(reservacionesRoot, 900, 500);
                    stage.setScene(reservacionesScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Button btnClientes = (Button) menuScene.lookup("#btnClientes");
        if (btnClientes != null) {
            btnClientes.setOnAction(event -> {
                try {
                    FXMLLoader clientesLoader = new FXMLLoader(HelloApplication.class.getResource("clientes.fxml"));
                    Parent clientesRoot = clientesLoader.load();
                    Scene clientesScene = new Scene(clientesRoot, 900, 500);
                    stage.setScene(clientesScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Button btnGenerarReporte = (Button) menuScene.lookup("#btnGenerarReporte");
        if (btnGenerarReporte != null) {
            btnGenerarReporte.setOnAction(event -> {
                try {
                    // Diálogo para seleccionar tipo de reporte
                    javafx.scene.control.ChoiceDialog<String> tipoDialog = new javafx.scene.control.ChoiceDialog<>("Ocupación", java.util.Arrays.asList("Ocupación", "Ingresos", "Predicción"));
                    tipoDialog.setTitle("Tipo de Reporte");
                    tipoDialog.setHeaderText("Seleccione el tipo de reporte");
                    java.util.Optional<String> tipoResult = tipoDialog.showAndWait();
                    if (!tipoResult.isPresent()) return;
                    String tipoReporte = tipoResult.get();

                    String jrxml = null;
                    java.util.Map<String, Object> params = new java.util.HashMap<>();

                    if ("Ingresos".equals(tipoReporte)) {

                        javafx.scene.control.TextInputDialog idDialog = new javafx.scene.control.TextInputDialog();
                        idDialog.setTitle("ID de Reservación");
                        idDialog.setHeaderText("Ingrese el ID de la reservación");
                        java.util.Optional<String> idResult = idDialog.showAndWait();
                        if (!idResult.isPresent() || idResult.get().isEmpty()) return;
                        int idReservacion = Integer.parseInt(idResult.get());
                        params.put("id_reservacion", idReservacion);
                        jrxml = "/com/vane/hotel/reportes/Ingresos.jrxml";
                    } else {

                        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
                        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
                        grid.setHgap(10); grid.setVgap(10);
                        javafx.scene.control.DatePicker dpInicio = new javafx.scene.control.DatePicker();
                        javafx.scene.control.DatePicker dpFin = new javafx.scene.control.DatePicker();
                        grid.add(new javafx.scene.control.Label("Fecha inicio:"), 0, 0);
                        grid.add(dpInicio, 1, 0);
                        grid.add(new javafx.scene.control.Label("Fecha fin:"), 0, 1);
                        grid.add(dpFin, 1, 1);
                        javafx.scene.control.Button btnAceptar = new javafx.scene.control.Button("Aceptar");
                        grid.add(btnAceptar, 1, 2);
                        javafx.scene.Scene scene = new javafx.scene.Scene(grid, 300, 150);
                        dialogStage.setScene(scene);
                        dialogStage.setTitle("Seleccionar Fechas");
                        final boolean[] aceptado = {false};
                        btnAceptar.setOnAction(ev -> { aceptado[0] = true; dialogStage.close(); });
                        dialogStage.showAndWait();
                        if (!aceptado[0] || dpInicio.getValue() == null || dpFin.getValue() == null) return;
                        java.time.LocalDate fechaInicio = dpInicio.getValue();
                        java.time.LocalDate fechaFin = dpFin.getValue();
                        params.put("fecha_inicio", java.sql.Date.valueOf(fechaInicio));
                        params.put("fecha_fin", java.sql.Date.valueOf(fechaFin));
                        if ("Ocupación".equals(tipoReporte)) {
                            jrxml = "/com/vane/hotel/reportes/ocupacion.jrxml";
                        } else if ("Predicción".equals(tipoReporte)) {
                            jrxml = "/com/vane/hotel/reportes/prediccion.jrxml";
                        }
                    }

                    if (jrxml == null) return;

                    java.io.InputStream input = HelloApplication.class.getResourceAsStream(jrxml);
                    if (input == null) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "No se encontró la plantilla del reporte: " + jrxml);
                        alert.showAndWait();
                        return;
                    }
                    net.sf.jasperreports.engine.JasperReport jasperReport = net.sf.jasperreports.engine.JasperCompileManager.compileReport(input);
                    java.sql.Connection conn = com.vane.hotel.dao.Conexion.conectar();
                    net.sf.jasperreports.engine.JasperPrint jasperPrint = net.sf.jasperreports.engine.JasperFillManager.fillReport(jasperReport, params, conn);

                    // Mostrar el reporte en una ventana emergente antes de guardar
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        net.sf.jasperreports.view.JasperViewer viewer = new net.sf.jasperreports.view.JasperViewer(jasperPrint, false);
                        viewer.setTitle("Vista previa del reporte");
                        viewer.setVisible(true);
                    });

                    // Preguntar si desea guardar el PDF
                    javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "¿Desea guardar el reporte como PDF?", javafx.scene.control.ButtonType.YES, javafx.scene.control.ButtonType.NO);
                    confirmAlert.setTitle("Guardar PDF");
                    java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.YES) {
                        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                        fileChooser.setTitle("Guardar Reporte PDF");
                        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PDF", "*.pdf"));
                        java.io.File file = fileChooser.showSaveDialog(stage);
                        if (file != null) {
                            net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
                            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "Reporte guardado exitosamente.");
                            alert.showAndWait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Error al generar el reporte: " + e.getMessage());
                    alert.showAndWait();
                }
            });
        }
    }
}
