import java.time.LocalDate;
import java.util.ArrayList;

public class GestorHuespedes {
	private ArrayList<Huesped> huespedes = new ArrayList<Huesped>();

	public ArrayList<Huesped> getHuespedes() {
		return this.huespedes;
	}

	public void setHuespedes(ArrayList<Huesped> huespedes) {
		this.huespedes = huespedes;
	}

	public GestorHuespedes() {
	}

	// CREATE - añadir cualquiera.
	public boolean anadirHuesped(Huesped huesped) {
		// Validar duplicados y código
		if (huesped == null || huesped.getCodigo() == null ||
				huesped.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (buscarHuespedPorCodigo(huesped.getCodigo()) != null) {
			return false; // Código duplicado
		}
		huespedes.add(huesped);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodosHuespedes() {
		String resultado = "";
		if (huespedes == null || huespedes.size() == 0) {
			return "No hay huespedes registrados.";
		}

		for (int i = 0; i < huespedes.size(); i++) {
			resultado = resultado + huespedes.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Huesped buscarHuespedPorCodigo(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}
		Huesped resultado = null;
		for (int i = 0; i < this.huespedes.size(); i++) {
			if (codigo.equalsIgnoreCase(this.huespedes.get(i).getCodigo())) {
				resultado = this.huespedes.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionHuesped(
			String nuevoNombre, String codigo, LocalDate nuevaFechaNacimiento, String nuevoTelefono, String nuevoCorreo,
			int nuevaEdad) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Huesped huespedRecuperado = buscarHuespedPorCodigo(codigo);
		if (huespedRecuperado == null) {
			return false;
		}

		huespedRecuperado.setNombre(nuevoNombre);
		huespedRecuperado.setFechaNacimiento(nuevaFechaNacimiento);
		huespedRecuperado.setTelefono(nuevoTelefono);
		huespedRecuperado.setCorreo(nuevoCorreo);
		huespedRecuperado.setEdad(nuevaEdad);
		return true;
	}

	// DELETE objeto
	public boolean eliminarHuesped(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}
		Huesped huespedRecuperado = buscarHuespedPorCodigo(codigo);
		if (huespedRecuperado == null) {
			return false;
		} else {
			this.huespedes.remove(huespedRecuperado);
			return true;
		}
	}
}
