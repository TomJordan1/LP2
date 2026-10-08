package pe.edu.uni.menu.modelo;

public class Bebida extends Producto {

    private final int mililitros;

    public Bebida(String nombre, double precio, int mililitros) {
        super(nombre, precio);
        this.mililitros = mililitros;
    }

    @Override
    protected String detalle() {
        return mililitros + " ml";
    }
}
