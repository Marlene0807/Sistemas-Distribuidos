// ServerRace.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerRace {

    private final String raceName;
    private final int listenPort;
    private final int meta;
    private final String coordHost;
    private final int coordPort;
    private final int minClients;
    private final long waitMillis;

    private final List<Socket> clients = Collections.synchronizedList(new ArrayList<>());
    private final Map<Socket, Integer> positions = new ConcurrentHashMap<>();
    private volatile boolean raceStarted = false;
    private volatile boolean raceFinished = false;
    private PrintWriter coordOut;
    private BufferedReader coordIn;

    public ServerRace(String raceName, int listenPort, int meta, String coordHost, int coordPort, int minClients, long waitMillis) {
        this.raceName = raceName;
        this.listenPort = listenPort;
        this.meta = meta;
        this.coordHost = coordHost;
        this.coordPort = coordPort;
        this.minClients = minClients;
        this.waitMillis = waitMillis;
    }

    public void start() {
        // conectar al Coordinador
        try {
            Socket coordSocket = new Socket(coordHost, coordPort);
            coordOut = new PrintWriter(coordSocket.getOutputStream(), true);
            coordIn = new BufferedReader(new InputStreamReader(coordSocket.getInputStream()));
            coordOut.println("REGISTER " + raceName + " " + listenPort + " " + meta);
        } catch (IOException e) {
            System.err.println("Error conectando al coordinador: " + e.getMessage());
            return;
        }

        // aceptar tortugas en otro hilo
        new Thread(this::acceptClients).start();

        // esperar hasta tener 2 tortugas minimo o a que pase el tiempo minimo
        waitForClientsAndNotifyCoordinator();

        // esperando INICIAR del coordinador
        waitForCoordinatorStart();

        // carrera
        runRace();
    }

    private void acceptClients() {
        System.out.println(raceName + " escuchando tortugas en puerto " + listenPort);
        try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
            while (!raceStarted) {
                Socket client = serverSocket.accept();
                clients.add(client);
                positions.put(client, 0);
                System.out.println("[" + raceName + "] Tortuga conectada. Total: " + clients.size());
            }
        } catch (IOException e) {
            System.err.println("[" + raceName + "] Error accept: " + e.getMessage());
        }
    }

    private void waitForClientsAndNotifyCoordinator() {
        long start = System.currentTimeMillis();
        while (true) {
            long elapsed = System.currentTimeMillis() - start;
            if (clients.size() >= minClients) break;
            if (elapsed >= waitMillis) break;
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
        // envia mensaje al coordinador de que esta listo
        System.out.println("[" + raceName + "] NOTIFICANDO LISTO al coordinador (clients=" + clients.size() + ")");
        coordOut.println("LISTO " + raceName);
    }

    private void waitForCoordinatorStart() {
        System.out.println("[" + raceName + "] esperando instruccion INICIAR del coordinador...");
        try {
            String line;
            while ((line = coordIn.readLine()) != null) {
                if ("INICIAR".equalsIgnoreCase(line.trim())) {
                    System.out.println("[" + raceName + "] Coordinador autorizo INICIAR");
                    raceStarted = true;
                    break;
                } else {
                    // ignnorar mensajes
                }
            }
        } catch (IOException e) {
            System.err.println("[" + raceName + "] Error leyendo coordinador: " + e.getMessage());
        }
    }

    private void runRace() {
        if (clients.isEmpty()) {
            System.out.println("[" + raceName + "] No hay tortugas para correr. Abortando.");
            return;
        }
        System.out.println("[" + raceName + "] INICIANDO carrera con " + clients.size() + " tortugas (meta=" + meta + ")");
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, clients.size()));
        for (Socket client : new ArrayList<>(clients)) {
            pool.execute(() -> handleClientRace(client));
        }
        pool.shutdown();
        try { pool.awaitTermination(10, TimeUnit.MINUTES); } catch (InterruptedException ignored) {}
        System.out.println("[" + raceName + "] Carrera finalizada localmente.");
    }

    private void handleClientRace(Socket client) {
        String tortugaId = "T-" + client.getPort();
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            Random rnd = new Random();
            while (!raceFinished) {
                int advance = rnd.nextInt(10) + 1;
                positions.compute(client, (k,v) -> (v == null ? 0 : v) + advance);
                int pos = positions.get(client);
                out.println("POS " + raceName + " " + tortugaId + " " + pos);

                if (pos >= meta && !raceFinished) {
                    raceFinished = true;
                    out.println("GANADOR " + raceName + " " + tortugaId);
                    // envia mensaje de que ha ganado
                    broadcastToClients("GANADOR " + raceName + " " + tortugaId);
                    // envia mensaje al coordinador
                    coordOut.println("GANADOR " + raceName + " " + tortugaId);
                    break;
                }
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("[" + raceName + "] error tortuga " + tortugaId + ": " + e.getMessage());
        } finally {
            try { client.close(); } catch (IOException ignored) {}
        }
    }

    private void broadcastToClients(String msg) {
        synchronized (clients) {
            for (Socket c : clients) {
                try {
                    PrintWriter w = new PrintWriter(c.getOutputStream(), true);
                    w.println(msg);
                } catch (IOException ignored) {}
            }
        }
    }

    // carrera principal
    public static void main(String[] args) {
        if (args.length < 7) {
            System.out.println("Uso: java ServerRace <raceName> <listenPort> <meta> <coordHost> <coordPort> <minClients> <waitMillis>");
            System.out.println("Ejemplo: java ServerRace RACE50 5001 50 localhost 6000 2 60000"); //Poner asi en la ejecucion
            return;
        }
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int meta = Integer.parseInt(args[2]);
        String ch = args[3];
        int cp = Integer.parseInt(args[4]);
        int min = Integer.parseInt(args[5]);
        long wait = Long.parseLong(args[6]);
        ServerRace server = new ServerRace(name, port, meta, ch, cp, min, wait);
        server.start();
    }
}