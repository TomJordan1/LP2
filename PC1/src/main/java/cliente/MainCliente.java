package cliente;

// ============================================================
// POO II - Semana 4 - PC1: Chat + Videollamada
// Cliente con ventana: video de hasta 6 personas, audio y chat.
// 1) Ejecuta primero servidor.MainServidor (una sola PC)
// 2) Cada participante ejecuta este Main y escribe nombre + IP del servidor
// Ver GUIA_USO.md para conectarse desde otras laptops.
// ============================================================

import cliente.ui.DialogoConexion;
import excepciones.ChatException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.IOException;

public class MainCliente {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // se queda el aspecto por defecto de Java
        }

        String error = null;
        DatosConexion datos = null;
        while (true) {
            datos = DialogoConexion.pedir(error, datos);
            if (datos == null) {
                System.exit(0);                         // canceló
            }
            try {
                new ClienteLlamada().iniciar(datos);
                return;                                 // la ventana sigue viva en sus hilos
            } catch (IOException e) {
                error = "No se pudo conectar a " + datos.getServidor() + ":" + datos.getPuerto()
                        + " (" + e.getMessage() + ")";
            } catch (ChatException e) {
                error = e.getMessage();
            }
        }
    }
}
