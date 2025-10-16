import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CarreraAPI {

    private static final String BASE_URL = "http://localhost:8080/tortugas";
    private static final Gson gson = new Gson();

    public static String registrarTortuga() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String id = in.readLine();
        in.close();
        return id;
    }

    public static Map<String, Object> avanzarTortuga(String id) throws Exception {
        URL url = new URL(BASE_URL + "/" + id + "/avanzar");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String json = in.readLine();
        in.close();

        // Convertir JSON a Map<String,Object>
        Map<String, Object> resultado = gson.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        return resultado;
    }

    public static String consultarGanador() throws Exception {
        URL url = new URL(BASE_URL + "/ganador");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String ganador = in.readLine();
        in.close();
        return ganador;
    }

    public static void reiniciarCarrera() throws Exception {
        URL url = new URL(BASE_URL + "/reiniciar");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.getInputStream().close();
    }
}
