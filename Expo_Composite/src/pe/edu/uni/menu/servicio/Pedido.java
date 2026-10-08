package pe.edu.uni.menu.servicio;

import java.util.ArrayList;
import java.util.List;

import pe.edu.uni.menu.modelo.ItemMenu;

public class Pedido {

    private final String cliente;
    private final List<ItemMenu> items = new ArrayList<>();

    public Pedido(String cliente) {
        this.cliente = cliente;
    }

    // acepta un plato suelto o un combo completo, para el pedido los dos son un item
    public void agregar(ItemMenu item) {
        items.add(item);
    }

    public String cliente() {
        return cliente;
    }

    public List<ItemMenu> items() {
        return List.copyOf(items);
    }

    public double total() {
        double total = 0;
        for (ItemMenu item : items) {
            total += item.precio();
        }
        return total;
    }
}
