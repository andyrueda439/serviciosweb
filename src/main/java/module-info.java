module ni.edu.uam.servicios_web {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens ni.edu.uam.servicios_web to javafx.fxml;
    opens ni.edu.uam.servicios_web.controller to javafx.fxml;
    opens ni.edu.uam.servicios_web.model to javafx.base;

    exports ni.edu.uam.servicios_web;
}