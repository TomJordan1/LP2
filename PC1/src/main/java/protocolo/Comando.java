package protocolo;

import excepciones.ComandoInvalidoException;

// Comandos de CHAT que el cliente envía como texto (paquete TEXTO).
// Cada uno sabe cuántos argumentos espera y cómo se escribe.

public enum Comando {
    LOGIN(1, "LOGIN|usuario"),
    TXT(2, "TXT|destino|texto"),
    USUARIOS(0, "USUARIOS"),
    HISTORIAL(0, "HISTORIAL"),
    SALIR(0, "SALIR");

    private final int argumentos;
    private final String formato;

    Comando(int argumentos, String formato) {
        this.argumentos = argumentos;
        this.formato = formato;
    }

    public int argumentos() {
        return argumentos;
    }

    public String formato() {
        return formato;
    }

    // Convierte el texto recibido en un Comando o lanza excepción
    public static Comando desde(String texto) {
        for (Comando c : values()) {
            if (c.name().equalsIgnoreCase(texto.trim())) {
                return c;
            }
        }
        throw new ComandoInvalidoException("Comando desconocido: " + texto);
    }
}
