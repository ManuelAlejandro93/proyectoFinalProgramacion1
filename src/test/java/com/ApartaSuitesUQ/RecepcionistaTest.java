package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Apartamento.Novedad;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Gestores.*;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import com.ApartaSuitesUQ.Usuarios.Recepcionista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RecepcionistaTest {

    // ========== PRUEBAS PARA TAREA 5.1: registrarReservasPorCanalDirecto()
    // ========== //

    @Test
    @DisplayName("Debe registrar la reserva en estado CONFIRMADA con canal DIRECTA cuando no hay doble venta")
    void testRegistrarReservasPorCanalDirectoExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        // Ajusta los parámetros del constructor de Recepcionista según tu clase
        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);

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
        recepcionista.registrarReservasPorCanalDirecto(titular, ocupantes, apartamento, entrada, salida);

        // Assert
        assertEquals(1, gestorReservas.getListReservas().size(),
                "Debería haberse registrado exactamente 1 reserva en el gestor");

        Reserva reservaCreada = gestorReservas.getListReservas().get(0);
        assertNotNull(reservaCreada.getCodigo(), "La reserva debe tener un código único generado");
        assertTrue(reservaCreada.getCodigo().startsWith("RES-"), "El código debe comenzar con 'RES-'");
        assertEquals(EstadoActual.CONFIRMADA, reservaCreada.getEstadoActual(),
                "Una reserva por canal directo debe nacer en estado CONFIRMADA");
        assertEquals(NombreCanal.DIRECTA, reservaCreada.getTheCanalReserva().nombreCanal(),
                "El canal de la reserva debe ser DIRECTA");
        assertEquals(titular, reservaCreada.getTheHuesped(), "El titular debe coincidir con el huésped enviado");
        assertEquals(apartamento, reservaCreada.getTheApartamento(), "El apartamento debe coincidir con el enviado");
        assertEquals(2, reservaCreada.getTheEstancia().getListHuespedes().size(),
                "La estancia debe contener los 2 ocupantes registrados");
    }

    @Test
    @DisplayName("No debe registrar la reserva si ya existe otra reserva activa con fechas solapadas (doble venta)")
    void testRegistrarReservasPorCanalDirectoConDobleVenta() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);

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

        // Primera reserva del 10 al 15 de diciembre
        recepcionista.registrarReservasPorCanalDirecto(titular1, ocupantes1, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Act: Intentamos registrar segunda reserva del 12 al 18 de diciembre (se
        // cruza)
        recepcionista.registrarReservasPorCanalDirecto(titular2, ocupantes2, apartamento,
                LocalDate.of(2026, 12, 12), LocalDate.of(2026, 12, 18));

        // Assert: Solo debe existir la primera reserva
        assertEquals(1, gestorReservas.getListReservas().size(),
                "No debería registrar una segunda reserva si hay solapamiento de fechas");
        assertEquals(titular1, gestorReservas.getListReservas().get(0).getTheHuesped(),
                "La única reserva en el sistema debe ser la primera");
    }

    @Test
    @DisplayName("No debe registrar la reserva cuando las fechas son inválidas (entrada posterior o igual a salida)")
    void testRegistrarReservasPorCanalDirectoFechasInvalidas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0,
                EstadoOperativo.PREPARADO, "Ninguno");

        // Act: Fecha de salida anterior a la fecha de entrada
        recepcionista.registrarReservasPorCanalDirecto(titular, ocupantes, apartamento,
                LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 15));

        // Assert
        assertTrue(gestorReservas.getListReservas().isEmpty(),
                "No debería crear la reserva si el rango de fechas es inválido");
    }

    // ========== PRUEBAS PARA TAREA 6.1: recibirReservasDelCanalExterno()
    // ========== //

    @Test
    @DisplayName("Debe registrar la reserva en estado CONFIRMADA con canal EXTERNA y su código cuando pasa todas las validaciones")
    void testRecibirReservasDelCanalExternoExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);

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
        String codigoExterno = "AIRBNB-884422";

        // Act
        recepcionista.recibirReservasDelCanalExterno(codigoExterno, titular, ocupantes, apartamento, entrada, salida);

        // Assert
        assertEquals(1, gestorReservas.getListReservas().size(),
                "Debería haberse registrado exactamente 1 reserva externa en el gestor");

        Reserva reservaCreada = gestorReservas.getListReservas().get(0);
        assertNotNull(reservaCreada.getCodigo(), "La reserva debe tener un código interno único generado");
        assertTrue(reservaCreada.getCodigo().startsWith("RES-"), "El código interno debe iniciar con 'RES-'");
        assertEquals(EstadoActual.CONFIRMADA, reservaCreada.getEstadoActual(),
                "Una reserva de canal externo debe nacer en estado CONFIRMADA");
        assertEquals(NombreCanal.EXTERNA, reservaCreada.getTheCanalReserva().nombreCanal(),
                "El canal de la reserva debe ser EXTERNA");
        assertEquals(codigoExterno, reservaCreada.getTheCanalReserva().codigoCanal(),
                "El código del canal debe coincidir con el código externo recibido");
        assertEquals(titular, reservaCreada.getTheHuesped(), "El titular debe coincidir con el huésped enviado");
        assertEquals(apartamento, reservaCreada.getTheApartamento(), "El apartamento debe coincidir con el enviado");
        assertEquals(2, reservaCreada.getTheEstancia().getListHuespedes().size(),
                "La estancia debe contener los 2 ocupantes registrados");
    }

    @Test
    @DisplayName("No debe registrar la reserva cuando el código externo ya existe en el sistema")
    void testRecibirReservasDelCanalExternoCodigoDuplicado() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);

        Huesped titular1 = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped titular2 = new Huesped("Carlos", "789", LocalDate.of(1990, 8, 22), "987", "carlos@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);

        ArrayList<Huesped> ocupantes1 = new ArrayList<>();
        ocupantes1.add(titular1);

        ArrayList<Huesped> ocupantes2 = new ArrayList<>();
        ocupantes2.add(titular2);

        // Dos apartamentos distintos y fechas distintas, pero el MISMO código externo
        Apartamento apartamento1 = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0,
                EstadoOperativo.PREPARADO, "Ninguno");
        Apartamento apartamento2 = new Apartamento("B2", "Piso 2 apart 10", 2, 4, new ArrayList<String>(), 25.0,
                EstadoOperativo.PREPARADO, "Ninguno");
        String codigoExternoRepetido = "BOOKING-554433";

        recepcionista.recibirReservasDelCanalExterno(codigoExternoRepetido, titular1, ocupantes1, apartamento1,
                LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5));

        // Act: Intentamos registrar otra reserva con el mismo código externo
        recepcionista.recibirReservasDelCanalExterno(codigoExternoRepetido, titular2, ocupantes2, apartamento2,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Assert
        assertEquals(1, gestorReservas.getListReservas().size(),
                "No debería registrar una segunda reserva con un código externo ya existente");
        assertEquals(titular1, gestorReservas.getListReservas().get(0).getTheHuesped(),
                "La única reserva en el sistema debe ser la primera");
    }

    @Test
    @DisplayName("No debe registrar la reserva cuando el número de ocupantes supera la capacidad máxima del apartamento")
    void testRecibirReservasDelCanalExternoExcedeCapacidad() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);

        Huesped h1 = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10), "321", "kevin@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped h2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20), "654", "laura@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);
        Huesped h3 = new Huesped("Mateo", "789", LocalDate.of(2024, 2, 15), "987", "mateo@gmail.com",
                new ArrayList<Reserva>(), gestorHuespedes);

        // 3 ocupantes (incluyendo menor de 6 años)
        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(h1);
        ocupantes.add(h2);
        ocupantes.add(h3);

        // Apartamento con capacidad máxima de 2 personas (cuarto parámetro = 2)
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 2, new ArrayList<String>(), 20.0,
                EstadoOperativo.PREPARADO, "Ninguno");

        // Act
        recepcionista.recibirReservasDelCanalExterno("AIRBNB-999111", h1, ocupantes, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Assert
        assertTrue(gestorReservas.getListReservas().isEmpty(),
                "No debería registrar la reserva externa porque 3 ocupantes superan la capacidad máxima de 2");
    }

    @Test
    @DisplayName("No debe registrar la reserva externa cuando hay doble venta en el mismo apartamento y fechas")
    void testRecibirReservasDelCanalExternoConDobleVenta() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567",
                "ana@apartasuites.com", "insano", "123", gestorEmpleados);

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

        // Primera reserva del 10 al 15 de diciembre
        recepcionista.recibirReservasDelCanalExterno("AIRBNB-1001", titular1, ocupantes1, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Act: Segunda reserva externa con otro código pero fechas solapadas (12 al 18
        // de diciembre)
        recepcionista.recibirReservasDelCanalExterno("BOOKING-2002", titular2, ocupantes2, apartamento,
                LocalDate.of(2026, 12, 12), LocalDate.of(2026, 12, 18));

        // Assert
        assertEquals(1, gestorReservas.getListReservas().size(),
                "No debería registrar la reserva externa si existe cruce de fechas en el apartamento");
        assertEquals("AIRBNB-1001", gestorReservas.getListReservas().get(0).getTheCanalReserva().codigoCanal(),
                "Debe mantenerse únicamente la primera reserva registrada");
    }

    // ========== PRUEBAS PARA TAREA 9.1: registrarPagoPorFolioID() ========== //

    @Test
    @DisplayName("Debe retornar true y agregar el pago al folio de la reserva cuando ambos son válidos")
    void testRegistrarPagoPorFolioIDExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, ocupantes);
        Folio folio = new Folio();
        folio.getListCargos().add(new Cargo("Alojamiento", 200.0, folio));

        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");
        Reserva reserva = new Reserva("RES-1101", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reserva);

        Pago pago = new Pago(80.0, MedioDePago.EFECTIVO, LocalDate.now(), folio);

        // Act
        boolean registrado = recepcionista.registrarPagoPorFolioID("RES-1101", pago);

        // Assert
        assertTrue(registrado, "Debe retornar true cuando se registra un pago válido en una reserva existente");
        assertEquals(1, folio.getListPagos().size(), "El folio debe contener exactamente 1 pago registrado");
        assertTrue(folio.getListPagos().contains(pago), "El pago agregado debe encontrarse en la lista de pagos del folio");
        assertEquals(120.0, folio.calcularSaldo(), 0.001, "El saldo del folio debe actualizarse restando el pago (200.0 - 80.0 = 120.0)");
    }

    @Test
    @DisplayName("Debe retornar false cuando el código de reserva no existe o es null")
    void testRegistrarPagoPorFolioIDReservaInexistente() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Folio folio = new Folio();
        Pago pago = new Pago(100.0, MedioDePago.TARJETA, LocalDate.now(), folio);

        // Act & Assert
        assertFalse(recepcionista.registrarPagoPorFolioID("CODIGO-INEXISTENTE", pago),
                "Debe retornar false si el código de reserva no existe");
        assertFalse(recepcionista.registrarPagoPorFolioID(null, pago),
                "Debe retornar false si el código de reserva es null");
    }

    @Test
    @DisplayName("Debe retornar false cuando el folio de la reserva es null o el objeto pago es null")
    void testRegistrarPagoPorFolioIDFolioOPagoNulo() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, ocupantes);
        Folio folioValido = new Folio();
        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

        // Reserva con folio válido para probar pago null
        Reserva reservaConFolio = new Reserva("RES-1102", titular, EstadoActual.CONFIRMADA, folioValido, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaConFolio);

        // Reserva con folio null
        Reserva reservaSinFolio = new Reserva("RES-1103", titular, EstadoActual.CONFIRMADA, null, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaSinFolio);

        Pago pagoValido = new Pago(50.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folioValido);

        // Act & Assert
        assertFalse(recepcionista.registrarPagoPorFolioID("RES-1102", null),
                "Debe retornar false si el objeto Pago enviado es null");
        assertTrue(folioValido.getListPagos().isEmpty(),
                "No debe agregarse ningún elemento null a la lista de pagos del folio");
        assertFalse(recepcionista.registrarPagoPorFolioID("RES-1103", pagoValido),
                "Debe retornar false si la reserva no tiene folio asociado (folio == null)");
    }

    // ========== PRUEBAS PARA TAREA 9.2: registrarCargoPorFolioID() ========== //

    @Test
    @DisplayName("Debe retornar true y agregar el cargo al folio de la reserva cuando ambos son válidos")
    void testRegistrarCargoPorFolioIDExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.OCUPADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, ocupantes);
        Folio folio = new Folio();
        folio.getListCargos().add(new Cargo("Alojamiento", 200.0, folio));

        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");
        Reserva reserva = new Reserva("RES-1201", titular, EstadoActual.EN_CURSO, folio, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reserva);

        Cargo cargoExtra = new Cargo("Desayuno y Minibar", 45.0, folio);

        // Act
        boolean registrado = recepcionista.registrarCargoPorFolioID("RES-1201", cargoExtra);

        // Assert
        assertTrue(registrado, "Debe retornar true cuando se registra un cargo válido en una reserva existente");
        assertEquals(2, folio.getListCargos().size(), "El folio debe contener los 2 cargos registrados");
        assertTrue(folio.getListCargos().contains(cargoExtra), "El cargo adicional debe encontrarse en la lista de cargos del folio");
        assertEquals(245.0, folio.calcularSaldo(), 0.001, "El saldo del folio debe incrementarse con el nuevo cargo (200.0 + 45.0 = 245.0)");
    }

    @Test
    @DisplayName("Debe retornar false cuando el código de reserva no existe o es null")
    void testRegistrarCargoPorFolioIDReservaInexistente() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Folio folio = new Folio();
        Cargo cargoExtra = new Cargo("Lavandería", 30.0, folio);

        // Act & Assert
        assertFalse(recepcionista.registrarCargoPorFolioID("CODIGO-INEXISTENTE", cargoExtra),
                "Debe retornar false si el código de reserva no existe");
        assertFalse(recepcionista.registrarCargoPorFolioID(null, cargoExtra),
                "Debe retornar false si el código de reserva es null");
    }

    @Test
    @DisplayName("Debe retornar false cuando el folio de la reserva es null o el objeto cargo es null")
    void testRegistrarCargoPorFolioIDFolioOCargoNulo() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.OCUPADO, "Ninguno");

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);

        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, ocupantes);
        Folio folioValido = new Folio();
        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

        // Reserva con folio válido para probar cargo null
        Reserva reservaConFolio = new Reserva("RES-1202", titular, EstadoActual.EN_CURSO, folioValido, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaConFolio);

        // Reserva con folio null
        Reserva reservaSinFolio = new Reserva("RES-1203", titular, EstadoActual.EN_CURSO, null, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.crear(reservaSinFolio);

        Cargo cargoValido = new Cargo("Minibar", 25.0, folioValido);

        // Act & Assert
        assertFalse(recepcionista.registrarCargoPorFolioID("RES-1202", null),
                "Debe retornar false si el objeto Cargo enviado es null");
        assertTrue(folioValido.getListCargos().isEmpty(),
                "No debe agregarse ningún elemento null a la lista de cargos del folio");
        assertFalse(recepcionista.registrarCargoPorFolioID("RES-1203", cargoValido),
                "Debe retornar false si la reserva no tiene folio asociado (folio == null)");
    }

    // ========== PRUEBAS PARA TAREA 9.3: anadirNovedadEnApartamento() ========== //

    @Test
    @DisplayName("Debe retornar true y agregar la novedad a la lista del apartamento cuando este existe")
    void testAnadirNovedadEnApartamentoExitoso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");
        gestorApartamentos.crear(apartamento);

        Novedad novedad = new Novedad("Lámpara de mesa rota en la habitación principal", apartamento);

        // Act
        boolean agregada = recepcionista.anadirNovedadEnApartamento("A1", novedad);

        // Assert
        assertTrue(agregada, "Debe retornar true al registrar la novedad en un apartamento existente");
        assertEquals(1, apartamento.getListNovedades().size(), "El apartamento debe tener exactamente 1 novedad registrada");
        assertTrue(apartamento.getListNovedades().contains(novedad), "La novedad debe encontrarse en la lista de novedades del apartamento");
    }

    @Test
    @DisplayName("Debe retornar false cuando el código del apartamento no existe o es null")
    void testAnadirNovedadEnApartamentoInexistente() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Novedad novedad = new Novedad("Fuga de agua en el baño", null);

        // Act & Assert
        assertFalse(recepcionista.anadirNovedadEnApartamento("CODIGO-INEXISTENTE", novedad),
                "Debe retornar false si el apartamento no existe en el gestor");
        assertFalse(recepcionista.anadirNovedadEnApartamento(null, novedad),
                "Debe retornar false si el código del apartamento es null");
    }

    @Test
    @DisplayName("No debe agregar elementos nulos a la lista de novedades del apartamento cuando la novedad es null")
    void testAnadirNovedadEnApartamentoConNovedadNula() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();
        GestorEmpleados gestorEmpleados = gestorGeneral.getTheGestorEmpleados();

        Recepcionista recepcionista = new Recepcionista("Ana", "999", LocalDate.of(1995, 4, 12), "3001234567", "ana@apartasuites.com", "insano", "123", gestorEmpleados);
        Apartamento apartamento = new Apartamento("A1", "Suite 101", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");
        gestorApartamentos.crear(apartamento);

        // Act
        boolean resultado = recepcionista.anadirNovedadEnApartamento("A1", null);

        // Assert
        assertTrue(resultado, "El método retorna true porque el apartamento sí existe");
        assertTrue(apartamento.getListNovedades().isEmpty(), "La lista de novedades debe permanecer vacía si se envía null");
    }
}
