package pe.edu.uni.menu.modelo;

public class Plato extends Producto {

    private final boolean picante;

    public Plato(String nombre, double precio, boolean picante) {
        super(nombre, precio);
        this.picante = picante;
    }

    @Override
    protected String detalle() {
        return picante ? "plato picante" : "plato";
    }
}
