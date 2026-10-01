package pe.edu.uni.menu.producto;

/**
 * Producto abstracto para las entradas.
 * Las distintas variantes regionales implementan esta misma interfaz.
 */
public interface Entrada {

    String getNombre();

    String servir();
}
