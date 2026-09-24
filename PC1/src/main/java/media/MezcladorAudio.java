package media;

import excepciones.MediaException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

// Reproduce a TODOS los demás por un solo parlante sumando sus voces.
//
//   tom   ──► [cola tom]   ─┐
//   raul ──► [cola raul] ─┼──► suma muestra a muestra ──► parlante
//   ricardo ──► [cola ricardo] ─┘      (cada 40 ms)
//
// Cada cola es un BlockingQueue (semana 3) y el mapa es concurrente:
// el hilo receptor produce, este hilo consume.
public class MezcladorAudio implements Runnable {

    private static final int MAX_EN_COLA = 6;      // > 240 ms acumulados = retraso: se descarta lo viejo
    private static final int PRE_CARGA = 2;        // espera 2 bloques antes de reproducir (absorbe saltos de red)

    private final Map<String, BlockingQueue<byte[]>> colas = new ConcurrentHashMap<>();
    private final Set<String> enEspera = new HashSet<>();   // solo lo usa este hilo
    private SourceDataLine parlante;
    private volatile boolean corriendo = true;

    public void abrir() throws MediaException {
        try {
            parlante = AudioSystem.getSourceDataLine(ConfiguracionMedia.FORMATO_AUDIO);
            parlante.open(ConfiguracionMedia.FORMATO_AUDIO, ConfiguracionMedia.BYTES_BLOQUE_AUDIO * 4);
            parlante.start();
        } catch (LineUnavailableException | IllegalArgumentException | SecurityException e) {
            throw new MediaException("No se pudo abrir los parlantes: " + e.getMessage());
        }
    }

    // Lo llama el hilo receptor cada vez que llega audio de alguien
    public void recibir(String origen, byte[] bloque) {
        BlockingQueue<byte[]> cola = colas.get(origen);
        if (cola == null) {
            colas.putIfAbsent(origen, new ArrayBlockingQueue<>(MAX_EN_COLA));
            cola = colas.get(origen);
        }
        while (!cola.offer(bloque)) {
            cola.poll();                            // cola llena: se bota el bloque más viejo
        }
    }

    public void quitar(String origen) {
        colas.remove(origen);   // enEspera se limpia sola: ese nombre ya no se recorre
    }

    @Override
    public void run() {
        int muestras = ConfiguracionMedia.BYTES_BLOQUE_AUDIO / 2;
        byte[] salida = new byte[ConfiguracionMedia.BYTES_BLOQUE_AUDIO];
        while (corriendo) {
            int[] suma = new int[muestras];
            for (Map.Entry<String, BlockingQueue<byte[]>> e : colas.entrySet()) {
                byte[] bloque = tomar(e.getKey(), e.getValue());
                if (bloque != null) {
                    sumar(suma, bloque);
                }
            }
            aBytes(suma, salida);
            parlante.write(salida, 0, salida.length);   // bloquea: marca el ritmo real del audio
        }
        parlante.drain();
        parlante.close();
    }

    private byte[] tomar(String origen, BlockingQueue<byte[]> cola) {
        if (enEspera.contains(origen)) {
            if (cola.size() < PRE_CARGA) {
                return null;                        // sigue precargando
            }
            enEspera.remove(origen);
        }
        byte[] bloque = cola.poll();
        if (bloque == null) {
            enEspera.add(origen);                   // se vació: vuelve a precargar
        }
        return bloque;
    }

    // PCM 16 bits little-endian: muestra = byte bajo + byte alto * 256
    private static void sumar(int[] suma, byte[] bloque) {
        int n = Math.min(suma.length, bloque.length / 2);
        for (int i = 0; i < n; i++) {
            suma[i] += (short) ((bloque[2 * i] & 0xFF) | (bloque[2 * i + 1] << 8));
        }
    }

    private static void aBytes(int[] suma, byte[] salida) {
        for (int i = 0; i < suma.length; i++) {
            int v = Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, suma[i]));   // evita saturar
            salida[2 * i] = (byte) v;
            salida[2 * i + 1] = (byte) (v >> 8);
        }
    }

    public void detener() {
        corriendo = false;
    }
}
