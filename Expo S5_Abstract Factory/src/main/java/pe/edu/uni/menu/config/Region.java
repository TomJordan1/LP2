package pe.edu.uni.menu.config;

/**
 * Opciones de región disponibles en el ejemplo.
 * El enum evita trabajar con textos distintos para representar una misma opción.
 */
public enum Region {

    CRIOLLA("Costa"),
    ANDINA("Sierra"),
    AMAZONICA("Selva");

    private String zona;

    Region(String zona) {
        this.zona = zona;
    }

    public String getZona() {
        return zona;
    }
}
