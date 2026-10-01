package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Bebida;

public class BebidaAmazonica implements Bebida {
    @Override
    public String servir() {
        return "Bebida: Aguajina se sirve fría, hecha de aguaje.";
    }
}
