package com.ApartaSuitesUQ.Usuarios;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Novedad;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recepcionista extends Empleado {

    public Recepcionista(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo, String usuario, String contrasenia) {
        super(nombre, codigo, fechaNacimiento, telefono, correo, usuario, contrasenia);
    }

    // todo: corregir esto.
    public void registrarReservasPorCanalDirecto(Huesped huespedTitular,
                                                 ArrayList<Huesped> ocupantes, Apartamento apartamento,
                                                 LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (GestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida)) {
            String codigoReserva = GestorReservas.generarCodigoUnico();

//            Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, apartamento);
            Folio nuevoFolio = new Folio(0);
            CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

            Reserva nuevaReserva = new Reserva(codigoReserva, huespedTitular, EstadoActual.CONFIRMADA, nuevoFolio, canalDirecto, apartamento, fechaEntrada, fechaSalida);

            boolean res = GestorReservas.anadirReserva(nuevaReserva);

            if (res) {
                //Operacion Exitosa
            }else{
                //Operacion Fallida
            }
        }
    }

    // todo: corregir esto.
    public void recibirReservasDelCanalExterno(String codigoExterno, Huesped huespedTitular,
                                               ArrayList<Huesped> ocupantes, Apartamento apartamento,
                                               LocalDate fechaEntrada, LocalDate fechaSalida) {

        if (!GestorReservas.existeCodigoExterno(codigoExterno)) {
            if (GestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida)) {
                if (GestorReservas.validarCapacidadMaxima(apartamento, ocupantes)) {
                    String codigoReserva = GestorReservas.generarCodigoUnico();

//                    Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, apartamento);
                    Folio nuevoFolio = new Folio(0);
                    CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, codigoExterno);

                    Reserva nuevaReserva = new Reserva(codigoReserva, huespedTitular, EstadoActual.CONFIRMADA, nuevoFolio, canalExterno, apartamento, fechaEntrada, fechaSalida);

                    boolean res = GestorReservas.anadirReserva(nuevaReserva);

                    if (res){
                        // Operacion Exitosa
                    }else{
                        //Operacion Fallida
                    }
                }
            }
        }
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
