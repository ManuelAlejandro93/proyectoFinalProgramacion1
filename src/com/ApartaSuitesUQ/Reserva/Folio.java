package com.ApartaSuitesUQ.Reserva;

import java.time.LocalDate;
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

	// Calcula el saldo a partir de los cargos y los pagos del folio.
	public double calcularSaldo() {
		double sumaCargos = 0;
		double sumaPagos = 0;

		if (this.listCargos != null) {
			for (Cargo cargo : this.listCargos) {
				sumaCargos += cargo.getPrecio();
			}
		}

		if (this.listPagos != null) {
			for (Pago pago : this.listPagos) {
				sumaPagos += pago.getMonto();
			}
		}

		this.saldo = sumaCargos - sumaPagos;
		return this.saldo;
	}

	// Agrega un pago válido al folio.
	public boolean anadirPago(Pago pago) {
		if (pago == null || pago.getMonto() <= 0) {
			return false;
		}

		if (pago.getMedioDePago() == null || pago.getFecha() == null) {
			return false;
		}

		if (this.listPagos == null) {
			this.listPagos = new ArrayList<Pago>();
		}

		this.listPagos.add(pago);
		return true;
	}

	// Agrega un cargo válido al folio.
	public boolean anadirCargo(Cargo cargo) {
		if (cargo == null || cargo.getPrecio() < 0) {
			return false;
		}

		if (cargo.getNombre() == null || cargo.getNombre().trim().isEmpty()) {
			return false;
		}

		if (this.listCargos == null) {
			this.listCargos = new ArrayList<Cargo>();
		}

		this.listCargos.add(cargo);
		return true;
	}

	// Registra una devolución como movimiento para el cálculo del saldo.
	public boolean registrarDevolicion(double monto) {
		if (monto < 0) {
			return false;
		}

		if (monto == 0) {
			return true;
		}

		Pago devolucion = new Pago(monto, MedioDePago.TRANSFERENCIA, LocalDate.now(), this);
		return this.anadirPago(devolucion);
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Pago> getListPagos() {
		return this.listPagos;
	}

	public void setListPagos(ArrayList<Pago> pagos) {
		this.listPagos = pagos;
	}

	public ArrayList<Cargo> getListCargos() {
		return this.listCargos;
	}

	public void setListCargos(ArrayList<Cargo> cargos) {
		this.listCargos = cargos;
	}

	public double getSaldo() {
		return this.saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	// ========== FIN DE GETS Y SETS ========== //
}