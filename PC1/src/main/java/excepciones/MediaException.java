package excepciones;

// Falla de un dispositivo multimedia (cámara, micrófono o parlantes).
// La llamada NO se cae por esto: se avisa y se sigue sin ese dispositivo.
public class MediaException extends ChatException {
    public MediaException(String mensaje) {
        super("MEDIA", mensaje);
    }
}
