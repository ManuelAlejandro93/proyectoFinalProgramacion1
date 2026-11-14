package com.ApartaSuitesUQ.Apartamento;

import java.time.LocalDate;

public class Bloqueo {
	private String motivo;
	private LocalDate fechaInicio;
	private LocalDate fechafinal;

	private Apartamento theApartamento;

	public Bloqueo(String motivo, LocalDate fechaInicio, LocalDate fechafinal, Apartamento theApartamento) {
		this.motivo = motivo;
		this.fechaInicio = fechaInicio;
		this.fechafinal = fechafinal;

		this.theApartamento = theApartamento;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechafinal() {
		return fechafinal;
	}

	public void setFechafinal(LocalDate fechafinal) {
		this.fechafinal = fechafinal;
	}

	public Apartamento getTheApartamento() {
		return theApartamento;
	}

	public void setTheApartamento(Apartamento theApartamento) {
		this.theApartamento = theApartamento;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// Muestra el motivo y el rango de fechas del bloqueo.
	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "motivo : " + (this.getMotivo() != null ? this.getMotivo() : "N/A") + "\n";
		mensaje += "fecha de inicio : " + (this.getFechaInicio() != null ? this.getFechaInicio() : "N/A") + "\n";
		mensaje += "fecha final : " + (this.getFechafinal() != null ? this.getFechafinal() : "N/A") + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}