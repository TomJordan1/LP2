package servidor;

import protocolo.Paquete;
import protocolo.ProtocoloChat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// La "sala" de la videollamada: quién está dentro y retransmisión de video/audio.
// El servidor NO procesa imágenes ni sonido: solo reenvía (modelo relay / SFU simple).
//
//   ana ──VIDEO──► Sala ──► bruno, carla, dario   (todos menos el que lo envió)
public class SalaLlamada {

    private final Map<String, CanalParticipante> participantes = new ConcurrentHashMap<>();
    private final ProtocoloChat protocolo;

    public SalaLlamada(ProtocoloChat protocolo) {
        this.protocolo = protocolo;
    }

    public void unir(String usuario, CanalParticipante canal) {
        // 1) al nuevo: quiénes ya estaban
        canal.enviarTexto(protocolo.participantes(nombres()));
        // 2) a los demás: llegó alguien
        for (CanalParticipante otro : participantes.values()) {
            otro.enviarTexto(protocolo.unido(usuario));
        }
        participantes.put(usuario, canal);
    }

    public void salir(String usuario) {
        if (usuario != null && participantes.remove(usuario) != null) {
            for (CanalParticipante otro : participantes.values()) {
                otro.enviarTexto(protocolo.salio(usuario));
            }
        }
    }

    public void retransmitir(String origen, Paquete paquete) {
        for (Map.Entry<String, CanalParticipante> e : participantes.entrySet()) {
            if (!e.getKey().equals(origen)) {
                e.getValue().enviar(paquete);
            }
        }
    }

    public List<String> nombres() {
        List<String> lista = new ArrayList<>(participantes.keySet());
        Collections.sort(lista);
        return lista;
    }
}
