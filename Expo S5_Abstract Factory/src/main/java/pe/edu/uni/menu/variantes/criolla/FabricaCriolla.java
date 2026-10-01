package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.fabrica.FabricaDeMenu;
import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Entrada;
import pe.edu.uni.menu.producto.Fondo;

/**
 * FÁBRICA CONCRETA de la variante Costa (criolla).
 *
 * Implementa la fábrica abstracta y produce SOLO productos de su región.
 * Es la única clase pública del paquete: es la "puerta de entrada"
 * a toda la familia criolla.
 *
 * Detalle clave: las firmas devuelven Entrada/Fondo/Bebida (abstractos),
 * aunque por dentro se hace new de clases concretas. El "new" queda
 * encerrado aquí y en ningún otro lugar del sistema.
 */
public class FabricaCriolla implements FabricaDeMenu {

    @Override
    public Entrada crearEntrada() {
        return new PapaALaHuancaina();
    }

    @Override
    public Fondo crearFondo() {
        return new LomoSaltado();
    }

    @Override
    public Bebida crearBebida() {
        return new ChichaMorada();
    }
}
