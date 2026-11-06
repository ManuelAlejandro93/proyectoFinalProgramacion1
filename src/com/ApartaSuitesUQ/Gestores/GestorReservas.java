package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Reserva.Reserva;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Reserva.EstadoActual;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorReservas implements IGestor<Reserva, String>{
	private ArrayList<Reserva> listReservas = new ArrayList<Reserva>();

	public GestorReservas() {
	}

	// Sobrecarga que recibe la lista para no depender de la inyección.
	public ArrayList<Apartamento> consultarApartamentosDisponibles(LocalDate entrada, LocalDate salida, int capacidadMinima, ArrayList<Apartamento> todos) {
		ArrayList<Apartamento> disponibles = new ArrayList<Apartamento>();

		if (entrada == null || salida == null || !entrada.isBefore(salida)) {
			return disponibles;
		}
		if (capacidadMinima <= 0 || todos == null) {
			return disponibles;
		}

		for (Apartamento apartamento : todos) {
			if (apartamento == null) {
				continue;
			}
			// 1. Capacidad mínima
			if (!validarCapacidadMaxima(apartamento, capacidadMinima)) {
				continue;
			}
			// 2. No debe estar fuera de servicio
			if (apartamento.getEstadoOperativo() == EstadoOperativo.FUERA_DE_SERVICIO) {
				continue;
			}
			// 3. Sin doble venta
			if (!validarSinDobleVenta(apartamento, entrada, salida, null)) {
				continue;
			}
			// 4. Sin bloqueos
			if (!validarBloqueos(apartamento, entrada, salida)) {
				continue;
			}
			disponibles.add(apartamento);
		}

		return disponibles;
	}

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	@Override
	public boolean crear(Reserva reserva) {
		if (reserva == null || reserva.getCodigo() == null ||
				reserva.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (consultar(reserva.getCodigo()) != null) {
			return false; // Código duplicado
		}

		listReservas.add(reserva);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	@Override
	public String consultarTodos() {
		String resultado = "";
		if (listReservas == null || listReservas.isEmpty()) {
			return "No hay reservas registradas.";
		}

		for (int i = 0; i < listReservas.size(); i++) {
			resultado = resultado + listReservas.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	@Override
	public Reserva consultar(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}

		Reserva resultado = null;
		for (int i = 0; i < listReservas.size(); i++) {
			if (codigo.equalsIgnoreCase(listReservas.get(i).getCodigo())) {
				resultado = listReservas.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	@Override
	public boolean actualizar(String codigo, Reserva reserva) {

//		Huesped nuevoHuespedEncargado, EstadoActual nuevoEstadoActual,
//				Folio nuevoFolio,
//				CanalReserva nuevoCanalReserva

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		if (reserva.getTheHuesped() == null ||
				reserva.getEstadoActual() == null || reserva.getTheFolio() == null ||
				reserva.getTheCanalReserva() == null) {
			return false; // Parámetros inválidos
		}

		Reserva reservaRecuperada = consultar(codigo);
		if (reservaRecuperada == null) {
			return false;
		}
		reservaRecuperada.setTheHuesped(reserva.getTheHuesped());
		reservaRecuperada.setEstadoActual(reserva.getEstadoActual());
		reservaRecuperada.setTheFolio(reserva.getTheFolio());
		reservaRecuperada.setTheCanalReserva(reserva.getTheCanalReserva());

		return true;
	}

	// DELETE objeto
	@Override
	public boolean borrar(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Reserva reservaRecuperada = consultar(codigo);
		if (reservaRecuperada == null) {
			return false;
		} else {
			listReservas.remove(reservaRecuperada);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //

	// ========== INICIO DE FUNCIONES DE VALIDACION ========== //
	public boolean existeCodigoExterno(String codigoExterno){
		return false;
	}

	// Sobrecarga para validar por número de personas, sin necesidad de crear la
	// lista.
	public boolean validarCapacidadMaxima(Apartamento apartamento, int totalOcupantes) {
		if (apartamento == null || totalOcupantes < 0) {
			return false;
		}
		return totalOcupantes <= apartamento.getCapacidad();
	}

	// Valida que una reserva no comparta noches con otra reserva activa
	// del mismo apartamento.
	public boolean validarSinDobleVenta(Apartamento apartamento, LocalDate entrada, LocalDate salida, String codigoReservaExcluir) {
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

			// Comprueba si los periodos se solapan.
			if (entrada.isBefore(salidaReserva) && entradaReserva.isBefore(salida)) {
				return false;
			}
		}

		return true;
	}

	// Valida que el rango solicitado no se cruce con ningún bloqueo del
	// apartamento.
	// La reserva ocupa [fechaEntrada, fechaSalida): el día de salida ya no se
	// ocupa.
	// El bloqueo ocupa [inicioBloqueo, finBloqueo]: su último día sí está
	// bloqueado.
	public boolean validarBloqueos(Apartamento apartamento, LocalDate fechaEntrada, LocalDate fechaSalida) {
		if (apartamento == null || fechaEntrada == null || fechaSalida == null
				|| !fechaEntrada.isBefore(fechaSalida)) {
			return false;
		}

		ArrayList<Bloqueo> bloqueosDelApartamento = apartamento.getListBloqueos();

		if (bloqueosDelApartamento == null || bloqueosDelApartamento.isEmpty()) {
			return true;
		}

		for (Bloqueo bloqueo : bloqueosDelApartamento) {
			if (bloqueo == null) {
				continue;
			}

			LocalDate inicioBloqueo = bloqueo.getFechaInicio();
			LocalDate finBloqueo = bloqueo.getFechafinal();

			if (inicioBloqueo == null || finBloqueo == null || finBloqueo.isBefore(inicioBloqueo)) {
				continue;
			}

			boolean reservaSaleAntesOElDiaQueEmpiezaElBloqueo = fechaSalida.isBefore(inicioBloqueo)
					|| fechaSalida.isEqual(inicioBloqueo);

			boolean reservaEntraDespuesDeQueTerminaElBloqueo = fechaEntrada.isAfter(finBloqueo);

			boolean hayCruceConElBloqueo = !reservaSaleAntesOElDiaQueEmpiezaElBloqueo
					&& !reservaEntraDespuesDeQueTerminaElBloqueo;

			if (hayCruceConElBloqueo) {
				return false;
			}
		}

		return true;
	}

	// ========== FIN DE FUNCIONES DE VALIDACION ========== //

	// ========== INICIO DE FUNCIONES AUXILIARES ========== //

	public String generarCodigoUnico(){
		return "A";
	}

	public boolean confirmarReserva(String codigoReserva){
		boolean res = false;

		Reserva reserva = consultar(codigoReserva);
		if (reserva != null){
			if (reserva.getEstadoActual() == EstadoActual.PENDIENTE){
				if(reserva.getTheCanalReserva().nombreCanal() == NombreCanal.PORTAL) {
					double valorTotal = reserva.getTheApartamento().getTheEstancia().calcularPrecioEstadia();
					double anticipoNecesario = valorTotal * 0.30;
					double saldoFolio = reserva.getTheFolio().calcularSaldo();

					if (saldoFolio >= anticipoNecesario){
						if (reserva.puedePasarA(EstadoActual.CONFIRMADA)){
							reserva.setEstadoActual(EstadoActual.CONFIRMADA);
							res = true;
						}
					}
				}
			}
		}

		return res;
	}

	public boolean cancelarReserva(String codigoReserva){
		boolean res = false;
		Reserva reserva = consultar(codigoReserva);

		if (reserva.getEstadoActual() == EstadoActual.PENDIENTE || reserva.getEstadoActual() == EstadoActual.CONFIRMADA ){
			if(reserva.puedePasarA(EstadoActual.CANCELADA)){
				double montoDevolucion = reserva.calcularDevolucion();
				boolean resDevolucion = reserva.getTheFolio().registrarDevolucion(montoDevolucion);

				if (resDevolucion){
					reserva.setEstadoActual(EstadoActual.CANCELADA);
					res = true;
				}
			}
		}

		return res;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Reserva> getListReservas() {
		return this.listReservas;
	}

	public void setListReservas(ArrayList<Reserva> listReservas) {
		this.listReservas = listReservas;
	}

	// ========== FIN DE GETS Y SETS ========== //

}
