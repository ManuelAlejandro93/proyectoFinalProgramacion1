package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Reserva.Reserva;
import com.ApartaSuitesUQ.Reserva.Folio;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Reserva.EstadoActual;
import com.ApartaSuitesUQ.Reserva.CanalReserva;

import java.util.ArrayList;


public class GestorReservas {
	private ArrayList<Reserva> listReservas = new ArrayList<Reserva>();

	public GestorReservas() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Reserva> getListReservas() {
		return this.listReservas;
	}

	public void setListReservas(ArrayList<Reserva> listReservas) {
		this.listReservas = listReservas;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	public boolean anadirReserva(Reserva reserva) {

		if (reserva == null || reserva.getCodigo() == null ||
				reserva.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (buscarReservaPorCodigo(reserva.getCodigo()) != null) {
			return false; // Código duplicado
		}

		listReservas.add(reserva);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodasReservas() {
		String resultado = "";
		if (listReservas == null || listReservas.isEmpty()) {
			return "No hay reservas registrados.";
		}

		for (int i = 0; i < listReservas.size(); i++) {
			resultado = resultado + listReservas.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Reserva buscarReservaPorCodigo(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}

		Reserva resultado = null;
		for (int i = 0; i < this.listReservas.size(); i++) {
			if (codigo.equalsIgnoreCase(this.listReservas.get(i).getCodigo())) {
				resultado = this.listReservas.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionReserva(
            String codigo, Huesped nuevoHuespedEncargado, EstadoActual nuevoEstadoActual,
            Folio nuevoFolio,
            CanalReserva nuevoCanalReserva) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		if (nuevoHuespedEncargado == null ||
				nuevoEstadoActual == null || nuevoFolio == null ||
				nuevoCanalReserva == null) {
			return false; // Parámetros inválidos
		}

		Reserva reservaRecuperada = buscarReservaPorCodigo(codigo);
		if (reservaRecuperada == null) {
			return false;
		}
		reservaRecuperada.setTheHuesped(nuevoHuespedEncargado);
		reservaRecuperada.setEstadoActual(nuevoEstadoActual);
		reservaRecuperada.setTheFolio(nuevoFolio);
		reservaRecuperada.setTheCanalReserva(nuevoCanalReserva);

		return true;
	}

	// DELETE objeto
	public boolean eliminarReserva(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Reserva reservaRecuperada = buscarReservaPorCodigo(codigo);
		if (reservaRecuperada == null) {
			return false;
		} else {
			this.listReservas.remove(reservaRecuperada);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //
}
