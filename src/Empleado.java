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

}
