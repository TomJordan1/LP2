package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.producto.Bebida;

/** PRODUCTO CONCRETO (package-private): bebida Sierra (andina). Solo FabricaAndina la instancia. */
class ApiMorado implements Bebida {

    @Override
    public String getNombre() {
        return "Api Morado";
    }

    @Override
    public String servir() {
        return "Bebida: " + getNombre() + " se sirve caliente, espeso y con canela.";
    }
}
