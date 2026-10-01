package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Bebida;

/**
 * Bebida de la familia criolla.
 */
public class ChichaMorada implements Bebida {

    @Override
    public String getNombre() {
        return "Chicha Morada";
    }

    @Override
    public String servir() {
        return "Bebida: " + getNombre() + " se sirve helada, con trocitos de piña.";
    }
}
