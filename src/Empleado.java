import java.time.LocalDate;

public class Empleado extends Persona {
	private String usuario;
	private String contraseña;

	public Empleado(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo, int edad,
			String usuario, String contraseña) {
		super(nombre, codigo, fechaNacimiento, telefono, correo, edad);
		this.usuario = usuario;
		this.contraseña = contraseña;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	// String nombrex
	// String codigox
	// LocalDate fechaNacimientox
	// String telefonox
	// String correox
	// int edadx
	// String usuario
	// String contraseña

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "nombre : " + this.getNombre() + "\n";
		mensaje += "codigo : " + this.getCodigo() + "\n";
		mensaje += "fecha de nacimiento : " + this.getFechaNacimiento().toString() + "\n";
		mensaje += "teléfono : " + this.getTelefono() + "\n";
		mensaje += "correo : " + this.getCorreo() + "\n";
		mensaje += "edad : " + this.getEdad() + "\n";
		mensaje += "usuario : " + this.getUsuario() + "\n";
		mensaje += "contraseña : " + this.getContraseña() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}
