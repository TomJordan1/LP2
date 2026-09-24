package cliente;

// Lo que el usuario puede hacer desde la ventana (lo implementa ClienteLlamada).
// La ventana solo conoce esta interfaz, no la lógica (separación vista / control).
public interface AccionesUsuario {

    void enviarChat(String entrada);

    boolean alternarCamara();       // devuelve el nuevo estado (true = encendida)

    boolean alternarMicrofono();

    void salir();
}
