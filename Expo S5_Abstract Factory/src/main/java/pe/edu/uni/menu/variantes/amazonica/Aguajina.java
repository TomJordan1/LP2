package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Bebida;

/** PRODUCTO CONCRETO (package-private): bebida Selva (amazónica). Solo FabricaAmazonica la instancia. */
class Aguajina implements Bebida {

    @Override
    public String getNombre() {
        return "Aguajina";
    }

    @Override
    public String servir() {
        return "Bebida: " + getNombre() + " se sirve fría, hecha de aguaje.";
    }
}
