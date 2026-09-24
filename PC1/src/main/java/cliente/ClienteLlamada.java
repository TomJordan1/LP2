package cliente;

import cliente.ui.VentanaLlamada;
import excepciones.ChatException;
import excepciones.MediaException;
import media.CamaraOpenCV;
import media.CamaraSimulada;
import media.CapturadorAudio;
import media.CodificadorJpeg;
import media.ConfiguracionMedia;
import media.FuenteVideo;
import media.MezcladorAudio;
import media.TransmisorVideo;
import protocolo.ConfiguracionRed;
import protocolo.Paquete;
import protocolo.TipoPaquete;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Controlador del cliente: arranca la conexión, los hilos de media y la ventana,
// y reparte lo que llega del servidor. Hilos del cliente:
//   Receptor  (escucha al servidor)      Salida-cliente (escribe al socket)
//   Camara    (captura y envía video)    Microfono (captura y envía audio)
//   Parlante  (mezcla y reproduce)       EDT de Swing (la ventana)
public class ClienteLlamada implements EventosLlamada, AccionesUsuario {

    private final PresentadorMensajes presentador = new PresentadorMensajes();
    private final TraductorEntrada traductor = new TraductorEntrada();
    private final CodificadorJpeg codificador = new CodificadorJpeg(ConfiguracionMedia.VIDEO_CALIDAD_JPEG);
    private final MezcladorAudio mezclador = new MezcladorAudio();

    private ConexionServidor conexion;
    private VentanaLlamada ventana;
    private CapturadorAudio microfono;
    private TransmisorVideo transmisor;
    private volatile boolean cerrando = false;

    public void iniciar(DatosConexion datos) throws IOException {
        conexion = new ConexionServidor(datos.getServidor(), datos.getPuerto(),
                ConfiguracionRed.TIEMPO_CONEXION_MS);
        List<String> previas = iniciarSesion(datos.getNombre());

        ventana = VentanaLlamada.crear(datos.getNombre(), this);
        for (String linea : previas) {
            ventana.chat().agregar(presentador.formatear(linea));
        }
        ventana.chat().agregar(TraductorEntrada.AYUDA);

        iniciarAudio();
        iniciarVideo(datos.getNombre(), datos.getCamara());
        new Thread(new ReceptorServidor(conexion, this), "Receptor").start();
    }

    // Envía LOGIN y espera OK o ERROR (máx. unos segundos). Devuelve los avisos previos.
    private List<String> iniciarSesion(String nombre) throws IOException {
        List<String> previas = new ArrayList<>();
        try {
            conexion.tiempoEsperaLectura(ConfiguracionRed.TIEMPO_CONEXION_MS);
            conexion.enviarTexto("LOGIN|" + nombre);
            while (true) {
                Paquete p = conexion.recibir();
                if (p.tipo() != TipoPaquete.TEXTO) {
                    continue;
                }
                String linea = p.comoTexto();
                if (linea.startsWith("OK|")) {
                    break;
                }
                if (linea.startsWith("ERROR|")) {
                    String[] partes = linea.split("\\|", 3);
                    throw new ChatException(partes[1], partes.length > 2 ? partes[2] : linea);
                }
                previas.add(linea);
            }
            conexion.tiempoEsperaLectura(0);
            return previas;
        } catch (IOException | ChatException e) {
            conexion.cerrar();
            throw e;
        }
    }

    private void iniciarAudio() {
        try {
            mezclador.abrir();
            new Thread(mezclador, "Parlante").start();
        } catch (MediaException e) {
            ventana.chat().agregar("! " + e.getMessage() + " (no escucharas a los demas)");
        }
        CapturadorAudio mic = new CapturadorAudio(conexion);
        try {
            mic.abrir();
            new Thread(mic, "Microfono").start();
            microfono = mic;
        } catch (MediaException e) {
            ventana.chat().agregar("! " + e.getMessage() + " (los demas no te escucharan)");
        }
    }

    private void iniciarVideo(String nombre, int indiceCamara) {
        FuenteVideo simulada = new CamaraSimulada(nombre,
                ConfiguracionMedia.VIDEO_ANCHO, ConfiguracionMedia.VIDEO_ALTO);
        FuenteVideo fuente = simulada;
        if (indiceCamara != DatosConexion.SIN_CAMARA) {
            FuenteVideo real = new CamaraOpenCV(indiceCamara,
                    ConfiguracionMedia.VIDEO_ANCHO, ConfiguracionMedia.VIDEO_ALTO);
            try {
                real.abrir();
                fuente = real;
            } catch (MediaException | LinkageError e) {   // LinkageError: faltan librerías nativas
                ventana.chat().agregar("! " + e.getMessage() + " -> usando camara simulada");
            }
        }
        ventana.chat().agregar("* Video: " + fuente.descripcion());
        transmisor = new TransmisorVideo(fuente, codificador, conexion,
                ventana.panelLocal(), ConfiguracionMedia.VIDEO_FPS);
        new Thread(transmisor, "Camara").start();
    }

    // ---------- lo que llega del servidor (hilo Receptor) ----------

    @Override
    public void alRecibirTexto(String linea) {
        String[] p = linea.split("\\|");
        switch (p[0]) {
            case "PARTICIPANTES":
                if (p.length > 1) {
                    for (String nombre : p[1].split(",")) {
                        ventana.agregarParticipante(nombre);
                    }
                }
                break;
            case "UNIDO":
                ventana.agregarParticipante(p[1]);
                break;
            case "SALIO":
                ventana.quitarParticipante(p[1]);
                mezclador.quitar(p[1]);
                break;
            default:
                if (!linea.startsWith("OK|Mensaje #")) {   // la confirmación de envío no se muestra
                    ventana.chat().agregar(presentador.formatear(linea));
                }
        }
    }

    @Override
    public void alRecibirVideo(String origen, byte[] jpeg) {
        BufferedImage imagen = codificador.decodificar(jpeg);
        if (imagen != null) {
            ventana.mostrarVideo(origen, imagen);
        }
    }

    @Override
    public void alRecibirAudio(String origen, byte[] pcm) {
        mezclador.recibir(origen, pcm);
    }

    @Override
    public void alDesconectar() {
        if (!cerrando) {
            ventana.avisar("Se perdio la conexion con el servidor.");
            salir();
        }
    }

    // ---------- lo que hace el usuario (ventana) ----------

    @Override
    public void enviarChat(String entrada) {
        if (entrada.trim().equalsIgnoreCase("/ayuda")) {
            ventana.chat().agregar(TraductorEntrada.AYUDA);
            return;
        }
        String linea = traductor.traducir(entrada);
        if (linea == null) {
            return;
        }
        conexion.enviarTexto(linea);
        if (linea.startsWith("TXT|")) {
            String[] p = linea.split("\\|", 3);
            String para = "TODOS".equals(p[1]) ? "" : " (a " + p[1] + ")";
            ventana.chat().agregar("Tu" + para + ": " + p[2]);
        }
    }

    @Override
    public boolean alternarCamara() {
        return transmisor != null && transmisor.alternar();
    }

    @Override
    public boolean alternarMicrofono() {
        return microfono != null && microfono.alternar();
    }

    @Override
    public synchronized void salir() {
        if (cerrando) {
            return;
        }
        cerrando = true;
        conexion.enviarTexto("SALIR");
        if (transmisor != null) {
            transmisor.detener();
        }
        if (microfono != null) {
            microfono.detener();
        }
        mezclador.detener();
        conexion.cerrar();
        try {
            Thread.sleep(300); // deja que la cámara se libere
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ventana.cerrar();
        System.exit(0);
    }
}
