package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Entrada;

/**
 * Entrada de la familia criolla.
 */
public class PapaALaHuancaina implements Entrada {

    @Override
    public String getNombre() {
        return "Papa a la Huancaína";
    }

    @Override
    public String servir() {
        return "Entrada: " + getNombre() + " se sirve fría, bañada en crema de ají amarillo.";
    }
}
