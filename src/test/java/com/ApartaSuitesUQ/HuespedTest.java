package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.GestorApartamentos;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorHuespedes;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HuespedTest {

    // ========== PRUEBAS PARA TAREA 6.2: crearReservaEnPortal() ========== //

    @Test
    @DisplayName("Debe retornar true y crear la reserva en estado PENDIENTE con canal PORTAL y titular igual al huésped actual")
    void testCrearReservaEnPortalExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped acompanante = new Huesped("Laura", "456", LocalDate.of(2002, 3, 18), "654", "laura@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);
        ocupantes.add(acompanante);

        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0,
                EstadoOperativo.PREPARADO, "Ninguno");
        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 15);

        // Act
        boolean creada = titular.crearReservaEnPortal(ocupantes, apartamento, entrada, salida);

        // Assert
        assertTrue(creada, "El método debería retornar true cuando la reserva en el portal se crea correctamente");
        assertEquals(1, gestorReservas.getListReservas().size(),
                "Debería haberse registrado exactamente 1 reserva en el gestor");

        Reserva reservaCreada = gestorReservas.getListReservas().get(0);
        assertNotNull(reservaCreada.getCodigo(), "La reserva debe tener un código único generado");
        assertTrue(reservaCreada.getCodigo().startsWith("RES-"), "El código debe iniciar con 'RES-'");
        assertEquals(EstadoActual.PENDIENTE, reservaCreada.getEstadoActual(),
                "Una reserva creada por el portal debe nacer en estado PENDIENTE");
        assertEquals(NombreCanal.PORTAL, reservaCreada.getTheCanalReserva().nombreCanal(),
                "El canal de la reserva debe ser PORTAL");
        assertEquals(titular, reservaCreada.getTheHuesped(),
                "El titular de la reserva debe ser el mismo huésped que invocó el método");
        assertEquals(apartamento, reservaCreada.getTheApartamento(), "El apartamento debe coincidir con el enviado");
        assertEquals(2, reservaCreada.getTheEstancia().getListHuespedes().size(),
                "La estancia debe contener los 2 ocupantes registrados");
    }

    @Test
    @DisplayName("Debe retornar false y no registrar la reserva cuando el número de ocupantes excede la capacidad máxima")
    void testCrearReservaEnPortalExcedeCapacidad() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10), "321", "kevin@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped h2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20), "654", "laura@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped h3 = new Huesped("Mateo", "789", LocalDate.of(2024, 2, 15), "987", "mateo@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);

        // 3 ocupantes (incluyendo menor de 6 años)
        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);
        ocupantes.add(h2);
        ocupantes.add(h3);

        // Apartamento con capacidad máxima de 2 personas (cuarto parámetro = 2)
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 2, new ArrayList<String>(), 20.0,
                EstadoOperativo.PREPARADO, "Ninguno");

        // Act
        boolean creada = titular.crearReservaEnPortal(ocupantes, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Assert
        assertFalse(creada, "Debe retornar false cuando la cantidad de ocupantes supera la capacidad máxima");
        assertTrue(gestorReservas.getListReservas().isEmpty(),
                "No debería haberse registrado ninguna reserva en el gestor");
    }

    @Test
    @DisplayName("Debe retornar false y no registrar la reserva cuando hay doble venta en el mismo apartamento y fechas")
    void testCrearReservaEnPortalConDobleVenta() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular1 = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped titular2 = new Huesped("Carlos", "789", LocalDate.of(1990, 8, 22), "987", "carlos@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);

        ArrayList<Huesped> ocupantes1 = new ArrayList<>();
        ocupantes1.add(titular1);

        ArrayList<Huesped> ocupantes2 = new ArrayList<>();
        ocupantes2.add(titular2);

        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0,
                EstadoOperativo.PREPARADO, "Ninguno");

        // Primera reserva exitosa del 10 al 15 de diciembre
        boolean primeraCreada = titular1.crearReservaEnPortal(ocupantes1, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Act: Segundo huésped intenta reservar el mismo apartamento del 12 al 18 de
        // diciembre
        boolean segundaCreada = titular2.crearReservaEnPortal(ocupantes2, apartamento,
                LocalDate.of(2026, 12, 12), LocalDate.of(2026, 12, 18));

        // Assert
        assertTrue(primeraCreada, "La primera reserva debe crearse exitosamente");
        assertFalse(segundaCreada, "La segunda reserva debe retornar false por solapamiento de fechas");
        assertEquals(1, gestorReservas.getListReservas().size(), "Solo debe existir la primera reserva en el sistema");
        assertEquals(titular1, gestorReservas.getListReservas().get(0).getTheHuesped(),
                "Debe conservarse la reserva del primer titular");
    }

    // ========== PRUEBAS PARA TAREA 8.1: cotizarApartamentoPorCodigo() ========== //

    @Test
    @DisplayName("Debe retornar el resumen formateado con los datos del apartamento, fechas y noches sin registrar ninguna reserva")
    void testCotizarApartamentoPorCodigoExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Apartamento apartamento = new Apartamento("A1", "Suite Panorámica", 2, 4, new ArrayList<String>(), 120.0, EstadoOperativo.PREPARADO, "Ninguno");
        gestorApartamentos.crear(apartamento);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 13); // 3 noches

        // Act
        String cotizacion = huesped.cotizarApartamentoPorCodigo("A1", entrada, salida);

        // Assert
        assertNotNull(cotizacion, "La cotización no debería ser null para un apartamento existente y fechas válidas");
        assertTrue(cotizacion.contains("Nombre: Suite Panorámica"), "Debe incluir el nombre del apartamento");
        assertTrue(cotizacion.contains("Fecha Entrada: " + entrada), "Debe incluir la fecha de entrada");
        assertTrue(cotizacion.contains("Fecha Salida: " + salida), "Debe incluir la fecha de salida");
        assertTrue(cotizacion.contains("Numero Noches: 3"), "Debe calcular y mostrar 3 noches");
        assertTrue(cotizacion.contains("Precio por Noche: "), "Debe incluir la línea de precio por noche");
        assertTrue(cotizacion.contains("Precio Total: "), "Debe incluir la línea de precio total");
        assertTrue(gestorReservas.getListReservas().isEmpty(), "Cotizar es solo una consulta y no debe crear reservas");
    }

    @Test
    @DisplayName("Debe retornar null cuando el código de apartamento no existe o es null")
    void testCotizarApartamentoPorCodigoInexistente() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 13);

        // Act & Assert
        assertNull(huesped.cotizarApartamentoPorCodigo("NO-EXISTE", entrada, salida),
                "Debe retornar null si el apartamento no está registrado en GestorApartamentos");
        assertNull(huesped.cotizarApartamentoPorCodigo(null, entrada, salida),
                "Debe retornar null si el código de apartamento es null");
    }

    @Test
    @DisplayName("Debe retornar null cuando las fechas son nulas o la fecha de entrada no es anterior a la de salida")
    void testCotizarApartamentoPorCodigoFechasInvalidas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Apartamento apartamento = new Apartamento("A1", "Suite Panorámica", 2, 4, new ArrayList<String>(), 120.0, EstadoOperativo.PREPARADO, "Ninguno");
        gestorApartamentos.crear(apartamento);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        LocalDate fecha = LocalDate.of(2026, 12, 10);

        // Act & Assert
        assertNull(huesped.cotizarApartamentoPorCodigo("A1", null, fecha),
                "Debe retornar null si la fecha de entrada es null");
        assertNull(huesped.cotizarApartamentoPorCodigo("A1", fecha, null),
                "Debe retornar null si la fecha de salida es null");
        assertNull(huesped.cotizarApartamentoPorCodigo("A1", fecha, fecha),
                "Debe retornar null si entrada y salida son el mismo día (0 noches)");
        assertNull(huesped.cotizarApartamentoPorCodigo("A1", fecha.plusDays(2), fecha),
                "Debe retornar null si la fecha de entrada es posterior a la de salida");
    }

    // ========== PRUEBAS PARA TAREA 8.2: consultarReservas() ========== //

    @Test
    @DisplayName("Debe retornar todas las reservas del huésped en cualquier estado y excluir las de otros huéspedes")
    void testConsultarReservasHuespedMultiplesEstados() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped huespedObjetivo = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Huesped otroHuesped = new Huesped("Laura", "456", LocalDate.of(2002, 3, 18), "654", "laura@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        Apartamento apartamento1 = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");
        Apartamento apartamento2 = new Apartamento("A2", "Suite 102", 2, 4, new ArrayList<String>(), 120.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantesObjetivo = new ArrayList<>();
        ocupantesObjetivo.add(huespedObjetivo);

        ArrayList<Huesped> ocupantesOtro = new ArrayList<>();
        ocupantesOtro.add(otroHuesped);

        Estancia estancia1 = new Estancia(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), apartamento1, ocupantesObjetivo);
        Estancia estancia2 = new Estancia(LocalDate.of(2026, 12, 5), LocalDate.of(2026, 12, 7), apartamento1, ocupantesObjetivo);
        Estancia estancia3 = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento1, ocupantesObjetivo);
        Estancia estanciaOtro = new Estancia(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), apartamento2, ocupantesOtro);

        // 3 reservas del huésped objetivo en distintos estados
        Reserva rPendiente = new Reserva("RES-801", huespedObjetivo, EstadoActual.PENDIENTE, new Folio(), new CanalReserva(NombreCanal.PORTAL, ""), apartamento1, estancia1, gestorReservas);
        Reserva rConfirmada = new Reserva("RES-802", huespedObjetivo, EstadoActual.CONFIRMADA, new Folio(), new CanalReserva(NombreCanal.DIRECTA, ""), apartamento1, estancia2, gestorReservas);
        Reserva rCancelada = new Reserva("RES-803", huespedObjetivo, EstadoActual.CANCELADA, new Folio(), new CanalReserva(NombreCanal.EXTERNA, "AIR-99"), apartamento1, estancia3, gestorReservas);

        // 1 reserva de otro huésped
        Reserva rDeOtro = new Reserva("RES-804", otroHuesped, EstadoActual.CONFIRMADA, new Folio(), new CanalReserva(NombreCanal.DIRECTA, ""), apartamento2, estanciaOtro, gestorReservas);

        gestorReservas.crear(rPendiente);
        gestorReservas.crear(rConfirmada);
        gestorReservas.crear(rCancelada);
        gestorReservas.crear(rDeOtro);

        // Act
        ArrayList<Reserva> misReservas = huespedObjetivo.consultarReservas();

        // Assert
        assertNotNull(misReservas, "La lista retornada no debe ser null");
        assertEquals(3, misReservas.size(), "Debe retornar exactamente las 3 reservas del huésped en todos sus estados");
        assertTrue(misReservas.contains(rPendiente), "Debe incluir la reserva en estado PENDIENTE");
        assertTrue(misReservas.contains(rConfirmada), "Debe incluir la reserva en estado CONFIRMADA");
        assertTrue(misReservas.contains(rCancelada), "Debe incluir la reserva en estado CANCELADA");
        assertFalse(misReservas.contains(rDeOtro), "No debe incluir reservas pertenecientes a otro huésped");
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando el huésped no tiene reservas asociadas en el sistema")
    void testConsultarReservasSinReservas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped huespedSinReservas = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Huesped otroHuesped = new Huesped("Laura", "456", LocalDate.of(2002, 3, 18), "654", "laura@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");
        ArrayList<Huesped> ocupantesOtro = new ArrayList<>();
        ocupantesOtro.add(otroHuesped);

        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), apartamento, ocupantesOtro);
        Reserva reservaDeOtro = new Reserva("RES-805", otroHuesped, EstadoActual.CONFIRMADA, new Folio(), new CanalReserva(NombreCanal.DIRECTA, ""), apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaDeOtro);

        // Act
        ArrayList<Reserva> misReservas = huespedSinReservas.consultarReservas();

        // Assert
        assertNotNull(misReservas, "Debe retornar una lista instanciada aunque esté vacía");
        assertTrue(misReservas.isEmpty(), "La lista debe estar vacía si el huésped no es titular de ninguna reserva");
    }

    @Test
    @DisplayName("No debe duplicar reservas cuando se invoca consultarReservas() varias veces consecutivas")
    void testConsultarReservasSinDuplicadosEnLlamadasMultiples() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(huesped);

        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), apartamento, ocupantes);
        Reserva reserva = new Reserva("RES-806", huesped, EstadoActual.CONFIRMADA, new Folio(), new CanalReserva(NombreCanal.DIRECTA, ""), apartamento, estancia, gestorReservas);
        gestorReservas.crear(reserva);

        // Act: Llamamos el método dos veces seguidas
        huesped.consultarReservas();
        ArrayList<Reserva> segundaConsulta = huesped.consultarReservas();

        // Assert
        assertEquals(1, segundaConsulta.size(), "Llamar consultarReservas() múltiples veces no debe acumular reservas duplicadas");
    }

    // ========== PRUEBAS PARA TAREA 8.3: consultarSaldoEnFolios() ========== //

    @Test
    @DisplayName("Debe sumar únicamente los saldos de las reservas activas (PENDIENTE, CONFIRMADA, EN_CURSO) e ignorar las inactivas")
    void testConsultarSaldoEnFoliosSoloReservasActivas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Huesped otroHuesped = new Huesped("Laura", "456", LocalDate.of(2002, 3, 18), "654", "laura@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        Apartamento apartamento1 = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");
        Apartamento apartamento2 = new Apartamento("A2", "Suite 102", 2, 4, new ArrayList<String>(), 120.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(huesped);

        ArrayList<Huesped> ocupantesOtro = new ArrayList<>();
        ocupantesOtro.add(otroHuesped);

        // 1. Reserva PENDIENTE con saldo de +150.0 (Cargo 200.0 - Pago 50.0) -> SÍ SUMA
        Folio folioPendiente = new Folio();
        folioPendiente.getListCargos().add(new Cargo("Alojamiento", 200.0, folioPendiente));
        folioPendiente.getListPagos().add(new Pago(50.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folioPendiente));
        Estancia est1 = new Estancia(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 3), apartamento1, ocupantes);
        Reserva rPendiente = new Reserva("RES-901", huesped, EstadoActual.PENDIENTE, folioPendiente, new CanalReserva(NombreCanal.PORTAL, ""), apartamento1, est1, gestorReservas);

        // 2. Reserva CONFIRMADA con saldo de +200.0 -> SÍ SUMA
        Folio folioConfirmada = new Folio();
        folioConfirmada.getListCargos().add(new Cargo("Alojamiento", 200.0, folioConfirmada));
        Estancia est2 = new Estancia(LocalDate.of(2026, 12, 5), LocalDate.of(2026, 12, 7), apartamento1, ocupantes);
        Reserva rConfirmada = new Reserva("RES-902", huesped, EstadoActual.CONFIRMADA, folioConfirmada, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento1, est2, gestorReservas);

        // 3. Reserva EN_CURSO con saldo de +80.0 (Cargo 100.0 - Pago 20.0) -> SÍ SUMA
        Folio folioEnCurso = new Folio();
        folioEnCurso.getListCargos().add(new Cargo("Minibar", 100.0, folioEnCurso));
        folioEnCurso.getListPagos().add(new Pago(20.0, MedioDePago.EFECTIVO, LocalDate.now(), folioEnCurso));
        Estancia est3 = new Estancia(LocalDate.of(2026, 11, 14), LocalDate.of(2026, 11, 16), apartamento1, ocupantes);
        Reserva rEnCurso = new Reserva("RES-903", huesped, EstadoActual.EN_CURSO, folioEnCurso, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento1, est3, gestorReservas);

        // 4. Reservas INACTIVAS con saldos pendientes que NO deben sumarse
        Folio folioFinalizada = new Folio();
        folioFinalizada.getListCargos().add(new Cargo("Alojamiento", 500.0, folioFinalizada));
        Estancia est4 = new Estancia(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 3), apartamento1, ocupantes);
        Reserva rFinalizada = new Reserva("RES-904", huesped, EstadoActual.FINALIZADA, folioFinalizada, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento1, est4, gestorReservas);

        Folio folioCancelada = new Folio();
        folioCancelada.getListCargos().add(new Cargo("Penalidad", 300.0, folioCancelada));
        Estancia est5 = new Estancia(LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 7), apartamento1, ocupantes);
        Reserva rCancelada = new Reserva("RES-905", huesped, EstadoActual.CANCELADA, folioCancelada, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento1, est5, gestorReservas);

        Folio folioNoShow = new Folio();
        folioNoShow.getListCargos().add(new Cargo("No-show", 400.0, folioNoShow));
        Estancia est6 = new Estancia(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 12), apartamento1, ocupantes);
        Reserva rNoShow = new Reserva("RES-906", huesped, EstadoActual.NO_SHOWN, folioNoShow, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento1, est6, gestorReservas);

        // 5. Reserva activa de OTRO huésped -> NO SUMA
        Folio folioOtro = new Folio();
        folioOtro.getListCargos().add(new Cargo("Alojamiento", 999.0, folioOtro));
        Estancia estOtro = new Estancia(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 4), apartamento2, ocupantesOtro);
        Reserva rOtro = new Reserva("RES-907", otroHuesped, EstadoActual.CONFIRMADA, folioOtro, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento2, estOtro, gestorReservas);

        gestorReservas.crear(rPendiente);
        gestorReservas.crear(rConfirmada);
        gestorReservas.crear(rEnCurso);
        gestorReservas.crear(rFinalizada);
        gestorReservas.crear(rCancelada);
        gestorReservas.crear(rNoShow);
        gestorReservas.crear(rOtro);

        // Act
        double saldoTotal = huesped.consultarSaldoEnFolios();

        // Assert: 150.0 + 200.0 + 80.0 = 430.0
        assertEquals(430.0, saldoTotal, 0.001, "Debe sumar únicamente los saldos de las reservas PENDIENTE, CONFIRMADA y EN_CURSO");
    }

    @Test
    @DisplayName("Debe retornar 0.0 cuando el huésped no tiene reservas activas o no tiene reservas registradas")
    void testConsultarSaldoEnFoliosSinReservasActivas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(huesped);

        // Solo tiene una reserva FINALIZADA con saldo
        Folio folioFinalizada = new Folio();
        folioFinalizada.getListCargos().add(new Cargo("Alojamiento", 250.0, folioFinalizada));
        Estancia est = new Estancia(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3), apartamento, ocupantes);
        Reserva rFinalizada = new Reserva("RES-908", huesped, EstadoActual.FINALIZADA, folioFinalizada, new CanalReserva(NombreCanal.DIRECTA, ""), apartamento, est, gestorReservas);
        gestorReservas.crear(rFinalizada);

        // Act
        double saldoTotal = huesped.consultarSaldoEnFolios();

        // Assert
        assertEquals(0.0, saldoTotal, 0.001, "Debe retornar 0.0 cuando ninguna reserva está en estado activo");
    }

    // ========== PRUEBAS PARA TAREA 8.4: cancelarReservaPorCodigo() ========== //

    @Test
    @DisplayName("Debe cancelar exitosamente una reserva CONFIRMADA o PENDIENTE que pertenece al huésped actual")
    void testCancelarReservaPorCodigoPropioExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Estancia estancia = new Estancia(LocalDate.now().plusDays(15), LocalDate.now().plusDays(17), apartamento, ocupantes);
        Folio folio = new Folio();
        Pago pagoAnticipo = new Pago(200.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folio);
        folio.getListPagos().add(pagoAnticipo);

        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");
        Reserva reserva = new Reserva("RES-1001", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reserva);

        // Act
        boolean cancelada = titular.cancelarReservaPorCodigo("RES-1001");

        // Assert
        assertTrue(cancelada, "Debe retornar true cuando el huésped cancela su propia reserva en estado CONFIRMADA");
        assertEquals(EstadoActual.CANCELADA, reserva.getEstadoActual(), "El estado de la reserva debe cambiar a CANCELADA");
    }

    @Test
    @DisplayName("Debe retornar false cuando un huésped intenta cancelar una reserva que pertenece a otro huésped")
    void testCancelarReservaPorCodigoDeOtroHuesped() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped duenoReserva = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Huesped otroHuesped = new Huesped("Laura", "456", LocalDate.of(2002, 3, 18), "654", "laura@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(duenoReserva);

        Estancia estancia = new Estancia(LocalDate.now().plusDays(15), LocalDate.now().plusDays(17), apartamento, ocupantes);
        Folio folio = new Folio();
        Pago pagoAnticipo = new Pago(200.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folio);
        folio.getListPagos().add(pagoAnticipo);

        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");
        Reserva reservaDeKevin = new Reserva("RES-1002", duenoReserva, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaDeKevin);

        // Act: Laura intenta cancelar la reserva de Kevin
        boolean cancelada = otroHuesped.cancelarReservaPorCodigo("RES-1002");

        // Assert
        assertFalse(cancelada, "Un huésped no debe poder cancelar una reserva cuyo titular es otra persona");
        assertEquals(EstadoActual.CONFIRMADA, reservaDeKevin.getEstadoActual(), "La reserva debe mantenerse en estado CONFIRMADA");
    }

    @Test
    @DisplayName("Debe retornar false cuando la reserva no existe o ya está en un estado no cancelable (ej. EN_CURSO)")
    void testCancelarReservaPorCodigoInexistenteOEstadoNoPermitido() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.OCUPADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
        Folio folio = new Folio();
        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

        // Reserva ya en estado EN_CURSO
        Reserva reservaEnCurso = new Reserva("RES-1003", titular, EstadoActual.EN_CURSO, folio, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaEnCurso);

        // Act & Assert
        assertFalse(titular.cancelarReservaPorCodigo("CODIGO-INEXISTENTE"), "Debe retornar false si la reserva no existe");
        assertFalse(titular.cancelarReservaPorCodigo(null), "Debe retornar false si el código es null");
        assertFalse(titular.cancelarReservaPorCodigo("RES-1003"), "No debe permitir cancelar una reserva que ya está EN_CURSO");
        assertEquals(EstadoActual.EN_CURSO, reservaEnCurso.getEstadoActual(), "El estado EN_CURSO no debe alterarse");
    }
}
