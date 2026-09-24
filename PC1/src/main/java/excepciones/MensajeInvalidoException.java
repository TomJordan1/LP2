package excepciones;

// Se lanza cuando un mensaje no cumple sus reglas (vacío, muy largo, caracteres prohibidos)
public class MensajeInvalidoException extends ChatException {

    public MensajeInvalidoException(String mensaje) {
        super("MENSAJE_INVALIDO", mensaje);
    }

    // Constructor protegido: permite que una subclase use su propio código
    protected MensajeInvalidoException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
