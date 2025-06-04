package com.vane.hotel.vista;

import com.vane.hotel.controlador.CCliente;
import com.vane.hotel.modelo.Cliente;
import com.vane.hotel.i18n;
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
    @FXML 
    private Button btnAgregar;
    @FXML 
    private Button btnActualizar;
    @FXML 
    private Button btnEliminar;
    @FXML 
    private Button btnLimpiar;
    @FXML 
    private Label lblNombre;
    @FXML 
    private Label lblApellido;
    @FXML 
    private Label lblTelefono;
    @FXML 
    private Label lblEmail;
    @FXML 
    private Label lblIdentificacionOficial;
    @FXML 
    private Label ttlClientes;

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
        actualizarTextos();
    }

    private void cargarClientes() {
        ObservableList<Cliente> clientes = controlador.getClientes();
        tblClientes.setItems(clientes);
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();

        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,30}$")) {
            mostrarAlerta("error.titulo", "error.nombre", Alert.AlertType.ERROR);
            return false;
        }
        if (!apellido.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,30}$")) {
            mostrarAlerta("error.titulo", "error.apellido", Alert.AlertType.ERROR);
            return false;
        }
        if (!telefono.matches("^\\d{10}$")) {
            mostrarAlerta("error.titulo", "error.telefono", Alert.AlertType.ERROR);
            return false;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta("error.titulo", "error.email", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void registrarCliente(ActionEvent event) {
        if (!validarCampos()) return;
        Cliente cliente = new Cliente(0, txtNombre.getText(), txtApellido.getText(), txtTelefono.getText(), txtEmail.getText(), txtIdentificacionOficial.getText());
        if (controlador.registrarCliente(cliente)) {
            mostrarAlerta("exito.titulo", "exito.registro", Alert.AlertType.INFORMATION);
            cargarClientes();
            limpiarCampos();
        } else {
            mostrarAlerta("error.titulo", "error.registro", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarCliente(ActionEvent event) {
        if (clienteSeleccionadoId == -1) {
            mostrarAlerta("atencion.titulo", "atencion.seleccionar", Alert.AlertType.WARNING);
            return;
        }
        if (!validarCampos()) return;
        Cliente cliente = new Cliente(clienteSeleccionadoId, txtNombre.getText(), txtApellido.getText(), txtTelefono.getText(), txtEmail.getText(), txtIdentificacionOficial.getText());
        if (controlador.actualizarCliente(cliente)) {
            mostrarAlerta("exito.titulo", "exito.actualizacion", Alert.AlertType.INFORMATION);
            cargarClientes();
            limpiarCampos();
        } else {
            mostrarAlerta("error.titulo", "error.actualizacion", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarCliente(ActionEvent event) {
        if (clienteSeleccionadoId == -1) {
            mostrarAlerta("atencion.titulo", "atencion.seleccionar", Alert.AlertType.WARNING);
            return;
        }

        if (controlador.eliminarCliente(clienteSeleccionadoId)) {
            mostrarAlerta("exito.titulo", "exito.eliminacion", Alert.AlertType.INFORMATION);
            cargarClientes();
            limpiarCampos();
        } else {
            mostrarAlerta("error.titulo", "error.eliminacion", Alert.AlertType.ERROR);
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
            mostrarAlerta("error.titulo", "error.menu", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String claveTitulo, String claveMensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(i18n.get(claveTitulo));
        alerta.setHeaderText(null);
        alerta.setContentText(i18n.get(claveMensaje));
        alerta.showAndWait();
    }

    private void actualizarTextos() {
        ttlClientes.setText(i18n.get("titulo.clientes"));
        
        btnAtras.setText(i18n.get("boton.atras"));
        btnAgregar.setText(i18n.get("boton.agregar"));
        btnActualizar.setText(i18n.get("boton.actualizar"));
        btnEliminar.setText(i18n.get("boton.eliminar"));
        btnLimpiar.setText(i18n.get("boton.limpiar"));

        lblNombre.setText(i18n.get("label.nombre"));
        lblApellido.setText(i18n.get("label.apellido"));
        lblTelefono.setText(i18n.get("label.telefono"));
        lblEmail.setText(i18n.get("label.email"));

        colNombre.setText(i18n.get("columna.nombre"));
        colApellido.setText(i18n.get("columna.apellido"));
        colTelefono.setText(i18n.get("columna.telefono"));
        colEmail.setText(i18n.get("columna.email"));
    }
}
