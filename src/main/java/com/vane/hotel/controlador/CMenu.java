package com.vane.hotel.controlador;

import com.vane.hotel.i18n;
import com.vane.hotel.Config;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

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
        actualizarTextos();
    }

    private void actualizarTextos() {
        txtTitulo.setText(i18n.get("titulo.bienvenida"));
        btnHabitaciones.setText(i18n.get("boton.habitaciones"));
        btnClientes.setText(i18n.get("boton.clientes"));
        btnReservaciones.setText(i18n.get("boton.reservaciones"));
    }
}

