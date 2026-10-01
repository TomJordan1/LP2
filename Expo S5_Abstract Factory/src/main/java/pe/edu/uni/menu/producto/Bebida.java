package pe.edu.uni.menu.producto;

/**
 * Producto abstracto para las bebidas.
 * Las fábricas concretas devuelven implementaciones de esta interfaz según su región.
 */
public interface Bebida {

    String getNombre();

    String servir();
}
