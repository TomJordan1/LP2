package pe.edu.uni.menu.config;

/**
 * Las VARIANTES disponibles de la familia de productos.
 * Usar un enum en vez de Strings evita errores de tipeo ("Criola").
 */
public enum Region {

    CRIOLLA("Costa"),
    ANDINA("Sierra"),
    AMAZONICA("Selva");

    private final String zona;

    Region(String zona) {
        this.zona = zona;
    }

    public String getZona() {
        return zona;
    }

    /** Convierte el texto ingresado por el usuario en una Region válida. */
    public static Region desdeTexto(String texto) {
        try {
            return Region.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Región desconocida: " + texto
                    + ". Usa CRIOLLA, ANDINA o AMAZONICA.");
        }
    }
}
