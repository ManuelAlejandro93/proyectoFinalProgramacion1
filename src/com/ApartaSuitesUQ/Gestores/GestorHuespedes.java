package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Usuarios.Huesped;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorHuespedes {
    private ArrayList<Huesped> listHuespedes = new ArrayList<Huesped>();

    public GestorHuespedes() {
    }

    // ========== INICIO DE GETS Y SETS ========== //

    public ArrayList<Huesped> getListHuespedes() {
        return this.listHuespedes;
    }

    public void setListHuespedes(ArrayList<Huesped> listHuespedes) {
        this.listHuespedes = listHuespedes;
    }

    // ========== FIN DE GETS Y SETS ========== //

    // ========== INICIO DE CRUD ========== //

    // CREATE - añadir cualquiera.
    public boolean anadirHuesped(Huesped huesped) {
        // Validar duplicados y código
        if (huesped == null || huesped.getCodigo() == null ||
                huesped.getCodigo().trim().isEmpty()) {
            return false; // Código inválido
        }
        if (buscarHuespedPorCodigo(huesped.getCodigo()) != null) {
            return false; // Código duplicado
        }
        listHuespedes.add(huesped);
        return true;
    }

    // READ (listar todas como texto, para mostrar en JOptionPane)
    // Leer
    public String mostrarTodosHuespedes() {
        String resultado = "";
        if (listHuespedes == null || listHuespedes.isEmpty()) {
            return "No hay huespedes registrados.";
        }

        for (int i = 0; i < listHuespedes.size(); i++) {
            resultado = resultado + listHuespedes.get(i).toString() + "\n";
        }
        return resultado;
    }

    // READ (buscar por id) devuelve objeto o null
    public Huesped buscarHuespedPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null; // Código inválido
        }
        Huesped resultado = null;
        for (int i = 0; i < this.listHuespedes.size(); i++) {
            if (codigo.equalsIgnoreCase(this.listHuespedes.get(i).getCodigo())) {
                resultado = this.listHuespedes.get(i);
            }
        }
        return resultado;
    }

    // UPDATE - actualiza objeto
    public boolean actualizarInformacionHuesped(
            String nuevoNombre, String codigo, LocalDate nuevaFechaNacimiento, String nuevoTelefono, String nuevoCorreo,
            int nuevaEdad) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return false; // Código inválido
        }

        Huesped huespedRecuperado = buscarHuespedPorCodigo(codigo);
        if (huespedRecuperado == null) {
            return false;
        }

        huespedRecuperado.setNombre(nuevoNombre);
        huespedRecuperado.setFechaNacimiento(nuevaFechaNacimiento);
        huespedRecuperado.setTelefono(nuevoTelefono);
        huespedRecuperado.setCorreo(nuevoCorreo);
        huespedRecuperado.setEdad(nuevaEdad);
        return true;
    }

    // DELETE objeto
    public boolean eliminarHuesped(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return false; // Código inválido
        }
        Huesped huespedRecuperado = buscarHuespedPorCodigo(codigo);
        if (huespedRecuperado == null) {
            return false;
        } else {
            this.listHuespedes.remove(huespedRecuperado);
            return true;
        }

    }
    // ========== FIN DE CRUD ========== //
}
