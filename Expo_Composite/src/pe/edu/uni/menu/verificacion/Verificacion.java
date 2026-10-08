package pe.edu.uni.menu.verificacion;

import pe.edu.uni.menu.modelo.Bebida;
import pe.edu.uni.menu.modelo.Combo;
import pe.edu.uni.menu.modelo.ItemMenu;
import pe.edu.uni.menu.modelo.Plato;
import pe.edu.uni.menu.servicio.Pedido;

public class Verificacion {

    private static int fallos = 0;

    public static void main(String[] args) {
        ItemMenu lomo = new Plato("Lomo saltado", 28.00, false);
        ItemMenu chicha = new Bebida("Chicha morada", 6.00, 500);
        ItemMenu causa = new Plato("Causa rellena", 15.00, false);

        comprobar("una hoja devuelve su propio precio", lomo.precio(), 28.00);

        Combo vacio = new Combo("Combo vacio", 0.10);
        comprobar("un combo vacio cuesta cero", vacio.precio(), 0.00);

        Combo personal = new Combo("Combo personal", 0.10).agregar(lomo).agregar(chicha);
        comprobar("el combo suma a sus hijos y aplica su descuento", personal.precio(), 30.60);

        Combo familiar = new Combo("Combo familiar", 0.15).agregar(personal).agregar(causa);
        comprobar("un combo dentro de otro se calcula de forma recursiva", familiar.precio(), 38.76);

        familiar.quitar(causa);
        comprobar("al quitar un hijo el precio se actualiza", familiar.precio(), 26.01);

        Pedido pedido = new Pedido("Mesa 1");
        pedido.agregar(personal);
        pedido.agregar(causa);
        comprobar("el pedido trata igual a combos y productos", pedido.total(), 45.60);

        comprobarError("un combo no puede contenerse a si mismo", () -> personal.agregar(personal));
        comprobarError("no se aceptan precios negativos", () -> new Plato("Arroz", -1, false));

        System.out.println(fallos == 0 ? "Todas las comprobaciones pasaron" : "Comprobaciones fallidas: " + fallos);
        if (fallos > 0) {
            System.exit(1);
        }
    }

    private static void comprobar(String caso, double obtenido, double esperado) {
        boolean correcto = Math.abs(obtenido - esperado) < 0.001;
        registrar(caso, correcto, String.format("esperado %.2f, obtenido %.2f", esperado, obtenido));
    }

    private static void comprobarError(String caso, Runnable accion) {
        boolean lanzo = false;
        try {
            accion.run();
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        registrar(caso, lanzo, "se esperaba una excepcion");
    }

    private static void registrar(String caso, boolean correcto, String detalle) {
        if (!correcto) {
            fallos++;
        }
        System.out.println((correcto ? "ok     " : "fallo  ") + caso + (correcto ? "" : " -> " + detalle));
    }
}
