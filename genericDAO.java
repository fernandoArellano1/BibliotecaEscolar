package dao;

import java.util.List;

public interface GenericDAO<T> {
    boolean insertar(T objeto);
    boolean actualizar(T objeto);
    boolean eliminar(int id);
    List<T> obtenerTodos();
}