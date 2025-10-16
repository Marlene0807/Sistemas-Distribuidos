public class Tortuga {
    private String id;
    private int posicion;

    public Tortuga(String id) {
        this.id = id;
        this.posicion = 0;
    }

    public void avanzar() {
        this.posicion += (int)(Math.random()*10) + 1;
    }

    public String getId() {
        return id;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
