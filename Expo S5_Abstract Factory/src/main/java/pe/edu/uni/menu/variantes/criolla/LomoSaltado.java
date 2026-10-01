package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Fondo de la familia criolla.
 */
public class LomoSaltado implements Fondo {

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
        return getNombre() + " acompañado de " + bebida.getNombre();
    }
}
