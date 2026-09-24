package repositorio;

import excepciones.DuplicadoException;
import excepciones.NoEncontradoException;
import modelo.Identificable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Implementación en memoria del repositorio genérico.
// Usa ConcurrentHashMap porque varios hilos del servidor guardan mensajes a la vez.
public class RepositorioMemoria<T extends Identificable> implements Repositorio<T> {

    private final Map<Integer, T> datos = new ConcurrentHashMap<>();

    @Override
    public void guardar(T elemento) {
        // putIfAbsent verifica y guarda en UN solo paso (atómico):
        // evita que dos hilos registren el mismo ID al mismo tiempo
        if (datos.putIfAbsent(elemento.id(), elemento) != null) {
            throw new DuplicadoException("El ID " + elemento.id() + " ya esta registrado");
        }
    }

    @Override
    public T buscar(int id) {
        T encontrado = datos.get(id);
        if (encontrado == null) {
            throw new NoEncontradoException("No hay nada con el ID " + id);
        }
        return encontrado;
    }

    @Override
    public void eliminar(int id) {
        if (datos.remove(id) == null) {
            throw new NoEncontradoException("No se puede borrar, el ID " + id + " no existe");
        }
    }

    @Override
    public List<T> listar() {
        List<T> copia = new ArrayList<>(datos.values());
        copia.sort((a, b) -> Integer.compare(a.id(), b.id()));   // orden de llegada
        return Collections.unmodifiableList(copia);              // nadie modifica el interno
    }

    @Override
    public int cantidad() {
        return datos.size();
    }
}
