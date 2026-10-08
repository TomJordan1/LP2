package pe.edu.uni.menu.app;

import pe.edu.uni.menu.modelo.Bebida;
import pe.edu.uni.menu.modelo.Combo;
import pe.edu.uni.menu.modelo.ItemMenu;
import pe.edu.uni.menu.modelo.Plato;
import pe.edu.uni.menu.servicio.Boleta;
import pe.edu.uni.menu.servicio.Pedido;

public class Main {

    public static void main(String[] args) {
        ItemMenu lomo = new Plato("Lomo saltado", 28.00, false);
        ItemMenu ceviche = new Plato("Ceviche clasico", 32.00, true);
        ItemMenu causa = new Plato("Causa rellena", 15.00, false);
        ItemMenu chicha = new Bebida("Chicha morada", 6.00, 500);
        ItemMenu limonada = new Bebida("Limonada", 5.00, 400);
        ItemMenu mazamorra = new Plato("Mazamorra morada", 9.00, false);

        Combo comboPersonal = new Combo("Combo personal", 0.10)
                .agregar(lomo)
                .agregar(chicha);

        Combo comboMarino = new Combo("Combo marino", 0.10)
                .agregar(ceviche)
                .agregar(limonada);

        // un combo que contiene otros combos ademas de productos sueltos
        Combo comboFamiliar = new Combo("Combo familiar", 0.15)
                .agregar(comboPersonal)
                .agregar(comboMarino)
                .agregar(causa);

        Pedido pedido = new Pedido("Mesa 4");
        pedido.agregar(comboFamiliar);
        pedido.agregar(mazamorra);

        System.out.println(new Boleta().generar(pedido));
    }
}
