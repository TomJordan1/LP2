package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Fábrica concreta para la familia criolla.
 * Aquí se decide qué implementación corresponde a cada tipo de producto de esta variante.
 */
public class FabricaCriolla implements FabricaDeMenu {

    @Override
    public Entrada crearEntrada() {
        return new PapaALaHuancaina();
    }

    @Override
    public Fondo crearFondo() {
        return new LomoSaltado();
    }

    @Override
    public Bebida crearBebida() {
        return new ChichaMorada();
    }
}
