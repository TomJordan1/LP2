package media;

import javax.sound.sampled.AudioFormat;

// Parámetros de calidad. Bajarlos = menos internet usado y menos retraso.
//   Video 320x240, 12 cuadros/s, JPEG 60 %  ≈ 80-120 KB/s por persona
//   Audio 16 kHz, 16 bits, mono             =  32 KB/s por persona
public final class ConfiguracionMedia {

    public static final int VIDEO_ANCHO = 320;
    public static final int VIDEO_ALTO = 240;
    public static final int VIDEO_FPS = 12;
    public static final float VIDEO_CALIDAD_JPEG = 0.6f;

    // PCM: 16000 muestras/s, 16 bits, 1 canal, con signo, little-endian
    public static final AudioFormat FORMATO_AUDIO = new AudioFormat(16000f, 16, 1, true, false);
    public static final int BYTES_BLOQUE_AUDIO = 1280;   // 40 ms de sonido por paquete

    private ConfiguracionMedia() {
    }
}
