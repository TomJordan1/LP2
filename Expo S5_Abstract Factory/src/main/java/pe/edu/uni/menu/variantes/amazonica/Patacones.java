package pe.edu.uni.menu.variantes.amazonica;

import pe.edu.uni.menu.producto.Entrada;

/** PRODUCTO CONCRETO (package-private): entrada Selva (amazónica). Solo FabricaAmazonica la instancia. */
class Patacones implements Entrada {

    @Override
    public String getNombre() {
        return "Patacones";
    }

    @Override
    public String servir() {
        return "Entrada: " + getNombre() + " se sirven crocantes, de plátano verde frito.";
    }
}
