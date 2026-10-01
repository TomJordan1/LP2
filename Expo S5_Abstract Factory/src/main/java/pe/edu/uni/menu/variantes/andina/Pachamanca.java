package pe.edu.uni.menu.variantes.andina;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Fondo;

/** PRODUCTO CONCRETO (package-private): fondo Sierra (andina). Solo FabricaAndina lo instancia. */
class Pachamanca implements Fondo {

    @Override
    public String getNombre() {
        return "Pachamanca";
    }

    @Override
    public String servir() {
        return "Fondo: " + getNombre() + " sale de la tierra, cocida entre piedras calientes.";
    }

    @Override
    public String acompanarCon(Bebida bebida) {
        return getNombre() + " acompañado de " + bebida.getNombre();
    }
}
