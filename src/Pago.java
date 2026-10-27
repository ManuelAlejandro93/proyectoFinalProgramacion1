import java.time.LocalDate;

public class Pago {
	private double monto;
	private MedioDePago medioDePago;
	private LocalDate fecha;

	public Pago(double monto, MedioDePago medioDePago, LocalDate fecha) {
		this.monto = monto;
		this.medioDePago = medioDePago;
		this.fecha = fecha;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		if (monto > 0) {
			this.monto = monto;
		}
	}

	public MedioDePago getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(MedioDePago medioDePago) {
		if (medioDePago != null) {
			this.medioDePago = medioDePago;
		}
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		if (fecha != null && !fecha.isAfter(LocalDate.now())) {
			this.fecha = fecha;
		}
	}

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "monto : " + this.getMonto() + "\n";
		mensaje += "medio de pago : " + this.getMedioDePago() + "\n";
		mensaje += "fecha : " + this.getFecha() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}