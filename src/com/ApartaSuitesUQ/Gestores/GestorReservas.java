package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Usuarios.Huesped;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorReservas implements IGestor<Reserva, String>{
	private ArrayList<Reserva> listReservas = new ArrayList<Reserva>();

	public GestorReservas() {
	}

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	@Override
	public boolean crear(Reserva reserva) {

		if (reserva == null || reserva.getCodigo() == null ||
				reserva.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (consultar(reserva.getCodigo()) != null) {
			return false; // Código duplicado
		}

		listReservas.add(reserva);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	@Override
	public String consultarTodos() {
		String resultado = "";
		if (listReservas == null || listReservas.isEmpty()) {
			return "No hay reservas registradas.";
		}

		for (int i = 0; i < listReservas.size(); i++) {
			resultado = resultado + listReservas.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	@Override
	public Reserva consultar(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}

		Reserva resultado = null;
		for (int i = 0; i < listReservas.size(); i++) {
			if (codigo.equalsIgnoreCase(listReservas.get(i).getCodigo())) {
				resultado = listReservas.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	@Override
	public boolean actualizar(
			String codigo, Reserva reserva) {

//		Huesped nuevoHuespedEncargado, EstadoActual nuevoEstadoActual,
//				Folio nuevoFolio,
//				CanalReserva nuevoCanalReserva

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		if (reserva.getTheHuesped() == null ||
				reserva.getEstadoActual() == null || reserva.getTheFolio() == null ||
				reserva.getTheCanalReserva() == null) {
			return false; // Parámetros inválidos
		}

		Reserva reservaRecuperada = consultar(codigo);
		if (reservaRecuperada == null) {
			return false;
		}
		reservaRecuperada.setTheHuesped(reserva.getTheHuesped());
		reservaRecuperada.setEstadoActual(reserva.getEstadoActual());
		reservaRecuperada.setTheFolio(reserva.getTheFolio());
		reservaRecuperada.setTheCanalReserva(reserva.getTheCanalReserva());

		return true;
	}

	// DELETE objeto
	@Override
	public boolean borrar(String codigo) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Reserva reservaRecuperada = consultar(codigo);
		if (reservaRecuperada == null) {
			return false;
		} else {
			listReservas.remove(reservaRecuperada);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //

	// ========== INICIO DE FUNCIONES DE VALIDACION ========== //
	public boolean validarSinDobleVenta(Apartamento apartamento, LocalDate fechaEntrada, LocalDate fechaSalida){
		return false;
	}

	public boolean existeCodigoExterno(String codigoExterno){
		return false;
	}

	public boolean validarCapacidadMaxima(Apartamento apartamento, ArrayList<Huesped> ocupantes){
		return false;
	}

	// ========== FIN DE FUNCIONES DE VALIDACION ========== //

	// ========== INICIO DE FUNCIONES AUXILIARES ========== //

	public String generarCodigoUnico(){
		return "A";
	}

	public boolean confirmarReserva(String codigoReserva){
		boolean res = false;

		Reserva reserva = consultar(codigoReserva);
		if (reserva != null){
			if (reserva.getEstadoActual() == EstadoActual.PENDIENTE){
				if(reserva.getTheCanalReserva().nombreCanal() == NombreCanal.PORTAL) {
					double valorTotal = reserva.getTheApartamento().getTheEstancia().calcularPrecioEstadia();
					double anticipoNecesario = valorTotal * 0.30;
					double saldoFolio = reserva.getTheFolio().calcularSaldo();

					if (saldoFolio >= anticipoNecesario){
						if (reserva.puedePasarA(EstadoActual.CONFIRMADA)){
							reserva.setEstadoActual(EstadoActual.CONFIRMADA);
							res = true;
						}
					}
				}
			}
		}

		return res;
	}

	public boolean cancelarReserva(String codigoReserva){
		boolean res = false;
		Reserva reserva = consultar(codigoReserva);

		if (reserva.getEstadoActual() == EstadoActual.PENDIENTE || reserva.getEstadoActual() == EstadoActual.CONFIRMADA ){
			if(reserva.puedePasarA(EstadoActual.CANCELADA)){
				double montoDevolucion = reserva.calcularDevolucion();
				boolean resDevolucion = reserva.getTheFolio().registrarDevolucion(montoDevolucion);

				if (resDevolucion){
					reserva.setEstadoActual(EstadoActual.CANCELADA);
					res = true;
				}
			}
		}

		return res;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Reserva> getListReservas() {
		return listReservas;
	}

	public void setListReservas(ArrayList<Reserva> nuevaListReservas) {	listReservas = nuevaListReservas;
	}

	// ========== FIN DE GETS Y SETS ========== //

}
