package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Bebida;

/**
 * Bebida de la familia amazónica.
 */
public class Aguajina implements Bebida {

    @Override
    public String getNombre() {
        return "Aguajina";
    }

    @Override
    public String servir() {
        return "Bebida: " + getNombre() + " se sirve fría, hecha de aguaje.";
    }
}
