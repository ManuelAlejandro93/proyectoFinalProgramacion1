public class GestorPoliticaCancelacion {
	private PoliticaDeCancelacion politicaDeCancelacion = new PoliticaDeCancelacion(1.0, 0.5, 0.0);
	// Política de cancelación:
	// devolucionCompleta: 1.0 (100%) - se devuelve todo
	// devolucionParcial: 0.5 (50%) - se devuelve la mitad
	// sinDevolucion: 0.0 (0%) - No se devuelve nada

	public GestorPoliticaCancelacion() {
	}

	public PoliticaDeCancelacion getPoliticaDeCancelacion() {
		return politicaDeCancelacion;
	}

	public void setPoliticaDeCancelacion(PoliticaDeCancelacion politicaDeCancelacion) {
		this.politicaDeCancelacion = politicaDeCancelacion;
	}

	public boolean actualizarDevolucionCompleta(double nuevaDevolucionCompleta) {
		if (nuevaDevolucionCompleta >= 0 && nuevaDevolucionCompleta <= 1) {
			politicaDeCancelacion.setDevolucionCompleta(nuevaDevolucionCompleta);
			return true;
		}
		return false;
	}

	public boolean actualizarDevolucionParcial(double nuevaDevolucionParcial) {
		if (nuevaDevolucionParcial >= 0 && nuevaDevolucionParcial <= 1) {
			politicaDeCancelacion.setDevolucionParcial(nuevaDevolucionParcial);
			return true;
		}
		return false;
	}

	public boolean actualizarSinDevolucion(double nuevoSinDevolucion) {
		if (nuevoSinDevolucion >= 0 && nuevoSinDevolucion <= 1) {
			politicaDeCancelacion.setSinDevolucion(nuevoSinDevolucion);
			return true;
		}
		return false;
	}

	public double obtenerDevolucionCompleta() {
		return politicaDeCancelacion.getDevolucionCompleta();
	}

	public double obtenerDevolucionParcial() {
		return politicaDeCancelacion.getDevolucionParcial();
	}

	public double obtenerSinDevolucion() {
		return politicaDeCancelacion.getSinDevolucion();
	}
}