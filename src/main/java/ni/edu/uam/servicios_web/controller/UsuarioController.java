package ni.edu.uam.servicios_web.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.servicios_web.model.Usuario;
import ni.edu.uam.servicios_web.service.UsuarioService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.util.List;

public class UsuarioController {

    @FXML private Button btnConsultar;
    @FXML private Label lblEstado;

    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsername;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTelefono;
    @FXML private TableColumn<Usuario, String> colCiudad;
    @FXML private TableColumn<Usuario, String> colEmpresa;

    private final UsuarioService service = new UsuarioService();

    @FXML

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
    }

    @FXML
    private void consultar() {
        Task<List<Usuario>> tarea = new Task<>() {
            @Override
            protected List<Usuario> call() throws Exception {
                return service.obtenerUsuarios();   // corre en otro hilo
            }
        };

        btnConsultar.disableProperty().bind(tarea.runningProperty());
        lblEstado.setText("Consultando la API REST...");

        tarea.setOnSucceeded(e -> {
            List<Usuario> usuarios = tarea.getValue();
            tblUsuarios.setItems(FXCollections.observableArrayList(usuarios));
            lblEstado.setText("Usuarios encontrados: " + usuarios.size());
        });

        tarea.setOnFailed(e -> {
            Throwable error = tarea.getException();
            lblEstado.setText("No se pudo consultar: " + error.getMessage());
        });


        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }
    @FXML
    private void abrirClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ni/edu/uam/servicios_web/clientes-view.fxml"));
            Stage ventana = new Stage();
            ventana.setTitle("Consulta de clientes - API REST (Supabase)");
            ventana.setScene(new Scene(loader.load()));
            ventana.show();
        } catch (IOException e) {
            lblEstado.setText("No se pudo abrir la vista de clientes: " + e.getMessage());
        }
    }
}