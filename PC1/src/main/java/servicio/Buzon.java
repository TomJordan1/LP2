package servicio;

import modelo.Mensaje;

// Contrato de salida hacia UN usuario conectado (DIP).
// El servicio no sabe si detrás hay un socket TCP o una lista de prueba.
public interface Buzon {

    // Entrega un mensaje de chat al usuario
    void recibir(Mensaje mensaje);

    // Entrega un aviso del sistema (ej. "ana se unio al chat")
    void notificar(String evento);
}
