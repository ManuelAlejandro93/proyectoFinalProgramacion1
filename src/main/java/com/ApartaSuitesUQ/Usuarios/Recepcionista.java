package com.ApartaSuitesUQ.Usuarios;

import com.ApartaSuitesUQ.Reserva.Folio;
import com.ApartaSuitesUQ.Reserva.Reserva;
import com.ApartaSuitesUQ.*;
import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Novedad;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorEmpleados;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recepcionista extends Empleado {

    private GestorEmpleados theGestorEmpleados;
    private GestorGeneral gestorGeneral;
    private GestorReservas gestorReservas;

    //Gestores

	public Recepcionista(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
			String usuario, String contrasenia, GestorEmpleados theGestorEmpleados) {
		super(nombre, codigo, fechaNacimiento, telefono, correo, usuario, contrasenia);
        this.theGestorEmpleados = theGestorEmpleados;
        this.gestorGeneral = theGestorEmpleados.getTheGestorGeneral();
        this.gestorReservas = this.gestorGeneral.getTheGestorReservas();

	}

    // todo: corregir esto.
    public void registrarReservasPorCanalDirecto(Huesped huespedTitular,
                                                 ArrayList<Huesped> ocupantes, Apartamento apartamento,
                                                 LocalDate fechaEntrada, LocalDate fechaSalida) {
        String codigoReserva = gestorReservas.generarCodigoUnico();
        if (gestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida, codigoReserva)) {

            Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, apartamento, ocupantes);
            Folio nuevoFolio = new Folio();
            CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

            Reserva nuevaReserva = new Reserva(codigoReserva, huespedTitular, EstadoActual.CONFIRMADA, nuevoFolio, canalDirecto, apartamento, nuevaEstancia, this.gestorReservas);

            boolean res = gestorReservas.crear(nuevaReserva);

            if (res) {
                //Operacion Exitosa
            } else {
                //Operacion Fallida
            }
        }
    }

    // todo: corregir esto.
    public void recibirReservasDelCanalExterno(String codigoExterno, Huesped huespedTitular,
                                               ArrayList<Huesped> ocupantes, Apartamento apartamento,
                                               LocalDate fechaEntrada, LocalDate fechaSalida) {

        if (!gestorReservas.existeCodigoExterno(codigoExterno)) {
            if (gestorReservas.validarCapacidadMaxima(apartamento, ocupantes.size())) {
                String codigoReserva = gestorReservas.generarCodigoUnico();
                if (gestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida, codigoReserva)) {

                    Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, apartamento, ocupantes);
                    Folio nuevoFolio = new Folio();
                    CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, codigoExterno);

                    Reserva nuevaReserva = new Reserva(codigoReserva, huespedTitular, EstadoActual.CONFIRMADA, nuevoFolio, canalExterno, apartamento, nuevaEstancia, this.gestorReservas);

                    boolean res = gestorReservas.crear(nuevaReserva);

                    if (res) {
                        // Operacion Exitosa
                    } else {
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
