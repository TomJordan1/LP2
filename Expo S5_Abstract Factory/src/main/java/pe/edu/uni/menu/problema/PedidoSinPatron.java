package pe.edu.uni.menu.problema;

/**
 * Versión sencilla del mismo problema sin usar Abstract Factory.
 * Se incluye para observar cómo la elección de la región termina apareciendo en cada creación de producto.
 */
public class PedidoSinPatron {

    public static void demostrar() {
        String region = "CRIOLLA";

        String entrada = crearEntrada(region);
        String fondo = crearFondo(region);
        String bebida = crearBebida(region);

        System.out.println("\n--- Ejemplo sin Abstract Factory ---");
        System.out.println("  Entrada: " + entrada);
        System.out.println("  Fondo:   " + fondo);
        System.out.println("  Bebida:  " + bebida);
    }

    private static String crearEntrada(String region) {
        if (region.equals("CRIOLLA")) {
            return "Papa a la Huancaína";
        } else if (region.equals("ANDINA")) {
            return "Choclo con Queso";
        } else {
            return "Patacones";
        }
    }

    private static String crearFondo(String region) {
        if (region.equals("CRIOLLA")) {
            return "Lomo Saltado";
        } else if (region.equals("ANDINA")) {
            return "Pachamanca";
        } else {
            return "Juane";
        }
    }

    private static String crearBebida(String region) {
        if (region.equals("CRIOLLA")) {
            return "Chicha Morada";
        } else if (region.equals("ANDINA")) {
            return "Api Morado";
        } else {
            return "Aguajina";
        }
    }
}
