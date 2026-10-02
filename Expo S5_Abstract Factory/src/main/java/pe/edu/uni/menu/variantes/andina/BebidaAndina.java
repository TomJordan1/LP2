package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.producto.Bebida;

public class BebidaAndina implements Bebida{
    @Override
    public String servir() {
        return "Bebida: Api Morado se sirve caliente, espeso y con canela.";
    }
}
