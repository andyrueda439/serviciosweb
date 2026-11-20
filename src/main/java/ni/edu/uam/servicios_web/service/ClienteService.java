package ni.edu.uam.servicios_web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ni.edu.uam.servicios_web.model.Cliente;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private static final String URL =
            "https://sqpvqkkdfgtzdgrjrgxs.supabase.co/rest/v1/customers?order=id.asc";

    public List<Cliente> obtenerClientes() throws IOException, InterruptedException {

        // La clave NO va en el código: se lee de la variable de entorno SUPABASE_API_KEY
        String apiKey = System.getenv("SUPABASE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IOException("Falta la variable de entorno SUPABASE_API_KEY");
        }

        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", apiKey)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error HTTP " + response.statusCode() + " al obtener los clientes");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode datos = mapper.readTree(response.body());

        if (!datos.isArray()) {
            throw new IOException("La respuesta no contiene un arreglo JSON válido");
        }

        List<Cliente> clientes = new ArrayList<>();
        for (JsonNode nodo : datos) {
            clientes.add(new Cliente(
                    nodo.path("id").asInt(),
                    nodo.path("first_name").asText(),
                    nodo.path("last_name").asText(),
                    nodo.path("email").asText(),
                    nodo.path("phone").asText(),
                    nodo.path("date_of_birth").asText(),
                    nodo.path("country").asText(),
                    nodo.path("city").asText(),
                    nodo.path("address").asText(),
                    nodo.path("annual_income").asDouble(),
                    nodo.path("credit_score").asInt()
            ));
        }
        return clientes;
    }
}