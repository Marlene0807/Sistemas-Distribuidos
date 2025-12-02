package com.ejemplo.carrera.controller;

import com.ejemplo.carrera.model.Tortuga;
import com.ejemplo.carrera.service.CarreraService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tortugas")
@CrossOrigin(origins = "http://localhost:5173")
public class CarreraController {

    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    // Registrar una nueva tortuga
    @PostMapping
    public String registrarTortuga() {
        return carreraService.registrarTortuga();
    }

    // Avanzar la tortuga por id
    @PostMapping("/{id}/avanzar")
    public Map<String, Object> avanzar(@PathVariable String id) {
        return carreraService.avanzar(id);
    }

    // Obtener estado de todas las tortugas
    @GetMapping("/estado")
    public Map<String, Integer> estado() {
        return carreraService.obtenerEstado();
    }

    // Obtener ganador (si existe)
    @GetMapping("/ganador")
    public String ganador() {
        String g = carreraService.getGanador();
        return g != null ? g : "";
    }

    // Reiniciar carrera
    @PostMapping("/reiniciar")
    public void reiniciar() {
        carreraService.reiniciarCarrera();
    }
}
