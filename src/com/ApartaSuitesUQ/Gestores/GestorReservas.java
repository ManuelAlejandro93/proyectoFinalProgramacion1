package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Reserva.NombreCanal;
import com.ApartaSuitesUQ.Reserva.Reserva;
import com.ApartaSuitesUQ.Reserva.Folio;
import com.ApartaSuitesUQ.Reserva.EstadoActual;
import com.ApartaSuitesUQ.Reserva.CanalReserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class GestorReservas {
	private ArrayList<Reserva> listReservas = new ArrayList<Reserva>();
	private GestorApartamentos theGestorApartamentos;
	private Random generadorAleatorio = new Random();

	public GestorReservas() {
	}

	public GestorReservas(GestorApartamentos theGestorApartamentos) {
		this.theGestorApartamentos = theGestorApartamentos;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Reserva> getListReservas() {
		return this.listReservas;
	}

	public void setListReservas(ArrayList<Reserva> listReservas) {
		this.listReservas = listReservas;
	}

	public GestorApartamentos getTheGestorApartamentos() {
		return this.theGestorApartamentos;
	}

	public void setTheGestorApartamentos(GestorApartamentos theGestorApartamentos) {
		this.theGestorApartamentos = theGestorApartamentos;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// Genera un código con formato RES-timestamp-aleatorio que no exista en el
	// sistema.
	public String generarCodigoUnico() {
		while (true) {
			long marcaDeTiempo = System.currentTimeMillis();
			int numeroAleatorio = this.generadorAleatorio.nextInt(9000) + 1000;
			String codigoGenerado = "RES-" + marcaDeTiempo + "-" + numeroAleatorio;

			if (this.buscarReservaPorCodigo(codigoGenerado) == null) {
				return codigoGenerado;
			}
		}
	}

	// Retorna true si el código externo ya está siendo utilizado
	// por una reserva cuyo canal sea EXTERNA.
	public boolean existeCodigoExterno(String codigoExterno) {
		if (codigoExterno == null || codigoExterno.trim().isEmpty()
				|| this.listReservas == null) {
			return false;
		}

		for (Reserva reserva : this.listReservas) {
			if (reserva == null) {
				continue;
			}

			CanalReserva canal = reserva.getTheCanalReserva();

			if (canal == null || canal.nombreCanal() != NombreCanal.EXTERNA) {
				continue;
			}

			String codigoDelCanal = canal.codigoCanal();

			if (codigoExterno.equals(codigoDelCanal)) {
				return true;
			}
		}

		return false;
	}

	// Valida que todos los ocupantes, incluidos los menores de 6 años,
	// no superen la capacidad máxima del apartamento.
	public boolean validarCapacidadMaxima(Apartamento apartamento, ArrayList<Huesped> ocupantes) {
		if (apartamento == null || ocupantes == null) {
			return false;
		}

		return validarCapacidadMaxima(apartamento, ocupantes.size());
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

	// Retorna apartamentos con capacidad suficiente, sin doble venta,
	// sin bloqueos y que no estén fuera de servicio.
	public ArrayList<Apartamento> consultarApartamentosDisponibles(LocalDate entrada, LocalDate salida,
			int capacidadMinima) {
		if (this.theGestorApartamentos == null ||
				this.theGestorApartamentos.getListApartamentos() == null) {
			return new ArrayList<Apartamento>();
		}

		return consultarApartamentosDisponibles(
				entrada,
				salida,
				capacidadMinima,
				this.theGestorApartamentos.getListApartamentos());
	}

	// Sobrecarga que recibe la lista para no depender de la inyección.
	public ArrayList<Apartamento> consultarApartamentosDisponibles(LocalDate entrada, LocalDate salida,
			int capacidadMinima, ArrayList<Apartamento> todos) {
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

			// 1. Capacidad mínima.
			if (!validarCapacidadMaxima(apartamento, capacidadMinima)) {
				continue;
			}

			// 2. No debe estar fuera de servicio.
			if (apartamento.getEstadoOperativo() == EstadoOperativo.FUERA_DE_SERVICIO) {
				continue;
			}

			// 3. Sin doble venta.
			if (!validarSinDobleVenta(apartamento, entrada, salida, null)) {
				continue;
			}

			// 4. Sin bloqueos.
			if (!validarBloqueos(apartamento, entrada, salida)) {
				continue;
			}

			disponibles.add(apartamento);
		}

		return disponibles;
	}

	// ========== INICIO DE CRUD ========== //

	// CREATE - añade una reserva si cumple las validaciones.
	public boolean anadirReserva(Reserva reserva) {
		if (reserva == null || reserva.getCodigo() == null ||
				reserva.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}

		if (this.buscarReservaPorCodigo(reserva.getCodigo()) != null) {
			return false; // Código interno duplicado
		}

		CanalReserva canalReserva = reserva.getTheCanalReserva();

		// Si la reserva proviene de un canal externo, su código externo
		// no puede estar registrado en otra reserva.
		if (canalReserva != null &&
				canalReserva.nombreCanal() == NombreCanal.EXTERNA &&
				this.existeCodigoExterno(canalReserva.codigoCanal())) {
			return false; // Código externo duplicado
		}

		Estancia estancia = reserva.getTheEstancia();
		Apartamento apartamento = reserva.getTheApartamento();

		// Valida capacidad, disponibilidad y bloqueos cuando hay datos suficientes.
		if (estancia != null && apartamento != null) {
			if (apartamento.getEstadoOperativo() == EstadoOperativo.FUERA_DE_SERVICIO) {
				return false;
			}

			if (!validarCapacidadMaxima(apartamento, estancia.getListHuespedes())) {
				return false; // Excede la capacidad o faltan datos de ocupantes
			}

			if (!validarSinDobleVenta(
					apartamento,
					estancia.getFechaEntrada(),
					estancia.getFechaSalida(),
					null)) {
				return false; // No disponible en esas fechas
			}

			if (!validarBloqueos(
					apartamento,
					estancia.getFechaEntrada(),
					estancia.getFechaSalida())) {
				return false; // Hay bloqueo en esas fechas
			}
		}

		this.listReservas.add(reserva);
		return true;
	}

	// READ: lista todas las reservas como texto.
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

	// READ: busca una reserva por código.
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

	// UPDATE: actualiza la información de una reserva.
	public boolean actualizarInformacionReserva(
			String codigo,
			Huesped nuevoHuespedEncargado,
			EstadoActual nuevoEstadoActual,
			Folio nuevoFolio,
			CanalReserva nuevoCanalReserva) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		if (nuevoHuespedEncargado == null ||
				nuevoEstadoActual == null ||
				nuevoFolio == null ||
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

	// DELETE: elimina una reserva por código.
	public boolean eliminarReserva(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Reserva reservaRecuperada = this.buscarReservaPorCodigo(codigo);

		if (reservaRecuperada == null) {
			return false;
		}

		this.listReservas.remove(reservaRecuperada);
		return true;
	}

	// ========== FIN DE CRUD ========== //
	public boolean confirmarReserva(String codigoReserva) {
		boolean res = false;

		Reserva reserva = buscarReservaPorCodigo(codigoReserva);
		if (reserva != null) {
			if (reserva.getEstadoActual() == EstadoActual.PENDIENTE) {
				if (reserva.getTheCanalReserva().nombreCanal() == NombreCanal.PORTAL) {
					double valorTotal = reserva.getTheApartamento().getTheEstancia().calcularPrecioEstadia();
					double anticipoNecesario = valorTotal * 0.30;
					double saldoFolio = reserva.getTheFolio().calcularSaldo();

					if (saldoFolio >= anticipoNecesario) {
						if (reserva.puedePasarA(EstadoActual.CONFIRMADA)) {
							reserva.setEstadoActual(EstadoActual.CONFIRMADA);
							res = true;
						}
					}
				}
			}
		}

		return res;
	}

	public boolean cancelarReserva(String codigoReserva) {
		boolean res = false;
		Reserva reserva = this.buscarReservaPorCodigo(codigoReserva);

		if (reserva.getEstadoActual() == EstadoActual.PENDIENTE || reserva.getEstadoActual() == EstadoActual.CONFIRMADA) {
			if (reserva.puedePasarA(EstadoActual.CANCELADA)) {
				double montoDevolucion = reserva.calcularDevolucion();
				boolean resDevolucion = reserva.getTheFolio().registrarDevolucion(montoDevolucion);

				if (resDevolucion) {
					reserva.setEstadoActual(EstadoActual.CANCELADA);
					res = true;
				}
			}
		}

		return res;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //
}