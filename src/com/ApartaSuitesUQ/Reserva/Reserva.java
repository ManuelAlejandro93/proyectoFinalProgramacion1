package com.ApartaSuitesUQ.Reserva;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Usuarios.Huesped;

public class Reserva {
	private String codigo;
	private EstadoActual estadoActual;

	private Huesped theHuesped;
	private Folio theFolio;
	private CanalReserva theCanalReserva;
	private Apartamento theApartamento;

	public Reserva(String codigo, Huesped theHuesped, EstadoActual estadoActual, Folio theFolio,
			CanalReserva canalReserva, Apartamento theApartamento) {
		this.codigo = codigo;
		this.estadoActual = estadoActual;

		this.theHuesped = theHuesped;
		this.theFolio = theFolio;
		this.theCanalReserva = canalReserva;
		this.theApartamento = theApartamento;
	}

	// Valida si el estado actual permite pasar al nuevo estado.
	public boolean puedePasarA(EstadoActual nuevoEstado) {
		if (this.estadoActual == null || nuevoEstado == null) {
			return false;
		}

		switch (this.estadoActual) {
			case PENDIENTE:
				return nuevoEstado == EstadoActual.CONFIRMADA
						|| nuevoEstado == EstadoActual.CANCELADA;

			case CONFIRMADA:
				return nuevoEstado == EstadoActual.EN_CURSO
						|| nuevoEstado == EstadoActual.CANCELADA
						|| nuevoEstado == EstadoActual.NO_SHOWN;

			case EN_CURSO:
				return nuevoEstado == EstadoActual.FINALIZADA;

			case FINALIZADA:
			case CANCELADA:
			case NO_SHOWN:
				return false;

			default:
				return false;
		}
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		if (codigo != null && !codigo.trim().isEmpty()) {
			this.codigo = codigo;
		}
	}

	public EstadoActual getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(EstadoActual estadoActual) {
		if (estadoActual != null) {
			this.estadoActual = estadoActual;
		}
	}

	public Huesped getTheHuesped() {
		return theHuesped;
	}

	public void setTheHuesped(Huesped huespedEncargado) {
		if (huespedEncargado != null) {
			this.theHuesped = huespedEncargado;
		}
	}

	public Folio getTheFolio() {
		return theFolio;
	}

	public void setTheFolio(Folio folio) {
		if (folio != null) {
			this.theFolio = folio;
		}
	}

	public CanalReserva getTheCanalReserva() {
		return theCanalReserva;
	}

	public void setTheCanalReserva(CanalReserva canalReserva) {
		if (canalReserva != null) {
			this.theCanalReserva = canalReserva;
		}
	}

	public Apartamento getTheApartamento() {
		return theApartamento;
	}

	public void setTheApartamento(Apartamento theApartamento) {
		this.theApartamento = theApartamento;
	}

	// ========== FIN DE GETS Y SETS ========== //

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "codigo : " + (this.getCodigo() != null ? this.getCodigo() : "N/A") + "\n";
		mensaje += "huespedEncargado : "
				+ (this.getTheHuesped() != null ? this.getTheHuesped().getNombre() : "N/A") + "\n";

		mensaje += "estado actual : " + (this.getEstadoActual() != null ? this.getEstadoActual() : "N/A") + "\n";
		mensaje += "saldo : " + (this.getTheFolio() != null ? this.getTheFolio().calcularSaldo() : "N/A") + "\n";
		mensaje += "canal de reserva : " + (this.getTheCanalReserva() != null ? this.getTheCanalReserva() : "N/A") + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}