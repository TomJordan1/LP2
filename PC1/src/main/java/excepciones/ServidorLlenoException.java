package excepciones;

// Se usa cuando el servidor ya atiende al máximo de clientes permitidos
public class ServidorLlenoException extends ChatException {
    public ServidorLlenoException(String mensaje) {
        super("SERVIDOR_LLENO", mensaje);
    }
}
