package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Apartamento.Novedad;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;

import java.util.ArrayList;

public class GestorApartamentos {
	private ArrayList<Apartamento> listApartamentos = new ArrayList<Apartamento>();

	public GestorApartamentos() {
	}

	// ========== INICIO DE GETS Y SETS ========== //

	public ArrayList<Apartamento> getListApartamentos() {
		return this.listApartamentos;
	}

	public void setListApartamentos(ArrayList<Apartamento> listApartamentos) {
		this.listApartamentos = listApartamentos;
	}

	// ========== FIN DE GETS Y SETS ========== //

	// ========== INICIO DE CRUD ========== //

	// CREATE - añadir cualquiera.
	public boolean anadirApartamento(Apartamento apartamento) {
		if (apartamento == null || apartamento.getCodigo() == null ||
				apartamento.getCodigo().trim().isEmpty()) {
			return false; // Código inválido
		}
		if (buscarApartamentoPorCodigo(apartamento.getCodigo()) != null) {
			return false; // Código duplicado
		}
		listApartamentos.add(apartamento);
		return true;
	}

	// READ (listar todas como texto, para mostrar en JOptionPane)
	// Leer
	public String mostrarTodosApartamentos() {
		String resultado = "";
		if (listApartamentos == null || listApartamentos.isEmpty()) {
			return "No hay apartamentos registrados.";
		}

		for (int i = 0; i < listApartamentos.size(); i++) {
			resultado = resultado + listApartamentos.get(i).toString() + "\n";
		}
		return resultado;
	}

	// READ (buscar por id) devuelve objeto o null
	public Apartamento buscarApartamentoPorCodigo(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return null; // Código inválido
		}
		Apartamento resultado = null;
		for (int i = 0; i < this.listApartamentos.size(); i++) {
			if (codigo.equalsIgnoreCase(this.listApartamentos.get(i).getCodigo())) {
				resultado = this.listApartamentos.get(i);
			}
		}
		return resultado;
	}

	// UPDATE - actualiza objeto
	public boolean actualizarInformacionApartamento(
			String codigo, String nuevoNombre, int nuevosDormitorios, int nuevaCapacidad,
			ArrayList<String> nuevasCaracteristicas,
			double nuevaTarifaIndividual, EstadoOperativo nuevoEstadoOperativo, String nuevoMotivoFueraDeServicio,
			ArrayList<Novedad> nuevoNovedades,
			ArrayList<Bloqueo> nuevoBloqueos) {

		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}

		Apartamento apartamentoRecuperado = buscarApartamentoPorCodigo(codigo);
		if (apartamentoRecuperado == null) {
			return false;
		}
		apartamentoRecuperado.setNombre(nuevoNombre);
		apartamentoRecuperado.setDormitorios(nuevosDormitorios);
		apartamentoRecuperado.setCapacidad(nuevaCapacidad);
		apartamentoRecuperado.setCaracteristicas(nuevasCaracteristicas);
		apartamentoRecuperado.setTarifaIndividual(nuevaTarifaIndividual);
		apartamentoRecuperado.setEstadoOperativo(nuevoEstadoOperativo);
		apartamentoRecuperado.setMotivoFueraDeServicio(nuevoMotivoFueraDeServicio);
		apartamentoRecuperado.setListNovedades(nuevoNovedades);
		apartamentoRecuperado.setListBloqueos(nuevoBloqueos);
		return true;
	}

	// DELETE objeto
	public boolean eliminarApartamento(String codigo) {
		if (codigo == null || codigo.trim().isEmpty()) {
			return false; // Código inválido
		}
		Apartamento apartamentoRecuperado = buscarApartamentoPorCodigo(codigo);
		if (apartamentoRecuperado == null) {
			return false;
		} else {
			this.listApartamentos.remove(apartamentoRecuperado);
			return true;
		}
	}

	// ========== FIN DE CRUD ========== //
}
