import java.time.LocalDate;

public class Bloqueo {
	private String motivo;
	private LocalDate fechaInicio;
	private LocalDate fechafinal;

	public Bloqueo(String motivo, LocalDate fechaInicio, LocalDate fechafinal) {
		this.motivo = motivo;
		this.fechaInicio = fechaInicio;
		this.fechafinal = fechafinal;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechafinal() {
		return fechafinal;
	}

	public void setFechafinal(LocalDate fechafinal) {
		this.fechafinal = fechafinal;
	}

}
