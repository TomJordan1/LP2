package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Fondo de la familia amazónica.
 */
public class Juane implements Fondo {

    @Override
    public String getNombre() {
        return "Juane";
    }

    @Override
    public String servir() {
        return "Fondo: " + getNombre() + " se desenvuelve de la hoja de bijao humeante.";
    }

    @Override
    public String acompanarCon(Bebida bebida) {
        return getNombre() + " acompañado de " + bebida.getNombre();
    }
}
