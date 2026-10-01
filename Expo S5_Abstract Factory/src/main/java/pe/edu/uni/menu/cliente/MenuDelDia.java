package pe.edu.uni.menu.cliente;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Reúne los productos que forman el menú preparado por el restaurante.
 * No participa en la creación de los productos; solo los agrupa para presentarlos juntos.
 */
public class MenuDelDia {

    private Entrada entrada;
    private Fondo fondo;
    private Bebida bebida;

    public MenuDelDia(Entrada entrada, Fondo fondo, Bebida bebida) {
        this.entrada = entrada;
        this.fondo = fondo;
        this.bebida = bebida;
    }

    public String presentar() {
        return "  " + entrada.servir() + System.lineSeparator()
             + "  " + fondo.servir() + System.lineSeparator()
             + "  " + bebida.servir() + System.lineSeparator()
             + "  Acompañamiento: " + fondo.acompanarCon(bebida);
    }
}
