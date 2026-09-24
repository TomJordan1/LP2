package excepciones;

// Excepción base del chat.
// Todas las reglas rotas del sistema heredan de aquí, así el servidor puede
// atraparlas con un solo catch y responder "ERROR|CODIGO|detalle" (polimorfismo).
public class ChatException extends RuntimeException {

    private final String codigo;

    public ChatException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    // Código corto que viaja en el protocolo (ej. DUPLICADO, NO_ENCONTRADO)
    public String getCodigo() {
        return codigo;
    }
}
