package pe.edu.uni.menu.cliente;

import java.util.Objects;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * CLIENTE del patrón.
 *
 * Mira los imports: no aparece NINGÚN paquete de "variantes".
 * El cliente solo conoce:
 *   - la fábrica abstracta (FabricaDeMenu)
 *   - los productos abstractos (Entrada, Fondo, Bebida)
 *
 * Por eso este archivo NO cambia aunque mañana agreguemos una región
 * nueva (Principio Abierto/Cerrado).
 */
public class Restaurante {

    // La fábrica llega desde afuera (inyección por constructor).
    // El restaurante no decide QUÉ región es: eso lo decide la configuración.
    private final FabricaDeMenu fabrica;

    public Restaurante(FabricaDeMenu fabrica) {
        this.fabrica = Objects.requireNonNull(fabrica, "La fábrica no puede ser null");
    }

    /**
     * Pide los tres productos a la MISMA fábrica.
     * Esa es la garantía del patrón: misma fábrica => misma familia
     * => productos compatibles. No hay if, no hay new, no hay mezcla posible.
     */
    public MenuDelDia prepararMenuDelDia() {
        Entrada entrada = fabrica.crearEntrada();
        Fondo fondo = fabrica.crearFondo();
        Bebida bebida = fabrica.crearBebida();
        return new MenuDelDia(entrada, fondo, bebida);
    }
}
