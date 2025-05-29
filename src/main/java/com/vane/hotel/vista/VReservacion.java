package com.vane.hotel.vista;

import com.vane.hotel.controlador.CReservacion;
import com.vane.hotel.modelo.Reservacion;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.time.ZoneId;
import java.util.Date;

public class VReservacion {

    @FXML
    private TextField clienteIdField, habitacionIdField;
    @FXML
    private DatePicker fechaEntradaPicker, fechaSalidaPicker;
    @FXML
    private ComboBox<String> estadoCombo;
    @FXML
    private TableView<Reservacion> tablaReservaciones;
    @FXML
    private TableColumn<Reservacion, Integer> colId, colClienteId, colHabitacionId;
    @FXML
    private TableColumn<Reservacion, String> colFechaEntrada, colFechaSalida, colEstado;
    @FXML
    private Button btnAtras;

    private final CReservacion controlador = new CReservacion();
    private int reservacionSeleccionadaId = -1;

    @FXML
    public void initialize() {
        estadoCombo.getItems().addAll("activa", "finalizada", "cancelada");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colClienteId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCliente_id()).asObject());
        colHabitacionId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getHabitacion_id()).asObject());
        colFechaEntrada.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFecha_entrada().toString()));
        colFechaSalida.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFecha_salida().toString()));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        cargarReservaciones();

        tablaReservaciones.setOnMouseClicked(this::seleccionarReservacion);
    }

    private void cargarReservaciones() {
        ObservableList<Reservacion> reservaciones = controlador.getReservaciones();
        tablaReservaciones.setItems(reservaciones);
    }

    private boolean validarCampos() {
        if (!clienteIdField.getText().matches("^\\d+$")) {
            mostrarAlerta("Error", "ID de cliente inválido.", Alert.AlertType.ERROR);
            return false;
        }
        if (!habitacionIdField.getText().matches("^\\d+$")) {
            mostrarAlerta("Error", "ID de habitación inválido.", Alert.AlertType.ERROR);
            return false;
        }
        if (fechaEntradaPicker.getValue() == null || fechaSalidaPicker.getValue() == null) {
            mostrarAlerta("Error", "Seleccione fechas de entrada y salida.", Alert.AlertType.ERROR);
            return false;
        }
        if (estadoCombo.getValue() == null || estadoCombo.getValue().isEmpty()) {
            mostrarAlerta("Error", "Seleccione un estado.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void agregarReservacion() {
        if (!validarCampos()) return;
        Reservacion reservacion = new Reservacion(
                0,
                Integer.parseInt(clienteIdField.getText()),
                Integer.parseInt(habitacionIdField.getText()),
                Date.from(fechaEntradaPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(fechaSalidaPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                estadoCombo.getValue()
        );
        if (controlador.registrarReservacion(reservacion)) {
            mostrarAlerta("Éxito", "Reservación agregada", Alert.AlertType.INFORMATION);
            cargarReservaciones();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo agregar la reservación", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarReservacion() {
        if (reservacionSeleccionadaId == -1) {
            mostrarAlerta("Atención", "Seleccione una reservación para actualizar", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;
        Reservacion reservacion = new Reservacion(
                reservacionSeleccionadaId,
                Integer.parseInt(clienteIdField.getText()),
                Integer.parseInt(habitacionIdField.getText()),
                Date.from(fechaEntradaPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(fechaSalidaPicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                estadoCombo.getValue()
        );
        if (controlador.actualizarReservacion(reservacion)) {
            mostrarAlerta("Éxito", "Reservación actualizada", Alert.AlertType.INFORMATION);
            cargarReservaciones();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar la reservación", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarReservacion() {
        if (reservacionSeleccionadaId == -1) {
            mostrarAlerta("Atención", "Seleccione una reservación para eliminar", Alert.AlertType.WARNING);
            return;
        }
        if (controlador.eliminarReservacion(reservacionSeleccionadaId)) {
            mostrarAlerta("Éxito", "Reservación eliminada", Alert.AlertType.INFORMATION);
            cargarReservaciones();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar la reservación", Alert.AlertType.ERROR);
        }
    }

    private void seleccionarReservacion(MouseEvent event) {
        Reservacion r = tablaReservaciones.getSelectionModel().getSelectedItem();
        if (r != null) {
            reservacionSeleccionadaId = r.getId();
            clienteIdField.setText(String.valueOf(r.getCliente_id()));
            habitacionIdField.setText(String.valueOf(r.getHabitacion_id()));
            fechaEntradaPicker.setValue(((java.sql.Date) r.getFecha_entrada()).toLocalDate());
            fechaSalidaPicker.setValue(((java.sql.Date) r.getFecha_salida()).toLocalDate());
            estadoCombo.setValue(r.getEstado());
        }
    }

    @FXML
    private void limpiarCampos() {
        clienteIdField.clear();
        habitacionIdField.clear();
        fechaEntradaPicker.setValue(null);
        fechaSalidaPicker.setValue(null);
        estadoCombo.getSelectionModel().clearSelection();
        reservacionSeleccionadaId = -1;
    }

    @FXML
    private void volverMenu() {
        try {
            Stage stage = (Stage) btnAtras.getScene().getWindow();
            // Cerrar la ventana actual
            stage.close();
            // Volver a lanzar la aplicación principal
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
