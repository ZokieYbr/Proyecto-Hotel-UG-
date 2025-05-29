package com.vane.hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

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
    }


    public static void main(String[] args) {
        launch();
    }
}