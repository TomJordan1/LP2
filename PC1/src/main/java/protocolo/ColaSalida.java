package protocolo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

//   PRODUCTORES: cámara, micrófono, chat, otros participantes (muchos hilos)
//   CONSUMIDOR : este Runnable, el único que escribe en el socket
// Tiempo real: si la red va lenta se DESCARTA video/audio viejo en vez de acumular retraso.
// Se usa igual en el servidor (un canal por participante) y en el cliente.

public class ColaSalida implements Runnable, SalidaPaquetes {

    private static final int CAPACIDAD = 64;
    private static final Paquete FIN = Paquete.texto("__FIN__");   // píldora venenosa

    private final BlockingQueue<Paquete> cola = new ArrayBlockingQueue<>(CAPACIDAD);
    private final DataOutputStream salida;
    private final CodecPaquete codec;
    private volatile boolean cerrada = false;

    public ColaSalida(DataOutputStream salida, CodecPaquete codec) {
        this.salida = salida;
        this.codec = codec;
    }

    @Override
    public void enviar(Paquete p) {
        if (cerrada) {
            return;
        }
        // Video: si ya hay media cola ocupada, este cuadro llegaría tarde -> se descarta
        if (p.tipo() == TipoPaquete.VIDEO && cola.size() > CAPACIDAD / 2) {
            return;
        }
        if (cola.offer(p)) {
            return;
        }
        // El texto (chat) no se debe perder: espera un poco a que haya espacio
        if (p.tipo() == TipoPaquete.TEXTO) {
            try {
                cola.offer(p, 1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Paquete p = cola.take();
                if (p == FIN) {
                    break;
                }
                codec.escribir(salida, p);
                if (cola.isEmpty()) {
                    salida.flush();          // agrupa escrituras cuando hay ráfagas
                }
            }
            salida.flush();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            // el otro extremo cerró la conexión
        } finally {
            cerrada = true;
        }
    }

    public void cerrar() {
        if (cerrada) {
            return;
        }
        try {
            cola.offer(FIN, 1, TimeUnit.SECONDS);   // deja salir lo pendiente (ej. "SALIR")
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        cerrada = true;
    }
}
