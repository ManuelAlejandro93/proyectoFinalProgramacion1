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
		this.codigo = codigo;
	}

	public Huesped getHuespedEncargado() {
		return huespedEncargado;
	}

	public void setHuespedEncargado(Huesped huespedEncargado) {
		this.huespedEncargado = huespedEncargado;
	}

	public Estancia getEstancia() {
		return estancia;
	}

	public void setEstancia(Estancia estancia) {
		this.estancia = estancia;
	}

	public EstadoActual getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(EstadoActual estadoActual) {
		this.estadoActual = estadoActual;
	}

	public Folio getFolio() {
		return folio;
	}

	public void setFolio(Folio folio) {
		this.folio = folio;
	}

	public CanalDeReserva getCanalDeReserva() {
		return canalDeReserva;
	}

	public void setCanalDeReserva(CanalDeReserva canalDeReserva) {
		this.canalDeReserva = canalDeReserva;
	}

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "codigo : " + this.getCodigo() + "\n";
		mensaje += "huespedEncargado : " + this.getHuespedEncargado().getNombre() + "\n";
		mensaje += "estancia : " + "desde: " + this.getEstancia().getFechaEntrada().toString() + " hasta: "
				+ this.getEstancia().getFechaSalida().toString() + "\n";
		mensaje += "estado actual : " + this.getEstadoActual() + "\n";
		mensaje += "saldo : " + this.getFolio().calcularSaldo() + "\n";
		mensaje += "canal de reserva : " + this.getCanalDeReserva() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}
}
