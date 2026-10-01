package pe.edu.uni.menu.app;

import java.util.Scanner;

import pe.edu.uni.menu.cliente.MenuDelDia;
import pe.edu.uni.menu.cliente.Restaurante;
import pe.edu.uni.menu.config.Region;
import pe.edu.uni.menu.config.SelectorDeFabrica;
import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.problema.PedidoSinPatron;

/*
 * ENUNCIADO
 * Un restaurante ofrece un "menú del día" (entrada + fondo + bebida) en tres
 * variantes regionales: criolla, andina y amazónica. Los tres platos deben
 * pertenecer siempre a la misma región, y debe poder agregarse una región
 * nueva sin modificar el código del restaurante.
 * Solución: patrón creacional Abstract Factory.
 *
 * Ejecución:
 *   sin argumentos           -> menú interactivo
 *   CRIOLLA|ANDINA|AMAZONICA -> muestra esa región directamente
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            mostrarMenuDe(Region.desdeTexto(args[0]));
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== MENÚ REGIONAL — Abstract Factory ===");
            System.out.println("1) Criolla   2) Andina   3) Amazónica");
            System.out.println("4) Ver las tres regiones con el MISMO cliente");
            System.out.println("5) Ver el problema SIN patrón");
            System.out.print("Opción: ");
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1": mostrarMenuDe(Region.CRIOLLA); break;
                case "2": mostrarMenuDe(Region.ANDINA); break;
                case "3": mostrarMenuDe(Region.AMAZONICA); break;
                case "4":
                    for (Region region : Region.values()) {
                        mostrarMenuDe(region);
                    }
                    break;
                case "5":
                    System.out.println("\n--- Pedido SIN Abstract Factory ---");
                    PedidoSinPatron.demostrar();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Los 3 pasos del patrón en acción:
     *   1) Configuración elige la fábrica concreta (única vez que se sabe la región).
     *   2) Se la entrega al cliente como FabricaDeMenu (abstracción).
     *   3) El cliente trabaja sin saber qué región recibió.
     */
    private static void mostrarMenuDe(Region region) {
        FabricaDeMenu fabrica = SelectorDeFabrica.crearPara(region);   // 1
        Restaurante restaurante = new Restaurante(fabrica);           // 2
        MenuDelDia menu = restaurante.prepararMenuDelDia();           // 3

        System.out.println("\n--- Menú " + region + " (" + region.getZona() + ") ---");
        System.out.println(menu.presentar());
    }
}
