package servicio;

import modelo.MensajeTexto;

// Las validaciones viven en los constructores de cada mensaje.
public class FabricaMensajes {

    private final GeneradorIds ids;

    public FabricaMensajes(GeneradorIds ids) {
        this.ids = ids;
    }

    public MensajeTexto crearTexto(String remitente, String destino, String contenido) {
        return new MensajeTexto(ids.siguiente(), remitente, destino, contenido);
    }

}
