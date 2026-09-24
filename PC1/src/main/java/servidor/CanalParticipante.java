package servidor;

import modelo.Mensaje;
import protocolo.CodecPaquete;
import protocolo.ColaSalida;
import protocolo.Paquete;
import protocolo.ProtocoloChat;
import servicio.Buzon;

import java.io.DataOutputStream;

// Todo lo que el servidor le manda a UN participante pasa por aquí.
// - Para el ServicioChat es un Buzon (recibe mensajes de chat).
// - Para la SalaLlamada es el destino de video y audio.
// Por dentro usa una ColaSalida con su propio hilo escritor.
public class CanalParticipante implements Buzon {

    private final ColaSalida cola;
    private final ProtocoloChat protocolo;
    private final Thread escritor;

    public CanalParticipante(DataOutputStream salida, CodecPaquete codec,
                             ProtocoloChat protocolo, String nombreHilo) {
        this.cola = new ColaSalida(salida, codec);
        this.protocolo = protocolo;
        this.escritor = new Thread(cola, nombreHilo);
        this.escritor.start();
    }

    @Override
    public void recibir(Mensaje mensaje) {
        enviarTexto(protocolo.mensaje(mensaje));
    }

    @Override
    public void notificar(String evento) {
        enviarTexto(protocolo.info(evento));
    }

    public void enviarTexto(String linea) {
        cola.enviar(Paquete.texto(linea));
    }

    public void enviar(Paquete paquete) {
        cola.enviar(paquete);
    }

    // Cierra la cola y espera (máx. 2 s) a que el escritor vacíe lo pendiente
    public void cerrar() throws InterruptedException {
        cola.cerrar();
        escritor.join(2000);
    }
}
