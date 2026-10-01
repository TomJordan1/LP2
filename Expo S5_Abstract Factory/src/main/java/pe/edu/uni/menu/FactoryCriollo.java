package TareaAbstractFactory;

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
