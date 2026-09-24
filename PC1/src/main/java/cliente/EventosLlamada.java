package cliente;

// Lo que el ReceptorServidor avisa cuando llega algo (lo implementa ClienteLlamada).
public interface EventosLlamada {

    void alRecibirTexto(String linea);

    void alRecibirVideo(String origen, byte[] jpeg);

    void alRecibirAudio(String origen, byte[] pcm);

    void alDesconectar();
}
