package protocolo;

import java.nio.charset.StandardCharsets;

// Unidad que viaja por el socket: tipo + quién lo origina + bytes.
// Inmutable. El texto del chat de la semana 4 viaja DENTRO de un paquete TEXTO.
public final class Paquete {

    private final TipoPaquete tipo;
    private final String origen;     // lo pone el SERVIDOR (no se confía en el cliente)
    private final byte[] datos;

    public Paquete(TipoPaquete tipo, String origen, byte[] datos) {
        this.tipo = tipo;
        this.origen = origen == null ? "" : origen;
        this.datos = datos;
    }

    public static Paquete texto(String linea) {
        return new Paquete(TipoPaquete.TEXTO, "", linea.getBytes(StandardCharsets.UTF_8));
    }

    public static Paquete media(TipoPaquete tipo, byte[] datos) {
        return new Paquete(tipo, "", datos);
    }

    // Copia con el origen real (lo usa el servidor al retransmitir)
    public Paquete conOrigen(String nuevoOrigen) {
        return new Paquete(tipo, nuevoOrigen, datos);
    }

    public TipoPaquete tipo() {
        return tipo;
    }

    public String origen() {
        return origen;
    }

    public byte[] datos() {
        return datos;
    }

    public String comoTexto() {
        return new String(datos, StandardCharsets.UTF_8);
    }
}
