package com.ApartaSuitesUQ.Reserva;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorPoliticaCancelacion;

import java.time.LocalDate;

public class Reserva {
    private String codigo;
    private EstadoActual estadoActual;

	private Huesped theHuesped;
	private Folio theFolio;
	private CanalReserva theCanalReserva;
	private Apartamento theApartamento;
	private Estancia theEstancia;

    //Gestores
    GestorPoliticaCancelacion gestorPoliticaCancelacion = new GestorPoliticaCancelacion();

	// Permite crear la reserva con su estancia y el gestor de política de
	// cancelación.
	public Reserva(String codigo, Huesped theHuesped, EstadoActual estadoActual, Folio theFolio,
			CanalReserva canalReserva, Apartamento theApartamento, Estancia theEstancia) {
		this.codigo = codigo;
		this.estadoActual = estadoActual;

		this.theHuesped = theHuesped;
		this.theFolio = theFolio;
		this.theCanalReserva = canalReserva;
		this.theApartamento = theApartamento;
		this.theEstancia = theEstancia;
	}

    // ========== INICIO DE FUNCIONES DE VALIDACION ========== //

    // Valida si el estado actual permite pasar al nuevo estado.
    public boolean puedePasarA(EstadoActual nuevoEstado) {
        if (this.estadoActual == null || nuevoEstado == null) {
            return false;
        }

        switch (this.estadoActual) {
            case PENDIENTE:
                return nuevoEstado == EstadoActual.CONFIRMADA
                        || nuevoEstado == EstadoActual.CANCELADA;

            case CONFIRMADA:
                return nuevoEstado == EstadoActual.EN_CURSO
                        || nuevoEstado == EstadoActual.CANCELADA
                        || nuevoEstado == EstadoActual.NO_SHOWN;

            case EN_CURSO:
                return nuevoEstado == EstadoActual.FINALIZADA;

            case FINALIZADA:
            case CANCELADA:
            case NO_SHOWN:
                return false;

            default:
                return false;
        }
    }

    // ========== FIN DE FUNCIONES DE VALIDACION ========== //

    // ========== INICIO DE FUNCIONES AUXILIARES ========== //

    // Calcula la devolución según el valor de la estancia y la política de
    // cancelación.
    public double calcularDevolucion() {
        if (this.theEstancia == null || this.gestorPoliticaCancelacion == null) {
            return 0.0;
        }

        LocalDate fechaEntrada = this.theEstancia.getFechaEntrada();
        double valorTotal = this.theEstancia.calcularPrecioEstadia();

        return this.gestorPoliticaCancelacion.calcularDevolicion(valorTotal, fechaEntrada);
    }

    @Override
    public String toString() {
        String mensaje = "" + "\n";
        mensaje += "===============" + "\n";
        mensaje += "codigo : " + (this.getCodigo() != null ? this.getCodigo() : "N/A") + "\n";
        mensaje += "huespedEncargado : "
                + (this.getTheHuesped() != null ? this.getTheHuesped().getNombre() : "N/A") + "\n";

        mensaje += "estado actual : " + (this.getEstadoActual() != null ? this.getEstadoActual() : "N/A") + "\n";
        mensaje += "saldo : " + (this.getTheFolio() != null ? this.getTheFolio().calcularSaldo() : "N/A") + "\n";
        mensaje += "canal de reserva : " + (this.getTheCanalReserva() != null ? this.getTheCanalReserva() : "N/A") + "\n";
        mensaje += "===============" + "\n";

        return mensaje;
    }

    // ========== FIN DE FUNCIONES AUXILIARES ========== //

    // ========== INICIO DE GETS Y SETS ========== //

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo != null && !codigo.trim().isEmpty()) {
            this.codigo = codigo;
        }
    }

    public EstadoActual getEstadoActual() {
        return estadoActual;
    }

    public boolean setEstadoActual(EstadoActual estadoActual) {
        if (this.puedePasarA(estadoActual)) {
            this.estadoActual = estadoActual;
            return true;
        }
        return false;
    }

    public Huesped getTheHuesped() {
        return theHuesped;
    }

    public void setTheHuesped(Huesped huespedEncargado) {
        if (huespedEncargado != null) {
            this.theHuesped = huespedEncargado;
        }
    }

    public Folio getTheFolio() {
        return theFolio;
    }

    public void setTheFolio(Folio folio) {
        if (folio != null) {
            this.theFolio = folio;
        }
    }

    public CanalReserva getTheCanalReserva() {
        return theCanalReserva;
    }

    public void setTheCanalReserva(CanalReserva canalReserva) {
        if (canalReserva != null) {
            this.theCanalReserva = canalReserva;
        }
    }

    public Apartamento getTheApartamento() {
        return theApartamento;
    }

    public void setTheApartamento(Apartamento theApartamento) {
        this.theApartamento = theApartamento;
    }

    public Estancia getTheEstancia() {
        return this.theEstancia;
    }

    public void setTheEstancia(Estancia theEstancia) {
        this.theEstancia = theEstancia;
    }

    public GestorPoliticaCancelacion getGestorPoliticaCancelacion() {
        return this.gestorPoliticaCancelacion;
    }

    public void setGestorPoliticaCancelacion(GestorPoliticaCancelacion gestorPoliticaCancelacion) {
        this.gestorPoliticaCancelacion = gestorPoliticaCancelacion;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
