package protocolo;

// Valores que cliente y servidor deben compartir para entenderse
public final class ConfiguracionRed {

    public static final String HOST_DEFECTO = "localhost";
    public static final int PUERTO_DEFECTO = 5000;
    public static final int MAX_PARTICIPANTES = 6;   // tamaño del pool limitado (mínimo 4 en llamada)
    public static final int TIEMPO_CONEXION_MS = 5000;

    private ConfiguracionRed() {
        // clase de constantes: no se instancia
    }
}
