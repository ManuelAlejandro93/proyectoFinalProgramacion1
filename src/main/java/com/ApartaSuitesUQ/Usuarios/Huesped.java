package com.ApartaSuitesUQ.Usuarios;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorHuespedes;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Gestores.GestorApartamentos;
import com.ApartaSuitesUQ.Reserva.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Huesped extends Persona {
  private String foto = "Foto no ingresada";
  private boolean titular = false;
  private ArrayList<Reserva> listReservas;

  private GestorHuespedes theGestorHuepedes;

  // Gestores
  private GestorGeneral gestorGeneral;
  private GestorReservas gestorReservas;
  private GestorApartamentos gestorApartamentos;

  public Huesped(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
      ArrayList<Reserva> listReservas, GestorHuespedes theGestorHuespedes) {
    super(nombre, codigo, fechaNacimiento, telefono, correo);
    // Si la lista llega null, se inicializa vacía para evitar NPE.
    this.listReservas = (listReservas != null) ? listReservas : new ArrayList<Reserva>();
    this.theGestorHuepedes = theGestorHuespedes;

    // Solo obtiene los gestores si existen, para evitar NPE al construir.
    if (theGestorHuespedes != null) {
      this.gestorGeneral = theGestorHuespedes.getTheGestorGeneral();
      if (this.gestorGeneral != null) {
        this.gestorReservas = this.gestorGeneral.getTheGestorReservas();
        this.gestorApartamentos = this.gestorGeneral.getTheGestorApartamentos();
      }
    }
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
   * @return El método debería devolver información como:
   *         "El apartamento Villa Juliana tiene un precio de 6000000"
   *
   */
  public String cotizarApartamentoPorCodigo(String codigoApartamento, LocalDate fechaEntrada, LocalDate fechaSalida) {
    String res = null;

    // Validación defensiva: se necesita el gestor de apartamentos.
    if (gestorApartamentos == null) {
      return null;
    }

    Apartamento apartamento = gestorApartamentos.consultar(codigoApartamento);

    // Si el apartamento es null o las fechas son inválidas, no se puede cotizar.
    if (apartamento == null || fechaEntrada == null || fechaSalida == null || !fechaEntrada.isBefore(fechaSalida)) {
      return null;
    }

    // Crea una estancia temporal usando los parámetros recibidos.
    Estancia estancia = new Estancia(fechaEntrada, fechaSalida, apartamento, new ArrayList<Huesped>());
    double precioTotal = estancia.calcularPrecioEstadia();

    // Valida que la lista de noches no sea null ni esté vacía antes de acceder a la
    // primera.
    String precioPorNoche = "N/A";
    if (estancia.getListNoches() != null && !estancia.getListNoches().isEmpty()) {
      precioPorNoche = String.valueOf(estancia.getListNoches().get(0).getPrecio());
    }

    res = "Nombre: " + apartamento.getNombre() +
        "\n Fecha Entrada: " + estancia.getFechaEntrada() +
        "\n Fecha Salida: " + estancia.getFechaSalida() +
        "\n Numero Noches: " + estancia.getNoches() +
        "\n Precio por Noche: " + precioPorNoche +
        "\n Precio Total: " + precioTotal;

    return res;
  }

  // todo: corregir esto.

  /**
   *
   * @param huespedes
   * @return devuelve falso o verdadero si se pudo hacer o no la reserva
   */
  public boolean crearReservaEnPortal(ArrayList<Huesped> ocupantes, Apartamento apartamento, LocalDate fechaEntrada,
      LocalDate fechaSalida) {
    // Validación defensiva: sin gestor de reservas u ocupantes no se puede
    // reservar.
    if (gestorReservas == null || ocupantes == null) {
      return false;
    }

    if (gestorReservas.validarCapacidadMaxima(apartamento, ocupantes.size())) {
      String codigoReserva = gestorReservas.generarCodigoUnico();
      if (gestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida, codigoReserva)) {

        Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, apartamento, ocupantes);
        Folio nuevoFolio = new Folio();
        CanalReserva canalExterno = new CanalReserva(NombreCanal.PORTAL, "");

        Reserva nuevaReserva = new Reserva(codigoReserva, this, EstadoActual.PENDIENTE, nuevoFolio, canalExterno,
            apartamento, nuevaEstancia, this.gestorReservas);

        boolean res = gestorReservas.crear(nuevaReserva);

        if (res) {
          // Operacion Exitosa
          return true;
        } else {
          // Operacion Fallida
          return false;
        }
      }
    }

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

  // Descomentar este método (IMPORTANTE)
  public ArrayList<Reserva> consultarReservas() {
    // Garantiza que la lista exista y se limpia para no acumular duplicados.
    if (this.listReservas == null) {
      this.listReservas = new ArrayList<Reserva>();
    }
    this.listReservas.clear();

    // Si no hay gestor o no tiene reservas, se retorna la lista vacía.
    if (gestorReservas == null || gestorReservas.getListReservas() == null) {
      return this.listReservas;
    }

    for (Reserva reserva : gestorReservas.getListReservas()) {
      if (reserva == null) {
        continue;
      }

      Huesped titular = reserva.getTheHuesped();
      if (titular == this) {
        this.listReservas.add(reserva);
      }
    }

    return this.listReservas;
  }

  // todo: corregir esto.

  /**
   *
   * @return el estado de la reserva String
   */
  public String consultarEstadoReserva() {
    return null;
  }

  // todo: corregir esto.

  /**
   *
   * @return saldo en folios
   */
  public double consultarSaldoEnFolios() {
    double saldoTotal = 0.0;
    ArrayList<Reserva> misReservas = consultarReservas();

    for (Reserva reserva : misReservas) {
      if (reserva.getEstadoActual() == EstadoActual.PENDIENTE
          || reserva.getEstadoActual() == EstadoActual.CONFIRMADA
          || reserva.getEstadoActual() == EstadoActual.EN_CURSO) {
        Folio folio = reserva.getTheFolio();
        // Si la reserva no tiene folio, no aporta saldo.
        if (folio == null) {
          continue;
        }
        double saldo = folio.calcularSaldo();
        saldoTotal += saldo;
      }
    }

    return saldoTotal;
  }

  // todo: corregir esto.

  /**
   *
   * @param codigoReserva
   * @return
   */
  public boolean cancelarReservaPorCodigo(String codigoReserva) {
    boolean res = false;

    // Validación defensiva: se necesita el gestor de reservas.
    if (gestorReservas == null) {
      return false;
    }

    Reserva reserva = gestorReservas.consultar(codigoReserva);

    // Si la reserva no existe, no se puede cancelar.
    if (reserva == null) {
      return false;
    }

    if (reserva.getTheHuesped() == this) {
      if (reserva.getEstadoActual() == EstadoActual.PENDIENTE
          || reserva.getEstadoActual() == EstadoActual.CONFIRMADA) {
        res = gestorReservas.cancelarReserva(codigoReserva);
      }
    }

    return res;
  }

  // Muestra los datos personales del huésped, su foto y si es titular.
  @Override
  public String toString() {

    String mensaje = "" + "\n";
    mensaje += "===============" + "\n";
    mensaje += "nombre : " + (this.getNombre() != null ? this.getNombre() : "N/A") + "\n";
    mensaje += "codigo : " + (this.getCodigo() != null ? this.getCodigo() : "N/A") + "\n";
    mensaje += "fecha de nacimiento : " + (this.getFechaNacimiento() != null ? this.getFechaNacimiento() : "N/A")
        + "\n";
    mensaje += "edad : " + this.getEdad() + "\n";
    mensaje += "telefono : " + (this.getTelefono() != null ? this.getTelefono() : "N/A") + "\n";
    mensaje += "correo : " + (this.getCorreo() != null ? this.getCorreo() : "N/A") + "\n";
    mensaje += "foto : " + (this.getFoto() != null ? this.getFoto() : "Foto no ingresada") + "\n";
    mensaje += "titular : " + (this.isTitular() ? "Sí" : "No") + "\n";
    mensaje += "===============" + "\n";

    return mensaje;
  }

  // ========== INICIO DE GETS Y SETS ========== //
  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    if (foto != null && !foto.trim().isEmpty()) {
      this.foto = foto;
    }
  }

  public boolean isTitular() {
    return titular;
  }

  public void setTitular(boolean titular) {
    this.titular = titular;
  }

  public ArrayList<Reserva> getListReservas() {
    return listReservas;
  }

  public void setListReservas(ArrayList<Reserva> listReservas) {
    this.listReservas = listReservas;
  }

  // ========== FIN DE GETS Y SETS ========== //
}