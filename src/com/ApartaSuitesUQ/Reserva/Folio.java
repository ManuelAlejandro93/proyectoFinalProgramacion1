package com.ApartaSuitesUQ.Reserva;

import java.util.ArrayList;

public class Folio {
	private double saldo = 0;

	private ArrayList<Pago> listPagos;
	private ArrayList<Cargo> listCargos;

	public Folio(ArrayList<Pago> listPagos, ArrayList<Cargo> listCargos, double saldo) {
		this.listPagos = listPagos;
		this.listCargos = listCargos;
		this.saldo = saldo;
	}

	public double calcularSaldo() {
		double sumaCargos = 0;
		double sumaPagos = 0;

		if (listCargos != null) {
			for (Cargo cargo : listCargos) {
				sumaCargos += cargo.getPrecio();
			}
		}

		if (listPagos != null) {
			for (Pago pago : listPagos) {
				sumaPagos += pago.getMonto();
			}
		}

		this.saldo = sumaCargos - sumaPagos;
		return saldo;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Pago> getListPagos() {
		return listPagos;
	}

	public void setListPagos(ArrayList<Pago> pagos) {
		this.listPagos = pagos;
	}

	public ArrayList<Cargo> getListCargos() {
		return listCargos;
	}

	public void setListCargos(ArrayList<Cargo> cargos) {
		this.listCargos = cargos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	// ========== FIN DE GETS Y SETS ========== //
}