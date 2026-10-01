package pe.edu.uni.menu.problema;

/**
 * ANTES DEL PATRÓN: así se vería el código sin Abstract Factory.
 * Esta clase existe solo para contrastar en la exposición. NO es un modelo a seguir.
 *
 * Problemas que muestra:
 *   1. El mismo if/else de regiones se repite en CADA método de creación.
 *      Agregar una región obliga a tocar todos esos métodos.
 *   2. Nada impide mezclar regiones: cada producto se pide por separado.
 *   3. El código que arma el pedido conoce todos los platos concretos.
 */
public final class PedidoSinPatron {

    private PedidoSinPatron() {
    }

    public static void demostrar() {
        String region = "CRIOLLA";

        String entrada = crearEntrada(region);
        String fondo = crearFondo(region);
        // Error humano: alguien cambió solo esta línea. El compilador no se queja.
        String bebida = crearBebida("AMAZONICA");

        System.out.println("  Entrada: " + entrada);
        System.out.println("  Fondo:   " + fondo);
        System.out.println("  Bebida:  " + bebida + "   <-- ¡no combina con el resto!");
    }

    // Cada método repite la misma decisión. Es la "explosión de if/else".
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
