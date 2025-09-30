import java.util.Random;

class Tortuga extends Thread {
    private String nombre;
    private static boolean carreraTerminada = false; // Para detener la carrera cuando alguien gana

    public Tortuga(String nombre) {
        this.nombre = nombre;
    }

    public void run() {
        Random rand = new Random();

        for (int paso = 1; paso <= 10; paso++) {
            if (carreraTerminada) break; // Si alguien ya ganó, el hilo termina

            System.out.println(nombre + " avanzo al paso " + paso);

            try {
                Thread.sleep(rand.nextInt(1000) + 500); // Pausa aleatoria entre 500 y 1500 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Si llega aquí y la carrera no ha terminado, este corredor es el ganador
        if (!carreraTerminada) {
            carreraTerminada = true;
            System.out.println(nombre + " ha ganado la carrera!");
        }
    }
}

public class carreraTortugas {
    public static void main(String[] args){
        System.out.println("Carrera iniciada");

        Tortuga t1 = new Tortuga("Blue");
        Tortuga t2 = new Tortuga("Tom");
        Tortuga t3 = new Tortuga("Sully");

        t1.start();
        t2.start();
        t3.start();
    }
}