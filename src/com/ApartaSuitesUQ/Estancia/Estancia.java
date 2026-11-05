package com.ApartaSuitesUQ.Estancia;

import com.ApartaSuitesUQ.Apartamento.Apartamento;

import java.time.LocalDate;
import java.util.ArrayList;

public class Estancia {
	private LocalDate fechaEntrada;
	private LocalDate fechaSalida;
	private int noches;

	private ArrayList<Noche> listNoches;
	private Apartamento theApartamento;

	public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida, ArrayList<Noche> listNoches,
			Apartamento theApartamento) {
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;

		this.listNoches = listNoches;
		this.theApartamento = theApartamento;
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

	// todo: hacer esto.
	// método de requisito
	public void calcularPrecioEstadia() {
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

	// ========== FIN DE GETS Y SETS ========== //
}