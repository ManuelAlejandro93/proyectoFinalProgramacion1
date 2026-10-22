import java.time.LocalDate;

public class Pago {
	private long monto;
	private MedioDePago medioDePago;
	private LocalDate fecha;

	public Pago(long monto, MedioDePago medioDePago, LocalDate fecha) {
		this.monto = monto;
		this.medioDePago = medioDePago;
		this.fecha = fecha;
	}

	public long getMonto() {
		return monto;
	}

	public void setMonto(long monto) {
		this.monto = monto;
	}

	public MedioDePago getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(MedioDePago medioDePago) {
		this.medioDePago = medioDePago;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

}
