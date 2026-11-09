package com.ApartaSuitesUQ.Estancia;

import java.time.LocalDate;

public class Noche {
	private LocalDate fecha;
	private Temporada temporada;
	private double precio;
	private double precioPorTemporada;

	private Estancia theEstancia;

	// Cada noche debe crearse con todas las especificaciones para evitar
	// confusiones con otras noches.
	public Noche(LocalDate fecha, Temporada temporada, double precio, Estancia theEstancia) {
		this.fecha = fecha;
		this.setTemporada(temporada);
		this.precio = precio;

		this.theEstancia = theEstancia;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public LocalDate getFecha() {
		return this.fecha;
	}

	public void setFecha(LocalDate fecha) {
		if (fecha != null) {
			this.fecha = fecha;
		}
	}

	public Temporada getTemporada() {
		return this.temporada;
	}

	public void setTemporada(Temporada temporada) {
		if (temporada != null) {
			this.temporada = temporada;

			switch (temporada) {
				case BAJA:
					this.setPrecioPorTemporada(1.0);
					break;
				case MEDIA:
					this.setPrecioPorTemporada(1.5);
					break;
				case ALTA:
					this.setPrecioPorTemporada(2.0);
					break;
			}
		}
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		if (precio >= 0) {
			this.precio = precio;
		}
	}

	public double getPrecioPorTemporada() {
		return this.precioPorTemporada;
	}

	public void setPrecioPorTemporada(double precioPorTemporada) {
		if ((this.temporada == Temporada.BAJA && precioPorTemporada == 1.0) ||
				(this.temporada == Temporada.MEDIA && precioPorTemporada == 1.5) ||
				(this.temporada == Temporada.ALTA && precioPorTemporada == 2.0)) {
			this.precioPorTemporada = precioPorTemporada;
		}
	}

	public Estancia getTheEstancia() {
		return this.theEstancia;
	}

	public void setTheEstancia(Estancia theEstancia) {
		this.theEstancia = theEstancia;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// ========== INICIO DE FUNCIONES AUXILIARES ========== //

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "fecha : " + this.getFecha().toString() + "\n";
		mensaje += "temporada : " + this.getTemporada().toString() + "\n";
		mensaje += "precio : " + this.getPrecio() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //

}