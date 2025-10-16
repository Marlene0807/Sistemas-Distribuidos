package com.ejemplo.carrera.model;

public class Tortuga {
    private final String id;
    private int posicion;

    public Tortuga(String id) {
        this.id = id;
        this.posicion = 0;
    }

    public void avanzar() {
        int avance = (int)(Math.random() * 10) + 1;
        this.posicion += avance;
    }

    public String getId() {
        return id;
    }

    public int getPosicion() {
        return posicion;
    }
}
