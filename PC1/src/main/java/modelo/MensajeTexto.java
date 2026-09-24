package modelo;

import excepciones.MensajeInvalidoException;

// Mensaje de texto: agrega el contenido escrito y sus reglas de validación
public class MensajeTexto extends Mensaje {

    public static final int MAX_CARACTERES = 300;

    private final String contenido;

    public MensajeTexto(int id, String remitente, String destino, String contenido) {
        super(id, remitente, destino);
        validar(contenido);
        this.contenido = contenido.trim();
    }

    private static void validar(String contenido) {
        if (contenido == null || contenido.isBlank()) {
            throw new MensajeInvalidoException("El texto no puede estar vacio");
        }
        if (contenido.length() > MAX_CARACTERES) {
            throw new MensajeInvalidoException(
                    "El texto supera los " + MAX_CARACTERES + " caracteres");
        }
        if (contenido.contains(SEPARADOR)) {
            throw new MensajeInvalidoException(
                    "El texto no puede contener el caracter " + SEPARADOR);
        }
    }

    public String getContenido() {
        return contenido;
    }

    @Override
    public String codigo() {
        return "TXT";
    }

    @Override
    public String mostrar() {
        return encabezado() + ": " + contenido;
    }

    @Override
    protected String contenidoSerializado() {
        return contenido;
    }
}
