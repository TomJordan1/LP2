package TareaAbstractFactory.Factory;

import TareaAbstractFactory.Bebida.Bebida;
import TareaAbstractFactory.Entrada.Entrada;
import TareaAbstractFactory.Fondo.Fondo;

public interface FactoryMenu {
    Entrada crearEntrada();
    Fondo crearFondo();
    Bebida crearBebida();
}
