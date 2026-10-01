package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Entrada;

/**
 * Entrada de la familia amazónica.
 */
public class Patacones implements Entrada {

    @Override
    public String getNombre() {
        return "Patacones";
    }

    @Override
    public String servir() {
        return "Entrada: " + getNombre() + " se sirven crocantes, de plátano verde frito.";
    }
}
