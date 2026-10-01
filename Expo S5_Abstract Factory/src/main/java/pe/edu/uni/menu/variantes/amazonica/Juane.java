package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Bebida;
import pe.edu.uni.menu.producto.Fondo;

/** PRODUCTO CONCRETO (package-private): fondo Selva (amazónica). Solo FabricaAmazonica lo instancia. */
class Juane implements Fondo {

    @Override
    public String getNombre() {
        return "Juane";
    }

    @Override
    public String servir() {
        return "Fondo: " + getNombre() + " se desenvuelve de la hoja de bijao humeante.";
    }

    @Override
    public String acompanarCon(Bebida bebida) {
        return getNombre() + " acompañado de " + bebida.getNombre();
    }
}
