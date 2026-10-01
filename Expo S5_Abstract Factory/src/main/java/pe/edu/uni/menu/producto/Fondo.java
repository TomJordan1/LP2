package pe.edu.uni.menu.producto;

/**
 * PRODUCTO ABSTRACTO #2 de la familia "menú".
 *
 * Además de servirse, el fondo COLABORA con otro producto de la familia
 * (la bebida). Esta colaboración es la razón de ser del patrón: solo
 * tiene sentido entre productos de la MISMA variante (misma región).
 */
public interface Fondo {

    String getNombre();

    String servir();

    /**
     * Colaboración entre productos de la familia.
     * Recibe la ABSTRACCIÓN Bebida, no una clase concreta:
     * el fondo tampoco se acopla a una bebida específica.
     */
    String acompanarCon(Bebida bebida);
}
