public class PoliticaDeCancelacion {
	private double devolucionCompleta; // 100% - se devuelve todo
	private double devolucionParcial; // 50% - se devuelve la mitad
	private double sinDevolucion; // 0% - No se devuelve nada

	public PoliticaDeCancelacion(double devolucionCompleta, double devolucionParcial,
			double sinDevolucion) {
		this.devolucionCompleta = devolucionCompleta;
		this.devolucionParcial = devolucionParcial;
		this.sinDevolucion = sinDevolucion;
	}

	public double getDevolucionCompleta() {
		return devolucionCompleta;
	}

	public void setDevolucionCompleta(double devolucionCompleta) {
		if (devolucionCompleta >= 0 && devolucionCompleta <= 1) {
			this.devolucionCompleta = devolucionCompleta;
		}
	}

	public double getDevolucionParcial() {
		return devolucionParcial;
	}

	public void setDevolucionParcial(double devolucionParcial) {
		if (devolucionParcial >= 0 && devolucionParcial <= 1) {
			this.devolucionParcial = devolucionParcial;
		}
	}

	public double getSinDevolucion() {
		return sinDevolucion;
	}

	public void setSinDevolucion(double sinDevolucion) {
		if (sinDevolucion >= 0 && sinDevolucion <= 1) {
			this.sinDevolucion = sinDevolucion;
		}
	}
}