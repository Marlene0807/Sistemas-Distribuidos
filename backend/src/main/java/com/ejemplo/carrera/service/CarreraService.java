package com.ejemplo.carrera.service;

import com.ejemplo.carrera.model.Tortuga;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;

@Service
public class CarreraService {

    private static final int META = 100;
    private final Map<String, Tortuga> tortugas = new ConcurrentHashMap<>();
    private final AtomicReference<String> idGanador = new AtomicReference<>(null);

    public CarreraService() {
        // iniciamos con 3 tortugas
        register("tortuga1");
        register("tortuga2");
        register("tortuga3");
    }

    private String register(String id) {
        Tortuga t = new Tortuga(id);
        tortugas.put(id, t);
        return id;
    }

    public String registrarTortuga() {
        String id = UUID.randomUUID().toString();
        tortugas.put(id, new Tortuga(id));
        System.out.println("Registrada tortuga con ID: " + id);
        return id;
    }

    public Map<String, Integer> obtenerEstado() {
        Map<String, Integer> estado = new HashMap<>();
        for (Tortuga t : tortugas.values()) {
            estado.put(t.getId(), t.getPosicion());
        }
        return estado;
    }

    public Map<String, Object> avanzar(String id) {
        Map<String, Object> respuesta = new HashMap<>();

        if (!tortugas.containsKey(id)) {
            respuesta.put("error", "Tortuga no encontrada");
            return respuesta;
        }

        if (idGanador.get() != null) {
            Tortuga t = tortugas.get(id);
            respuesta.put("posicion", t.getPosicion());
            respuesta.put("estado", "terminado");
            respuesta.put("ganador", idGanador.get());
            return respuesta;
        }

        Tortuga t = tortugas.get(id);
        int avance = ThreadLocalRandom.current().nextInt(1, 11); // 1..10
        t.avanzar(avance);
        respuesta.put("posicion", t.getPosicion());

        if (t.getPosicion() >= META && idGanador.compareAndSet(null, id)) {
            respuesta.put("estado", "ganador");
            respuesta.put("ganador", id);
            System.out.println("La tortuga " + id + " ha ganado la carrera!");
        } else {
            respuesta.put("estado", "en_carrera");
        }

        return respuesta;
    }

    public String getGanador() {
        return idGanador.get();
    }

    public void reiniciarCarrera() {
        tortugas.clear();
        idGanador.set(null);
        register("tortuga1");
        register("tortuga2");
        register("tortuga3");
        System.out.println("Carrera reiniciada");
    }
}
