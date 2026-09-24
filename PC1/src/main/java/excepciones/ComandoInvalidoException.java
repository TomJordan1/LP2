package excepciones;

// Se lanza cuando una línea del protocolo está mal formada
// (comando desconocido, faltan argumentos, número mal escrito, sin LOGIN...)
public class ComandoInvalidoException extends ChatException {
    public ComandoInvalidoException(String mensaje) {
        super("COMANDO_INVALIDO", mensaje);
    }
}
