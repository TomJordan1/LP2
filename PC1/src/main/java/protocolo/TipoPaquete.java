package protocolo;

import excepciones.ComandoInvalidoException;

// Qué viaja dentro de un paquete y cuánto puede pesar como máximo.
// El límite evita que un cliente malicioso o dañado reserve memoria sin control.
public enum TipoPaquete {
    TEXTO(1, 8 * 1024),        // líneas del protocolo de chat (semana 4)
    VIDEO(2, 256 * 1024),      // un cuadro de cámara comprimido en JPEG
    AUDIO(3, 16 * 1024);       // un bloque de sonido PCM

    private final byte codigo;
    private final int maxBytes;

    TipoPaquete(int codigo, int maxBytes) {
        this.codigo = (byte) codigo;
        this.maxBytes = maxBytes;
    }

    public byte codigo() {
        return codigo;
    }

    public int maxBytes() {
        return maxBytes;
    }

    public static TipoPaquete desde(byte codigo) {
        for (TipoPaquete t : values()) {
            if (t.codigo == codigo) {
                return t;
            }
        }
        throw new ComandoInvalidoException("Tipo de paquete desconocido: " + codigo);
    }
}
