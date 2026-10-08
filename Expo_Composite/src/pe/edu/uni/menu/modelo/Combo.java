package pe.edu.uni.menu.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Combo implements ItemMenu {

    private final String nombre;
    private final double descuento;
    private final List<ItemMenu> items = new ArrayList<>();

    public Combo(String nombre, double descuento) {
        if (descuento < 0 || descuento >= 1) {
            throw new IllegalArgumentException("El descuento va entre 0 y 1");
        }
        this.nombre = nombre;
        this.descuento = descuento;
    }

    // devuelve el mismo combo para poder encadenar varios agregar seguidos
    public Combo agregar(ItemMenu item) {
        if (item == this) {
            throw new IllegalArgumentException("Un combo no puede contenerse a si mismo");
        }
        items.add(item);
        return this;
    }

    public void quitar(ItemMenu item) {
        items.remove(item);
    }

    public List<ItemMenu> items() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public String nombre() {
        return nombre;
    }

    // el combo no guarda un precio propio, lo pregunta a sus hijos sin saber si son productos u otros combos
    @Override
    public double precio() {
        double suma = 0;
        for (ItemMenu item : items) {
            suma += item.precio();
        }
        return suma * (1 - descuento);
    }

    @Override
    public String describir(int nivel) {
        StringBuilder texto = new StringBuilder();
        texto.append("  ".repeat(nivel))
                .append("+ ").append(nombre)
                .append(" (").append(Math.round(descuento * 100)).append("% dscto)  S/ ")
                .append(String.format("%.2f", precio()))
                .append(System.lineSeparator());
        for (ItemMenu item : items) {
            texto.append(item.describir(nivel + 1));
        }
        return texto.toString();
    }
}
