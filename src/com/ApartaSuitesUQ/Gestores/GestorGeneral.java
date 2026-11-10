package com.ApartaSuitesUQ.Gestores;

public class GestorGeneral {
	private GestorPoliticaCancelacion theGestorPoliticaCancelacion = new GestorPoliticaCancelacion();
	private GestorEmpleados theGestorEmpleados = new GestorEmpleados(this);
	private GestorApartamentos theGestorApartamentos = new GestorApartamentos();
	private GestorReservas theGestorReservas = new GestorReservas(this);
	private GestorHuespedes theGestorHuespedes = new GestorHuespedes(this);
	private GestorNoches theGestorNoches = new GestorNoches();

	public GestorGeneral() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public GestorPoliticaCancelacion getTheGestorPoliticaCancelacion() {
		return theGestorPoliticaCancelacion;
	}

	public void setTheGestorPoliticaCancelacion(GestorPoliticaCancelacion theGestorPoliticaCancelacion) {
		this.theGestorPoliticaCancelacion = theGestorPoliticaCancelacion;
	}

	public GestorEmpleados getTheGestorEmpleados() {
		return theGestorEmpleados;
	}

	public void setTheGestorEmpleados(GestorEmpleados theGestorEmpleados) {
		this.theGestorEmpleados = theGestorEmpleados;
	}

	public GestorApartamentos getTheGestorApartamentos() {
		return theGestorApartamentos;
	}

	public void setTheGestorApartamentos(GestorApartamentos theGestorApartamentos) {
		this.theGestorApartamentos = theGestorApartamentos;
	}

	public GestorReservas getTheGestorReservas() {
		return theGestorReservas;
	}

	public void setTheGestorReservas(GestorReservas theGestorReservas) {
		this.theGestorReservas = theGestorReservas;
	}

	public GestorHuespedes getTheGestorHuespedes() {
		return theGestorHuespedes;
	}

	public void setTheGestorHuespedes(GestorHuespedes theGestorHuespedes) {
		this.theGestorHuespedes = theGestorHuespedes;
	}

	public GestorNoches getTheGestorNoches() {
		return theGestorNoches;
	}

	public void setTheGestorNoches(GestorNoches theGestorNoches) {
		this.theGestorNoches = theGestorNoches;
	}

	// ========== FIN DE GETS Y SETS ========== //
}