package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Estancia.Noche;
import com.ApartaSuitesUQ.Estancia.Temporada;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorNoches {
	private ArrayList<Noche> listNoches = new ArrayList<Noche>();

	public GestorNoches() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Noche> getListNoches() {
		return this.listNoches;
	}

	public void setListNoches(ArrayList<Noche> listNoches) {
		this.listNoches = listNoches;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	public boolean anadirNoche(Noche noche) {
		if (noche == null || noche.getFecha() == null) {
			return false; // Fecha inválida
		}
		if (buscarNochePorFecha(noche.getFecha()) != null) {
			return false; // Fecha duplicada
		}
		listNoches.add(noche);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodasNoches() {
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
	public Noche buscarNochePorFecha(LocalDate fecha) {
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

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionNoche(
            LocalDate fecha, Temporada nuevaTemporada, double nuevoPrecio) {

		if (fecha == null) {
			return false; // Fecha inválida
		}
		if (nuevaTemporada == null) {
			return false; // com.ApartaSuitesUQ.Estancia.Temporada inválida
		}
		if (nuevoPrecio < 0) {
			return false; // Precio inválido
		}

		Noche nocheRecuperada = buscarNochePorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		}

		nocheRecuperada.setTemporada(nuevaTemporada);
		nocheRecuperada.setPrecio(nuevoPrecio);
		return true;
	}

	// DELETE objeto
	public boolean eliminarNoche(LocalDate fecha) {

		if (fecha == null) {
			return false; // Fecha inválida
		}
		Noche nocheRecuperada = buscarNochePorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		} else {
			this.listNoches.remove(nocheRecuperada);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //
}
