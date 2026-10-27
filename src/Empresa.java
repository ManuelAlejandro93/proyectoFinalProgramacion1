public class Empresa {
	private String nombre;
	private String nit;
	private GestorGeneral gestorGeneral = new GestorGeneral();

	public Empresa(String nombre, String nit) {
		this.nombre = nombre;
		this.nit = nit;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public GestorGeneral getGestorGeneral() {
		return gestorGeneral;
	}

	public void setGestorGeneral(GestorGeneral gestorGeneral) {
		this.gestorGeneral = gestorGeneral;
	}

}
