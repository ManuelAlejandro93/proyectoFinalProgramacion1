package com.ApartaSuitesUQ.Gestores;

import java.util.List;

public interface IGestor<T, ID> {

	boolean crear(T entidad);

	T consultar(ID id);

	String consultarTodos();

	boolean actualizar(ID id, T entidad);

	boolean borrar(ID id);
}