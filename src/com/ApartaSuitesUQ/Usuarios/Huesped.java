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

    //Gestores
    private GestorGeneral gestorGeneral;
    private GestorReservas gestorReservas;
    private GestorApartamentos gestorApartamentos;



  public Huesped(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
      ArrayList<Reserva> listReservas, GestorHuespedes theGestorHuespedes) {
    super(nombre, codigo, fechaNacimiento, telefono, correo);
    this.listReservas = listReservas;
    this.theGestorHuepedes = theGestorHuespedes;
    this.gestorGeneral = theGestorHuespedes.getTheGestorGeneral();
    this.gestorReservas = this.gestorGeneral.getTheGestorReservas();
    this.gestorApartamentos = this.gestorGeneral.getTheGestorApartamentos();
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
     * "El apartamento Villa Juliana tiene un precio de 6000000"
     *
     */
    public String cotizarApartamentoPorCodigo(String codigoApartamento, LocalDate fechaEntrada, LocalDate fechaSalida) {
        String res = null;
        Apartamento apartamento = gestorApartamentos.consultar(codigoApartamento);
        if (apartamento != null) {
            Estancia estancia = apartamento.getTheEstancia();
            double precioTotal = estancia.calcularPrecioEstadia();
            res = "Nombre: " + apartamento.getNombre() +
                    "\n Fecha Entrada: " + estancia.getFechaEntrada() +
                    "\n Fecha Salida: " + estancia.getFechaSalida() +
                    "\n Numero Noches: " + estancia.getNoches() +
                    "\n Precio por Noche: " + estancia.getListNoches().getFirst().getPrecio() +
                    "\n Precio Total: " + precioTotal;
        }

        return res;
    }

    // todo: corregir esto.

    /**
     *
     * @param huespedes
     * @return devuelve falso o verdadero si se pudo hacer o no la reserva
     */
    public boolean crearReservaEnPortal(ArrayList<Huesped> ocupantes, Apartamento apartamento, LocalDate
            fechaEntrada, LocalDate fechaSalida) {
        if (gestorReservas.validarCapacidadMaxima(apartamento, ocupantes.size())) {
            String codigoReserva = gestorReservas.generarCodigoUnico();
            if (gestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida, codigoReserva)) {

                Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, apartamento, ocupantes);
                Folio nuevoFolio = new Folio(0);
                CanalReserva canalExterno = new CanalReserva(NombreCanal.PORTAL, "");

                Reserva nuevaReserva = new Reserva(codigoReserva, this, EstadoActual.PENDIENTE, nuevoFolio, canalExterno, apartamento, nuevaEstancia, this.gestorReservas);

                boolean res = gestorReservas.crear(nuevaReserva);

                if (res) {
                    // Operacion Exitosa
                    return true;
                } else {
                    //Operacion Fallida
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
        for (Reserva reserva : gestorReservas.getListReservas()) {
            Huesped titular = reserva.getTheHuesped();
            if (titular == this) {
                listReservas.add(reserva);
            }
        }

        return listReservas;
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
        Reserva reserva = gestorReservas.consultar(codigoReserva);
        if (reserva.getTheHuesped() == this){
            if(reserva.getEstadoActual() == EstadoActual.PENDIENTE
                    || reserva.getEstadoActual() == EstadoActual.CONFIRMADA){
                res = gestorReservas.cancelarReserva(codigoReserva);
            }
        }

        return res;
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
