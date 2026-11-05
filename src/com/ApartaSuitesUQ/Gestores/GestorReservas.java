package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Reserva.Reserva;
import com.ApartaSuitesUQ.Reserva.Folio;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Reserva.EstadoActual;
import com.ApartaSuitesUQ.Reserva.CanalReserva;

import java.time.LocalDate;
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

	// Valida que todos los ocupantes, incluidos los menores de 6 años,
	// no superen la capacidad máxima del apartamento.
	public boolean validarCapacidadMaxima(Apartamento apartamento, ArrayList<Huesped> ocupantes) {
		if (apartamento == null || ocupantes == null) {
			return false;
		}

		int totalOcupantes = ocupantes.size();
		int capacidadMaxima = apartamento.getCapacidad();

		return totalOcupantes <= capacidadMaxima;
	}

	// Valida que una reserva no comparta noches con otra reserva activa
	// del mismo apartamento.
	public boolean validarSinDobleVenta(Apartamento apartamento, LocalDate entrada,
			LocalDate salida, String codigoReservaExcluir) {
		if (apartamento == null || apartamento.getCodigo() == null ||
				apartamento.getCodigo().trim().isEmpty() ||
				entrada == null || salida == null || !entrada.isBefore(salida)) {
			return false;
		}

		if (this.listReservas == null) {
			return true;
		}

		for (Reserva reserva : this.listReservas) {
			if (reserva == null) {
				continue;
			}

			if (codigoReservaExcluir != null &&
					codigoReservaExcluir.equalsIgnoreCase(reserva.getCodigo())) {
				continue;
			}

			EstadoActual estado = reserva.getEstadoActual();

			if (estado != EstadoActual.PENDIENTE &&
					estado != EstadoActual.CONFIRMADA &&
					estado != EstadoActual.EN_CURSO) {
				continue;
			}

			Apartamento apartamentoReserva = reserva.getTheApartamento();

			if (apartamentoReserva == null ||
					!apartamento.getCodigo().equalsIgnoreCase(apartamentoReserva.getCodigo())) {
				continue;
			}

			Estancia estancia = reserva.getTheEstancia();

			// Si faltan fechas de una reserva activa del mismo apartamento,
			// no se puede garantizar que esté disponible.
			if (estancia == null || estancia.getFechaEntrada() == null ||
					estancia.getFechaSalida() == null) {
				return false;
			}

			LocalDate entradaReserva = estancia.getFechaEntrada();
			LocalDate salidaReserva = estancia.getFechaSalida();

			if (!entradaReserva.isBefore(salidaReserva)) {
				return false;
			}

			if (entrada.isBefore(salidaReserva) && entradaReserva.isBefore(salida)) {
				return false;
			}
		}

		return true;
	}

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	public boolean anadirReserva(Reserva reserva) {

		if (reserva == null || reserva.getCodigo() == null ||
				reserva.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (this.buscarReservaPorCodigo(reserva.getCodigo()) != null) {
			return false; // Código duplicado
		}

		this.listReservas.add(reserva);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodasReservas() {
		String resultado = "";
		if (this.listReservas == null || this.listReservas.isEmpty()) {
			return "No hay reservas registradas.";
		}

		for (int i = 0; i < this.listReservas.size(); i++) {
			resultado = resultado + this.listReservas.get(i).toString() + "\n";
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

		Reserva reservaRecuperada = this.buscarReservaPorCodigo(codigo);
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

		Reserva reservaRecuperada = this.buscarReservaPorCodigo(codigo);
		if (reservaRecuperada == null) {
			return false;
		} else {
			this.listReservas.remove(reservaRecuperada);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //
}