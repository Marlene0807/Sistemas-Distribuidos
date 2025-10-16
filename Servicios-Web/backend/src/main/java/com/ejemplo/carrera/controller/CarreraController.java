package com.ejemplo.carrera.controller;

import com.ejemplo.carrera.model.Tortuga;
import com.ejemplo.carrera.service.CarreraService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tortugas")
public class CarreraController {
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    // Registrar una nueva tortuga
    @PostMapping(consumes = "*/*")
    public String registrarTortuga() {
        return carreraService.registrarTortuga();
    }

    // Cada tortuga avanza por su ID
    @PostMapping("/{id}/avanzar")
    public Map<String, Object> avanzar(@PathVariable String id) {
        return carreraService.avanzar(id);
    }

    // Obtener el estado de la carrera (todas las tortugas y sus posiciones)
    @GetMapping("/estado")
    public Map<String, Integer> estadoCarrera() {
        return carreraService.getEstadoCarrera();
    }

    // Obtener el nombre de la tortuga ganadora (si hay una)
    @GetMapping("/ganador")
    public String ganador() {
        return carreraService.getGanador();
    }

    // Reiniciar los valores en cada ejecución
    @PostMapping("/reiniciar")
    public void reiniciarCarrera() {
        carreraService.reiniciarCarrera();
    }
}