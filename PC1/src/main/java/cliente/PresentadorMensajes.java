package cliente;

// Convierte las líneas de texto del servidor en frases legibles para el panel de chat.
public class PresentadorMensajes {

    public String formatear(String linea) {
        String[] p = linea.split("\\|");
        switch (p[0]) {
            case "MSG":
                return mensaje(p, "");
            case "HIST":
                return mensaje(p, "(historial) ");
            case "OK":
                return "[ok] " + resto(p, 1);
            case "ERROR":
                return "[error] " + resto(p, 2);
            case "INFO":
                return "* " + resto(p, 1);
            default:
                return linea;
        }
    }

    // MSG|TXT|remitente|destino|hora|texto
    private String mensaje(String[] p, String prefijo) {
        if (p.length < 6) {
            return prefijo + String.join("|", p);
        }
        String para = "TODOS".equals(p[3]) ? "" : " (privado)";
        return prefijo + "[" + p[4] + "] " + p[2] + para + ": " + p[5];
    }

    private String resto(String[] p, int desde) {
        StringBuilder sb = new StringBuilder();
        for (int i = desde; i < p.length; i++) {
            if (i > desde) {
                sb.append(" ");
            }
            sb.append(p[i]);
        }
        return sb.toString();
    }
}
