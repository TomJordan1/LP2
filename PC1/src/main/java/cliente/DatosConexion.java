package cliente;

// Lo que el usuario escribe en la ventana de ingreso. Inmutable.
public class DatosConexion {

    public static final int SIN_CAMARA = -1;

    private final String nombre;
    private final String servidor;
    private final int puerto;
    private final int camara;

    public DatosConexion(String nombre, String servidor, int puerto, int camara) {
        this.nombre = nombre;
        this.servidor = servidor;
        this.puerto = puerto;
        this.camara = camara;
    }

    public String getNombre() {
        return nombre;
    }

    public String getServidor() {
        return servidor;
    }

    public int getPuerto() {
        return puerto;
    }

    public int getCamara() {
        return camara;
    }
}
