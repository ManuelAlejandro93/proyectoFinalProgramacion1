package com.ApartaSuitesUQ.Usuarios;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Huesped extends Persona {
    private String foto = "Foto no ingresada";
    private boolean titular = false;
    private ArrayList<Reserva> listReservas;

    //Gestores
    GestorReservas gestorReservas = new GestorReservas();

    public Huesped(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo) {
        super(nombre, codigo, fechaNacimiento, telefono, correo);
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
    public String cotizarApartamentoPorCodigo(String codigoApartamento) {
        return null;
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

                Reserva nuevaReserva = new Reserva(codigoReserva, this, EstadoActual.PENDIENTE, nuevoFolio, canalExterno, apartamento, nuevaEstancia);

                boolean res = gestorReservas.crear(nuevaReserva);

                if (res) {
                    // Operacion Exitosa
                } else {
                    //Operacion Fallida
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
        // TODO: Implementar - retornar lista de reservas del huésped
        return null;
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
