package pe.edu.uni.menu.producto;

/**
 * Producto abstracto para los fondos.
 * Además de servirse, el fondo puede trabajar con otra abstracción del mismo menú: Bebida.
 */
public interface Fondo {

    String getNombre();

    String servir();

    String acompanarCon(Bebida bebida);
}
