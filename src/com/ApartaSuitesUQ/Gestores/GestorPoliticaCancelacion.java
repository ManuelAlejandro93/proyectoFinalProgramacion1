package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.PoliticaDeCancelacion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class GestorPoliticaCancelacion {
	private PoliticaDeCancelacion politicaDeCancelacion = new PoliticaDeCancelacion(1.0, 0.5, 0.0);
	// Política de cancelación:
	// devolucionCompleta: 1.0 (100%) - se devuelve todo
	// devolucionParcial: 0.5 (50%) - se devuelve la mitad
	// sinDevolucion: 0.0 (0%) - No se devuelve nada

	public GestorPoliticaCancelacion() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public PoliticaDeCancelacion getPoliticaDeCancelacion() {
		return this.politicaDeCancelacion;
	}

	public void setPoliticaDeCancelacion(PoliticaDeCancelacion politicaDeCancelacion) {
		this.politicaDeCancelacion = politicaDeCancelacion;
	}

	// ========== FIN DE GETS Y SETS ========== //

	public boolean actualizarDevolucionCompleta(double nuevaDevolucionCompleta) {
		if (nuevaDevolucionCompleta >= 0 && nuevaDevolucionCompleta <= 1) {
			this.politicaDeCancelacion.setDevolucionCompleta(nuevaDevolucionCompleta);
			return true;
		}
		return false;
	}

	public boolean actualizarDevolucionParcial(double nuevaDevolucionParcial) {
		if (nuevaDevolucionParcial >= 0 && nuevaDevolucionParcial <= 1) {
			this.politicaDeCancelacion.setDevolucionParcial(nuevaDevolucionParcial);
			return true;
		}
		return false;
	}

	public boolean actualizarSinDevolucion(double nuevoSinDevolucion) {
		if (nuevoSinDevolucion >= 0 && nuevoSinDevolucion <= 1) {
			this.politicaDeCancelacion.setSinDevolucion(nuevoSinDevolucion);
			return true;
		}
		return false;
	}

	public double obtenerDevolucionCompleta() {
		return this.politicaDeCancelacion.getDevolucionCompleta();
	}

	public double obtenerDevolucionParcial() {
		return this.politicaDeCancelacion.getDevolucionParcial();
	}

	public double obtenerSinDevolucion() {
		return this.politicaDeCancelacion.getSinDevolucion();
	}

	// Calcula el monto de devolución según los días de antelación.
	public double calcularDevolicion(double valorOriginal, LocalDate fechaEntrada) {
		if (fechaEntrada == null) {
			return 0.0;
		}

		LocalDate hoy = LocalDate.now();
		long diasAntelacion = ChronoUnit.DAYS.between(hoy, fechaEntrada);
		double porcentajeDevolucion = 0.0;

		if (diasAntelacion > 7) {
			porcentajeDevolucion = this.obtenerDevolucionCompleta();
		} else if (diasAntelacion >= 2) {
			porcentajeDevolucion = this.obtenerDevolucionParcial();
		} else {
			porcentajeDevolucion = this.obtenerSinDevolucion();
		}

		double montoDevolucion = valorOriginal * porcentajeDevolucion;
		return montoDevolucion;
	}
}