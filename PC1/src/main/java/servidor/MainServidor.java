package servidor;

// ============================================================
// POO II - Semana 4 - PC1: Chat + Videollamada (hasta 6 personas)
// ------------------------------------------------------------
// Servidor central: recibe chat, video y audio de cada participante
// y lo reenvía a los demás. NO usa cámara ni micrófono.
// Uso:  MainServidor [puerto]      (por defecto 5000)
// Los participantes se conectan con cliente.MainCliente
// indicando la IP que este programa muestra al arrancar.
// ============================================================

import modelo.Mensaje;
import protocolo.ConfiguracionRed;
import protocolo.ProtocoloChat;
import repositorio.Repositorio;
import repositorio.RepositorioMemoria;
import servicio.FabricaMensajes;
import servicio.GeneradorIds;
import servicio.ServicioChat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainServidor {

    public static void main(String[] args) throws IOException {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : ConfiguracionRed.PUERTO_DEFECTO;

        // Raíz de composición: aquí se conectan las piezas (inyección manual, DIP)
        Repositorio<Mensaje> historial = new RepositorioMemoria<>();
        ProtocoloChat protocolo = new ProtocoloChat();
        ServicioChat servicio = new ServicioChat(historial);
        SalaLlamada sala = new SalaLlamada(protocolo);
        FabricaMensajes fabrica = new FabricaMensajes(new GeneradorIds());

        mostrarDirecciones(puerto);
        new ServidorLlamada(puerto, ConfiguracionRed.MAX_PARTICIPANTES,
                servicio, sala, fabrica, protocolo).iniciar();
    }

    // Imprime las IP de esta PC para que los demás sepan a dónde conectarse
    private static void mostrarDirecciones(int puerto) throws SocketException {
        System.out.println("==============================================");
        System.out.println(" Comparte una de estas direcciones:");
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface red = interfaces.nextElement();
            if (!red.isUp() || red.isLoopback()) {
                continue;
            }
            Enumeration<InetAddress> ips = red.getInetAddresses();
            while (ips.hasMoreElements()) {
                InetAddress ip = ips.nextElement();
                if (ip instanceof Inet4Address) {
                    System.out.println("   " + ip.getHostAddress() + " : " + puerto
                            + "   (" + red.getDisplayName() + ")");
                }
            }
        }
        System.out.println(" Misma PC: localhost : " + puerto);
        System.out.println("==============================================");
    }
}
