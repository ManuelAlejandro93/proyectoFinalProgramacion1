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
		Noche nocheRecuperada = buscarNochePorFecha(fecha);
		if (nocheRecuperada == null) {
			return false;
		} else {
			this.noches.remove(nocheRecuperada);
			return true;
		}
	}
}
