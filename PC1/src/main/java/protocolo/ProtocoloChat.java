package protocolo;

import excepciones.ChatException;
import excepciones.ComandoInvalidoException;
import modelo.Mensaje;
import modelo.Transmisible;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

// Traductor entre TEXTO (lo que viaja por el socket) y OBJETOS (lo que usa Java).
//
// Cliente -> Servidor : LOGIN|ana   TXT|bruno|hola   TXT|TODOS|hola
//                       USUARIOS    HISTORIAL        SALIR
// Servidor -> Cliente : OK|detalle  ERROR|CODIGO|detalle  INFO|evento
//                       MSG|<mensaje serializado>   HIST|<mensaje serializado>
//                       PARTICIPANTES|ana,bruno   UNIDO|carla   SALIO|carla   (sala de llamada)
public class ProtocoloChat {

    private static final String SEP = Transmisible.SEPARADOR;
    private static final String SEP_REGEX = Pattern.quote(SEP);

    // ---------- entrada: texto -> Peticion ----------

    public Peticion leer(String linea) {
        if (linea == null || linea.isBlank()) {
            throw new ComandoInvalidoException("Linea vacia");
        }
        String[] cabeza = linea.split(SEP_REGEX, 2);          // [comando, resto]
        Comando comando = Comando.desde(cabeza[0]);

        if (comando.argumentos() == 0) {
            return new Peticion(comando, new ArrayList<>());
        }
        if (cabeza.length < 2) {
            throw faltanArgumentos(comando);
        }
        // el último argumento se queda con lo que sobre (así el texto se valida después)
        String[] args = cabeza[1].split(SEP_REGEX, comando.argumentos());
        if (args.length != comando.argumentos()) {
            throw faltanArgumentos(comando);
        }
        return new Peticion(comando, new ArrayList<>(Arrays.asList(args)));
    }

    private ComandoInvalidoException faltanArgumentos(Comando comando) {
        return new ComandoInvalidoException(comando + " espera " + comando.argumentos()
                + " argumento(s). Formato: " + comando.formato());
    }

    // ---------- salida: objetos -> texto ----------

    public String mensaje(Mensaje m) {
        return "MSG" + SEP + m.serializar();
    }

    public String historial(Mensaje m) {
        return "HIST" + SEP + m.serializar();
    }

    public String ok(String detalle) {
        return "OK" + SEP + detalle;
    }

    public String error(ChatException e) {
        return "ERROR" + SEP + e.getCodigo() + SEP + e.getMessage();
    }

    public String info(String evento) {
        return "INFO" + SEP + evento;
    }

    // ---------- sala de videollamada ----------

    public String participantes(List<String> nombres) {
        return "PARTICIPANTES" + SEP + String.join(",", nombres);
    }

    public String unido(String usuario) {
        return "UNIDO" + SEP + usuario;
    }

    public String salio(String usuario) {
        return "SALIO" + SEP + usuario;
    }
}
