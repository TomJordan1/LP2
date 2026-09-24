package servicio;

import excepciones.ComandoInvalidoException;
import excepciones.DuplicadoException;
import excepciones.NoEncontradoException;
import modelo.Mensaje;
import repositorio.Repositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Corazón del chat: aplica las REGLAS del negocio.
// - quién puede conectarse
// - a quién se entrega cada mensaje
// - qué se guarda en el historial
// No sabe nada de sockets ni del formato del protocolo (SRP + DIP).
public class ServicioChat {

    private static final int MIN_NOMBRE = 3;
    private static final int MAX_NOMBRE = 15;

    private final Repositorio<Mensaje> historial;

    // usuario -> su buzón. Concurrente: lo usan todos los hilos del servidor.
    private final Map<String, Buzon> conectados = new ConcurrentHashMap<>();

    public ServicioChat(Repositorio<Mensaje> historial) {
        this.historial = historial;
    }

    public void conectar(String usuario, Buzon buzon) {
        validarNombre(usuario);
        // putIfAbsent: si dos clientes piden el mismo nombre a la vez, solo uno gana
        if (conectados.putIfAbsent(usuario, buzon) != null) {
            throw new DuplicadoException("El usuario " + usuario + " ya esta conectado");
        }
        avisarATodosMenos(usuario, usuario + " se unio al chat");
    }

    public void desconectar(String usuario) {
        if (usuario != null && conectados.remove(usuario) != null) {
            avisarATodosMenos(usuario, usuario + " salio del chat");
        }
    }

    // Envía un mensaje de chat (cualquier subclase de Mensaje: polimorfismo).
    // Devuelve a cuántos usuarios se entregó.
    public int enviar(Mensaje mensaje) {
        exigirConectado(mensaje.getRemitente());

        if (mensaje.esParaTodos()) {
            historial.guardar(mensaje);
            return difundir(mensaje);
        }

        Buzon destino = conectados.get(mensaje.getDestino());
        if (destino == null) {
            throw new NoEncontradoException(
                    "El usuario " + mensaje.getDestino() + " no esta conectado");
        }
        historial.guardar(mensaje);
        destino.recibir(mensaje);
        return 1;
    }

    // Mensajes que involucran al usuario, en orden de llegada
    public List<Mensaje> historialDe(String usuario) {
        List<Mensaje> resultado = new ArrayList<>();
        for (Mensaje m : historial.listar()) {
            if (m.involucraA(usuario)) {
                resultado.add(m);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<String> usuariosConectados() {
        List<String> nombres = new ArrayList<>(conectados.keySet());
        Collections.sort(nombres);
        return Collections.unmodifiableList(nombres);
    }

    public int totalMensajes() {
        return historial.cantidad();
    }

    // ---------- reglas privadas ----------

    private int difundir(Mensaje mensaje) {
        int entregados = 0;
        for (Map.Entry<String, Buzon> e : conectados.entrySet()) {
            if (!e.getKey().equals(mensaje.getRemitente())) {
                e.getValue().recibir(mensaje);
                entregados++;
            }
        }
        return entregados;
    }

    private void avisarATodosMenos(String usuario, String evento) {
        for (Map.Entry<String, Buzon> e : conectados.entrySet()) {
            if (!e.getKey().equals(usuario)) {
                e.getValue().notificar(evento);
            }
        }
    }

    private void exigirConectado(String usuario) {
        if (!conectados.containsKey(usuario)) {
            throw new NoEncontradoException("El usuario " + usuario + " no esta conectado");
        }
    }

    private static void validarNombre(String usuario) {
        if (usuario == null
                || usuario.length() < MIN_NOMBRE
                || usuario.length() > MAX_NOMBRE
                || !usuario.matches("[A-Za-z0-9_]+")) {
            throw new ComandoInvalidoException("Nombre invalido: usa de " + MIN_NOMBRE
                    + " a " + MAX_NOMBRE + " letras, numeros o _");
        }
        if (usuario.equalsIgnoreCase(Mensaje.PARA_TODOS)) {
            throw new ComandoInvalidoException("El nombre " + usuario + " esta reservado");
        }
    }
}
