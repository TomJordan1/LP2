package TareaAbstractFactory.Factory;

import TareaAbstractFactory.Bebida.Bebida;
import TareaAbstractFactory.Bebida.BebidaCriolla;
import TareaAbstractFactory.Entrada.Entrada;
import TareaAbstractFactory.Entrada.EntradaCriolla;
import TareaAbstractFactory.Fondo.Fondo;
import TareaAbstractFactory.Fondo.FondoCriollo;

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
