package cliente.ui;

import media.VisorVideo;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

// Un recuadro del mosaico: muestra el último cuadro de video de UNA persona.
// Si no llega video en 2 s, muestra la inicial del nombre.
public class PanelVideo extends JPanel implements VisorVideo {

    private static final long SIN_VIDEO_MS = 2000;
    private static final Color FONDO = new Color(32, 34, 40);
    private static final Color ACENTO = new Color(123, 92, 196);

    private final String nombre;
    private volatile BufferedImage imagen;     // la escribe otro hilo, la lee el EDT
    private volatile long ultimo;

    public PanelVideo(String nombre) {
        this.nombre = nombre;
        setPreferredSize(new Dimension(320, 240));
        setBackground(FONDO);
    }

    @Override
    public void mostrar(BufferedImage nueva) {
        imagen = nueva;
        ultimo = nueva == null ? 0 : System.currentTimeMillis();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int w = getWidth();
        int h = getHeight();

        BufferedImage actual = imagen;
        if (actual != null && System.currentTimeMillis() - ultimo < SIN_VIDEO_MS) {
            dibujarImagen(g2, actual, w, h);
        } else {
            dibujarSinVideo(g2, w, h);
        }
        dibujarNombre(g2, h);
    }

    // Escala manteniendo la proporción y centra
    private void dibujarImagen(Graphics2D g2, BufferedImage img, int w, int h) {
        double escala = Math.min(w / (double) img.getWidth(), h / (double) img.getHeight());
        int dw = (int) (img.getWidth() * escala);
        int dh = (int) (img.getHeight() * escala);
        g2.drawImage(img, (w - dw) / 2, (h - dh) / 2, dw, dh, null);
    }

    private void dibujarSinVideo(Graphics2D g2, int w, int h) {
        int d = Math.max(40, Math.min(w, h) / 3);
        g2.setColor(ACENTO);
        g2.fillOval((w - d) / 2, (h - d) / 2, d, d);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, d / 2));
        String inicial = nombre.substring(0, 1).toUpperCase();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(inicial, (w - fm.stringWidth(inicial)) / 2, (h + fm.getAscent() - fm.getDescent()) / 2);
    }

    private void dibujarNombre(Graphics2D g2, int h) {
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        int ancho = g2.getFontMetrics().stringWidth(nombre) + 16;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(8, h - 30, ancho, 22, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawString(nombre, 16, h - 14);
    }
}
