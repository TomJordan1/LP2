package pe.edu.uni.menu.config;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.variantes.amazonica.FabricaAmazonica;
import pe.edu.uni.menu.variantes.andina.FabricaAndina;
import pe.edu.uni.menu.variantes.criolla.FabricaCriolla;

/**
 * Punto de configuración: el ÚNICO lugar que conoce las fábricas concretas.
 *
 * Equivale al "ApplicationConfigurator" de refactoring.guru.
 * OJO: este switch NO es el patrón Abstract Factory; es solo la decisión
 * de "qué fábrica usar" que se toma UNA vez al iniciar la aplicación.
 * Agregar una región nueva = una clase fábrica nueva + un case aquí.
 */
public final class SelectorDeFabrica {

    // Clase utilitaria: no tiene sentido crear instancias de ella.
    private SelectorDeFabrica() {
    }

    public static FabricaDeMenu crearPara(Region region) {
        switch (region) {
            case CRIOLLA:
                return new FabricaCriolla();
            case ANDINA:
                return new FabricaAndina();
            case AMAZONICA:
                return new FabricaAmazonica();
            default:
                throw new IllegalArgumentException("Sin fábrica para: " + region);
        }
    }
}
