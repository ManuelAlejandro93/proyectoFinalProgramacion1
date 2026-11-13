package com.ApartaSuitesUQ.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;

public class Folio {
	private double saldo = 0;

	private ArrayList<Pago> listPagos;
	private ArrayList<Cargo> listCargos;

	public Folio() {
		this.listPagos = new ArrayList<Pago>();
		this.listCargos = new ArrayList<Cargo>();
		calcularSaldo();
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

		// Actualiza el saldo después de agregar el pago.
		this.calcularSaldo();
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

		// Actualiza el saldo después de agregar el cargo.
		this.calcularSaldo();
		return true;
	}

	// Registra una devolución como movimiento para el cálculo del saldo.
	public boolean registrarDevolucion(double monto) {
		if (monto < 0) {
			return false;
		}

		if (monto == 0) {
			return true;
		}

		Pago devolucion = new Pago(monto, MedioDePago.TRANSFERENCIA, LocalDate.now(), this);
		return this.anadirPago(devolucion);
	}

	// ========== INICIO DE FUNCIONES AUXILIARES ========== //

	// Calcula el saldo a partir de los cargos y los pagos del folio.
	public double calcularSaldo() {
		double sumaCargos = 0;
		double sumaPagos = 0;

		if (this.listCargos != null) {
			for (Cargo cargo : this.listCargos) {
				if (cargo != null) {
					sumaCargos += cargo.getPrecio();
				}
			}
		}

		if (this.listPagos != null) {
			for (Pago pago : this.listPagos) {
				if (pago != null) {
					sumaPagos += pago.getMonto();
				}
			}
		}

		this.saldo = sumaCargos - sumaPagos;
		return this.saldo;
	}

	// Muestra el resumen del folio: saldo actual, total de cargos y total de pagos.
	@Override
	public String toString() {
		double totalCargos = 0;
		double totalPagos = 0;

		if (this.listCargos != null) {
			for (Cargo cargo : this.listCargos) {
				if (cargo != null) {
					totalCargos += cargo.getPrecio();
				}
			}
		}

		if (this.listPagos != null) {
			for (Pago pago : this.listPagos) {
				if (pago != null) {
					totalPagos += pago.getMonto();
				}
			}
		}

		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "saldo actual : $" + String.format("%.2f", this.calcularSaldo()) + "\n";
		mensaje += "total de cargos : $" + String.format("%.2f", totalCargos) + "\n";
		mensaje += "total de pagos : $" + String.format("%.2f", totalPagos) + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //

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
		// Recalcula para reflejar cualquier cambio en los cargos o pagos.
		return this.calcularSaldo();
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	// ========== FIN DE GETS Y SETS ========== //
}