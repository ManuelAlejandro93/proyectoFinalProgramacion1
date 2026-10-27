import java.time.LocalDate;

public class Noche {
	private LocalDate fecha;
	private Temporada temporada;
	private double precio;

	// cada noche debe crearse con todas las especificaciones para evitar
	// confusiones con otras noches.
	public Noche(LocalDate fecha, Temporada temporada, double precio) {
		this.fecha = fecha;
		this.temporada = temporada;
		this.precio = precio;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		if (fecha != null) {
			this.fecha = fecha;
		}
		this.fecha = fecha;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		if (temporada != null) {
			this.temporada = temporada;
		}
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		if (precio >= 0) {
			this.precio = precio;
		}
	}

	@Override
	public String toString() {

		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "fecha : " + this.getFecha().toString() + "\n";
		mensaje += "temporada : " + this.getTemporada().toString() + "\n";
		mensaje += "precio : " + this.getPrecio() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}
