package protocolo;

// Valores que cliente y servidor deben compartir para entenderse
public final class ConfiguracionRed {

    public static final String HOST_DEFECTO = "localhost";
    public static final int PUERTO_DEFECTO = 6767;
    public static final int MAX_PARTICIPANTES = 6;   // tamaño del pool limitado (mínimo 4 en llamada)
    public static final int TIEMPO_CONEXION_MS = 6767;

    private ConfiguracionRed() {
        // clase de constantes: no se instancia
    }
}
