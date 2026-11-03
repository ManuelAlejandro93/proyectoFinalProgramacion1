package com.ApartaSuitesUQ.Apartamento;

public class Novedad {
	private String nombre;

	private Apartamento theApartamento;

	public Novedad(String nombre, Apartamento theApartamento) {
		this.nombre = nombre;

		this.theApartamento = theApartamento;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if (nombre != null && !nombre.trim().isEmpty()) {
			this.nombre = nombre;
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
		return nombre;
	}
}