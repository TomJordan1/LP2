package pe.edu.uni.menu.fabrica;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * FÁBRICA ABSTRACTA (el corazón del patrón).
 *
 * Declara UN método de creación por cada producto de la familia.
 * Los tipos de retorno son las INTERFACES de producto, nunca clases
 * concretas: así quien use la fábrica no sabe (ni le importa) qué
 * variante está recibiendo.
 *
 * Regla mental:  1 método por TIPO de producto  (filas de la matriz)
 *                1 clase concreta por VARIANTE  (columnas de la matriz)
 */
public interface FabricaDeMenu {

    Entrada crearEntrada();

    Fondo crearFondo();

    Bebida crearBebida();
}
