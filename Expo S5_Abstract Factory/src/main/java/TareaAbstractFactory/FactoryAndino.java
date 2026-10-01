package TareaAbstractFactory;

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
