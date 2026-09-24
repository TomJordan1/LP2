package servidor;

import excepciones.ServidorLlenoException;
import protocolo.CodecPaquete;
import protocolo.Paquete;
import protocolo.ProtocoloChat;
import servicio.FabricaMensajes;
import servicio.ServicioChat;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

// Servidor TCP con pool LIMITADO (semana 4) para chat + videollamada.
//
//  accept() ──► ¿hay cupo? (Semaphore) ──sí──► pool.execute(ManejadorParticipante)
//                                      └─no──► ERROR|SERVIDOR_LLENO y cerrar
public class ServidorLlamada {

    private final int puerto;
    private final ServicioChat servicio;
    private final SalaLlamada sala;
    private final FabricaMensajes fabrica;
    private final ProtocoloChat protocolo;
    private final CodecPaquete codec = new CodecPaquete();

    private final ExecutorService pool;
    private final Semaphore cupos;

    public ServidorLlamada(int puerto, int maxParticipantes, ServicioChat servicio,
                           SalaLlamada sala, FabricaMensajes fabrica, ProtocoloChat protocolo) {
        this.puerto = puerto;
        this.servicio = servicio;
        this.sala = sala;
        this.fabrica = fabrica;
        this.protocolo = protocolo;
        this.pool = Executors.newFixedThreadPool(maxParticipantes);
        this.cupos = new Semaphore(maxParticipantes);
    }

    public void iniciar() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {   // escucha en TODAS las interfaces
            System.out.println("[servidor] escuchando en el puerto " + puerto
                    + " (cupos: " + cupos.availablePermits() + ")");
            while (!serverSocket.isClosed()) {
                Socket cliente;
                try {
                    cliente = serverSocket.accept();
                } catch (SocketException e) {
                    break;
                }
                cliente.setTcpNoDelay(true);   // envía de inmediato: menos retraso en tiempo real
                if (cupos.tryAcquire()) {
                    pool.execute(new ManejadorParticipante(cliente, servicio, sala,
                            fabrica, protocolo, codec, cupos));
                } else {
                    rechazar(cliente);
                }
            }
        } finally {
            pool.shutdown();
        }
    }

    private void rechazar(Socket cliente) {
        try (Socket s = cliente) {
            DataOutputStream salida = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
            codec.escribir(salida, Paquete.texto(protocolo.error(
                    new ServidorLlenoException("La sala esta llena, intenta mas tarde"))));
            salida.flush();
        } catch (IOException e) {
            System.out.println("[servidor] no se pudo rechazar al cliente");
        }
    }
}
