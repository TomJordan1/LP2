package media;

import excepciones.MediaException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Comprime cada cuadro a JPEG (≈ 8 KB en vez de 230 KB sin comprimir) y lo descomprime al llegar.
// Usa javax.imageio (Java estándar).
public class CodificadorJpeg {

    private final float calidad;

    public CodificadorJpeg(float calidad) {
        this.calidad = calidad;
    }

    public byte[] codificar(BufferedImage imagen) throws MediaException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam parametros = writer.getDefaultWriteParam();
        parametros.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        parametros.setCompressionQuality(calidad);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream(16 * 1024);
        try (ImageOutputStream salida = ImageIO.createImageOutputStream(bytes)) {
            writer.setOutput(salida);
            writer.write(null, new IIOImage(imagen, null, null), parametros);
        } catch (IOException e) {
            throw new MediaException("No se pudo comprimir el cuadro: " + e.getMessage());
        } finally {
            writer.dispose();
        }
        return bytes.toByteArray();
    }

    public BufferedImage decodificar(byte[] jpeg) {
        try {
            return ImageIO.read(new ByteArrayInputStream(jpeg));
        } catch (IOException e) {
            return null;   // cuadro dañado: se ignora, llegará otro en 80 ms
        }
    }
}
