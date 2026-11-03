package com.ApartaSuitesUQ.Estancia;

import java.time.LocalDate;

public class Noche {
	private LocalDate fecha;
	private Temporada temporada;
	private double precio;

	private Estancia theEstancia;

	// cada noche debe crearse con todas las especificaciones para evitar
	// confusiones con otras noches.
	public Noche(LocalDate fecha, Temporada temporada, double precio, Estancia theEstancia) {
		this.fecha = fecha;
		this.temporada = temporada;
		this.precio = precio;

		this.theEstancia = theEstancia;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		if (fecha != null) {
			this.fecha = fecha;
		}
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		if (temporada != null) {
			this.temporada = temporada;
		}
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		if (precio >= 0) {
			this.precio = precio;
		}
	}

	public Estancia getTheEstancia() {
		return theEstancia;
	}

	public void setTheEstancia(Estancia theEstancia) {
		this.theEstancia = theEstancia;
	}

	// ========== FIN DE GETS Y SETS ========== //

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
}
