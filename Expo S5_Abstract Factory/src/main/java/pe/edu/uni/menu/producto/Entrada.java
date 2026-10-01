package pe.edu.uni.menu.producto;

/**
 * PRODUCTO ABSTRACTO #1 de la familia "menú".
 *
 * Rol en el patrón: define QUÉ sabe hacer cualquier entrada, sin decir
 * de qué región es. El cliente solo conoce esta interfaz; nunca sabrá
 * si recibió una Papa a la Huancaína o unos Patacones.
 */
public interface Entrada {

    /** Nombre del plato, para mostrarlo en el menú. */
    String getNombre();

    /** Acción propia de una entrada: servirse antes del fondo. */
    String servir();
}
