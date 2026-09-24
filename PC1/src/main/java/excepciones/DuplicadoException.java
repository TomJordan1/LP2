package excepciones;

// Se lanza cuando se intenta registrar algo que ya existe (ID o usuario repetido)
public class DuplicadoException extends ChatException {
    public DuplicadoException(String mensaje) {
        super("DUPLICADO", mensaje);
    }
}
