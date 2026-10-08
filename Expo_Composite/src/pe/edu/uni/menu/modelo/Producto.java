package pe.edu.uni.menu.modelo;

public abstract class Producto implements ItemMenu {

    private final String nombre;
    private final double precio;

    protected Producto(String nombre, double precio) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El producto necesita un nombre");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String nombre() {
        return nombre;
    }

    @Override
    public double precio() {
        return precio;
    }

    protected abstract String detalle();

    @Override
    public String describir(int nivel) {
        return "  ".repeat(nivel) + "- " + nombre + " (" + detalle() + ")  S/ "
                + String.format("%.2f", precio) + System.lineSeparator();
    }
}
