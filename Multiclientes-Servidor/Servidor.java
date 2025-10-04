import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Servidor {
    private static final int PUERTO = 5500;
    private static final int META = 100;
    private static final int MIN_TORTUGAS = 2;
    private static final int TIEMPO_ESPERA = 60; // 1 minuto

    private static List<PrintWriter> clientes = new ArrayList<>();
    private static Map<Integer, Integer> posiciones = new ConcurrentHashMap<>();
    private static boolean carreraIniciada = false;
    private static boolean carreraTerminada = false;
    private static int contadorTortugas = 0;

    public static void main(String[] args) {
        System.out.println("Servidor de la carrera de Tortugas iniciado en el puerto " + PUERTO);
        
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            long tiempoInicio = System.currentTimeMillis();

            while (true) {
                Socket socket = serverSocket.accept();
                contadorTortugas++;
                int id = contadorTortugas;
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                clientes.add(salida);
                posiciones.put(id, 0);

                pool.execute(new ManejadorTortuga(socket, id));

                // Verifica si se debe iniciar la carrera
                long tiempoActual = System.currentTimeMillis();
                long segundos = (tiempoActual - tiempoInicio) / 1000;

                if (!carreraIniciada && (contadorTortugas >= MIN_TORTUGAS || segundos >= TIEMPO_ESPERA)) {
                    carreraIniciada = true;
                    broadcast("La carrera ha terminado!");
                    System.out.println("Carrera iniciada con " + contadorTortugas + "tortugas");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManejadorTortuga implements Runnable {
        private Socket socket;
        private int id;
        
        public ManejadorTortuga(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                while (!carreraIniciada) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Random rand = new Random();
                while (!carreraTerminada) {
                    int avance = rand.nextInt(3) + 1; // avanzan de 1 a 3 pasos
                    int nuevaPos = posiciones.get(id) + avance;
                    posiciones.put(id, nuevaPos);

                    broadcast("Tortuga #" + id + "avanzo a " + nuevaPos);

                    if (nuevaPos >= META && !carreraTerminada) {
                        carreraTerminada = true;
                        anunciarGanador(id);
                        break;
                    }

                    Thread.sleep(1000);
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcast(String mensaje) {
        for (PrintWriter cliente : clientes) {
            cliente.println(mensaje);
        }
    }

    private static void anunciarGanador(int id) {
        broadcast("La tortuga #" + id + "ha ganado la carrera!");
        System.out.println("Carrera finalizada. Gano la tortuga #" + id);
    }
}