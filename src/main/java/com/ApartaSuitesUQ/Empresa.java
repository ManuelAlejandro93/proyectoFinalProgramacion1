package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Gestores.GestorGeneral;

public class Empresa {
	private String nombre;
	private String nit;

	private GestorGeneral theGestorGeneral = new GestorGeneral();

	public Empresa(String nombre, String nit) {
		this.nombre = nombre;
		this.nit = nit;
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if (nombre != null && !nombre.trim().isEmpty()) {
			this.nombre = nombre;
		}
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public GestorGeneral getTheGestorGeneral() {
		return theGestorGeneral;
	}

	public void setTheGestorGeneral(GestorGeneral theGestorGeneral) {
		this.theGestorGeneral = theGestorGeneral;
	}

	// ========== FIN DE GETS Y SETS ========== //
}
