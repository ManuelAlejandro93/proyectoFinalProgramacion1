package com.ApartaSuitesUQ.Estancia;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Usuarios.Huesped;

import java.time.LocalDate;
import java.util.ArrayList;

public class Estancia {
	private LocalDate fechaEntrada;
	private LocalDate fechaSalida;
	private int noches;

	private ArrayList<Noche> listNoches;
	private ArrayList<Huesped> listHuespedes;
	private Apartamento theApartamento;

	// Conserva el constructor existente para no afectar las llamadas actuales.
//	public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida, ArrayList<Noche> listNoches,
//			Apartamento theApartamento) {
//		this(fechaEntrada, fechaSalida, listNoches, theApartamento, new ArrayList<Huesped>());
//	}

	// Permite crear la estancia con sus huéspedes.
	public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida,
			Apartamento theApartamento, ArrayList<Huesped> listHuespedes) {
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.theApartamento = theApartamento;
		this.listHuespedes = listHuespedes;
		this.asignarNoches();
	}

	// Crea una noche por cada fecha de la estancia, sin incluir la fecha de salida.
	public void asignarNoches() {
		if (this.fechaEntrada == null || this.fechaSalida == null ||
				!this.fechaEntrada.isBefore(this.fechaSalida)) {
			return;
		}

		if (this.listNoches == null) {
			this.listNoches = new ArrayList<Noche>();
		} else if (!this.listNoches.isEmpty()) {
			this.listNoches.clear();
		}

		int nocheCount = 0;
		LocalDate fechaActual = this.fechaEntrada;

		while (fechaActual.isBefore(this.fechaSalida)) {
			Noche noche = new Noche(fechaActual, Temporada.BAJA, 0.0, this);
			this.listNoches.add(noche);
			nocheCount++;
			fechaActual = fechaActual.plusDays(1);
		}

		this.noches = nocheCount;
	}

	// Calcula el precio sumando la tarifa ajustada de cada noche por cada huésped
	// pagante.
	public double calcularPrecioEstadia() {
		if (this.listNoches == null || this.listNoches.isEmpty()) {
			return 0.0;
		}

		if (this.theApartamento == null) {
			return 0.0;
		}

		int ocupantesPagantes = 0;

		if (this.listHuespedes != null) {
			for (Huesped huesped : this.listHuespedes) {
				if (huesped != null && huesped.getEdad() >= 6) {
					ocupantesPagantes++;
				}
			}
		}

		double precioTotal = 0.0;

		for (Noche noche : this.listNoches) {
			double tarifa = this.theApartamento.getTarifaIndividual();
			double precioPorNoche = tarifa * ocupantesPagantes * noche.getPrecioPorTemporada();
			precioTotal += precioPorNoche;
		}

		return precioTotal;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public LocalDate getFechaEntrada() {
		return this.fechaEntrada;
	}

	public void setFechaEntrada(LocalDate fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public LocalDate getFechaSalida() {
		return this.fechaSalida;
	}

	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Apartamento getTheApartamento() {
		return this.theApartamento;
	}

	public void setTheApartamento(Apartamento theApartamento) {
		this.theApartamento = theApartamento;
	}

	public ArrayList<Noche> getListNoches() {
		return this.listNoches;
	}

	public void setListNoches(ArrayList<Noche> listNoches) {
		this.listNoches = listNoches;
	}

	public ArrayList<Huesped> getListHuespedes() {
		return this.listHuespedes;
	}

	public void setListHuespedes(ArrayList<Huesped> listHuespedes) {
		this.listHuespedes = listHuespedes;
	}

	public int getNoches() {
		return noches;
	}

	public void setNoches(int noches) {
		this.noches = noches;
	}

	// ========== FIN DE GETS Y SETS ========== //
}
