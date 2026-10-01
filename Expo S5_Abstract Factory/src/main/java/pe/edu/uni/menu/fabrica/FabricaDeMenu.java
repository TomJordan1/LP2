package pe.edu.uni.menu.fabrica;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Fábrica abstracta del ejemplo.
 * Indica qué tipos de productos debe poder crear cualquier familia de menú regional.
 */
public interface FabricaDeMenu {

    Entrada crearEntrada();

    Fondo crearFondo();

    Bebida crearBebida();
}
