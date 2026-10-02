package pe.edu.uni.menu.app;

import java.util.Scanner;

import pe.edu.uni.menu.cliente.Restaurante;
import pe.edu.uni.menu.fabrica.FactoryMenu;
import pe.edu.uni.menu.variantes.amazonica.FactoryAmazonico;
import pe.edu.uni.menu.variantes.andina.FactoryAndino;
import pe.edu.uni.menu.variantes.criolla.FactoryCriollo;

public class Main {
    public static void main(String[] args) {
        Restaurante restaurante = new Restaurante();
        Scanner sc = new Scanner(System.in);
        int cliente = 1;
        String opcion = "";

        while (!opcion.equals("0")) {
            System.out.println();
            System.out.println("=== MENU REGIONAL - Abstract Factory ===");
            System.out.println("1) Menu Criollo");
            System.out.println("2) Menu Andino");
            System.out.println("3) Menu Amazonico");
            System.out.println("4) Ver los tres menus");
            System.out.println("0) Salir");
            System.out.print("Cliente " + cliente + ", elija una opcion: ");
            if (!sc.hasNextLine()) {
                break;
            }
            opcion = sc.nextLine().trim();

            if (opcion.equals("4")) {
                System.out.println("Cliente " + cliente + " prefiere Menu Criollo");
                restaurante.servir(new FactoryCriollo());
                System.out.println("Cliente " + cliente + " prefiere Menu Andino");
                restaurante.servir(new FactoryAndino());
                System.out.println("Cliente " + cliente + " prefiere Menu Amazonico");
                restaurante.servir(new FactoryAmazonico());
                cliente++;
            } else if (opcion.equals("0")) {
                System.out.println("Gracias por su visita.");
            } else {
                FactoryMenu factory = elegirFactory(opcion);
                if (factory == null) {
                    System.out.println("Opcion no valida.");
                } else {
                    System.out.println("Cliente " + cliente + " prefiere " + nombreMenu(opcion));
                    restaurante.servir(factory);
                    cliente++;
                }
            }
        }
        sc.close();
    }

    // Unico lugar que conoce las fabricas concretas: traduce la opcion en pantalla a una fabrica.
    private static FactoryMenu elegirFactory(String opcion) {
        switch (opcion) {
            case "1": return new FactoryCriollo();
            case "2": return new FactoryAndino();
            case "3": return new FactoryAmazonico();
            default:  return null;
        }
    }

    private static String nombreMenu(String opcion) {
        switch (opcion) {
            case "1": return "Menu Criollo";
            case "2": return "Menu Andino";
            default:  return "Menu Amazonico";
        }
    }
}
