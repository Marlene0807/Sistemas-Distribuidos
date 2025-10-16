package com.ejemplo.carrera.service;

import com.ejemplo.carrera.model.Tortuga;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CarreraService {
    private static final int META = 100;
    private final Map<String, Tortuga> tortugas = new ConcurrentHashMap<>();
    private final AtomicReference<String> idGanador = new AtomicReference<>(null);

    // Registra una nueva tortuga y devuelve su ID
    public synchronized String registrarTortuga() {
        String id = UUID.randomUUID().toString();
        tortugas.put(id, new Tortuga(id));
        System.out.println("Registrada tortuga con ID: " + id);
        return id;
    }

    // Hace avanzar a la tortuga y devuelve un mapa con informacion del estado
    public synchronized Map<String, Object> avanzar(String id) {
        Map<String, Object> respuesta = new HashMap<>();

        Tortuga t = tortugas.get(id);
        if (t == null) {
            respuesta.put("error", "Tortuga no encontrada");
            return respuesta;
        }

        if (idGanador.get() != null) {
            respuesta.put("estado", "terminado");
            respuesta.put("ganador", idGanador.get());
            respuesta.put("posicion", t.getPosicion());
            return respuesta;
        }

        t.avanzar();
        respuesta.put("posicion", t.getPosicion());

        if (t.getPosicion() >= META && idGanador.compareAndSet(null, id)) {
            respuesta.put("estado", "ganador");
            System.out.println("La tortuga " + id + " ha ganado la carrera!");
        } else {
            respuesta.put("estado", "en_carrera");
        }

        return respuesta;
    }

    // Devuelve el estado completo de la carrera (ID de tortuga y posicion)
    public Map<String, Integer> getEstadoCarrera() {
        Map<String, Integer> estado = new LinkedHashMap<>();
        for (Tortuga t : tortugas.values()) {
            estado.put(t.getId(), t.getPosicion());
        }
        return estado;
    }

    // Devuelve el ID del ganador, si existe
    public String getGanador() {
        return idGanador.get() != null ? idGanador.get() : "La carrera aun no tiene ganador!";
    }

    // Reinicia la carrera
    public void reiniciarCarrera() {
        tortugas.clear();
        idGanador.set(null);
        System.out.println("Carrera reiniciada!");
    }
}
