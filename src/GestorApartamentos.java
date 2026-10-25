import java.time.LocalDate;
import java.util.ArrayList;

public class GestorApartamentos {
	private ArrayList<Apartamento> apartamentos = new ArrayList<Apartamento>();

	public ArrayList<Apartamento> getApartamentos() {
		return this.apartamentos;
	}

	public void setApartamentos(ArrayList<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}

	public GestorApartamentos() {
	}

	// CREATE - añadir cualquiera.
	public boolean anadirApartamento(Apartamento apartamento) {
		apartamentos.add(apartamento);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodosApartamentos() {
		String resultado = "";
		if (apartamentos == null || apartamentos.size() == 0) {
			return "No hay apartamentos registrados.";
		}

		for (int i = 0; i < apartamentos.size(); i++) {
			resultado = resultado + apartamentos.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Apartamento buscarApartamentoPorCodigo(String codigo) {
		Apartamento resultado = null;
		for (int i = 0; i < this.apartamentos.size(); i++) {
			if (codigo.equalsIgnoreCase(this.apartamentos.get(i).getCodigo())) {
				resultado = this.apartamentos.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionApartamento(
			String codigo, String nuevoNombre, int nuevosDormitorios, int nuevaCapacidad,
			ArrayList<String> nuevasCaracteristicas,
			int nuevaTarifaIndividual, EstadoOperativo nuevoEstadoOperativo, String nuevoMotivoFueraDeServicio,
			ArrayList<Novedad> nuevoNovedades,
			ArrayList<Bloqueo> nuevoBloqueos) {

		Apartamento apartamentoRecuperado = buscarApartamentoPorCodigo(codigo);
		if (apartamentoRecuperado == null) {
			return false;
		}
		apartamentoRecuperado.setNombre(nuevoNombre);
		apartamentoRecuperado.setDormitorios(nuevosDormitorios);
		apartamentoRecuperado.setCapacidad(nuevaCapacidad);
		apartamentoRecuperado.setCaracteristicas(nuevasCaracteristicas);
		apartamentoRecuperado.setTarifaIndividual(nuevaTarifaIndividual);
		apartamentoRecuperado.setEstadoOperativo(nuevoEstadoOperativo);
		apartamentoRecuperado.setMotivoFueraDeServicio(nuevoMotivoFueraDeServicio);
		apartamentoRecuperado.setNovedades(nuevoNovedades);
		apartamentoRecuperado.setBloqueos(nuevoBloqueos);
		return true;
	}

	// DELETE objeto
	public boolean eliminarApartamento(String codigo) {
		Apartamento apartamentoRecuperado = buscarApartamentoPorCodigo(codigo);
		if (apartamentoRecuperado == null) {
			return false;
		} else {
			this.apartamentos.remove(apartamentoRecuperado);
			return true;
		}
	}
}
