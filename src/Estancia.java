import java.time.LocalDate;
import java.util.ArrayList;

public class Estancia {
	private LocalDate fechaEntrada;
	private LocalDate fechaSalida;
	private ArrayList<Noche> noches = new ArrayList<Noche>();

	public Estancia(LocalDate fechaEntrada, LocalDate fechaSalida) {
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
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

	public ArrayList<Noche> getNoches() {
		return noches;
	}

	public void setNoches(ArrayList<Noche> noches) {
		this.noches = noches;
	}

}
