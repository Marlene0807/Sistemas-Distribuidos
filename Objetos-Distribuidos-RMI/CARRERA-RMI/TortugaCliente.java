import java.rmi.Naming;
import java.util.Map;
import java.util.Scanner;

public class TortugaCliente {
    public static void main(String[] args) {
        try {
            CarreraInterfaz carrera = (CarreraInterfaz) Naming.lookup("rmi://localhost/CarreraTortugas");
            Scanner sc = new Scanner(System.in);

            System.out.print("Nombre de la tortuga: "); // se registra la tortuga
            String nombre = sc.nextLine(); 

            System.out.println(carrera.registrarTortuga(nombre));

            while (true) {
                System.out.println("Presiona ENTER para avanzar..."); // al presionar ENTER se avanza de manera aleatoria
                sc.nextLine();
                System.out.println(carrera.avanzar(nombre));

                String ganador = carrera.obtenerGanador(); // tablero de posiciones
                if (ganador != null) {
                    System.out.println(" Carrera finalizada, Ganador: " + ganador);
                    break;
                }

                Map<String, Integer> posiciones = carrera.obtenerPosiciones();
                System.out.println("Posiciones: " + posiciones);
            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
