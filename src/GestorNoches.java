import java.time.LocalDate;
import java.util.ArrayList;

public class GestorNoches {
	private ArrayList<Noche> noches = new ArrayList<Noche>();

	public ArrayList<Noche> getNoches() {
		return this.noches;
	}

	public void setNoches(ArrayList<Noche> noches) {
		this.noches = noches;
	}

	public GestorNoches() {
	}

	// CREATE - añadir cualquiera.
	public boolean anadirNoche(Noche noche) {
		if (noche == null || noche.getFecha() == null) {
			return false; // Fecha inválida
		}
		if (buscarNochePorFecha(noche.getFecha()) != null) {
			return false; // Fecha duplicada
		}
		noches.add(noche);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodasNoches() {
		String resultado = "";
		if (noches == null || noches.size() == 0) {
			return "No hay noches registradas.";
		}

		for (int i = 0; i < noches.size(); i++) {
			resultado = resultado + noches.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Noche buscarNochePorFecha(LocalDate fecha) {
		if (fecha == null) {
			return null; // Fecha inválida
		}
		Noche resultado = null;
		for (int i = 0; i < this.noches.size(); i++) {
			if (fecha.equals(noches.get(i).getFecha())) {
				resultado = this.noches.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionNoche(
			LocalDate fecha, Temporada nuevaTemporada, double nuevoPrecio) {

		if (fecha == null) {
			return false; // Fecha inválida
		}
		if (nuevaTemporada == null) {
			return false; // Temporada inválida
		}
		if (nuevoPrecio < 0) {
			return false; // Precio inválido
		}

		Noche nocheRecuperada = buscarNochePorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		}

		nocheRecuperada.setTemporada(nuevaTemporada);
		nocheRecuperada.setPrecio(nuevoPrecio);
		return true;
	}

	// DELETE objeto
	public boolean eliminarNoche(LocalDate fecha) {

		if (fecha == null) {
			return false; // Fecha inválida
		}
		Noche nocheRecuperada = buscarNochePorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		} else {
			this.noches.remove(nocheRecuperada);
			return true;
		}
	}
}
