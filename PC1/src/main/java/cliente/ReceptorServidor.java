package cliente;

import excepciones.ChatException;
import protocolo.Paquete;

import java.io.IOException;

// Hilo que ESCUCHA al servidor todo el tiempo y reparte cada paquete según su tipo.
public class ReceptorServidor implements Runnable {

    private final ConexionServidor conexion;
    private final EventosLlamada eventos;

    public ReceptorServidor(ConexionServidor conexion, EventosLlamada eventos) {
        this.conexion = conexion;
        this.eventos = eventos;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Paquete p = conexion.recibir();
                switch (p.tipo()) {
                    case TEXTO:
                        eventos.alRecibirTexto(p.comoTexto());
                        break;
                    case VIDEO:
                        eventos.alRecibirVideo(p.origen(), p.datos());
                        break;
                    case AUDIO:
                        eventos.alRecibirAudio(p.origen(), p.datos());
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ChatException e) {
            eventos.alDesconectar();
        }
    }
}
