package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Fábrica concreta para la familia amazónica.
 * Aquí se decide qué implementación corresponde a cada tipo de producto de esta variante.
 */
public class FabricaAmazonica implements FabricaDeMenu {

    @Override
    public Entrada crearEntrada() {
        return new Patacones();
    }

    @Override
    public Fondo crearFondo() {
        return new Juane();
    }

    @Override
    public Bebida crearBebida() {
        return new Aguajina();
    }
}
