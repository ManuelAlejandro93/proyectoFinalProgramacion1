import java.util.ArrayList;

public class GestorReservas {
	private ArrayList<Reserva> reservas = new ArrayList<Reserva>();

	public ArrayList<Reserva> getReservas() {
		return this.reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}

	public GestorReservas() {
	}

	// CREATE - añadir cualquiera.
	public boolean anadirReserva(Reserva reserva) {

		if (reserva == null || reserva.getCodigo() == null ||
				reserva.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (buscarReservaPorCodigo(reserva.getCodigo()) != null) {
			return false; // Código duplicado
		}

		reservas.add(reserva);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodasReservas() {
		String resultado = "";
		if (reservas == null || reservas.size() == 0) {
			return "No hay reservas registrados.";
		}

		for (int i = 0; i < reservas.size(); i++) {
			resultado = resultado + reservas.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Reserva buscarReservaPorCodigo(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}

		Reserva resultado = null;
		for (int i = 0; i < this.reservas.size(); i++) {
			if (codigo.equalsIgnoreCase(this.reservas.get(i).getCodigo())) {
				resultado = this.reservas.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionReserva(
			String codigo, Huesped nuevoHuespedEncargado, Estancia nuevaEstancia, EstadoActual nuevoEstadoActual,
			Folio nuevoFolio,
			CanalDeReserva nuevoCanalDeReserva) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		if (nuevoHuespedEncargado == null || nuevaEstancia == null ||
				nuevoEstadoActual == null || nuevoFolio == null ||
				nuevoCanalDeReserva == null) {
			return false; // Parámetros inválidos
		}

		Reserva reservaRecuperada = buscarReservaPorCodigo(codigo);
		if (reservaRecuperada == null) {
			return false;
		}
		reservaRecuperada.setHuespedEncargado(nuevoHuespedEncargado);
		reservaRecuperada.setEstancia(nuevaEstancia);
		reservaRecuperada.setEstadoActual(nuevoEstadoActual);
		reservaRecuperada.setFolio(nuevoFolio);
		reservaRecuperada.setCanalDeReserva(nuevoCanalDeReserva);

		return true;
	}

	// DELETE objeto
	public boolean eliminarReserva(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Reserva reservaRecuperada = buscarReservaPorCodigo(codigo);
		if (reservaRecuperada == null) {
			return false;
		} else {
			this.reservas.remove(reservaRecuperada);
			return true;
		}
	}
}
