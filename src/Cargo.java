public class Cargo {
	private String nombre;
	private double precio;

	public Cargo(String nombre, double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		if (precio >= 0) {
			this.precio = precio;
		}
	}

	@Override
	public String toString() {
		String mensaje = "";
		mensaje += nombre + " : $" + precio;
		return mensaje;
	}
}