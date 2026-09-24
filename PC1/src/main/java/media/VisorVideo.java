package media;

import java.awt.image.BufferedImage;

// Contrato de "algo que muestra imágenes" (lo implementa el panel de la ventana).
// Así TransmisorVideo no depende de Swing (DIP).
public interface VisorVideo {
    void mostrar(BufferedImage imagen);
}
