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

	public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida, ArrayList<Noche> listNoches , Apartamento theApartamento) {
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;

		this.listNoches = listNoches;
		this.theApartamento = theApartamento;
		this.asignarNoches();
	}

	// todo: hacer esto.
	// asignar las noches respecto a las noches generales de la empresa.
	public void asignarNoches() {

	}

	// todo: hacer esto.
	// método de requisito
	public void calcularPrecioEstadia() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public LocalDate getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(LocalDate fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public LocalDate getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Apartamento getTheApartamento() {
		return theApartamento;
	}

	public void setTheApartamento(Apartamento theApartamento) {
		this.theApartamento = theApartamento;
	}

	public ArrayList<Noche> getListNoches() {
		return listNoches;
	}

	public void setListNoches(ArrayList<Noche> listNoches) {
		this.listNoches = listNoches;
	}

	// ========== FIN DE GETS Y SETS ========== //
}
