package com.ApartaSuitesUQ.Reserva;

public class Cargo {
	private String nombre;
	private double precio;

	private Folio theFolio;

	public Cargo(String nombre, double precio, Folio theFolio) {
		this.nombre = nombre;
		this.precio = precio;

		this.theFolio = theFolio;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		if (precio >= 0) {
			this.precio = precio;
		}
	}

	public Folio getTheFolio() {
		return theFolio;
	}

	public void setTheFolio(Folio theFolio) {
		this.theFolio = theFolio;
	}

	// ========== FIN DE GETS Y SETS ========== //

	@Override
	public String toString() {
		String mensaje = "";
		mensaje += nombre + " : $" + precio;
		return mensaje;
	}
}