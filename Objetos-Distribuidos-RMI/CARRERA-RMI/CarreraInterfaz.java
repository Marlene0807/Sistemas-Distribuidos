// esta interfaz la usan los clientes (tortugas) y el servidor
import java.rmi.Remote; // se usa a traves de RMI
import java.rmi.RemoteException;
import java.util.Map;

public interface CarreraInterfaz extends Remote {
    String registrarTortuga(String nombre) throws RemoteException;
    String avanzar(String nombre) throws RemoteException;
    Map<String, Integer> obtenerPosiciones() throws RemoteException;
    String obtenerGanador() throws RemoteException;
}
