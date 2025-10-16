import java.util.Map;

public class ClienteTortuga {
    public static void main(String[] args) {
        try {
            // Registrar la tortuga en el servidor
            String id = CarreraAPI.registrarTortuga();
            Tortuga tortuga = new Tortuga(id);

            System.out.println("Tortuga registrada con ID: " + tortuga.getId());
            System.out.println("Esperando iniciar la carrera...");

            while (true) {
                // Llamamos al endpoint /avanzar/{id} y recibimos un JSON convertido a Map
                Map<String, Object> respuesta = CarreraAPI.avanzarTortuga(tortuga.getId());

                int posicion = ((Number) respuesta.get("posicion")).intValue();
                tortuga.setPosicion(posicion);

                String estado = (String) respuesta.get("estado");
                System.out.println("Tortuga avanza a posicion: " + posicion + " (" + estado + ")");

                if ("ganador".equals(estado)) {
                    System.out.println("Has ganado la carrera!");
                    break;
                } else if ("terminado".equals(estado)) {
                    String ganador = (String) respuesta.get("ganador");
                    System.out.println("Carrera terminada. Gano otra tortuga: " + ganador);
                    break;
                }

                Thread.sleep(1000);
            }

            System.out.println("Carrera finalizada para esta tortuga.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
