import java.util.ArrayList;

public class Folio {
	private ArrayList<Pago> pagos = new ArrayList<Pago>();
	private ArrayList<Cargo> cargos = new ArrayList<Cargo>();
	private double saldo = 0;

	public Folio() {
	}

	public Folio(ArrayList<Pago> pagos, ArrayList<Cargo> cargos) {
		this.pagos = pagos;
		this.cargos = cargos;
		calcularSaldo();
	}

	public Folio(ArrayList<Pago> pagos, ArrayList<Cargo> cargos, double saldo) {
		this.pagos = pagos;
		this.cargos = cargos;
		this.saldo = saldo;
	}

	// todo: arreglar esto después, entender bien qué hay que hacer.
	public double calcularSaldo() {
		return 0.0;
	}

	public ArrayList<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(ArrayList<Pago> pagos) {
		this.pagos = pagos;
	}

	public ArrayList<Cargo> getCargos() {
		return cargos;
	}

	public void setCargos(ArrayList<Cargo> cargos) {
		this.cargos = cargos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}
