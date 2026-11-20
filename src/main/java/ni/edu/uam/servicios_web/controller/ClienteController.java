package ni.edu.uam.servicios_web.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.servicios_web.model.Cliente;
import ni.edu.uam.servicios_web.service.ClienteService;

import java.util.List;

public class ClienteController {

    @FXML private Button btnConsultar;
    @FXML private Label lblEstado;

    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colFechaNacimiento;
    @FXML private TableColumn<Cliente, String> colPais;
    @FXML private TableColumn<Cliente, String> colCiudad;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, Double> colIngreso;
    @FXML private TableColumn<Cliente, Integer> colPuntaje;


    private final ClienteService service = new ClienteService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colIngreso.setCellValueFactory(new PropertyValueFactory<>("ingresoAnual"));
        colPuntaje.setCellValueFactory(new PropertyValueFactory<>("puntajeCredito"));
    }

    @FXML
    private void consultar() {
        Task<List<Cliente>> tarea = new Task<>() {
            @Override
            protected List<Cliente> call() throws Exception {
                return service.obtenerClientes();   // corre en otro hilo
            }
        };

        btnConsultar.disableProperty().bind(tarea.runningProperty());
        lblEstado.setText("Consultando la API REST...");

        tarea.setOnSucceeded(e -> {
            List<Cliente> clientes = tarea.getValue();

            tblClientes.setItems(FXCollections.observableArrayList(clientes));
            lblEstado.setText("Clientes encontrados: " + clientes.size());
        });

        tarea.setOnFailed(e -> {
            Throwable error = tarea.getException();
            lblEstado.setText("No se pudo consultar: " + error.getMessage());
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }
}