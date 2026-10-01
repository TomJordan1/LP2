package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Fondo;

/**
 * PRODUCTO CONCRETO: fondo de la variante Costa (criolla).
 * Colabora con la bebida a través de la interfaz Bebida.
 */
class LomoSaltado implements Fondo {

    @Override
    public String getNombre() {
        return "Lomo Saltado";
    }

    @Override
    public String servir() {
        return "Fondo: " + getNombre() + " sale del wok con papas fritas y arroz.";
    }

    @Override
    public String acompanarCon(Bebida bebida) {
        // Aquí NO validamos si la bebida es de la misma región.
        // No hace falta: el patrón garantiza que, si ambos productos
        // salieron de la MISMA fábrica, ya son compatibles.
        return getNombre() + " acompañado de " + bebida.getNombre();
    }
}
