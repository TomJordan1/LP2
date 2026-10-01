package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.producto.Bebida;

/**
 * Bebida de la familia andina.
 */
public class ApiMorado implements Bebida {

    @Override
    public String getNombre() {
        return "Api Morado";
    }

    @Override
    public String servir() {
        return "Bebida: " + getNombre() + " se sirve caliente, espeso y con canela.";
    }
}
