package TareaAbstractFactory.Factory;

import TareaAbstractFactory.Bebida.Bebida;
import TareaAbstractFactory.Bebida.BebidaAmazonica;
import TareaAbstractFactory.Entrada.Entrada;
import TareaAbstractFactory.Entrada.EntradaAmazonica;
import TareaAbstractFactory.Fondo.Fondo;
import TareaAbstractFactory.Fondo.FondoAmazonico;

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
