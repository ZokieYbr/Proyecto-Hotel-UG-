package com.vane.hotel.vista;

import com.vane.hotel.controlador.CCliente;
import com.vane.hotel.modelo.Cliente;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class VCliente {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtIdentificacionOficial;
    @FXML
    private TableView<Cliente> tblClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colApellido;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TableColumn<Cliente, String> colEmail;
    @FXML
    private Button btnAtras;

    private final CCliente controlador = new CCliente();
    private int clienteSeleccionadoId = -1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colApellido.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getApellido()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        cargarClientes();

        tblClientes.setOnMouseClicked(this::seleccionarCliente);
    }

    private void cargarClientes() {
        ObservableList<Cliente> clientes = controlador.getClientes();
        tblClientes.setItems(clientes);
    }
// Agrega estos métodos en VCliente.java

    private boolean validarCampos() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();

        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,30}$")) {
            mostrarAlerta("Error", "Nombre inválido. Solo letras y espacios (2-30 caracteres).", Alert.AlertType.ERROR);
            return false;
        }
        if (!apellido.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,30}$")) {
            mostrarAlerta("Error", "Apellido inválido. Solo letras y espacios (2-30 caracteres).", Alert.AlertType.ERROR);
            return false;
        }
        if (!telefono.matches("^\\d{10}$")) {
            mostrarAlerta("Error", "Teléfono inválido. Deben ser 10 dígitos.", Alert.AlertType.ERROR);
            return false;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta("Error", "Correo electrónico inválido.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

// Modifica registrarCliente y actualizarCliente para usar la validación

    @FXML
    private void registrarCliente(ActionEvent event) {
        if (!validarCampos()) return;
        Cliente cliente = new Cliente(0, txtNombre.getText(), txtApellido.getText(), txtTelefono.getText(), txtEmail.getText(), txtIdentificacionOficial.getText());
        if (controlador.registrarCliente(cliente)) {
            mostrarAlerta("Éxito", "Cliente registrado", Alert.AlertType.INFORMATION);
            cargarClientes();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el cliente", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarCliente(ActionEvent event) {
        if (clienteSeleccionadoId == -1) {
            mostrarAlerta("Atención", "Seleccione un cliente para actualizar", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;
        Cliente cliente = new Cliente(clienteSeleccionadoId, txtNombre.getText(), txtApellido.getText(), txtTelefono.getText(), txtEmail.getText(), txtIdentificacionOficial.getText());
        if (controlador.actualizarCliente(cliente)) {
            mostrarAlerta("Éxito", "Cliente actualizado", Alert.AlertType.INFORMATION);
            cargarClientes();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el cliente", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarCliente(ActionEvent event) {
        if (clienteSeleccionadoId == -1) {
            mostrarAlerta("Atención", "Seleccione un cliente para eliminar", Alert.AlertType.WARNING);
            return;
        }

        if (controlador.eliminarCliente(clienteSeleccionadoId)) {
            mostrarAlerta("Éxito", "Cliente eliminado", Alert.AlertType.INFORMATION);
            cargarClientes();
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo eliminar el cliente", Alert.AlertType.ERROR);
        }
    }

    private void seleccionarCliente(MouseEvent event) {
        Cliente cliente = tblClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            clienteSeleccionadoId = cliente.getId();
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
            txtTelefono.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getEmail());
            txtIdentificacionOficial.setText(cliente.getIdentificacionOficial());
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtIdentificacionOficial.clear();
        clienteSeleccionadoId = -1;
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
