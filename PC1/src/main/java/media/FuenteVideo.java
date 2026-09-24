package media;

import excepciones.MediaException;

import java.awt.image.BufferedImage;

// Contrato de "algo que produce imágenes" (abstracción, semana 2).
// Hoy: CamaraOpenCV (webcam real) o CamaraSimulada (sin webcam). Mañana: compartir pantalla.
public interface FuenteVideo {

    void abrir() throws MediaException;

    // Devuelve una imagen nueva o null si en este momento no hay cuadro disponible
    BufferedImage capturar() throws MediaException;

    void cerrar();

    String descripcion();
}
