package TareaAbstractFactory.Factory;

import TareaAbstractFactory.Bebida.Bebida;
import TareaAbstractFactory.Bebida.BebidaAndina;
import TareaAbstractFactory.Entrada.Entrada;
import TareaAbstractFactory.Entrada.EntradaAndina;
import TareaAbstractFactory.Fondo.Fondo;
import TareaAbstractFactory.Fondo.FondoAndino;

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
