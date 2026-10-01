package pe.edu.uni.menu.config;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.variantes.amazonica.FabricaAmazonica;
import pe.edu.uni.menu.variantes.andina.FabricaAndina;
import pe.edu.uni.menu.variantes.criolla.FabricaCriolla;

/**
 * Relaciona la región elegida con una fábrica concreta.
 * La selección queda separada del Restaurante para que este pueda trabajar únicamente con FabricaDeMenu.
 */
public class SelectorDeFabrica {

    public static FabricaDeMenu crearPara(Region region) {
        switch (region) {
            case CRIOLLA:
                return new FabricaCriolla();
            case ANDINA:
                return new FabricaAndina();
            case AMAZONICA:
                return new FabricaAmazonica();
            default:
                throw new IllegalArgumentException("Región no válida");
        }
    }
}
