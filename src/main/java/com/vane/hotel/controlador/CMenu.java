package com.vane.hotel.controlador;

import com.vane.hotel.i18n;
import com.vane.hotel.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CMenu {
    @FXML
    private ComboBox<String> comboIdioma;
    @FXML
    private Button btnHabitaciones;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnReservaciones;
    @FXML
    private Button btnPrediccion;
    @FXML
    private Text txtTitulo;

    @FXML
    public void initialize() {
        comboIdioma.setOnAction(event -> {
            String seleccion = comboIdioma.getValue();
            if ("Español".equals(seleccion)) {
                Config.set("idioma", "es");
            } else if ("Inglés".equals(seleccion)) {
                Config.set("idioma", "en");
            }
            i18n.reload();
            actualizarTextos();
        });

        btnPrediccion.setOnAction(event -> abrirVentanaEstadisticas());

        actualizarTextos();
    }

    private void abrirVentanaEstadisticas() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/vane/hotel/estadisticas.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Predicción de Ocupación");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTextos() {
        txtTitulo.setText(i18n.get("titulo.bienvenida"));
        btnHabitaciones.setText(i18n.get("boton.habitaciones"));
        btnClientes.setText(i18n.get("boton.clientes"));
        btnReservaciones.setText(i18n.get("boton.reservaciones"));
        btnPrediccion.setText(i18n.get("boton.prediccion")); // Puedes añadir esta clave al archivo de i18n
    }
}
