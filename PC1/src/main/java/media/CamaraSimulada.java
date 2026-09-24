package media;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.time.LocalTime;

// Cámara "de mentira": dibuja un fondo animado con el nombre.
// Sirve si la laptop no tiene webcam, si otra app la está usando,
// o para abrir varios clientes en la MISMA PC (solo uno puede usar la webcam real).
public class CamaraSimulada implements FuenteVideo {

    private final String nombre;
    private final int ancho;
    private final int alto;
    private int paso = 0;

    public CamaraSimulada(String nombre, int ancho, int alto) {
        this.nombre = nombre;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public void abrir() {
        // no hay dispositivo que abrir
    }

    @Override
    public BufferedImage capturar() {
        paso = (paso + 3) % ancho;
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(new GradientPaint(0, 0, new Color(74, 127, 193), ancho, alto, new Color(123, 92, 196)));
        g.fillRect(0, 0, ancho, alto);
        g.setColor(new Color(255, 255, 255, 90));
        g.fillOval(paso - 40, alto / 2 - 40, 80, 80);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        g.drawString(nombre, 16, 36);
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        g.drawString("camara simulada  " + LocalTime.now().withNano(0), 16, alto - 16);
        g.dispose();
        return img;
    }

    @Override
    public void cerrar() {
        // nada que liberar
    }

    @Override
    public String descripcion() {
        return "Camara simulada";
    }
}
