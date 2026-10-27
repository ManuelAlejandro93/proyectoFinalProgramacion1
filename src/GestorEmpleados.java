import java.time.LocalDate;
import java.util.ArrayList;

public class GestorEmpleados {
	private ArrayList<Empleado> empleados = new ArrayList<Empleado>();

	public ArrayList<Empleado> getEmpleados() {
		return this.empleados;
	}

	public void setEmpleados(ArrayList<Empleado> empleados) {
		this.empleados = empleados;
	}

	public GestorEmpleados() {
	}

	// CREATE - añadir cualquiera.
	public boolean anadirEmpleado(Empleado empleado) {
		if (empleado == null || empleado.getCodigo() == null ||
				empleado.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (buscarEmpleadoPorCodigo(empleado.getCodigo()) != null) {
			return false; // Código duplicado
		}
		empleados.add(empleado);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodosEmpleados() {
		String resultado = "";
		if (empleados == null || empleados.size() == 0) {
			return "No hay empleados registradas.";
		}

		for (int i = 0; i < empleados.size(); i++) {
			resultado = resultado + empleados.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Empleado buscarEmpleadoPorCodigo(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}
		Empleado resultado = null;
		for (int i = 0; i < this.empleados.size(); i++) {
			if (codigo.equalsIgnoreCase(this.empleados.get(i).getCodigo())) {
				resultado = this.empleados.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionEmpleado(
			String nuevoNombre, String codigo, LocalDate nuevaFechaNacimiento, String nuevoTelefono, String nuevoCorreo,
			int nuevaEdad,
			String nuevoUsuario, String nuevaContraseña) {

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
		empleadoRecuperado.setContraseña(nuevaContraseña);
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
			this.empleados.remove(empleadoRecuperado);
			return true;
		}
	}
}
