package com.vane.hotel.vista;

import com.vane.hotel.controlador.CHabitacion;
import com.vane.hotel.modelo.Habitacion;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class VHabitacion {

    @FXML private TextField numeroField, precio_nocheField;
    @FXML private ComboBox<String> tipoCombo, estadoCombo;
    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, Integer> colId, colNumero;
    @FXML private TableColumn<Habitacion, String> colTipo, colEstado;
    @FXML private TableColumn<Habitacion, Double> colprecio_noche;
    @FXML private Button btnAtras;

    private final CHabitacion controlador = new CHabitacion();
    private int habitacionSeleccionadaId = -1;

    @FXML
    public void initialize() {
        estadoCombo.getItems().addAll("disponible", "reservada", "fuera de servicio");
        tipoCombo.getItems().addAll("estándar", "suite", "doble");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNumero.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getNumero()).asObject());
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colprecio_noche.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getprecio_noche()).asObject());
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        cargarHabitaciones();

        tablaHabitaciones.setOnMouseClicked(this::seleccionarHabitacion);
    }

    private void cargarHabitaciones() {
        ObservableList<Habitacion> habitaciones = controlador.getHabitaciones();
        tablaHabitaciones.setItems(habitaciones);
    }

    private boolean validarCampos() {
        String numero = numeroField.getText();
        String tipo = tipoCombo.getValue();
        String precio_noche = precio_nocheField.getText();
        String estado = estadoCombo.getValue();

        if (!numero.matches("^\\d{1,4}$")) {
            mostrarAlerta("Error", "Número inválido. Solo dígitos (1-4).", Alert.AlertType.ERROR);
            return false;
        }
        if (tipo == null || tipo.isEmpty()) {
            mostrarAlerta("Error", "Seleccione un tipo de habitación.", Alert.AlertType.ERROR);
            return false;
        }
        if (!precio_noche.matches("^\\d+(\\.\\d{1,2})?$")) {
            mostrarAlerta("Error", "precio_noche inválido. Debe ser un número válido.", Alert.AlertType.ERROR);
            return false;
        }
        if (estado == null || estado.isEmpty()) {
            mostrarAlerta("Error", "Seleccione un estado.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void agregarHabitacion() {
        if (!validarCampos()) return;
        Habitacion habitacion = new Habitacion(0,
                Integer.parseInt(numeroField.getText()),
                tipoCombo.getValue(),
                Double.parseDouble(precio_nocheField.getText()),
                estadoCombo.getValue()
        );
        if (controlador.registrarHabitacion(habitacion)) {
            mostrarAlerta("Éxito", "Habitación agregada", Alert.AlertType.INFORMATION);
            cargarHabitaciones();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo agregar la habitación", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarHabitacion() {
        if (habitacionSeleccionadaId == -1) {
            mostrarAlerta("Atención", "Seleccione una habitación para actualizar", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;
        Habitacion habitacion = new Habitacion(
                habitacionSeleccionadaId,
                Integer.parseInt(numeroField.getText()),
                tipoCombo.getValue(),
                Double.parseDouble(precio_nocheField.getText()),
                estadoCombo.getValue()
        );
        if (controlador.actualizarHabitacion(habitacion)) {
            mostrarAlerta("Éxito", "Habitación actualizada", Alert.AlertType.INFORMATION);
            cargarHabitaciones();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar la habitación", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarHabitacion() {
        if (habitacionSeleccionadaId == -1) {
            mostrarAlerta("Atención", "Seleccione una habitación para eliminar", Alert.AlertType.WARNING);
            return;
        }
        if (controlador.eliminarHabitacion(habitacionSeleccionadaId)) {
            mostrarAlerta("Éxito", "Habitación eliminada", Alert.AlertType.INFORMATION);
            cargarHabitaciones();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar la habitación", Alert.AlertType.ERROR);
        }
    }

    private void seleccionarHabitacion(MouseEvent event) {
        Habitacion h = tablaHabitaciones.getSelectionModel().getSelectedItem();
        if (h != null) {
            habitacionSeleccionadaId = h.getId();
            numeroField.setText(String.valueOf(h.getNumero()));
            tipoCombo.setValue(h.getTipo());
            precio_nocheField.setText(String.valueOf(h.getprecio_noche()));
            estadoCombo.setValue(h.getEstado());
        }
    }

    @FXML
    private void limpiarCampos() {
        numeroField.clear();
        tipoCombo.getSelectionModel().clearSelection();
        precio_nocheField.clear();
        estadoCombo.getSelectionModel().clearSelection();
        habitacionSeleccionadaId = -1;
    }

    @FXML
    private void volverMenu() {
        try {
            Stage stage = (Stage) btnAtras.getScene().getWindow();
            stage.close();
            new com.vane.hotel.HelloApplication().start(new Stage());
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo volver al menú", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
