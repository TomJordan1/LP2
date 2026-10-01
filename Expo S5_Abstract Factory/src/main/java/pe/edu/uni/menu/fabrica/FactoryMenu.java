package pe.edu.uni.menu.fabrica;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

public interface FactoryMenu {
    Entrada crearEntrada();
    Fondo crearFondo();
    Bebida crearBebida();
}
