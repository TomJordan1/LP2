package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Fondo;

public class FondoCriollo implements Fondo{
    @Override
    public String servir() {
        return "Fondo: Lomo Saltado sale del wok con papas fritas y arroz.";
    }
}
