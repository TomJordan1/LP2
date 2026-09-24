package media;

import excepciones.MediaException;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

// Webcam real usando JavaCV (envoltorio Java de OpenCV + FFmpeg).
// OpenCVFrameGrabber abre la cámara N del sistema (0 = la integrada normalmente).
public class CamaraOpenCV implements FuenteVideo {

    private final int indice;
    private final int ancho;
    private final int alto;
    private final Java2DFrameConverter convertidor = new Java2DFrameConverter();
    private OpenCVFrameGrabber grabber;

    public CamaraOpenCV(int indice, int ancho, int alto) {
        this.indice = indice;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public void abrir() throws MediaException {
        try {
            grabber = new OpenCVFrameGrabber(indice);
            grabber.setImageWidth(ancho);
            grabber.setImageHeight(alto);
            grabber.start();
        } catch (FrameGrabber.Exception | UnsatisfiedLinkError e) {
            throw new MediaException("No se pudo abrir la camara " + indice + ": " + e.getMessage());
        }
    }

    @Override
    public BufferedImage capturar() throws MediaException {
        try {
            Frame cuadro = grabber.grab();
            if (cuadro == null || cuadro.image == null) {
                return null;
            }
            return escalar(convertidor.convert(cuadro));
        } catch (FrameGrabber.Exception e) {
            throw new MediaException("Error leyendo la camara: " + e.getMessage());
        }
    }

    // Copia a una imagen NUEVA del tamaño configurado (la cámara puede ignorar el tamaño pedido
    // y el convertidor reutiliza su imagen interna en cada llamada)
    private BufferedImage escalar(BufferedImage original) {
        BufferedImage destino = new BufferedImage(ancho, alto, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = destino.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, ancho, alto, null);
        g.dispose();
        return destino;
    }

    @Override
    public void cerrar() {
        try {
            if (grabber != null) {
                grabber.stop();
                grabber.release();
            }
        } catch (FrameGrabber.Exception e) {
            System.out.println("Aviso al cerrar camara: " + e.getMessage());
        }
    }

    @Override
    public String descripcion() {
        return "Camara " + indice + " (OpenCV)";
    }
}
