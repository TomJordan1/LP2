package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * FÁBRICA CONCRETA de la variante Sierra (andina).
 * Misma estructura que FabricaCriolla: solo cambian los productos que crea.
 */
public class FabricaAndina implements FabricaDeMenu {

    @Override
    public Entrada crearEntrada() {
        return new ChocloConQueso();
    }

    @Override
    public Fondo crearFondo() {
        return new Pachamanca();
    }

    @Override
    public Bebida crearBebida() {
        return new ApiMorado();
    }
}
