package media;

import excepciones.MediaException;
import protocolo.Paquete;
import protocolo.SalidaPaquetes;
import protocolo.TipoPaquete;

import java.awt.image.BufferedImage;

// Hilo de la cámara: captura -> vista previa local -> JPEG -> paquete VIDEO.
// Marca el ritmo (FPS) durmiendo lo que sobra de cada ciclo.
public class TransmisorVideo implements Runnable {

    private final FuenteVideo fuente;
    private final CodificadorJpeg codificador;
    private final SalidaPaquetes salida;
    private final VisorVideo vistaLocal;
    private final long periodoMs;

    private volatile boolean corriendo = true;
    private volatile boolean activo = true;     // botón "cámara on/off"

    public TransmisorVideo(FuenteVideo fuente, CodificadorJpeg codificador,
                           SalidaPaquetes salida, VisorVideo vistaLocal, int fps) {
        this.fuente = fuente;
        this.codificador = codificador;
        this.salida = salida;
        this.vistaLocal = vistaLocal;
        this.periodoMs = 1000L / fps;
    }

    @Override
    public void run() {
        try {
            while (corriendo) {
                long inicio = System.currentTimeMillis();
                if (activo) {
                    transmitirCuadro();
                } else {
                    vistaLocal.mostrar(null);
                }
                long resto = periodoMs - (System.currentTimeMillis() - inicio);
                Thread.sleep(Math.max(resto, 5));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            fuente.cerrar();
        }
    }

    private void transmitirCuadro() {
        try {
            BufferedImage cuadro = fuente.capturar();
            if (cuadro != null) {
                vistaLocal.mostrar(cuadro);
                salida.enviar(Paquete.media(TipoPaquete.VIDEO, codificador.codificar(cuadro)));
            }
        } catch (MediaException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean alternar() {
        activo = !activo;
        return activo;
    }

    public void detener() {
        corriendo = false;
    }
}
