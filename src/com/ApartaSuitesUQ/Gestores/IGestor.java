package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Apartamento.Apartamento;

import java.time.LocalDate;
import java.util.List;

public interface IGestor<T, ID> {

    boolean crear(T entidad);

    T consultar(ID id);

    String consultarTodos();

    boolean actualizar(ID id, T entidad);

    boolean borrar(ID id);
}