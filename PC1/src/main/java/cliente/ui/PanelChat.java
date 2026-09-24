package cliente.ui;

import cliente.AccionesUsuario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

// Columna derecha: historial del chat + caja para escribir.
public class PanelChat extends JPanel {

    private final JTextArea historial = new JTextArea();
    private final JTextField campo = new JTextField();

    public PanelChat(AccionesUsuario acciones) {
        super(new BorderLayout(4, 4));
        setPreferredSize(new Dimension(320, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titulo = new JLabel("Chat");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 15f));

        historial.setEditable(false);
        historial.setLineWrap(true);
        historial.setWrapStyleWord(true);

        JButton enviar = new JButton("Enviar");
        JPanel abajo = new JPanel(new BorderLayout(4, 4));
        abajo.add(campo, BorderLayout.CENTER);
        abajo.add(enviar, BorderLayout.EAST);

        add(titulo, BorderLayout.NORTH);
        add(new JScrollPane(historial), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        // Enter o clic en Enviar
        campo.addActionListener(e -> enviar(acciones));
        enviar.addActionListener(e -> enviar(acciones));
    }

    private void enviar(AccionesUsuario acciones) {
        String texto = campo.getText();
        campo.setText("");
        acciones.enviarChat(texto);
    }

    // Se puede llamar desde cualquier hilo
    public void agregar(String linea) {
        SwingUtilities.invokeLater(() -> {
            historial.append(linea + "\n");
            historial.setCaretPosition(historial.getDocument().getLength());
        });
    }
}
