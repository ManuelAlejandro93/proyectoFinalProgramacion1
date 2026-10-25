import java.time.LocalDate;
import java.util.ArrayList;

public class Huesped extends Persona {
  private String foto = "Foto no ingresada";

  public Huesped(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo, int edad) {
    super(nombre, codigo, fechaNacimiento, telefono, correo, edad);
  }

  public Huesped(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo, int edad,
      String foto) {
    super(nombre, codigo, fechaNacimiento, telefono, correo, edad);
    this.foto = foto;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  // todo: corregir esto.
  /**
   * 
   * @param fechaCheckIn
   * @param fechaCheckOut
   * @param numeroPersonas
   * @return
   */
  public ArrayList<Apartamento> consultarApartamentos(LocalDate fechaCheckIn, LocalDate fechaCheckOut,
      int numeroPersonas) {

    return null;
  }

  // todo: corregir esto.
  /**
   * 
   * @param codigoApartamento
   * @return
   *         El método debería devolver información como:
   *         "El apartamento Villa Juliana tiene un precio de 6000000"
   * 
   */
  public String cotizarApartamentoPorCodigo(String codigoApartamento) {
    return null;
  }

  // todo: corregir esto.
  /**
   * 
   * @param huespedes
   * @return
   *         devuelve falso o verdadero si se pudo hacer o no la reserva
   */
  public boolean crearReservaEnPortal(ArrayList<Huesped> huespedes) {
    return false;
  }

  // todo: corregir esto.
  /**
   * 
   * @param pago
   * @return
   */
  public boolean pagarAnticipo(Pago pago) {
    return false;
  }

  // todo: corregir esto.
  /**
   * 
   * @param pago
   * @return
   */
  public boolean pagarAbono(Pago pago) {
    return false;
  }

  // todo: important todo.
  // public ArrayList<Reserva> consultarReservas() {
  // return null;
  // }

  // todo: corregir esto.
  /**
   * 
   * @return
   *         el estado de la reserva String
   */
  public String consultarEstadoReserva() {
    return null;
  }

  // todo: corregir esto.
  /**
   * 
   * @return
   *         saldo en folios
   */
  public String consultarSaldoEnFolios() {
    return null;
  }

  // todo: corregir esto.
  /**
   * 
   * @param codigoReserva
   * @return
   */
  public boolean cancelarReservaPorCodigo(String codigoReserva) {
    return false;
  }

  @Override
  public String toString() {

    String mensaje = "" + "\n";
    mensaje += "===============" + "\n";
    mensaje += "codigo : " + this.getCodigo() + "\n";
    mensaje += "fecha de nacimiento : " + this.getFechaNacimiento() + "\n";
    mensaje += "telefono : " + this.getTelefono() + "\n";
    mensaje += "correo : " + this.getCorreo() + "\n";
    mensaje += "edad : " + this.getEdad() + "\n";
    mensaje += "===============" + "\n";

    return mensaje;
  }
}
