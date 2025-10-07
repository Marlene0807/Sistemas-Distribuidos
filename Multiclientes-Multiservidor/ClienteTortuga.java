// ClienteTortuga.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ClienteTortuga {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java ClienteTortuga <host:port> [<host2:port2> ...]");
            return;
        }
        ExecutorService pool = Executors.newCachedThreadPool();
        for (String target : args) {
            String[] p = target.split(":");
            String host = p[0];
            int port = Integer.parseInt(p[1]);
            pool.execute(() -> runClient(host, port));
        }
        pool.shutdown();
    }

    private static void runClient(String host, int port) {
        try (Socket s = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            System.out.println("Conectado a " + host + ":" + port + " (localPort=" + s.getLocalPort() + ")");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("[" + host + ":" + port + "] " + line);
            }

        } catch (IOException e) {
            System.err.println("Error tortuga en " + host + ":" + port + " -> " + e.getMessage());
        }
    }
}
