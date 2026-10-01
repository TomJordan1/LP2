package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.producto.Entrada;

/**
 * Entrada de la familia andina.
 */
public class ChocloConQueso implements Entrada {

    @Override
    public String getNombre() {
        return "Choclo con Queso";
    }

    @Override
    public String servir() {
        return "Entrada: " + getNombre() + " se sirve tibio, con queso fresco serrano.";
    }
}
