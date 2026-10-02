package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.fabrica.FactoryMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

public class FactoryAmazonico implements FactoryMenu{
    @Override
    public Entrada crearEntrada() {
        return new EntradaAmazonica();
    }

    @Override
    public Fondo crearFondo() {
        return new FondoAmazonico();
    }

    @Override
    public Bebida crearBebida() {
        return new BebidaAmazonica();
    }
}
