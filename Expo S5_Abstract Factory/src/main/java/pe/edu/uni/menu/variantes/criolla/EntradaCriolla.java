package pe.edu.uni.menu.variantes.criolla;

import pe.edu.uni.menu.producto.Entrada;

public class EntradaCriolla implements Entrada{
    @Override
    public String servir() {
        return "Entrada: Papa a la Huancaína se sirve fría, bañada en crema de ají amarillo.";
    }
}
