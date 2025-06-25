module com.vane.hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires jasperreports;
    requires java.desktop;

    opens com.vane.hotel.controlador to javafx.fxml;
    exports com.vane.hotel;
    exports com.vane.hotel.vista to javafx.fxml;
    exports com.vane.hotel.controlador;
    exports com.vane.hotel.modelo;

    opens com.vane.hotel.vista to javafx.fxml;
}