//Practica 1
//Multiprocesamiento e hilos

import java.util.Random;

class Tortuga extends Thread {
    private String nombre;
    private static volatile boolean carreraTerminada = false; // Para detener la carrera cuando alguien gana como el synchronized
    private static final Object lock = new Object(); // Para el bloqueo compartido

    public Tortuga(String nombre) {
        this.nombre = nombre;
    }

    public void run() {
        Random rand = new Random();

        for (int paso = 1; paso <= 10; paso++) {
            if (carreraTerminada) break; // Si alguien ya ganoo me detengo

            System.out.println(nombre + " avanzo al paso " + paso);

            try {
                Thread.sleep(rand.nextInt(1000) + 500); // Pausa aleatoria entre 500 y 1500 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Solo una tortuga gana
        synchronized(lock){ // Solo un hilo entra al bloque de carrera terminada
            if (!carreraTerminada) {
                carreraTerminada = true;
                System.out.println(nombre + " ha ganado la carrera!");
            }
        }
    }
}

public class CarreraTortugas {
    public static void main(String[] args){
        System.out.println("Carrera iniciada");

        Tortuga t1 = new Tortuga("Pekas");
        Tortuga t2 = new Tortuga("Panzon");
        Tortuga t3 = new Tortuga("Momo");
        Tortuga t4 = new Tortuga("Abu");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}