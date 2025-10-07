// Coordinador.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Coordinador {
    private static final int PORT = 6000;
    private static final Map<String, PrintWriter> servidores = new ConcurrentHashMap<>();
    private static final Map<String, String> ganadores = new ConcurrentHashMap<>();
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("Coordinador arrancando en puerto " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket s = serverSocket.accept();
                pool.execute(new HandlerServidor(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    static class HandlerServidor implements Runnable {
        private final Socket socket;
        HandlerServidor(Socket socket) { this.socket = socket; }

        @Override
        public void run() {
            String name = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                // leer registros y mensajes
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length == 0) continue;
                    String cmd = parts[0];

                    if ("REGISTER".equalsIgnoreCase(cmd) && parts.length >= 4) {
                        // REGISTRAR <name> <port> <meta>
                        name = parts[1];
                        servidores.put(name, out);
                        System.out.println("Servidor registrado: " + name + " (port=" + parts[2] + ", meta=" + parts[3] + ")");
                        out.println("OK REGISTRADO");
                    } else if ("LISTO".equalsIgnoreCase(cmd) && parts.length >=2) {
                        String race = parts[1];
                        System.out.println("Coordinador: servidor LISTO para carrera " + race + " -> autorizando INICIAR");
                        out.println("INICIAR");
                    } else if ("GANADOR".equalsIgnoreCase(cmd) && parts.length >=3) {
                        String race = parts[1];
                        String winner = parts[2];
                        ganadores.put(race, winner);
                        System.out.println("Coordinador: recibio GANADOR para " + race + " -> " + winner);
                        // Si todos los servidores tienen ganadores se imprime un breve resumen
                        maybePrintSummary();
                    } else {
                        System.out.println("Coordinador recibio: " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Conexion con servidor cerrada: " + name);
            } finally {
                if (name != null) servidores.remove(name);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }
    }

    private static void maybePrintSummary() {
        // En caso de que todos los servidores tengan ganadores, se muestran
        if (!servidores.isEmpty() && ganadores.keySet().containsAll(servidores.keySet())) {
            System.out.println("=== RESUMEN DE GANADORES ===");
            for (String race : ganadores.keySet()) {
                System.out.println("Carrera " + race + " -> Ganador: " + ganadores.get(race));
            }
            System.out.println("============================");
        }
    }
}
