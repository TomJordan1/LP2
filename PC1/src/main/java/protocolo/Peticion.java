package protocolo;

import excepciones.ComandoInvalidoException;

import java.util.Collections;
import java.util.List;

// Una línea del cliente ya interpretada: comando + argumentos.
// Inmutable: una vez creada no cambia.
public class Peticion {

    private final Comando comando;
    private final List<String> argumentos;

    public Peticion(Comando comando, List<String> argumentos) {
        this.comando = comando;
        this.argumentos = Collections.unmodifiableList(argumentos);
    }

    public Comando getComando() {
        return comando;
    }

    public String arg(int posicion) {
        return argumentos.get(posicion).trim();
    }

    public int argEntero(int posicion) {
        try {
            return Integer.parseInt(arg(posicion));
        } catch (NumberFormatException e) {
            throw new ComandoInvalidoException("Se esperaba un numero entero: " + arg(posicion));
        }
    }

    public double argDecimal(int posicion) {
        try {
            return Double.parseDouble(arg(posicion));
        } catch (NumberFormatException e) {
            throw new ComandoInvalidoException("Se esperaba un numero decimal: " + arg(posicion));
        }
    }
}
