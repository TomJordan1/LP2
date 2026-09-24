package cliente;

// Convierte lo que se escribe en la caja de chat a una línea del protocolo.
//   hola a todos      ->  TXT|TODOS|hola a todos
//   @bruno hola       ->  TXT|bruno|hola
//   /usuarios  /historial
// No valida reglas: eso lo hace el servidor (una sola fuente de verdad).
public class TraductorEntrada {

    public static final String AYUDA = String.join(System.lineSeparator(),
            "  Comandos del chat:",
            "    texto libre       -> mensaje para todos",
            "    @usuario texto    -> mensaje privado",
            "    /usuarios         -> quien esta conectado",
            "    /historial        -> tus mensajes",
            "    /ayuda            -> esta ayuda");

    // Devuelve null cuando no hay nada que enviar
    public String traducir(String entrada) {
        String texto = entrada.trim();
        if (texto.isEmpty() || texto.equalsIgnoreCase("/ayuda")) {
            return null;
        }
        if (texto.startsWith("@")) {
            String[] partes = texto.substring(1).split("\\s+", 2);
            return "TXT|" + partes[0] + "|" + (partes.length > 1 ? partes[1] : "");
        }
        if (texto.equalsIgnoreCase("/usuarios")) {
            return "USUARIOS";
        }
        if (texto.equalsIgnoreCase("/historial")) {
            return "HISTORIAL";
        }
        return "TXT|TODOS|" + texto;
    }
}
