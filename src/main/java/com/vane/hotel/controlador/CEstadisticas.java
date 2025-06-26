package com.vane.hotel.controlador;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;


public class CEstadisticas {

    private final AnalizadorOcupacion analizador = new AnalizadorOcupacion();
    private final CHabitacion controladorHabitacion = new CHabitacion();

    @FXML
    private ComboBox<String> mesCombo;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private Label lblResultado;
    @FXML
    public void initialize() {
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(true);

        List<String> futuros = new ArrayList<>();
        int añoActual = LocalDate.now().getYear();
        for (int a = 0; a <= 1; a++) {
            for (int m = 1; m <= 12; m++) {
                futuros.add((añoActual + a) + "-" + String.format("%02d", m));
            }
        }
        mesCombo.getItems().addAll(futuros);
        mesCombo.getSelectionModel().selectFirst();

        cargarGraficoHistorico();
    }

    private void cargarGraficoHistorico() {
        lineChart.getData().clear();

        XYChart.Series<String, Number> serieHist = new XYChart.Series<>();
        serieHist.setName("Histórico");

        Map<String, Double> historico = analizador.calcularOcupacionHistorica();
        historico.forEach((mes, diasOcupados) -> {
            serieHist.getData().add(new XYChart.Data<>(mes, diasOcupados));
        });

        lineChart.getData().add(serieHist);
    }

    @FXML
    public void calcularPrediccion() {
        String mesFuturo = mesCombo.getValue();
        if (mesFuturo == null || mesFuturo.isEmpty()) {
            lblResultado.setText("Selecciona un mes válido.");
            return;
        }

        double ocupacionEstimada = analizador.predecirOcupacion(mesFuturo);

        int totalHabitaciones = controladorHabitacion.contarHabitaciones();
        YearMonth ym = YearMonth.parse(mesFuturo);
        int diasDelMes = ym.lengthOfMonth();

        double porcentaje = (ocupacionEstimada / (totalHabitaciones * diasDelMes)) * 100;
        lblResultado.setText(String.format("Ocupación estimada para %s: %.2f%%", mesFuturo, porcentaje));

        XYChart.Series<String, Number> seriePred = new XYChart.Series<>();
        seriePred.setName("Predicción");
        seriePred.getData().add(new XYChart.Data<>(mesFuturo, ocupacionEstimada));

        lineChart.getData().add(seriePred);
    }

    @FXML
    private void cerrarVentana() {
        javafx.stage.Stage stage = (javafx.stage.Stage) mesCombo.getScene().getWindow();
        stage.close();
    }

}
