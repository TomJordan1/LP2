package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.fabrica.FactoryMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

public class FactoryCriollo implements FactoryMenu{
    @Override
    public Entrada crearEntrada() {
        return new EntradaCriolla();
    }

    @Override
    public Fondo crearFondo() {
        return new FondoCriollo();
    }

    @Override
    public Bebida crearBebida() {
        return new BebidaCriolla();
    }
}
