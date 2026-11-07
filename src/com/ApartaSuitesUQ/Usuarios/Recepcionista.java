package com.ApartaSuitesUQ.Usuarios;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Novedad;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Estancia.Noche;
import com.ApartaSuitesUQ.Gestores.GestorPoliticaCancelacion;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.CanalReserva;
import com.ApartaSuitesUQ.Reserva.Cargo;
import com.ApartaSuitesUQ.Reserva.EstadoActual;
import com.ApartaSuitesUQ.Reserva.Folio;
import com.ApartaSuitesUQ.Reserva.NombreCanal;
import com.ApartaSuitesUQ.Reserva.Pago;
import com.ApartaSuitesUQ.Reserva.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recepcionista extends Empleado {

	public Recepcionista(String nombre, String codigo, LocalDate fechaNacimiento, String telefono, String correo,
			String usuario, String contrasenia) {
		super(nombre, codigo, fechaNacimiento, telefono, correo, usuario, contrasenia);
	}

	public boolean registrarReservasPorCanalDirecto(Huesped huespedTitular,
			ArrayList<Huesped> ocupantes, Apartamento apartamento,
			LocalDate fechaEntrada, LocalDate fechaSalida, GestorReservas gestorReservas,
			GestorPoliticaCancelacion gestorPoliticaCancelacion) {
		String codigoReserva = gestorReservas.generarCodigoUnico();
		if (gestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida, codigoReserva)) {

			Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, new ArrayList<Noche>(), apartamento);
			Folio nuevoFolio = new Folio(new ArrayList<Pago>(), new ArrayList<Cargo>(), 0.0);
			CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

			Reserva nuevaReserva = new Reserva(codigoReserva, huespedTitular, EstadoActual.CONFIRMADA, nuevoFolio,
					canalDirecto, apartamento, nuevaEstancia, gestorPoliticaCancelacion, fechaEntrada, fechaSalida);

			boolean res = gestorReservas.anadirReserva(nuevaReserva);

			if (res) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// todo: corregir esto.
	public boolean recibirReservasDelCanalExterno(String codigoExterno, Huesped huespedTitular,
			ArrayList<Huesped> ocupantes, Apartamento apartamento,
			LocalDate fechaEntrada, LocalDate fechaSalida, GestorReservas gestorReservas,
			GestorPoliticaCancelacion gestorPoliticaCancelacion) {

		if (!gestorReservas.existeCodigoExterno(codigoExterno)) {
			if (gestorReservas.validarCapacidadMaxima(apartamento, ocupantes.size())) {
				String codigoReserva = gestorReservas.generarCodigoUnico();
				if (gestorReservas.validarSinDobleVenta(apartamento, fechaEntrada, fechaSalida, codigoReserva)) {

					Estancia nuevaEstancia = new Estancia(fechaEntrada, fechaSalida, new ArrayList<Noche>(), apartamento);
					Folio nuevoFolio = new Folio(new ArrayList<Pago>(), new ArrayList<Cargo>(), 0.0);
					CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, codigoExterno);

					Reserva nuevaReserva = new Reserva(codigoReserva, huespedTitular, EstadoActual.CONFIRMADA, nuevoFolio,
							canalExterno, apartamento, nuevaEstancia, gestorPoliticaCancelacion, fechaEntrada, fechaSalida);

					boolean res = gestorReservas.anadirReserva(nuevaReserva);

					if (res) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
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
