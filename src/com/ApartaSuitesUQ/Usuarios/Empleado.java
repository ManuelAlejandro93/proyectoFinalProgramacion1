package com.ApartaSuitesUQ.Usuarios;

import java.time.LocalDate;

public class Empleado extends Persona {
	private String usuario;
	private String contrasenia;

	public Empleado(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
			String usuario, String contrasenia) {
		super(nombre, codigo, fechaNacimiento, telefono, correo);
		this.usuario = usuario;
		this.contrasenia = contrasenia;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	// ========== FIN DE GETS Y SETS ========== //

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
		mensaje += "contraseña : " + this.getContrasenia() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}
