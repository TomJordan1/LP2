package pe.edu.uni.menu.cliente;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * Objeto que agrupa los tres productos de UNA familia.
 *
 * Solo guarda abstracciones (Entrada, Fondo, Bebida). Es inmutable
 * (campos final, sin setters): una vez armado el menú, nadie puede
 * cambiarle la bebida por la de otra región.
 */
public final class MenuDelDia {

    private final Entrada entrada;
    private final Fondo fondo;
    private final Bebida bebida;

    public MenuDelDia(Entrada entrada, Fondo fondo, Bebida bebida) {
        this.entrada = entrada;
        this.fondo = fondo;
        this.bebida = bebida;
    }

    /** Arma el texto del menú usando solo métodos de las interfaces. */
    public String presentar() {
        return "  " + entrada.servir() + System.lineSeparator()
             + "  " + fondo.servir() + System.lineSeparator()
             + "  " + bebida.servir() + System.lineSeparator()
             + "  Maridaje: " + fondo.acompanarCon(bebida);
    }
}
