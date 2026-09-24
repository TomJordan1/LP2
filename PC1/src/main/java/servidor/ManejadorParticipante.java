package servidor;

import excepciones.ChatException;
import excepciones.ComandoInvalidoException;
import modelo.Mensaje;
import protocolo.CodecPaquete;
import protocolo.Comando;
import protocolo.Paquete;
import protocolo.Peticion;
import protocolo.ProtocoloChat;
import servicio.FabricaMensajes;
import servicio.ServicioChat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Semaphore;

// Atiende a UN participante de principio a fin (corre dentro del pool).
//   TEXTO -> ProtocoloChat -> ServicioChat   (reglas del chat)
//   VIDEO / AUDIO -> SalaLlamada.retransmitir (solo si ya hizo LOGIN)
public class ManejadorParticipante implements Runnable {

    private final Socket socket;
    private final ServicioChat servicio;
    private final SalaLlamada sala;
    private final FabricaMensajes fabrica;
    private final ProtocoloChat protocolo;
    private final CodecPaquete codec;
    private final Semaphore cupos;

    private String usuario;   // null hasta que haga LOGIN

    public ManejadorParticipante(Socket socket, ServicioChat servicio, SalaLlamada sala,
                                 FabricaMensajes fabrica, ProtocoloChat protocolo,
                                 CodecPaquete codec, Semaphore cupos) {
        this.socket = socket;
        this.servicio = servicio;
        this.sala = sala;
        this.fabrica = fabrica;
        this.protocolo = protocolo;
        this.codec = codec;
        this.cupos = cupos;
    }

    @Override
    public void run() {
        CanalParticipante canal = null;
        try (Socket s = socket) {
            DataInputStream entrada = new DataInputStream(
                    new BufferedInputStream(s.getInputStream(), 64 * 1024));
            DataOutputStream salida = new DataOutputStream(
                    new BufferedOutputStream(s.getOutputStream(), 64 * 1024));
            canal = new CanalParticipante(salida, codec, protocolo, "Salida-" + s.getPort());
            canal.notificar("Bienvenido. Identificate con LOGIN|tuNombre");
            atender(entrada, canal);
        } catch (IOException e) {
            System.out.println("[servidor] conexion cerrada: " + usuario);
        } catch (ChatException e) {
            System.out.println("[servidor] paquete invalido de " + usuario + ": " + e.getMessage());
        } finally {
            servicio.desconectar(usuario);
            sala.salir(usuario);
            cerrar(canal);
            cupos.release();
            System.out.println("[servidor] salio: " + usuario
                    + " | en sala: " + sala.nombres());
        }
    }

    private void atender(DataInputStream entrada, CanalParticipante canal) throws IOException {
        while (true) {
            Paquete p = codec.leer(entrada);          // bloquea hasta que llegue algo
            switch (p.tipo()) {
                case TEXTO:
                    if (!procesarTexto(p.comoTexto(), canal)) {
                        return;                       // pidió SALIR
                    }
                    break;
                case VIDEO:
                case AUDIO:
                    if (usuario != null) {
                        sala.retransmitir(usuario, p.conOrigen(usuario));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // Devuelve false cuando la conversación debe terminar
    private boolean procesarTexto(String linea, CanalParticipante canal) {
        try {
            Peticion p = protocolo.leer(linea);
            if (usuario == null && p.getComando() != Comando.LOGIN
                    && p.getComando() != Comando.SALIR) {
                throw new ComandoInvalidoException("Primero identificate con LOGIN|tuNombre");
            }
            switch (p.getComando()) {
                case LOGIN:
                    login(p.arg(0), canal);
                    break;
                case TXT:
                    Mensaje m = fabrica.crearTexto(usuario, p.arg(0), p.arg(1));
                    int entregados = servicio.enviar(m);
                    canal.enviarTexto(protocolo.ok("Mensaje #" + m.id()
                            + " entregado a " + entregados + " usuario(s)"));
                    break;
                case USUARIOS:
                    canal.enviarTexto(protocolo.ok("Conectados: "
                            + String.join(", ", servicio.usuariosConectados())));
                    break;
                case HISTORIAL:
                    List<Mensaje> mensajes = servicio.historialDe(usuario);
                    canal.enviarTexto(protocolo.ok("Historial: " + mensajes.size() + " mensaje(s)"));
                    for (Mensaje h : mensajes) {
                        canal.enviarTexto(protocolo.historial(h));
                    }
                    break;
                case SALIR:
                    canal.enviarTexto(protocolo.ok("Hasta luego"));
                    return false;
                default:
                    throw new ComandoInvalidoException("Comando no soportado");
            }
        } catch (ChatException e) {
            canal.enviarTexto(protocolo.error(e));   // regla rota: avisa y sigue conectado
        }
        return true;
    }

    private void login(String nombre, CanalParticipante canal) {
        if (usuario != null) {
            throw new ComandoInvalidoException("Ya iniciaste sesion como " + usuario);
        }
        servicio.conectar(nombre, canal);
        usuario = nombre;
        canal.enviarTexto(protocolo.ok("Sesion iniciada como " + nombre));
        sala.unir(nombre, canal);
        System.out.println("[servidor] entro: " + nombre + " | en sala: " + sala.nombres());
    }

    private void cerrar(CanalParticipante canal) {
        if (canal == null) {
            return;
        }
        try {
            canal.cerrar();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
