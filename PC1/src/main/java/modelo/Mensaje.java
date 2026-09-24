package modelo;

import excepciones.MensajeInvalidoException;

import java.time.LocalTime;

// Clase base abstracta: datos comunes de CUALQUIER mensaje.
// No se puede instanciar "un mensaje genérico": siempre es texto o video.
public abstract class Mensaje implements Identificable, Transmisible {

    // Destino especial: el mensaje va para todos los conectados
    public static final String PARA_TODOS = "TODOS";

    private final int id;
    private final String remitente;
    private final String destino;
    private final LocalTime hora;

    protected Mensaje(int id, String remitente, String destino) {
        if (remitente == null || remitente.isBlank()) {
            throw new MensajeInvalidoException("El remitente es obligatorio");
        }
        if (destino == null || destino.isBlank()) {
            throw new MensajeInvalidoException("El destino es obligatorio");
        }
        this.id = id;
        this.remitente = remitente.trim();
        this.destino = destino.trim();
        this.hora = LocalTime.now().withNano(0);
    }

    @Override
    public int id() {
        return id;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestino() {
        return destino;
    }

    public LocalTime getHora() {
        return hora;
    }

    public boolean esParaTodos() {
        return PARA_TODOS.equalsIgnoreCase(destino);
    }

    // ¿Este mensaje le interesa al usuario? (lo envió, lo recibió o fue para todos)
    public boolean involucraA(String usuario) {
        return remitente.equals(usuario) || destino.equals(usuario) || esParaTodos();
    }

    // Cada subclase dice su código en el protocolo (ej. "TXT")
    public abstract String codigo();

    // Cada subclase decide cómo se ve en consola
    public abstract String mostrar();

    // Cada subclase aporta SOLO sus campos propios para el protocolo
    protected abstract String contenidoSerializado();

    // Método plantilla: la parte común la arma la base, la parte variable la subclase
    @Override
    public String serializar() {
        return codigo() + SEPARADOR + remitente + SEPARADOR + destino
                + SEPARADOR + hora + SEPARADOR + contenidoSerializado();
    }

    // Encabezado común reutilizado por las subclases en mostrar()
    protected String encabezado() {
        String para = esParaTodos() ? "todos" : destino;
        return "[" + hora + "] " + remitente + " -> " + para;
    }

    @Override
    public String toString() {
        return "#" + id + " " + mostrar();
    }
}
