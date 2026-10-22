import java.time.LocalDate;

public class Administrador extends Empleado {

	public Administrador(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
			int edad, String usuario, String contraseña) {
		super(nombre, codigo, fechaNacimiento, telefono, correo, edad, usuario, contraseña);
	}

	// todo: corregir esto.
	public void contratarRecepcionista() {
	}

	// todo: corregir esto.
	public void editarDatosRecepcionista() {
	}

	// todo: corregir esto.
	public void eliminarRecepcionista() {
	}

	// todo: corregir esto.
	public void crearApartamento() {
	}

	// todo: corregir esto.
	public void modificarApartamento() {
	}

	// todo: corregir esto.
	public void eliminarApartamento() {
	}

	// todo: corregir esto.
	public void definirFechasTemporada() {
	}

	// todo: corregir esto.
	public void definirTarifaApartamento() {
	}

	// todo: corregir esto.
	public void configurarPoliticaCancelacion() {
	}

	// todo: corregir esto.
	public void registrarBloqueoApartamento() {
	}

	// todo: corregir esto.
	public void consultarCredencialesRecepcionista(String usuarioRecepcionista, String contraseniaRecepcionista) {
	}

	// todo: corregir esto.
	public void consultarOcupacionPorApartamento(String codigoApartamento) {
	}

	// todo: corregir esto.
	public void consultarIngresosPorPeriodo(LocalDate fechaInicio, LocalDate fechaFinal) {
	}

	// todo: corregir esto.
	/**
	 * 
	 * @param nombreCanal
	 * @return
	 *         Reservas Incluidas canceladas y No-shown
	 */
	// public ArrayList<Reserva> consultarReservasPorCanal(NombreCanal nombreCanal)
	// {
	// }

	// todo: corregir esto.
	public void consultarSaldosPendientes() {
	}

	// todo: corregir esto.
	public void consultarApartamentosConMasNovedades() {
	}

}
