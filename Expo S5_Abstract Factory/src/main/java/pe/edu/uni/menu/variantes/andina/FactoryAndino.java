package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.fabrica.FactoryMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

public class FactoryAndino implements FactoryMenu{
    @Override
    public Entrada crearEntrada() {
        return new EntradaAndina();
    }

    @Override
    public Fondo crearFondo() {
        return new FondoAndino();
    }

    @Override
    public Bebida crearBebida() {
        return new BebidaAndina();
    }
}
