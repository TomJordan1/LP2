package TareaAbstractFactory;

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
