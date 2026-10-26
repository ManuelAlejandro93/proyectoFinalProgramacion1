public class GestorPoliticaCancelacion {
	private PoliticaDeCancelacion politicaDeCancelacion = new PoliticaDeCancelacion(1, 0.5, 1);
	// politica de cancelación
	// 1 - 100% - se devuelve todo
	// 0.5 - 50% - se devuelve la mitad
	// 1 - 100% - No se devuelve nada

	public GestorPoliticaCancelacion() {
	}

	public PoliticaDeCancelacion getPoliticaDeCancelacion() {
		return politicaDeCancelacion;
	}

	public void setPoliticaDeCancelacion(PoliticaDeCancelacion politicaDeCancelacion) {
		this.politicaDeCancelacion = politicaDeCancelacion;
	}

}
