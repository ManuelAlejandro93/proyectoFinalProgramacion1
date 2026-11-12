package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Estancia.Noche;
import com.ApartaSuitesUQ.Estancia.Temporada;
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

public class ReservaTest {

    // ========== PRUEBAS PARA TAREA 3.1: puedePasarA() ========== //

    @Test
    @DisplayName("Debe retornar false al intentar cambiar desde FINALIZADA (estado final)")
    void testFinalizadaNoPermiteCambios() {

        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);
        reserva.setEstadoActual(EstadoActual.FINALIZADA);

        // Act & Assert
        assertFalse(reserva.puedePasarA(EstadoActual.PENDIENTE), "No debe permitir pasar de FINALIZADA a PENDIENTE");
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir pasar de FINALIZADA a CONFIRMADA");
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir pasar de FINALIZADA a EN_CURSO");
        assertFalse(reserva.puedePasarA(EstadoActual.CANCELADA), "No debe permitir pasar de FINALIZADA a CANCELADA");
        assertFalse(reserva.puedePasarA(EstadoActual.NO_SHOWN), "No debe permitir pasar de FINALIZADA a NO_SHOWN");
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir pasar de FINALIZADA a FINALIZADA");
    }

    @Test
    @DisplayName("Debe retornar false al intentar cambiar desde CANCELADA (estado final)")
    void testCanceladaNoPermiteCambios() {
        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);

        reserva.setEstadoActual(EstadoActual.CANCELADA);

        // Act & Assert
        assertFalse(reserva.puedePasarA(EstadoActual.PENDIENTE), "No debe permitir pasar de CANCELADA a PENDIENTE");
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir pasar de CANCELADA a CONFIRMADA");
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir pasar de CANCELADA a EN_CURSO");
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir pasar de CANCELADA a FINALIZADA");
        assertFalse(reserva.puedePasarA(EstadoActual.NO_SHOWN), "No debe permitir pasar de CANCELADA a NO_SHOWN");
        assertFalse(reserva.puedePasarA(EstadoActual.CANCELADA), "No debe permitir pasar de CANCELADA a CANCELADA");
    }

    @Test
    @DisplayName("Debe retornar false al intentar cambiar desde NO_SHOWN (estado final)")
    void testNoShowNoPermiteCambios() {
        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);

        reserva.setEstadoActual(EstadoActual.NO_SHOWN);

        // Act & Assert
        assertFalse(reserva.puedePasarA(EstadoActual.PENDIENTE), "No debe permitir pasar de NO_SHOWN a PENDIENTE");
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir pasar de NO_SHOWN a CONFIRMADA");
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir pasar de NO_SHOWN a EN_CURSO");
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir pasar de NO_SHOWN a FINALIZADA");
        assertFalse(reserva.puedePasarA(EstadoActual.CANCELADA), "No debe permitir pasar de NO_SHOWN a CANCELADA");
        assertFalse(reserva.puedePasarA(EstadoActual.NO_SHOWN), "No debe permitir pasar de NO_SHOWN a NO_SHOWN");
    }

    @Test
    @DisplayName("Debe permitir confirmar (CONFIRMADA) únicamente desde PENDIENTE")
    void testSoloSePuedeConfirmarDesdePendiente() {
        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);

        // Act & Assert: Caso válido
        reserva.setEstadoActual(EstadoActual.PENDIENTE);
        assertTrue(reserva.puedePasarA(EstadoActual.CONFIRMADA), "Debe permitir confirmar desde PENDIENTE");

        // Act & Assert: Casos inválidos
        reserva.setEstadoActual(EstadoActual.CONFIRMADA);
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir confirmar desde CONFIRMADA");

        reserva.setEstadoActual(EstadoActual.EN_CURSO);
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir confirmar desde EN_CURSO");

        reserva.setEstadoActual(EstadoActual.FINALIZADA);
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir confirmar desde FINALIZADA");

        reserva.setEstadoActual(EstadoActual.CANCELADA);
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir confirmar desde CANCELADA");

        reserva.setEstadoActual(EstadoActual.NO_SHOWN);
        assertFalse(reserva.puedePasarA(EstadoActual.CONFIRMADA), "No debe permitir confirmar desde NO_SHOWN");
    }

    @Test
    @DisplayName("Debe permitir hacer check-in (EN_CURSO) únicamente desde CONFIRMADA")
    void testSoloSePuedeHacerCheckInDesdeConfirmada() {
        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);

        // Act & Assert: Caso válido
        reserva.setEstadoActual(EstadoActual.CONFIRMADA);
        assertTrue(reserva.puedePasarA(EstadoActual.EN_CURSO), "Debe permitir hacer check-in desde CONFIRMADA");

        // Act & Assert: Casos inválidos
        reserva.setEstadoActual(EstadoActual.PENDIENTE);
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir hacer check-in desde PENDIENTE");

        reserva.setEstadoActual(EstadoActual.EN_CURSO);
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir hacer check-in desde EN_CURSO");

        reserva.setEstadoActual(EstadoActual.FINALIZADA);
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir hacer check-in desde FINALIZADA");

        reserva.setEstadoActual(EstadoActual.CANCELADA);
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir hacer check-in desde CANCELADA");

        reserva.setEstadoActual(EstadoActual.NO_SHOWN);
        assertFalse(reserva.puedePasarA(EstadoActual.EN_CURSO), "No debe permitir hacer check-in desde NO_SHOWN");
    }

    @Test
    @DisplayName("Debe permitir hacer check-out (FINALIZADA) únicamente desde EN_CURSO")
    void testSoloSePuedeHacerCheckOutDesdeEnCurso() {
        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);

        // Act & Assert: Caso válido
        reserva.setEstadoActual(EstadoActual.EN_CURSO);
        assertTrue(reserva.puedePasarA(EstadoActual.FINALIZADA), "Debe permitir hacer check-out desde EN_CURSO");

        // Act & Assert: Casos inválidos
        reserva.setEstadoActual(EstadoActual.PENDIENTE);
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir hacer check-out desde PENDIENTE");

        reserva.setEstadoActual(EstadoActual.CONFIRMADA);
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir hacer check-out desde CONFIRMADA");

        reserva.setEstadoActual(EstadoActual.FINALIZADA);
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir hacer check-out desde FINALIZADA");

        reserva.setEstadoActual(EstadoActual.CANCELADA);
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir hacer check-out desde CANCELADA");

        reserva.setEstadoActual(EstadoActual.NO_SHOWN);
        assertFalse(reserva.puedePasarA(EstadoActual.FINALIZADA), "No debe permitir hacer check-out desde NO_SHOWN");
    }

    @Test
    @DisplayName("Debe retornar false cuando el estado actual o el nuevo estado es null")
    void testPuedePasarAConEstadosNulos() {
        //Gestores
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 05), "321", "kevin67@gmail.com", gestorHuespedes );
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026,12,20), LocalDate.of(2026,12,30), apartamento);

        // Arrange
        Reserva reserva = new Reserva("123", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);

        reserva.setEstadoActual(EstadoActual.PENDIENTE);

        // Act
        boolean resultadoNuevoNulo = reserva.puedePasarA(null);

        reserva.setEstadoActual(null);
        boolean resultadoActualNulo = reserva.puedePasarA(EstadoActual.CONFIRMADA);

        // Assert
        assertFalse(resultadoNuevoNulo, "Debería retornar false si el nuevo estado es null");
        assertFalse(resultadoActualNulo, "Debería retornar false si el estado actual es null");
    }

    // ========== PRUEBAS PARA TAREA 3.2: calcularDevolucion() ========== //

    @Test
    @DisplayName("Debe devolver el 100% del valor de la estancia si se cancela con más de 7 días de anticipación")
    void testCalcularDevolucionCompleta() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", gestorHuespedes);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.now().plusDays(10); // > 7 días de anticipación
        LocalDate salida = LocalDate.now().plusDays(20);
        Estancia estancia = new Estancia(entrada, salida, apartamento);

        estancia.getListHuespedes().add(huesped);
        for (Noche noche : estancia.getListNoches()) {
            noche.setPrecioPorTemporada(1.0);
        }

        Reserva reserva = new Reserva("123", null, EstadoActual.CONFIRMADA, folio, canalReserva, apartamento, estancia, gestorReservas);

        // Act
        double devolucion = reserva.calcularDevolucion();

        // Assert: 200.0 * 1.0 = 200.0
        assertEquals(200.0, devolucion, 0.001, "Debería retornar el 100% del valor de la estancia (200.0)");
    }

    @Test
    @DisplayName("Debe devolver el 50% del valor de la estancia si se cancela entre 2 y 7 días de anticipación")
    void testCalcularDevolucionParcial() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", gestorHuespedes);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.now().plusDays(5); // Entre 2 y 7 días de anticipación
        LocalDate salida = LocalDate.now().plusDays(15);
        Estancia estancia = new Estancia(entrada, salida, apartamento);

        estancia.getListHuespedes().add(huesped);
        for (Noche noche : estancia.getListNoches()) {
            noche.setPrecioPorTemporada(1.0);
        }

        Reserva reserva = new Reserva("123", null, EstadoActual.CONFIRMADA, folio, canalReserva, apartamento, estancia, gestorReservas);

        // Act
        double devolucion = reserva.calcularDevolucion();

        // Assert: 200.0 * 0.5 = 100.0
        assertEquals(100.0, devolucion, 0.001, "Debería retornar el 50% del valor de la estancia (100.0)");
    }

    @Test
    @DisplayName("Debe devolver 0.0 si se cancela con menos de 2 días de anticipación")
    void testCalcularDevolucionSinReembolso() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        Huesped huesped = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", gestorHuespedes);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.now().plusDays(1); // < 2 días de anticipación
        LocalDate salida = LocalDate.now().plusDays(5);
        Estancia estancia = new Estancia(entrada, salida, apartamento);

        estancia.getListHuespedes().add(huesped);
        for (Noche noche : estancia.getListNoches()) {
            noche.setPrecioPorTemporada(1.0);
        }

        Reserva reserva = new Reserva("123", null, EstadoActual.CONFIRMADA, folio, canalReserva, apartamento, estancia, gestorReservas);

        // Act
        double devolucion = reserva.calcularDevolucion();

        // Assert
        assertEquals(0.0, devolucion, 0.001, "Debería retornar 0.0 cuando la cancelación es con menos de 2 días");
    }

    @Test
    @DisplayName("Debe retornar 0.0 cuando la estancia de la reserva es null")
    void testCalcularDevolucionConEstanciaNula() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.now().plusDays(10), LocalDate.now().plusDays(20), apartamento);

        // Pasamos la estancia válida al constructor para cumplir con el parámetro non-null
        Reserva reserva = new Reserva("123", null, EstadoActual.CONFIRMADA, folio, canalReserva, apartamento, estancia, gestorReservas);
        reserva.setTheEstancia(null);

        // Act
        double devolucion = reserva.calcularDevolucion();

        // Assert
        assertEquals(0.0, devolucion, 0.001, "Debería retornar 0.0 si theEstancia es null");
    }
}
