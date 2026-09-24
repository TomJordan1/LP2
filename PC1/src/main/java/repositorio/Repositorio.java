package repositorio;

import modelo.Identificable;

import java.util.List;

// Contrato del almacenamiento (DIP): el servicio depende de ESTA interfaz,
// no de cómo se guardan los datos (memoria, archivo, base de datos...).
public interface Repositorio<T extends Identificable> {

    void guardar(T elemento);

    T buscar(int id);

    void eliminar(int id);

    List<T> listar();

    int cantidad();
}
