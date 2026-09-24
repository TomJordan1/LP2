package protocolo;

// Contrato (DIP): "algo a lo que le puedo mandar paquetes".
// La cámara y el micrófono dependen de esto, no de un Socket concreto.
public interface SalidaPaquetes {
    void enviar(Paquete paquete);
}
