public class Novedad {
	private String nombre;

	public Novedad(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if (nombre != null && !nombre.trim().isEmpty()) {
			this.nombre = nombre;
		}
	}

	@Override
	public String toString() {
		return nombre;
	}
}