import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CarreraServidor extends UnicastRemoteObject implements CarreraInterfaz {

    private static final int META = 100;
    private final Map<String, Integer> posiciones = new HashMap<>();
    private String ganador = null;

    protected CarreraServidor() throws RemoteException {
        super();
    }

    @Override
    public synchronized String registrarTortuga(String nombre) throws RemoteException {
        if (!posiciones.containsKey(nombre)) {
            posiciones.put(nombre, 0);
            System.out.println("Tortuga registrada: " + nombre);
            return "Tortuga " + nombre + " registrada correctamente";
        }
        return "La tortuga " + nombre + " ya esta registrada";
    }

    @Override
    public synchronized String avanzar(String nombre) throws RemoteException {
        if (ganador != null) return "La carrera ha terminado, el ganador es: " + ganador;

        if (!posiciones.containsKey(nombre)) return "Tortuga no registrada";

        int avance = (int) (Math.random() * 10 + 1);
        posiciones.put(nombre, posiciones.get(nombre) + avance);

        if (posiciones.get(nombre) >= META && ganador == null) {
            ganador = nombre;
            System.out.println("La tortuga " + nombre + " ha ganado la carrera!");
            return "¡Has llegado a la meta, " + nombre + "!";
        }

        return nombre + " avanzo a " + posiciones.get(nombre);
    }

    @Override
    public synchronized Map<String, Integer> obtenerPosiciones() {
        return new HashMap<>(posiciones);
    }

    @Override
    public synchronized String obtenerGanador() {
        return ganador;
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // se levanta un registro RMI en el puerto 1099
            CarreraServidor carrera = new CarreraServidor();
            Naming.rebind("CarreraTortugas", carrera);
            System.out.println("Servidor RMI listo y esperando tortugas...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
