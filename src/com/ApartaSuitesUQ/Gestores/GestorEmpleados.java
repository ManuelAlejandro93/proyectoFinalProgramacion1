package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Usuarios.Empleado;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorEmpleados {
	private ArrayList<Empleado> listEmpleados = new ArrayList<Empleado>();

	public GestorEmpleados() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Empleado> getListEmpleados() {
		return this.listEmpleados;
	}

	public void setListEmpleados(ArrayList<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	public boolean anadirEmpleado(Empleado empleado) {
		if (empleado == null || empleado.getCodigo() == null ||
				empleado.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (buscarEmpleadoPorCodigo(empleado.getCodigo()) != null) {
			return false; // Código duplicado
		}
		listEmpleados.add(empleado);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodosEmpleados() {
		String resultado = "";
		if (listEmpleados == null || listEmpleados.isEmpty()) {
			return "No hay empleados registradas.";
		}

		for (int i = 0; i < listEmpleados.size(); i++) {
			resultado = resultado + listEmpleados.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Empleado buscarEmpleadoPorCodigo(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}
		Empleado resultado = null;
		for (int i = 0; i < this.listEmpleados.size(); i++) {
			if (codigo.equalsIgnoreCase(this.listEmpleados.get(i).getCodigo())) {
				resultado = this.listEmpleados.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionEmpleado(
			String nuevoNombre, String codigo, LocalDate nuevaFechaNacimiento, String nuevoTelefono, String nuevoCorreo,
			int nuevaEdad,
			String nuevoUsuario, String nuevaContrasenia) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Empleado empleadoRecuperado = buscarEmpleadoPorCodigo(codigo);
		if (empleadoRecuperado == null) {
			return false;
		}
		empleadoRecuperado.setNombre(nuevoNombre);
		empleadoRecuperado.setFechaNacimiento(nuevaFechaNacimiento);
		empleadoRecuperado.setTelefono(nuevoTelefono);
		empleadoRecuperado.setCorreo(nuevoCorreo);
		empleadoRecuperado.setUsuario(nuevoUsuario);
		empleadoRecuperado.setContrasenia(nuevaContrasenia);
		return true;
	}

	// DELETE objeto
	public boolean eliminarEmpleado(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}
		Empleado empleadoRecuperado = buscarEmpleadoPorCodigo(codigo);
		if (empleadoRecuperado == null) {
			return false;
		} else {
			this.listEmpleados.remove(empleadoRecuperado);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //
}
