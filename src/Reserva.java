public class Reserva {
	private String codigo;
	private Huesped huespedEncargado;
	private Estancia estancia;
	private EstadoActual estadoActual;
	private Folio folio;
	private CanalDeReserva canalDeReserva;

	public Reserva(String codigo, Huesped huespedEncargado, Estancia estancia, EstadoActual estadoActual, Folio folio,
			CanalDeReserva canalDeReserva) {
		this.codigo = codigo;
		this.huespedEncargado = huespedEncargado;
		this.estancia = estancia;
		this.estadoActual = estadoActual;
		this.folio = folio;
		this.canalDeReserva = canalDeReserva;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		if (codigo != null && !codigo.trim().isEmpty()) {
			this.codigo = codigo;
		}
	}

	public Huesped getHuespedEncargado() {
		return huespedEncargado;
	}

	public void setHuespedEncargado(Huesped huespedEncargado) {
		if (huespedEncargado != null) {
			this.huespedEncargado = huespedEncargado;
		}
	}

	public Estancia getEstancia() {
		return estancia;
	}

	public void setEstancia(Estancia estancia) {
		if (estancia != null) {
			this.estancia = estancia;
		}
	}

	public EstadoActual getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(EstadoActual estadoActual) {
		if (estadoActual != null) {
			this.estadoActual = estadoActual;
		}
	}

	public Folio getFolio() {
		return folio;
	}

	public void setFolio(Folio folio) {
		if (folio != null) {
			this.folio = folio;
		}
	}

	public CanalDeReserva getCanalDeReserva() {
		return canalDeReserva;
	}

	public void setCanalDeReserva(CanalDeReserva canalDeReserva) {
		if (canalDeReserva != null) {
			this.canalDeReserva = canalDeReserva;
		}
	}

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "codigo : " + (this.getCodigo() != null ? this.getCodigo() : "N/A") + "\n";
		mensaje += "huespedEncargado : "
				+ (this.getHuespedEncargado() != null ? this.getHuespedEncargado().getNombre() : "N/A") + "\n";

		if (this.getEstancia() != null) {
			mensaje += "estancia : desde: " + this.getEstancia().getFechaEntrada() + " hasta: "
					+ this.getEstancia().getFechaSalida() + "\n";
		} else {
			mensaje += "estancia : N/A" + "\n";
		}

		mensaje += "estado actual : " + (this.getEstadoActual() != null ? this.getEstadoActual() : "N/A") + "\n";
		mensaje += "saldo : " + (this.getFolio() != null ? this.getFolio().calcularSaldo() : "N/A") + "\n";
		mensaje += "canal de reserva : " + (this.getCanalDeReserva() != null ? this.getCanalDeReserva() : "N/A") + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}
