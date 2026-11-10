package com.ApartaSuitesUQ.Reserva;

import java.time.LocalDate;

public class Pago {
	private double monto;
	private MedioDePago medioDePago;
	private LocalDate fecha;

	private Folio theFolio;

	public Pago(double monto, MedioDePago medioDePago, LocalDate fecha, Folio theFolio) {
		this.monto = monto;
		this.medioDePago = medioDePago;
		this.fecha = fecha;

		this.theFolio = theFolio;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		if (monto > 0) {
			this.monto = monto;
		}
	}

	public MedioDePago getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(MedioDePago medioDePago) {
		if (medioDePago != null) {
			this.medioDePago = medioDePago;
		}
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		if (fecha != null && !fecha.isAfter(LocalDate.now())) {
			this.fecha = fecha;
		}
	}

	public Folio getTheFolio() {
		return theFolio;
	}

	public void setTheFolio(Folio theFolio) {
		this.theFolio = theFolio;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// Muestra el pago en formato simple: monto, medio de pago y fecha.
	@Override
	public String toString() {
		String mensaje = "";
		mensaje += "$" + String.format("%.2f", this.getMonto());
		mensaje += " - " + (this.getMedioDePago() != null ? this.getMedioDePago() : "N/A");
		mensaje += " - " + (this.getFecha() != null ? this.getFecha() : "N/A");
		return mensaje;
	}
}