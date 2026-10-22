import java.time.LocalDate;

public class Recepcionista extends Empleado {

	public Recepcionista(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
			int edad, String usuario, String contraseña) {
		super(nombre, codigo, fechaNacimiento, telefono, correo, edad, usuario, contraseña);
	}

	// todo: corregir esto.
	public void registrarReservasPorCanalDirecto() {
	}

	// todo: corregir esto.
	public void recibirReservasDelCanalExterno() {
	}

	// todo: corregir esto.
	public void confirmarReservaPendiente() {
	}

	// todo: corregir esto.
	public void realizarCheckIn() {
	}

	// todo: corregir esto.
	public void realizarCheckOut() {
	}

	// todo: corregir esto.
	public void registrarPagoPorFolioID() {
	}

	// todo: corregir esto.
	public void registrarCargoPorFolioID() {
	}

	// todo: corregir esto.
	public void cambiarEstadoOperativoApartamento() {
	}

	// todo: corregir esto.
	public void anadirNovedadEnApartamento(String codigoApartamento, Novedad novedad) {
	}

	// todo: corregir esto.
	public void declararNoShown() {
	}
}
