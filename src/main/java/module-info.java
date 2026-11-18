module ni.edu.uam.servicios_web {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens ni.edu.uam.servicios_web to javafx.fxml;
    exports ni.edu.uam.servicios_web;
}