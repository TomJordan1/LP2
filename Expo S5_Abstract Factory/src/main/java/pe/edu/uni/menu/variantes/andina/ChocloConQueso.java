package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.producto.Entrada;

/** PRODUCTO CONCRETO (package-private): entrada Sierra (andina). Solo FabricaAndina la instancia. */
class ChocloConQueso implements Entrada {

    @Override
    public String getNombre() {
        return "Choclo con Queso";
    }

    @Override
    public String servir() {
        return "Entrada: " + getNombre() + " se sirve tibio, con queso fresco serrano.";
    }
}
