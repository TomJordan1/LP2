package pe.edu.uni.menu.servicio;

import pe.edu.uni.menu.modelo.ItemMenu;

public class Boleta {

    public String generar(Pedido pedido) {
        StringBuilder texto = new StringBuilder();
        texto.append("Pedido de ").append(pedido.cliente()).append(System.lineSeparator());
        for (ItemMenu item : pedido.items()) {
            texto.append(item.describir(1));
        }
        texto.append("Total a pagar: S/ ").append(String.format("%.2f", pedido.total()));
        return texto.toString();
    }
}
