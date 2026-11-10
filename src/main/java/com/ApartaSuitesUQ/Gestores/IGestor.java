package com.ApartaSuitesUQ.Gestores;

public interface IGestor<T, ID> {

    boolean crear(T entidad);

    T consultar(ID id);

    String consultarTodos();

    boolean actualizar(ID id, T entidad);

    boolean borrar(ID id);
}