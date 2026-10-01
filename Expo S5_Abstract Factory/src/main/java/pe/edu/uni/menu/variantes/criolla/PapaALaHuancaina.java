package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Entrada;

/**
 * PRODUCTO CONCRETO: entrada de la variante Costa (criolla).
 *
 * Es "package-private" (sin la palabra public): SOLO las clases de este
 * mismo paquete pueden hacer new PapaALaHuancaina(). En la práctica, la única que lo
 * hace es FabricaCriolla. Así el compilador IMPIDE que el cliente cree
 * productos sueltos y mezcle regiones por accidente.
 */
class PapaALaHuancaina implements Entrada {

    @Override
    public String getNombre() {
        return "Papa a la Huancaína";
    }

    @Override
    public String servir() {
        return "Entrada: " + getNombre() + " se sirve fría, bañada en crema de ají amarillo.";
    }
}
