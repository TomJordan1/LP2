package pe.edu.uni.menu.cliente;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Cliente del patrón Abstract Factory.
 * Recibe una FabricaDeMenu y le solicita los productos que necesita, sin elegir aquí sus clases concretas.
 */
public class Restaurante {

    private FabricaDeMenu fabrica;

    public Restaurante(FabricaDeMenu fabrica) {
        this.fabrica = fabrica;
    }

    public MenuDelDia prepararMenuDelDia() {
        // Los tres productos se solicitan a la misma fábrica seleccionada para este restaurante.
        Entrada entrada = fabrica.crearEntrada();
        Fondo fondo = fabrica.crearFondo();
        Bebida bebida = fabrica.crearBebida();
        return new MenuDelDia(entrada, fondo, bebida);
    }
}
