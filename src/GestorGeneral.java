public class GestorGeneral {
	private GestorPoliticaCancelacion gestorPoliticaCancelacion = new GestorPoliticaCancelacion();
	private GestorEmpleados gestorEmpleados = new GestorEmpleados();
	private GestorApartamentos gestorApartamentos = new GestorApartamentos();
	private GestorReservas gestorReservas = new GestorReservas();
	private GestorHuespedes gestorHuespedes = new GestorHuespedes();
	private GestorNoches gestorNoches = new GestorNoches();

	public GestorGeneral() {
	}

	public GestorPoliticaCancelacion getGestorPoliticaCancelacion() {
		return gestorPoliticaCancelacion;
	}

	public void setGestorPoliticaCancelacion(GestorPoliticaCancelacion gestorPoliticaCancelacion) {
		this.gestorPoliticaCancelacion = gestorPoliticaCancelacion;
	}

	public GestorEmpleados getGestorEmpleados() {
		return gestorEmpleados;
	}

	public void setGestorEmpleados(GestorEmpleados gestorEmpleados) {
		this.gestorEmpleados = gestorEmpleados;
	}

	public GestorApartamentos getGestorApartamentos() {
		return gestorApartamentos;
	}

	public void setGestorApartamentos(GestorApartamentos gestorApartamentos) {
		this.gestorApartamentos = gestorApartamentos;
	}

	public GestorReservas getGestorReservas() {
		return gestorReservas;
	}

	public void setGestorReservas(GestorReservas gestorReservas) {
		this.gestorReservas = gestorReservas;
	}

	public GestorHuespedes getGestorHuespedes() {
		return gestorHuespedes;
	}

	public void setGestorHuespedes(GestorHuespedes gestorHuespedes) {
		this.gestorHuespedes = gestorHuespedes;
	}

	public GestorNoches getGestorNoches() {
		return gestorNoches;
	}

	public void setGestorNoches(GestorNoches gestorNoches) {
		this.gestorNoches = gestorNoches;
	}

}