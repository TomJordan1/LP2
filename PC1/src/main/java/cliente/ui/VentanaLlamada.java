package cliente.ui;

import cliente.AccionesUsuario;
import media.VisorVideo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Ventana principal:  [ mosaico de videos ] [ chat ]
//                     [ Cámara | Micrófono | Salir   estado ]
// El mosaico se reorganiza solo: 1→1x1, 2→2x1, 3-4→2x2, 5-6→3x2.
public class VentanaLlamada extends JFrame {

    private final Map<String, PanelVideo> remotos = new ConcurrentHashMap<>();
    private final JPanel mosaico = new JPanel();
    private final PanelVideo local;
    private final PanelChat chat;
    private final JLabel estado = new JLabel();

    private VentanaLlamada(String usuario, AccionesUsuario acciones) {
        super("Videollamada POO II - " + usuario);
        local = new PanelVideo(usuario + " (tu)");
        chat = new PanelChat(acciones);
        mosaico.setBackground(new Color(20, 21, 26));

        JButton camara = new JButton("Camara: ON");
        JButton microfono = new JButton("Microfono: ON");
        JButton salir = new JButton("Salir");
        salir.setForeground(new Color(201, 79, 79));
        camara.addActionListener(e -> camara.setText(acciones.alternarCamara() ? "Camara: ON" : "Camara: OFF"));
        microfono.addActionListener(e ->
                microfono.setText(acciones.alternarMicrofono() ? "Microfono: ON" : "Microfono: OFF"));
        salir.addActionListener(e -> new Thread(acciones::salir).start());

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barra.add(camara);
        barra.add(microfono);
        barra.add(salir);
        barra.add(estado);

        setLayout(new BorderLayout());
        add(mosaico, BorderLayout.CENTER);
        add(chat, BorderLayout.EAST);
        add(barra, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(acciones::salir).start();
            }
        });

        reorganizar();
        new Timer(500, e -> mosaico.repaint()).start();   // detecta videos que dejaron de llegar
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Swing exige crear ventanas en su hilo (EDT): se crea allí y se espera
    public static VentanaLlamada crear(String usuario, AccionesUsuario acciones) {
        VentanaLlamada[] resultado = new VentanaLlamada[1];
        try {
            SwingUtilities.invokeAndWait(() -> resultado[0] = new VentanaLlamada(usuario, acciones));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("No se pudo crear la ventana", e.getCause());
        }
        return resultado[0];
    }

    public void agregarParticipante(String nombre) {
        SwingUtilities.invokeLater(() -> {
            if (!nombre.isEmpty() && !remotos.containsKey(nombre)) {
                remotos.put(nombre, new PanelVideo(nombre));
                reorganizar();
            }
        });
    }

    public void quitarParticipante(String nombre) {
        SwingUtilities.invokeLater(() -> {
            if (remotos.remove(nombre) != null) {
                reorganizar();
            }
        });
    }

    // Lo llama el hilo Receptor
    public void mostrarVideo(String origen, BufferedImage imagen) {
        PanelVideo panel = remotos.get(origen);
        if (panel != null) {
            panel.mostrar(imagen);
        }
    }

    private void reorganizar() {
        List<String> nombres = new ArrayList<>(remotos.keySet());
        Collections.sort(nombres);
        int total = 1 + nombres.size();
        int columnas = (int) Math.ceil(Math.sqrt(total));
        int filas = (int) Math.ceil(total / (double) columnas);

        mosaico.removeAll();
        mosaico.setLayout(new GridLayout(filas, columnas, 6, 6));
        mosaico.add(local);
        for (String n : nombres) {
            mosaico.add(remotos.get(n));
        }
        mosaico.revalidate();
        mosaico.repaint();
        estado.setText("   " + total + " en la llamada");
    }

    public VisorVideo panelLocal() {
        return local;
    }

    public PanelChat chat() {
        return chat;
    }

    public void avisar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Videollamada", JOptionPane.WARNING_MESSAGE);
    }

    public void cerrar() {
        SwingUtilities.invokeLater(this::dispose);
    }
}
