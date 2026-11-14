package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Estancia.Noche;
import com.ApartaSuitesUQ.Estancia.Temporada;
import com.ApartaSuitesUQ.Usuarios.Huesped;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorNoches implements IGestor<Noche, String> {
	private ArrayList<Noche> listNoches = new ArrayList<Noche>();

	public GestorNoches() {
	}

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	@Override
	public boolean crear(Noche noche) {
		if (noche == null || noche.getFecha() == null) {
			return false; // Fecha inválida
		}
		if (consultarPorFecha(noche.getFecha()) != null) {
			return false; // Fecha duplicada
		}
		listNoches.add(noche);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	@Override
	public String consultarTodos() {
		String resultado = "";
		if (listNoches == null || listNoches.isEmpty()) {
			return "No hay noches registradas.";
		}

		for (int i = 0; i < listNoches.size(); i++) {
			resultado = resultado + listNoches.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	@Override
	public Noche consultar(String codigo) {
		return null;
	}

	// READ (buscar por fecha) devuelve objeto o null
	public Noche consultarPorFecha(LocalDate fecha) {
		if (fecha == null) {
			return null; // Fecha inválida
		}
		Noche resultado = null;
		for (int i = 0; i < this.listNoches.size(); i++) {
			if (fecha.equals(listNoches.get(i).getFecha())) {
				resultado = this.listNoches.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto por codigo
	@Override
	public boolean actualizar(String codigo, Noche noche){
		return false;
	}

	// UPDATE - actualiza objeto por fecha
	public boolean actualizarPorFecha(LocalDate fecha, Temporada nuevaTemporada, double nuevoPrecio) {

		if (fecha == null) {
			return false; // Fecha inválida
		}
		if (nuevaTemporada == null) {
			return false; // com.ApartaSuitesUQ.Estancia.Temporada inválida
		}
		if (nuevoPrecio < 0) {
			return false; // Precio inválido
		}

		Noche nocheRecuperada = consultarPorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		}

		nocheRecuperada.setTemporada(nuevaTemporada);
		nocheRecuperada.setPrecio(nuevoPrecio);
		return true;
	}

	// DELETE objeto por id
	@Override
	public boolean borrar(String codigo){
		return false;
	}

	// DELETE objeto por fecha
	public boolean borrarPorFecha(LocalDate fecha) {

		if (fecha == null) {
			return false; // Fecha inválida
		}
		Noche nocheRecuperada = consultarPorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		} else {
			this.listNoches.remove(nocheRecuperada);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Noche> getListNoches() {
		return this.listNoches;
	}

	public void setListNoches(ArrayList<Noche> listNoches) {
		this.listNoches = listNoches;
	}

	// ========== FIN DE GETS Y SETS ========== //
}
