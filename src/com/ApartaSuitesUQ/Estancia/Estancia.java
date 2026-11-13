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
		// Garantiza que la lista exista aunque las fechas sean inválidas.
		if (this.listNoches == null) {
			this.listNoches = new ArrayList<Noche>();
		} else {
			this.listNoches.clear();
		}
		this.noches = 0;

		if (this.fechaEntrada == null || this.fechaSalida == null ||
				!this.fechaEntrada.isBefore(this.fechaSalida)) {
			return;
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

	// ========== INICIO DE FUNCIONES AUXILIARES ========== //

	// Muestra las fechas, el número de noches y el nombre del apartamento de la
	// estancia.
	@Override
	public String toString() {
		String nombreApartamento = "N/A";

		if (this.getTheApartamento() != null && this.getTheApartamento().getNombre() != null) {
			nombreApartamento = this.getTheApartamento().getNombre();
		}

		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "fecha de entrada : " + (this.getFechaEntrada() != null ? this.getFechaEntrada() : "N/A") + "\n";
		mensaje += "fecha de salida : " + (this.getFechaSalida() != null ? this.getFechaSalida() : "N/A") + "\n";
		mensaje += "noches : " + this.getNoches() + "\n";
		mensaje += "apartamento : " + nombreApartamento + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //
}