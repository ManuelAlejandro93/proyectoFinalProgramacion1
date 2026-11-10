package com.ApartaSuitesUQ.Apartamento;

import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Reserva.Reserva;

import java.util.ArrayList;

public class Apartamento {
	private String codigo;
	private String nombre;
	private int dormitorios;
	private int capacidad;
	private ArrayList<String> caracteristicas;
	private double tarifaIndividual;
	private EstadoOperativo estadoOperativo;
	private String motivoFueraDeServicio;

	private ArrayList<Novedad> listNovedades;
	private ArrayList<Bloqueo> listBloqueos;
	private ArrayList<Reserva> listReservas;
	private Estancia theEstancia;

	public Apartamento(String codigo, String nombre, int dormitorios, int capacidad, ArrayList<String> caracteristicas,
			double tarifaIndividual, EstadoOperativo estadoOperativo, String motivoFueraDeServicio
			) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.dormitorios = dormitorios;
		this.capacidad = capacidad;
		this.caracteristicas = caracteristicas;
		this.tarifaIndividual = tarifaIndividual;
		this.estadoOperativo = estadoOperativo;
		this.motivoFueraDeServicio = motivoFueraDeServicio;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getDormitorios() {
		return dormitorios;
	}

	public void setDormitorios(int dormitorios) {
		this.dormitorios = dormitorios;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public ArrayList<String> getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(ArrayList<String> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public double getTarifaIndividual() {
		return tarifaIndividual;
	}

	public void setTarifaIndividual(double tarifaIndividual) {
		this.tarifaIndividual = tarifaIndividual;
	}

	public EstadoOperativo getEstadoOperativo() {
		return estadoOperativo;
	}

	public void setEstadoOperativo(EstadoOperativo estadoOperativo) {
		this.estadoOperativo = estadoOperativo;
	}

	public String getMotivoFueraDeServicio() {
		return motivoFueraDeServicio;
	}

	public void setMotivoFueraDeServicio(String motivoFueraDeServicio) {
		this.motivoFueraDeServicio = motivoFueraDeServicio;
	}

	public ArrayList<Novedad> getListNovedades() {
		return listNovedades;
	}

	public void setListNovedades(ArrayList<Novedad> listNovedades) {
		this.listNovedades = listNovedades;
	}

	public ArrayList<Bloqueo> getListBloqueos() {
		return listBloqueos;
	}

	public void setListBloqueos(ArrayList<Bloqueo> listBloqueos) {
		this.listBloqueos = listBloqueos;
	}

	public ArrayList<Reserva> getListReservas() {
		return listReservas;
	}

	public void setListReservas(ArrayList<Reserva> listReservas) {
		this.listReservas = listReservas;
	}

	public Estancia getTheEstancia() {
		return theEstancia;
	}

	public void setTheEstancia(Estancia theEstancia) {
		this.theEstancia = theEstancia;
	}

	// ========== FIN DE GETS Y SETS ========== //

	public void anadirNovedad(Novedad novedad) {
		if (novedad != null) {
			this.listNovedades.add(novedad);
		}
	}

	public void anadirBloqueo(Bloqueo bloqueo) {
		if (bloqueo != null) {
			this.listBloqueos.add(bloqueo);
		}
	}

	public boolean eliminarNovedad(Novedad novedad) {
		return this.listNovedades.remove(novedad);
	}

	public boolean eliminarBloqueo(Bloqueo bloqueo) {
		return this.listBloqueos.remove(bloqueo);
	}


	// ========== INICIO DE FUNCIONES AUXILIARES ========== //

	@Override
	public String toString() {
		String mensaje = "" + "\n";
		mensaje += "===============" + "\n";
		mensaje += "nombre : " + this.getNombre() + "\n";
		mensaje += "codigo : " + this.getCodigo() + "\n";
		mensaje += "dormitorios : " + this.getDormitorios() + "\n";
		mensaje += "capacidad : " + this.getCapacidad() + "\n";
		mensaje += "caracteristicas : " + this.getCaracteristicas() + "\n";
		mensaje += "tarifa individual : " + this.getTarifaIndividual() + "\n";
		mensaje += "estado operativo : " + this.getEstadoOperativo() + "\n";
		mensaje += "motivo fuera de servicio : " + this.getMotivoFueraDeServicio() + "\n";
		mensaje += "listNovedades : " + this.getListNovedades() + "\n";
		mensaje += "bloqueos : " + this.getListBloqueos() + "\n";
		mensaje += "===============" + "\n";

		return mensaje;
	}

	// ========== FIN DE FUNCIONES AUXILIARES ========== //
}