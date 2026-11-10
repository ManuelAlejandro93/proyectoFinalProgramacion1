package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Usuarios.Huesped;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorHuespedes implements IGestor<Huesped, String> {
    private ArrayList<Huesped> listHuespedes = new ArrayList<Huesped>();

    private GestorGeneral theGestorGeneral;

    public GestorHuespedes(GestorGeneral theGestorGeneral) {
        this.theGestorGeneral = theGestorGeneral;
    }

    // ========== INICIO DE CRUD ========== //

    // CREATE - añadir cualquiera.
    @Override
    public boolean crear(Huesped huesped) {
        // Validar duplicados y código
        if (huesped == null || huesped.getCodigo() == null ||
                huesped.getCodigo().trim().isEmpty()) {
            return false; // Código inválido
        }
        if (consultar(huesped.getCodigo()) != null) {
            return false; // Código duplicado
        }
        listHuespedes.add(huesped);
        return true;
    }

    // READ (listar todas como texto, para mostrar en JOptionPane)
    @Override
    public String consultarTodos() {
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
    @Override
    public Huesped consultar(String codigo) {
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
    @Override
    public boolean actualizar(String codigo, Huesped huesped) {

//        String nuevoNombre, LocalDate nuevaFechaNacimiento, String nuevoTelefono, String nuevoCorreo,
//        int nuevaEdad

        if (codigo == null || codigo.trim().isEmpty()) {
            return false; // Código inválido
        }

        Huesped huespedRecuperado = consultar(codigo);
        if (huespedRecuperado == null) {
            return false;
        }

        huespedRecuperado.setNombre(huesped.getNombre());
        huespedRecuperado.setFechaNacimiento(huesped.getFechaNacimiento());
        huespedRecuperado.setTelefono(huesped.getTelefono());
        huespedRecuperado.setCorreo(huesped.getCorreo());
        huespedRecuperado.setEdad(huesped.getEdad());
        return true;
    }

    // DELETE objeto
    @Override
    public boolean borrar(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return false; // Código inválido
        }
        Huesped huespedRecuperado = consultar(codigo);
        if (huespedRecuperado == null) {
            return false;
        } else {
            this.listHuespedes.remove(huespedRecuperado);
            return true;
        }

    }
    // ========== FIN DE CRUD ========== //

    // ========== INICIO DE GETS Y SETS ========== //

    public ArrayList<Huesped> getListHuespedes() {
        return this.listHuespedes;
    }

    public void setListHuespedes(ArrayList<Huesped> listHuespedes) {
        this.listHuespedes = listHuespedes;
    }

    public GestorGeneral getTheGestorGeneral() {
        return theGestorGeneral;
    }

    public void setTheGestorGeneral(GestorGeneral theGestorGeneral) {
        this.theGestorGeneral = theGestorGeneral;
    }

    // ========== FIN DE GETS Y SETS ========== //
}
