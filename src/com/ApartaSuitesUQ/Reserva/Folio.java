package com.ApartaSuitesUQ.Reserva;

import java.util.ArrayList;

public class Folio {
	private double saldo = 0;

	private ArrayList<Pago> listPagos;
	private ArrayList<Cargo> listCargos;

//	public Folio(ArrayList<Pago> pagos, ArrayList<Cargo> cargos) {
//		this.listPagos = pagos;
//		this.listCargos = cargos;
//		calcularSaldo();
//	}

	public Folio(ArrayList<Pago> listPagos, ArrayList<Cargo> listCargos, double saldo) {
		this.listPagos = listPagos;
		this.listCargos = listCargos;
		this.saldo = saldo;
	}

	// todo: arreglar esto después, entender bien qué hay que hacer.
	public double calcularSaldo() {
		return 0.0;
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
