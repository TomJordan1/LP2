package media;

import excepciones.MediaException;
import protocolo.Paquete;
import protocolo.SalidaPaquetes;
import protocolo.TipoPaquete;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.Arrays;

// Hilo del micrófono (Java Sound, incluido en Java): lee bloques de 40 ms y los envía.
public class CapturadorAudio implements Runnable {

    private final SalidaPaquetes salida;
    private TargetDataLine microfono;

    private volatile boolean corriendo = true;
    private volatile boolean activo = true;     // botón "micrófono on/off" (silenciar)

    public CapturadorAudio(SalidaPaquetes salida) {
        this.salida = salida;
    }

    public void abrir() throws MediaException {
        try {
            microfono = AudioSystem.getTargetDataLine(ConfiguracionMedia.FORMATO_AUDIO);
            microfono.open(ConfiguracionMedia.FORMATO_AUDIO, ConfiguracionMedia.BYTES_BLOQUE_AUDIO * 4);
            microfono.start();
        } catch (LineUnavailableException | IllegalArgumentException | SecurityException e) {
            throw new MediaException("No se pudo abrir el microfono: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        byte[] bloque = new byte[ConfiguracionMedia.BYTES_BLOQUE_AUDIO];
        while (corriendo) {
            int leidos = microfono.read(bloque, 0, bloque.length);   // bloquea ~40 ms
            if (leidos > 0 && activo) {
                salida.enviar(Paquete.media(TipoPaquete.AUDIO, Arrays.copyOf(bloque, leidos)));
            }
        }
        microfono.stop();
        microfono.close();
    }

    public boolean alternar() {
        activo = !activo;
        return activo;
    }

    public void detener() {
        corriendo = false;
    }
}
