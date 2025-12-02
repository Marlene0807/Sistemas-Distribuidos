package com.ejemplo.carrera.model;

public class Tortuga {
    private String id;
    private int posicion;

    public Tortuga() {}

    public Tortuga(String id) {
        this.id = id;
        this.posicion = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void avanzar(int delta) {
        this.posicion += delta;
    }
}
