package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Usuarios.Empleado;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorEmpleados implements IGestor<Empleado, String> {
	private ArrayList<Empleado> listEmpleados = new ArrayList<Empleado>();

	private GestorGeneral theGestorGeneral;

	public GestorEmpleados(GestorGeneral theGestorGeneral) {
		this.theGestorGeneral = theGestorGeneral;
	}

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	@Override
	public boolean crear(Empleado empleado) {
		if (empleado == null || empleado.getCodigo() == null ||
				empleado.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (consultar(empleado.getCodigo()) != null) {
			return false; // Código duplicado
		}
		listEmpleados.add(empleado);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	@Override
	public String consultarTodos() {
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
	@Override
	public Empleado consultar(String codigo) {
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
	@Override
	public boolean actualizar(String codigo, Empleado empleado) {

//		String nuevoNombre, LocalDate nuevaFechaNacimiento, String nuevoTelefono, String nuevoCorreo,
//				String nuevoUsuario, String nuevaContrasenia

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Empleado empleadoRecuperado = consultar(codigo);
		if (empleadoRecuperado == null) {
			return false;
		}
		empleadoRecuperado.setNombre(empleado.getNombre());
		empleadoRecuperado.setFechaNacimiento(empleado.getFechaNacimiento());
		empleadoRecuperado.setTelefono(empleado.getTelefono());
		empleadoRecuperado.setCorreo(empleado.getCorreo());
		empleadoRecuperado.setUsuario(empleado.getUsuario());
		empleadoRecuperado.setContrasenia(empleado.getContrasenia());
		return true;
	}

	// DELETE objeto
	@Override
	public boolean borrar(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}
		Empleado empleadoRecuperado = consultar(codigo);
		if (empleadoRecuperado == null) {
			return false;
		} else {
			this.listEmpleados.remove(empleadoRecuperado);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Empleado> getListEmpleados() {
		return this.listEmpleados;
	}

	public void setListEmpleados(ArrayList<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}

	public GestorGeneral getTheGestorGeneral() {
		return theGestorGeneral;
	}

	public void setTheGestorGeneral(GestorGeneral theGestorGeneral) {
		this.theGestorGeneral = theGestorGeneral;
	}

	// ========== FIN DE GETS Y SETS ========== //
}
