package excepciones;

// Se lanza cuando se busca un ID o usuario que no existe / no está conectado
public class NoEncontradoException extends ChatException {
    public NoEncontradoException(String mensaje) {
        super("NO_ENCONTRADO", mensaje);
    }
}
