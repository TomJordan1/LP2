package cliente.ui;

import cliente.DatosConexion;
import protocolo.ConfiguracionRed;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

// Ventanita de ingreso: nombre, IP del servidor, puerto y cámara.
public final class DialogoConexion {

    private static final String[] CAMARAS = {
            "Camara 0 (integrada)", "Camara 1 (externa / USB)", "Camara 2", "Sin camara (simulada)"};
    private static final int OPCION_SIN_CAMARA = 3;

    private DialogoConexion() {
    }

    // Devuelve null si el usuario cancela
    public static DatosConexion pedir(String error, DatosConexion previos) {
        JTextField nombre = new JTextField(previos == null ? "" : previos.getNombre(), 16);
        JTextField servidor = new JTextField(previos == null
                ? ConfiguracionRed.HOST_DEFECTO : previos.getServidor(), 16);
        JTextField puerto = new JTextField(String.valueOf(previos == null
                ? ConfiguracionRed.PUERTO_DEFECTO : previos.getPuerto()), 6);
        JComboBox<String> camara = new JComboBox<>(CAMARAS);
        if (previos != null) {
            camara.setSelectedIndex(previos.getCamara() == DatosConexion.SIN_CAMARA
                    ? OPCION_SIN_CAMARA : Math.min(previos.getCamara(), 2));
        }

        JPanel campos = new JPanel(new GridLayout(0, 2, 6, 6));
        campos.add(new JLabel("Tu nombre (3-15, sin espacios):"));
        campos.add(nombre);
        campos.add(new JLabel("Servidor (IP o dominio):"));
        campos.add(servidor);
        campos.add(new JLabel("Puerto:"));
        campos.add(puerto);
        campos.add(new JLabel("Camara:"));
        campos.add(camara);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        if (error != null) {
            JLabel aviso = new JLabel("<html><body style='width:320px'>" + error + "</body></html>");
            aviso.setForeground(new Color(201, 79, 79));
            panel.add(aviso, BorderLayout.NORTH);
        }
        panel.add(campos, BorderLayout.CENTER);

        int respuesta = JOptionPane.showConfirmDialog(null, panel, "Unirse a la videollamada",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (respuesta != JOptionPane.OK_OPTION) {
            return null;
        }

        int indice = camara.getSelectedIndex() == OPCION_SIN_CAMARA
                ? DatosConexion.SIN_CAMARA : camara.getSelectedIndex();
        try {
            int numeroPuerto = Integer.parseInt(puerto.getText().trim());
            return new DatosConexion(nombre.getText().trim(), servidor.getText().trim(), numeroPuerto, indice);
        } catch (NumberFormatException e) {
            DatosConexion corregidos = new DatosConexion(nombre.getText().trim(), servidor.getText().trim(),
                    ConfiguracionRed.PUERTO_DEFECTO, indice);
            return pedir("El puerto debe ser un numero (ej. 5000).", corregidos);
        }
    }
}
