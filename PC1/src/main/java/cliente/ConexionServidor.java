package cliente;

import protocolo.CodecPaquete;
import protocolo.ColaSalida;
import protocolo.Paquete;
import protocolo.SalidaPaquetes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

// El único socket del cliente. Envía por una ColaSalida (un solo hilo escritor)
// y recibe con recibir() desde el hilo ReceptorServidor.
public class ConexionServidor implements SalidaPaquetes {

    private final Socket socket;
    private final DataInputStream entrada;
    private final ColaSalida cola;
    private final CodecPaquete codec = new CodecPaquete();

    public ConexionServidor(String host, int puerto, int tiempoMaxMs) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, puerto), tiempoMaxMs);
        socket.setTcpNoDelay(true);
        entrada = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 64 * 1024));
        DataOutputStream salida = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream(), 64 * 1024));
        cola = new ColaSalida(salida, codec);
        new Thread(cola, "Salida-cliente").start();
    }

    @Override
    public void enviar(Paquete paquete) {
        cola.enviar(paquete);
    }

    public void enviarTexto(String linea) {
        cola.enviar(Paquete.texto(linea));
    }

    public Paquete recibir() throws IOException {
        return codec.leer(entrada);
    }

    // 0 = esperar sin límite
    public void tiempoEsperaLectura(int ms) throws IOException {
        socket.setSoTimeout(ms);
    }

    public void cerrar() {
        cola.cerrar();
        try {
            Thread.sleep(200);          // deja salir el último "SALIR"
            socket.close();
        } catch (IOException e) {
            // ya estaba cerrado
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
