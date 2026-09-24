package servicio;

import java.util.concurrent.locks.ReentrantLock;

// Contador compartido de IDs (recurso compartido de la semana 2).
// Varios hilos piden IDs a la vez: sin lock, dos mensajes podrían recibir el mismo ID.
public class GeneradorIds {

    private final ReentrantLock lock = new ReentrantLock();
    private int ultimo = 0;

    public int siguiente() {
        lock.lock();            // entra a la sección crítica
        try {
            ultimo++;
            return ultimo;
        } finally {
            lock.unlock();      // siempre se libera, aunque ocurra un error
        }
    }
}
