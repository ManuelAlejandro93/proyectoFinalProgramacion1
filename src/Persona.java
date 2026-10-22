import java.time.LocalDate;

public abstract class Persona {
	private String nombre;
	private String codigo;
	private LocalDate fechaNacimiento;
	private String telefono;
	private String correo;
	private int edad;

	public Persona(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo, int edad) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.correo = correo;
		this.edad = edad;
		this.calcularEdad();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	// funciónque sirve para calcular la edad a partir de la fecha de nacimiento.
	public void calcularEdad() {
		LocalDate hoy = LocalDate.now();
		this.edad = hoy.getYear() - this.fechaNacimiento.getYear();

		// rebaja un año si no ha pasado la fecha de cumpleaños
		if (hoy.getMonthValue() < this.fechaNacimiento.getMonthValue() ||
				(hoy.getMonthValue() == this.fechaNacimiento.getMonthValue() &&
						hoy.getDayOfMonth() < this.fechaNacimiento.getDayOfMonth())) {
			this.edad--;
		}
	}
}
