package pe.edu.uni.menu.app;

import java.util.Scanner;

import pe.edu.uni.menu.cliente.MenuDelDia;
import pe.edu.uni.menu.cliente.Restaurante;
import pe.edu.uni.menu.config.Region;
import pe.edu.uni.menu.config.SelectorDeFabrica;
import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.problema.PedidoSinPatron;

/**
 * Punto de entrada del ejemplo.
 * La región se elige aquí y, a partir de ella, se obtiene la fábrica concreta que usará el restaurante.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== MENÚ REGIONAL - Abstract Factory ===");
        System.out.println("1) Criolla");
        System.out.println("2) Andina");
        System.out.println("3) Amazónica");
        System.out.println("4) Comparar con una versión sin el patrón");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                mostrarMenuDe(Region.CRIOLLA);
                break;
            case "2":
                mostrarMenuDe(Region.ANDINA);
                break;
            case "3":
                mostrarMenuDe(Region.AMAZONICA);
                break;
            case "4":
                PedidoSinPatron.demostrar();
                break;
            default:
                System.out.println("Opción no válida.");
        }

        scanner.close();
    }

    private static void mostrarMenuDe(Region region) {
        // A partir de este punto se trabaja con FabricaDeMenu, sin depender de una fábrica regional concreta.
        FabricaDeMenu fabrica = SelectorDeFabrica.crearPara(region);
        Restaurante restaurante = new Restaurante(fabrica);
        MenuDelDia menu = restaurante.prepararMenuDelDia();

        System.out.println("\n--- Menú " + region + " (" + region.getZona() + ") ---");
        System.out.println(menu.presentar());
    }
}
