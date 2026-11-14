package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Apartamento.Novedad;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorApartamentos;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorHuespedes;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Usuarios.Administrador;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Usuarios.Recepcionista;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class App {

    private static final String TITULO = "ApartaSuitesUQ - Demostración";
    private static final double PORCENTAJE_ANTICIPO = 0.30;

    private static GestorGeneral gestorGeneral;
    private static GestorApartamentos gestorApartamentos;
    private static GestorReservas gestorReservas;
    private static GestorHuespedes gestorHuespedes;
    private static Recepcionista recepcionista;

    // Se lanza cuando el usuario cierra o cancela un diálogo.
    private static class OperacionCanceladaException extends RuntimeException {
    }

    public static void main(String[] args) {
        inicializarGestores();
        cargarDatosDemo();
        mostrarMenuPrincipal();
    }

    // ========== INICIALIZACIÓN ========== //

    // Usa los gestores del GestorGeneral para que todas las clases
    // compartan las mismas listas.
    private static void inicializarGestores() {
        gestorGeneral = new GestorGeneral();
        gestorApartamentos = gestorGeneral.getTheGestorApartamentos();
        gestorReservas = gestorGeneral.getTheGestorReservas();
        gestorHuespedes = gestorGeneral.getTheGestorHuespedes();
    }

    private static void cargarDatosDemo() {
        LocalDate hoy = LocalDate.now();

        // Apartamentos
        Apartamento villaJuliana = new Apartamento("A1", "Villa Juliana", 3, 6,
                new ArrayList<String>(), 150000.0, EstadoOperativo.PREPARADO, "Ninguno");
        Apartamento suitePequena = new Apartamento("A2", "Suite Pequeña", 1, 2,
                new ArrayList<String>(), 80000.0, EstadoOperativo.PREPARADO, "Ninguno");
        Apartamento suiteFamiliar = new Apartamento("B1", "Suite Familiar", 2, 4,
                new ArrayList<String>(), 100000.0, EstadoOperativo.PREPARADO, "Ninguno");

        // A2 queda bloqueado dentro de 10 a 15 días.
        suitePequena.anadirBloqueo(new Bloqueo("Mantenimiento programado",
                hoy.plusDays(10), hoy.plusDays(15), suitePequena));

        gestorApartamentos.crear(villaJuliana);
        gestorApartamentos.crear(suitePequena);
        gestorApartamentos.crear(suiteFamiliar);

        // Huéspedes
        Huesped kevin = new Huesped("Kevin", "KEV001", LocalDate.of(2000, 5, 10),
                "3210000000", "kevin@mail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Huesped laura = new Huesped("Laura", "LAU002", LocalDate.of(1998, 8, 20),
                "3220000000", "laura@mail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Huesped mateo = new Huesped("Mateo", "MAT003", hoy.minusYears(4),
                "3230000000", "mateo@mail.com", new ArrayList<Reserva>(), gestorHuespedes);

        gestorHuespedes.crear(kevin);
        gestorHuespedes.crear(laura);
        gestorHuespedes.crear(mateo);

        // Empleados
        recepcionista = new Recepcionista("Ana", "EMP001", LocalDate.of(1995, 1, 1),
                "3000000000", "ana@mail.com", "ana01", "clave",
                gestorGeneral.getTheGestorEmpleados());
        Administrador administrador = new Administrador("Carlos", "ADM001",
                LocalDate.of(1990, 6, 15), "3010000000", "carlos@mail.com", "carlos01", "clave");

        gestorGeneral.getTheGestorEmpleados().crear(recepcionista);
        gestorGeneral.getTheGestorEmpleados().crear(administrador);

        // Reserva PENDIENTE por portal: sirve para demostrar la confirmación con
        // anticipo.
        ArrayList<Huesped> familiaKevin = new ArrayList<Huesped>();
        familiaKevin.add(kevin);
        familiaKevin.add(laura);
        familiaKevin.add(mateo);
        registrarReservaDemo("R-PEND1", kevin, EstadoActual.PENDIENTE,
                new CanalReserva(NombreCanal.PORTAL, ""), villaJuliana, familiaKevin,
                hoy.plusDays(30), hoy.plusDays(35));

        // Reserva CONFIRMADA que entra hoy: sirve para demostrar el check-in.
        ArrayList<Huesped> soloLaura = new ArrayList<Huesped>();
        soloLaura.add(laura);
        registrarReservaDemo("R-CHK01", laura, EstadoActual.CONFIRMADA,
                new CanalReserva(NombreCanal.DIRECTA, ""), suiteFamiliar, soloLaura,
                hoy, hoy.plusDays(3));

        // Reserva EXTERNA: sirve para demostrar que no se repite el código externo.
        ArrayList<Huesped> soloKevin = new ArrayList<Huesped>();
        soloKevin.add(kevin);
        registrarReservaDemo("R-EXT01", kevin, EstadoActual.CONFIRMADA,
                new CanalReserva(NombreCanal.EXTERNA, "BK-998877"), villaJuliana, soloKevin,
                hoy.plusDays(60), hoy.plusDays(62));
    }

    private static void registrarReservaDemo(String codigo, Huesped titular, EstadoActual estado,
            CanalReserva canal, Apartamento apartamento,
            ArrayList<Huesped> ocupantes,
            LocalDate entrada, LocalDate salida) {
        Estancia estancia = new Estancia(entrada, salida, apartamento, ocupantes);
        Folio folio = crearFolioConEstadia(estancia);
        gestorReservas.crear(new Reserva(codigo, titular, estado, folio, canal,
                apartamento, estancia, gestorReservas));
    }

    // Crea el folio con el valor de la estadía como primer cargo,
    // para que el saldo muestre lo que el huésped debe.
    private static Folio crearFolioConEstadia(Estancia estancia) {
        Folio folio = new Folio();
        folio.anadirCargo(new Cargo("Estadía", estancia.calcularPrecioEstadia(), folio));
        return folio;
    }

    // ========== MENÚ PRINCIPAL ========== //

    private static void mostrarMenuPrincipal() {
        String textoMenu = "MENÚ PRINCIPAL\n\n"
                + "1. Consultar disponibilidad y cotizar\n"
                + "2. Crear reserva (Portal / Directa / Externa)\n"
                + "3. Confirmar reserva pendiente\n"
                + "4. Gestionar folio (pago / cargo / ver)\n"
                + "5. Realizar check-in\n"
                + "6. Realizar check-out\n"
                + "7. Cancelar reserva\n"
                + "8. Gestionar apartamento (bloqueo / novedad / estado)\n"
                + "9. Ver estado del sistema\n"
                + "0. Salir\n\n"
                + "Seleccione una opción:";

        boolean continuar = true;
        while (continuar) {
            String opcion = JOptionPane.showInputDialog(null, textoMenu, TITULO,
                    JOptionPane.QUESTION_MESSAGE);

            // Cerrar el menú con la X o con Cancelar también sale del programa.
            if (opcion == null) {
                opcion = "0";
            }

            try {
                switch (opcion.trim()) {
                    case "1":
                        consultarDisponibilidadYCotizar();
                        break;
                    case "2":
                        crearReserva();
                        break;
                    case "3":
                        confirmarReservaPendiente();
                        break;
                    case "4":
                        gestionarFolio();
                        break;
                    case "5":
                        realizarCheckIn();
                        break;
                    case "6":
                        realizarCheckOut();
                        break;
                    case "7":
                        cancelarReserva();
                        break;
                    case "8":
                        gestionarApartamento();
                        break;
                    case "9":
                        verEstadoDelSistema();
                        break;
                    case "0":
                        mostrarInformacion("Hasta luego.");
                        continuar = false;
                        break;
                    default:
                        mostrarError("Opción no válida. Escriba un número del 0 al 9.");
                }
            } catch (OperacionCanceladaException e) {
                mostrarInformacion("Operación cancelada. Volviendo al menú.");
            } catch (RuntimeException e) {
                mostrarError("Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    // ========== OPCIÓN 1: DISPONIBILIDAD Y COTIZACIÓN ========== //

    private static void consultarDisponibilidadYCotizar() {
        LocalDate[] rango = leerRangoFechas();
        LocalDate entrada = rango[0];
        LocalDate salida = rango[1];

        int personasPagantes = leerEntero("Número de personas de 6 años o más:", 1);
        int menoresDeSeis = leerEntero("Número de menores de 6 años:", 0);
        int totalPersonas = personasPagantes + menoresDeSeis;

        ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(entrada, salida,
                totalPersonas);

        if (disponibles.isEmpty()) {
            mostrarInformacion("No hay apartamentos disponibles del " + entrada + " al " + salida
                    + " para " + totalPersonas + " personas.\n\n"
                    + "Se revisa: capacidad, bloqueos, doble venta y estado fuera de servicio.");
            return;
        }

        // Ocupantes ficticios para cotizar: los menores ocupan cupo pero no pagan.
        ArrayList<Huesped> ocupantesCotizacion = crearOcupantesCotizacion(personasPagantes, menoresDeSeis);

        StringBuilder resultado = new StringBuilder();
        resultado.append("Disponibles del ").append(entrada).append(" al ").append(salida)
                .append("\nPersonas: ").append(totalPersonas)
                .append(" (pagan ").append(personasPagantes).append(")\n\n");

        for (Apartamento apartamento : disponibles) {
            Estancia estanciaCotizada = new Estancia(entrada, salida, apartamento, ocupantesCotizacion);
            resultado.append("- ").append(apartamento.getCodigo()).append(" | ")
                    .append(apartamento.getNombre())
                    .append(" | capacidad: ").append(apartamento.getCapacidad())
                    .append(" | noches: ").append(estanciaCotizada.getNoches())
                    .append(" | total: ").append(formatearDinero(estanciaCotizada.calcularPrecioEstadia()))
                    .append("\n");
        }

        mostrarInformacion(resultado.toString());
    }

    private static ArrayList<Huesped> crearOcupantesCotizacion(int personasPagantes, int menoresDeSeis) {
        ArrayList<Huesped> ocupantes = new ArrayList<Huesped>();
        LocalDate hoy = LocalDate.now();

        for (int i = 0; i < personasPagantes; i++) {
            ocupantes.add(new Huesped("Cotización", "COT-A" + i, hoy.minusYears(30),
                    "", "", new ArrayList<Reserva>(), null));
        }
        for (int i = 0; i < menoresDeSeis; i++) {
            ocupantes.add(new Huesped("Cotización", "COT-M" + i, hoy.minusYears(3),
                    "", "", new ArrayList<Reserva>(), null));
        }
        return ocupantes;
    }

    // ========== OPCIÓN 2: CREAR RESERVA ========== //

    private static void crearReserva() {
        String tipoCanal = leerOpcion("Canal de la reserva (PORTAL / DIRECTA / EXTERNA):",
                "PORTAL", "DIRECTA", "EXTERNA");
        NombreCanal nombreCanal = NombreCanal.valueOf(tipoCanal);

        String codigoExterno = "";
        if (nombreCanal == NombreCanal.EXTERNA) {
            codigoExterno = leerTexto("Código de la reserva en el canal externo (ej: BK-123456):");
            if (gestorReservas.existeCodigoExterno(codigoExterno)) {
                mostrarError("Ya existe una reserva externa con el código " + codigoExterno + ".");
                return;
            }
        }

        Huesped titular = buscarHuesped("Código del huésped titular (KEV001, LAU002, MAT003):");
        if (titular == null) {
            return;
        }

        Apartamento apartamento = buscarApartamento("Código del apartamento (A1, A2, B1):");
        if (apartamento == null) {
            return;
        }

        LocalDate[] rango = leerRangoFechas();
        LocalDate entrada = rango[0];
        LocalDate salida = rango[1];

        ArrayList<Huesped> ocupantes = leerOcupantes(titular);
        if (ocupantes == null) {
            return;
        }

        // Validaciones con un mensaje específico para cada caso.
        if (apartamento.getEstadoOperativo() == EstadoOperativo.FUERA_DE_SERVICIO) {
            mostrarError("El apartamento está fuera de servicio: " + apartamento.getMotivoFueraDeServicio());
            return;
        }
        if (!gestorReservas.validarCapacidadMaxima(apartamento, ocupantes)) {
            mostrarError("Se supera la capacidad del apartamento (" + ocupantes.size()
                    + " personas para una capacidad de " + apartamento.getCapacidad() + ").\n"
                    + "Los menores de 6 años también ocupan cupo.");
            return;
        }
        if (!gestorReservas.validarBloqueos(apartamento, entrada, salida)) {
            mostrarError("El apartamento tiene un bloqueo en esas fechas.");
            return;
        }
        if (!gestorReservas.validarSinDobleVenta(apartamento, entrada, salida, null)) {
            mostrarError("El apartamento ya tiene una reserva activa en esas fechas.");
            return;
        }

        // Portal nace PENDIENTE; Directa y Externa nacen CONFIRMADA.
        EstadoActual estadoInicial = (nombreCanal == NombreCanal.PORTAL)
                ? EstadoActual.PENDIENTE
                : EstadoActual.CONFIRMADA;

        Estancia estancia = new Estancia(entrada, salida, apartamento, ocupantes);
        Folio folio = crearFolioConEstadia(estancia);
        String codigoReserva = gestorReservas.generarCodigoUnico();
        Reserva nuevaReserva = new Reserva(codigoReserva, titular, estadoInicial, folio,
                new CanalReserva(nombreCanal, codigoExterno), apartamento, estancia, gestorReservas);

        if (!gestorReservas.crear(nuevaReserva)) {
            mostrarError("No se pudo registrar la reserva.");
            return;
        }

        double valorTotal = estancia.calcularPrecioEstadia();
        String mensaje = "Reserva creada correctamente.\n\n"
                + "Código: " + codigoReserva + "\n"
                + "Estado: " + estadoInicial + "\n"
                + "Canal: " + nombreCanal + "\n"
                + "Noches: " + estancia.getNoches() + "\n"
                + "Valor total: " + formatearDinero(valorTotal);

        if (estadoInicial == EstadoActual.PENDIENTE) {
            mensaje += "\n\nPara confirmarla debe pagar un anticipo de "
                    + formatearDinero(valorTotal * PORCENTAJE_ANTICIPO) + " (30%).";
        }
        mostrarInformacion(mensaje);
    }

    // Pide los códigos de los acompañantes. El titular siempre se incluye.
    private static ArrayList<Huesped> leerOcupantes(Huesped titular) {
        ArrayList<Huesped> ocupantes = new ArrayList<Huesped>();
        ocupantes.add(titular);

        String respuesta = JOptionPane.showInputDialog(null,
                "Códigos de los acompañantes separados por coma (ej: LAU002,MAT003).\n"
                        + "El titular ya está incluido. Deje vacío si viaja solo.",
                TITULO, JOptionPane.QUESTION_MESSAGE);

        if (respuesta == null) {
            throw new OperacionCanceladaException();
        }
        if (respuesta.trim().isEmpty()) {
            return ocupantes;
        }

        for (String codigo : respuesta.split(",")) {
            String codigoLimpio = codigo.trim();
            if (codigoLimpio.isEmpty()) {
                continue;
            }

            Huesped acompanante = gestorHuespedes.consultar(codigoLimpio);
            if (acompanante == null) {
                mostrarError("No existe un huésped con el código " + codigoLimpio + ".");
                return null;
            }
            if (!ocupantes.contains(acompanante)) {
                ocupantes.add(acompanante);
            }
        }
        return ocupantes;
    }

    // ========== OPCIÓN 3: CONFIRMAR RESERVA ========== //

    private static void confirmarReservaPendiente() {
        Reserva reserva = buscarReserva("Código de la reserva pendiente (ej: R-PEND1):");
        if (reserva == null) {
            return;
        }

        if (reserva.getEstadoActual() != EstadoActual.PENDIENTE) {
            mostrarError("Solo se pueden confirmar reservas PENDIENTES.\nEstado actual: "
                    + reserva.getEstadoActual());
            return;
        }
        if (reserva.getTheCanalReserva() == null
                || reserva.getTheCanalReserva().nombreCanal() != NombreCanal.PORTAL) {
            mostrarError("Solo las reservas del PORTAL requieren confirmación por anticipo.");
            return;
        }

        if (gestorReservas.confirmarReserva(reserva.getCodigo())) {
            mostrarInformacion("Reserva " + reserva.getCodigo() + " confirmada correctamente.");
            return;
        }

        // Explica por qué no se confirmó.
        double anticipoNecesario = reserva.getTheEstancia() != null
                ? reserva.getTheEstancia().calcularPrecioEstadia() * PORCENTAJE_ANTICIPO
                : 0.0;
        double totalPagado = calcularTotalPagos(reserva.getTheFolio());

        mostrarError("No se pudo confirmar la reserva.\n\n"
                + "Anticipo requerido (30%): " + formatearDinero(anticipoNecesario) + "\n"
                + "Total pagado: " + formatearDinero(totalPagado) + "\n\n"
                + "Registre el pago en la opción 4 e intente de nuevo.");
    }

    // ========== OPCIÓN 4: FOLIO ========== //

    private static void gestionarFolio() {
        Reserva reserva = buscarReserva("Código de la reserva:");
        if (reserva == null) {
            return;
        }

        Folio folio = reserva.getTheFolio();
        if (folio == null) {
            mostrarError("La reserva no tiene folio asociado.");
            return;
        }

        String accion = leerOpcion("¿Qué desea hacer? (PAGO / CARGO / VER):", "PAGO", "CARGO", "VER");

        if (accion.equals("PAGO")) {
            double monto = leerMontoPositivo("Monto del pago:");
            String medio = leerOpcion("Medio de pago (EFECTIVO / TARJETA / TRANSFERENCIA):",
                    "EFECTIVO", "TARJETA", "TRANSFERENCIA");
            Pago pago = new Pago(monto, MedioDePago.valueOf(medio), LocalDate.now(), folio);

            if (!recepcionista.registrarPagoPorFolioID(reserva.getCodigo(), pago)) {
                mostrarError("No se pudo registrar el pago.");
                return;
            }
            mostrarInformacion("Pago registrado." + folio);

        } else if (accion.equals("CARGO")) {
            String nombreCargo = leerTexto("Nombre del cargo (ej: Lavandería, Minibar):");
            double monto = leerMontoPositivo("Valor del cargo:");
            Cargo cargo = new Cargo(nombreCargo, monto, folio);

            if (!recepcionista.registrarCargoPorFolioID(reserva.getCodigo(), cargo)) {
                mostrarError("No se pudo registrar el cargo.");
                return;
            }
            mostrarInformacion("Cargo registrado." + folio);

        } else {
            mostrarInformacion("Folio de la reserva " + reserva.getCodigo() + folio);
        }
    }

    // ========== OPCIONES 5 Y 6: CHECK-IN Y CHECK-OUT ========== //

    private static void realizarCheckIn() {
        Reserva reserva = buscarReserva("Código de la reserva confirmada (ej: R-CHK01):");
        if (reserva == null) {
            return;
        }

        if (gestorReservas.realizarCheckIn(reserva.getCodigo())) {
            mostrarInformacion("Check-in realizado.\n\n"
                    + "Reserva: " + reserva.getEstadoActual() + "\n"
                    + "Apartamento: " + reserva.getTheApartamento().getEstadoOperativo());
            return;
        }

        // Explica cuál condición no se cumplió.
        String motivo;
        Apartamento apartamento = reserva.getTheApartamento();
        LocalDate entrada = reserva.getTheEstancia() != null ? reserva.getTheEstancia().getFechaEntrada() : null;

        if (reserva.getEstadoActual() != EstadoActual.CONFIRMADA) {
            motivo = "La reserva debe estar CONFIRMADA (estado actual: " + reserva.getEstadoActual() + ").";
        } else if (entrada == null || LocalDate.now().isBefore(entrada)) {
            motivo = "Aún no llega la fecha de entrada (" + entrada + ").";
        } else if (apartamento == null || apartamento.getEstadoOperativo() != EstadoOperativo.PREPARADO) {
            motivo = "El apartamento debe estar PREPARADO (estado actual: "
                    + (apartamento != null ? apartamento.getEstadoOperativo() : "N/A") + ").\n"
                    + "Puede cambiarlo en la opción 8.";
        } else {
            motivo = "No se cumplen las condiciones para el check-in.";
        }
        mostrarError("No se pudo realizar el check-in.\n\n" + motivo);
    }

    private static void realizarCheckOut() {
        Reserva reserva = buscarReserva("Código de la reserva en curso:");
        if (reserva == null) {
            return;
        }

        if (gestorReservas.realizarCheckOut(reserva.getCodigo())) {
            mostrarInformacion("Check-out realizado.\n\n"
                    + "Reserva: " + reserva.getEstadoActual() + "\n"
                    + "Apartamento: " + reserva.getTheApartamento().getEstadoOperativo() + "\n"
                    + "Saldo final: " + formatearDinero(reserva.getTheFolio().getSaldo()));
        } else {
            mostrarError("No se pudo realizar el check-out.\n"
                    + "La reserva debe estar EN_CURSO (estado actual: " + reserva.getEstadoActual() + ").");
        }
    }

    // ========== OPCIÓN 7: CANCELAR ========== //

    private static void cancelarReserva() {
        Reserva reserva = buscarReserva("Código de la reserva a cancelar:");
        if (reserva == null) {
            return;
        }

        EstadoActual estado = reserva.getEstadoActual();
        if (estado != EstadoActual.PENDIENTE && estado != EstadoActual.CONFIRMADA) {
            mostrarError("Solo se pueden cancelar reservas PENDIENTES o CONFIRMADAS.\n"
                    + "Estado actual: " + estado);
            return;
        }

        // Se calcula antes de cancelar para mostrarlo al usuario.
        double montoDevolucion = reserva.calcularDevolucion();

        if (gestorReservas.cancelarReserva(reserva.getCodigo())) {
            mostrarInformacion("Reserva cancelada.\n\n"
                    + "Devolución según la política de cancelación: "
                    + formatearDinero(montoDevolucion) + reserva.getTheFolio());
        } else {
            mostrarError("No se pudo cancelar la reserva.");
        }
    }

    // ========== OPCIÓN 8: APARTAMENTOS ========== //

    private static void gestionarApartamento() {
        Apartamento apartamento = buscarApartamento("Código del apartamento (A1, A2, B1):");
        if (apartamento == null) {
            return;
        }

        String accion = leerOpcion("¿Qué desea hacer? (BLOQUEO / NOVEDAD / ESTADO):",
                "BLOQUEO", "NOVEDAD", "ESTADO");

        if (accion.equals("BLOQUEO")) {
            String motivo = leerTexto("Motivo del bloqueo:");
            LocalDate inicio = leerFecha("Fecha de inicio del bloqueo (aaaa-mm-dd):");
            LocalDate fin = leerFecha("Fecha final del bloqueo (aaaa-mm-dd):");
            if (fin.isBefore(inicio)) {
                mostrarError("La fecha final no puede ser anterior a la de inicio.");
                return;
            }
            apartamento.anadirBloqueo(new Bloqueo(motivo, inicio, fin, apartamento));
            mostrarInformacion("Bloqueo registrado en " + apartamento.getNombre()
                    + " del " + inicio + " al " + fin + ".");

        } else if (accion.equals("NOVEDAD")) {
            String descripcion = leerTexto("Descripción de la novedad (ej: Lámpara rota):");
            Novedad novedad = new Novedad(descripcion, apartamento);
            if (recepcionista.anadirNovedadEnApartamento(apartamento.getCodigo(), novedad)) {
                mostrarInformacion("Novedad registrada.\nNovedades actuales: "
                        + apartamento.getListNovedades());
            } else {
                mostrarError("No se pudo registrar la novedad.");
            }

        } else {
            String nuevoEstado = leerOpcion(
                    "Nuevo estado (PREPARADO / OCUPADO / PENDIENTE_DE_PREPARACION / FUERA_DE_SERVICIO):",
                    "PREPARADO", "OCUPADO", "PENDIENTE_DE_PREPARACION", "FUERA_DE_SERVICIO");
            EstadoOperativo estado = EstadoOperativo.valueOf(nuevoEstado);

            String motivo = "Ninguno";
            if (estado == EstadoOperativo.FUERA_DE_SERVICIO) {
                motivo = leerTexto("Motivo por el que queda fuera de servicio:");
            }

            apartamento.setEstadoOperativo(estado);
            apartamento.setMotivoFueraDeServicio(motivo);
            mostrarInformacion("Estado de " + apartamento.getNombre() + " actualizado a " + estado + ".");
        }
    }

    // ========== OPCIÓN 9: ESTADO DEL SISTEMA ========== //

    private static void verEstadoDelSistema() {
        StringBuilder resumen = new StringBuilder("=== APARTAMENTOS ===\n");
        for (Apartamento apartamento : gestorApartamentos.getListApartamentos()) {
            resumen.append(apartamento.getCodigo()).append(" | ").append(apartamento.getNombre())
                    .append(" | capacidad: ").append(apartamento.getCapacidad())
                    .append(" | estado: ").append(apartamento.getEstadoOperativo())
                    .append(" | bloqueos: ").append(apartamento.getListBloqueos().size())
                    .append(" | novedades: ").append(apartamento.getListNovedades().size())
                    .append("\n");
        }

        resumen.append("\n=== RESERVAS ===\n");
        if (gestorReservas.getListReservas().isEmpty()) {
            resumen.append("No hay reservas registradas.\n");
        }
        for (Reserva reserva : gestorReservas.getListReservas()) {
            Estancia estancia = reserva.getTheEstancia();
            resumen.append(reserva.getCodigo())
                    .append(" | ").append(reserva.getTheHuesped() != null ? reserva.getTheHuesped().getNombre() : "N/A")
                    .append(" | ")
                    .append(reserva.getTheApartamento() != null ? reserva.getTheApartamento().getCodigo() : "N/A")
                    .append(" | ")
                    .append(estancia != null ? estancia.getFechaEntrada() + " a " + estancia.getFechaSalida() : "N/A")
                    .append(" | ").append(reserva.getEstadoActual())
                    .append(" | ")
                    .append(reserva.getTheCanalReserva() != null ? reserva.getTheCanalReserva().nombreCanal() : "N/A")
                    .append(" | saldo: ")
                    .append(reserva.getTheFolio() != null ? formatearDinero(reserva.getTheFolio().getSaldo()) : "N/A")
                    .append("\n");
        }

        mostrarInformacion(resumen.toString());
    }

    // ========== BÚSQUEDAS ========== //

    private static Reserva buscarReserva(String mensaje) {
        String codigo = leerTexto(mensaje);
        Reserva reserva = gestorReservas.consultar(codigo);
        if (reserva == null) {
            mostrarError("No existe una reserva con el código " + codigo + ".");
        }
        return reserva;
    }

    private static Apartamento buscarApartamento(String mensaje) {
        String codigo = leerTexto(mensaje);
        Apartamento apartamento = gestorApartamentos.consultar(codigo);
        if (apartamento == null) {
            mostrarError("No existe un apartamento con el código " + codigo + ".");
        }
        return apartamento;
    }

    private static Huesped buscarHuesped(String mensaje) {
        String codigo = leerTexto(mensaje);
        Huesped huesped = gestorHuespedes.consultar(codigo);
        if (huesped == null) {
            mostrarError("No existe un huésped con el código " + codigo + ".");
        }
        return huesped;
    }

    // ========== LECTURA DE DATOS ========== //

    // Pide un texto no vacío. Si el usuario cancela, vuelve al menú.
    private static String leerTexto(String mensaje) {
        while (true) {
            String respuesta = JOptionPane.showInputDialog(null, mensaje, TITULO,
                    JOptionPane.QUESTION_MESSAGE);
            if (respuesta == null) {
                throw new OperacionCanceladaException();
            }
            if (!respuesta.trim().isEmpty()) {
                return respuesta.trim();
            }
            mostrarError("El valor no puede estar vacío.");
        }
    }

    private static String leerOpcion(String mensaje, String... opcionesValidas) {
        while (true) {
            String respuesta = leerTexto(mensaje).toUpperCase();
            for (String opcion : opcionesValidas) {
                if (opcion.equals(respuesta)) {
                    return respuesta;
                }
            }
            mostrarError("Opción no válida. Opciones: " + String.join(", ", opcionesValidas));
        }
    }

    private static int leerEntero(String mensaje, int minimo) {
        while (true) {
            try {
                int numero = Integer.parseInt(leerTexto(mensaje));
                if (numero >= minimo) {
                    return numero;
                }
                mostrarError("El número debe ser mayor o igual a " + minimo + ".");
            } catch (NumberFormatException e) {
                mostrarError("Debe escribir un número entero.");
            }
        }
    }

    private static double leerMontoPositivo(String mensaje) {
        while (true) {
            try {
                double monto = Double.parseDouble(leerTexto(mensaje).replace(",", "."));
                if (monto > 0) {
                    return monto;
                }
                mostrarError("El monto debe ser mayor que cero.");
            } catch (NumberFormatException e) {
                mostrarError("Debe escribir un número válido (ej: 150000).");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje + "\nEjemplo: " + LocalDate.now().plusDays(7)));
            } catch (DateTimeParseException e) {
                mostrarError("Formato de fecha inválido. Use aaaa-mm-dd.");
            }
        }
    }

    // Retorna [entrada, salida] garantizando que la salida sea posterior a la
    // entrada.
    private static LocalDate[] leerRangoFechas() {
        while (true) {
            LocalDate entrada = leerFecha("Fecha de entrada (aaaa-mm-dd):");
            LocalDate salida = leerFecha("Fecha de salida (aaaa-mm-dd):");
            if (entrada.isBefore(salida)) {
                return new LocalDate[] { entrada, salida };
            }
            mostrarError("La fecha de salida debe ser posterior a la de entrada.");
        }
    }

    // ========== UTILIDADES ========== //

    private static double calcularTotalPagos(Folio folio) {
        double total = 0.0;
        if (folio != null && folio.getListPagos() != null) {
            for (Pago pago : folio.getListPagos()) {
                if (pago != null) {
                    total += pago.getMonto();
                }
            }
        }
        return total;
    }

    private static String formatearDinero(double valor) {
        return String.format("$%,.2f", valor);
    }

    private static void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, TITULO, JOptionPane.ERROR_MESSAGE);
    }
}