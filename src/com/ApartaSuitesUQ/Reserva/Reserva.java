package com.ApartaSuitesUQ.Reserva;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Estancia.Estancia;

import java.time.LocalDate;

public class Reserva {
    private String codigo;
    private EstadoActual estadoActual;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    private Huesped theHuesped;
    private Folio theFolio;
    private CanalReserva theCanalReserva;
    private Apartamento theApartamento;

    public Reserva(String codigo, Huesped theHuesped, EstadoActual estadoActual, Folio theFolio,
                   CanalReserva canalReserva, Apartamento theApartamento, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.codigo = codigo;
        this.estadoActual = estadoActual;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;

        this.theHuesped = theHuesped;
        this.theFolio = theFolio;
        this.theCanalReserva = canalReserva;
        this.theApartamento = theApartamento;
    }

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

    public void setEstadoActual(EstadoActual estadoActual) {
        if (estadoActual != null) {
            this.estadoActual = estadoActual;
        }
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

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    // ========== FIN DE GETS Y SETS ========== //

    // ========== INICIO DE FUNCIONES DE VALIDACION ========== //

    public boolean puedePasarA(EstadoActual estadoNuevo){
        return false;
    }

    // ========== FIN DE FUNCIONES DE VALIDACION ========== //

    // ========== INICIO DE FUNCIONES AUXILIARES ========== //
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

    public double calcularDevolucion(){
        return 0.0;
    }

    // ========== FIN DE FUNCIONES AUXILIARES ========== //


}
