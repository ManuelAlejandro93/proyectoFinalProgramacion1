package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;
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

public class GestorReservasTest {

    // ========== PRUEBAS PARA TAREA 4.1: validarSinDobleVenta() ========== //

    @Test
    @DisplayName("Debe retornar true cuando no hay conflictos de fechas en el mismo apartamento")
    void testValidarSinDobleVentaSinConflicto() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Reserva existente del 10 al 15 de diciembre
        Estancia estanciaExistente = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);
        Reserva reservaExistente = new Reserva("R1", null, EstadoActual.CONFIRMADA, folio, canalReserva, apartamento, estanciaExistente, gestorReservas);
        gestorReservas.getListReservas().add(reservaExistente);

        // Nueva consulta: inicia el mismo día que termina la anterior (check-out 15 / check-in 15 -> NO se solapan)
        LocalDate nuevaEntrada = LocalDate.of(2026, 12, 15);
        LocalDate nuevaSalida = LocalDate.of(2026, 12, 20);

        // Act
        boolean disponible = gestorReservas.validarSinDobleVenta(apartamento, nuevaEntrada, nuevaSalida, null);

        // Assert
        assertTrue(disponible, "Debería permitir reservar cuando no hay solapamiento de fechas (check-in el día del check-out)");
    }

    @Test
    @DisplayName("Debe retornar false cuando existe una reserva activa con fechas solapadas en el mismo apartamento")
    void testValidarSinDobleVentaConSolapamiento() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Reserva activa (PENDIENTE, CONFIRMADA o EN_CURSO) del 10 al 15 de diciembre
        Estancia estanciaExistente = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);
        Reserva reservaExistente = new Reserva("R1", null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estanciaExistente, gestorReservas);
        gestorReservas.getListReservas().add(reservaExistente);

        // Nueva consulta del 12 al 18 de diciembre (se cruza del 12 al 15)
        LocalDate nuevaEntrada = LocalDate.of(2026, 12, 12);
        LocalDate nuevaSalida = LocalDate.of(2026, 12, 18);

        // Act
        boolean disponible = gestorReservas.validarSinDobleVenta(apartamento, nuevaEntrada, nuevaSalida, null);

        // Assert
        assertFalse(disponible, "Debería retornar false al detectar solapamiento con una reserva activa en el mismo apartamento");
    }

    @Test
    @DisplayName("Debe ignorar la reserva cuyo código coincide con codigoReservaExcluir (caso actualización)")
    void testValidarSinDobleVentaExcluyendoMismaReserva() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Reserva R1 del 10 al 15 de diciembre
        Estancia estanciaExistente = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);
        Reserva reservaExistente = new Reserva("R1", null, EstadoActual.CONFIRMADA, folio, canalReserva, apartamento, estanciaExistente, gestorReservas);
        gestorReservas.getListReservas().add(reservaExistente);

        // Se modifican fechas de la misma reserva R1 (del 11 al 16 de diciembre)
        LocalDate nuevaEntrada = LocalDate.of(2026, 12, 11);
        LocalDate nuevaSalida = LocalDate.of(2026, 12, 16);

        // Act
        boolean disponible = gestorReservas.validarSinDobleVenta(apartamento, nuevaEntrada, nuevaSalida, "R1");

        // Assert
        assertTrue(disponible, "Debería ignorar la reserva R1 al actualizarla y permitir el rango de fechas");
    }

    @Test
    @DisplayName("Debe ignorar reservas en estados no activos (CANCELADA, FINALIZADA, NO_SHOWN)")
    void testValidarSinDobleVentaIgnoraReservasInactivas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 15);
        Estancia estancia = new Estancia(entrada, salida, apartamento);

        // Tres reservas en las mismas fechas pero en estados inactivos
        Reserva rCancelada = new Reserva("R1", null, EstadoActual.CANCELADA, folio, canalReserva, apartamento, estancia, gestorReservas);
        Reserva rFinalizada = new Reserva("R2", null, EstadoActual.FINALIZADA, folio, canalReserva, apartamento, estancia, gestorReservas);
        Reserva rNoShow = new Reserva("R3", null, EstadoActual.NO_SHOWN, folio, canalReserva, apartamento, estancia, gestorReservas);

        gestorReservas.getListReservas().add(rCancelada);
        gestorReservas.getListReservas().add(rFinalizada);
        gestorReservas.getListReservas().add(rNoShow);

        // Act
        boolean disponible = gestorReservas.validarSinDobleVenta(apartamento, entrada, salida, null);

        // Assert
        assertTrue(disponible, "Debería retornar true porque ninguna de las reservas existentes está activa");
    }

    @Test
    @DisplayName("Debe permitir reservar en las mismas fechas si la reserva activa es de otro apartamento")
    void testValidarSinDobleVentaDistintoApartamento() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");

        Apartamento apartamentoA1 = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Apartamento apartamentoB2 = new Apartamento("B2", "Piso 2 apart 10", 4, 8, new ArrayList<String>(), 25.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 15);

        // Reserva activa en A1
        Estancia estanciaA1 = new Estancia(entrada, salida, apartamentoA1);
        Reserva reservaA1 = new Reserva("R1", null, EstadoActual.EN_CURSO, folio, canalReserva, apartamentoA1, estanciaA1, gestorReservas);
        gestorReservas.getListReservas().add(reservaA1);

        // Act: Consultamos disponibilidad para B2 en exactamente las mismas fechas
        boolean disponible = gestorReservas.validarSinDobleVenta(apartamentoB2, entrada, salida, null);

        // Assert
        assertTrue(disponible, "Debería permitir reservar B2 aunque A1 esté ocupado en las mismas fechas");
    }

    @Test
    @DisplayName("Debe retornar false cuando los parámetros de entrada son nulos o el rango de fechas es inválido")
    void testValidarSinDobleVentaParametrosInvalidos() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.of(2026, 12, 15);
        LocalDate salida = LocalDate.of(2026, 12, 10); // Salida anterior a la entrada

        // Act & Assert
        assertFalse(gestorReservas.validarSinDobleVenta(null, entrada, salida, null), "Debe fallar si apartamento es null");
        assertFalse(gestorReservas.validarSinDobleVenta(apartamento, null, LocalDate.of(2026, 12, 20), null), "Debe fallar si entrada es null");
        assertFalse(gestorReservas.validarSinDobleVenta(apartamento, LocalDate.of(2026, 12, 10), null, null), "Debe fallar si salida es null");
        assertFalse(gestorReservas.validarSinDobleVenta(apartamento, entrada, salida, null), "Debe fallar si entrada no es anterior a salida");
        assertFalse(gestorReservas.validarSinDobleVenta(apartamento, entrada, entrada, null), "Debe fallar si entrada es igual a salida");
    }

    // ========== PRUEBAS PARA TAREA 4.2: validarCapacidadMaxima() ========== //

    @Test
    @DisplayName("Debe retornar true cuando el número de ocupantes es menor o igual a la capacidad máxima (incluyendo menores de 6 años)")
    void testValidarCapacidadMaximaDentroDelLimite() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        // Apartamento con capacidad máxima de 3 personas (cuarto parámetro)
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 3, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // 2 adultos + 1 menor de 6 años (nacido en 2024) = 3 ocupantes en total
        Huesped adulto1 = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10), "321", "kevin@gmail.com", gestorHuespedes);
        Huesped adulto2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20), "654", "laura@gmail.com", gestorHuespedes);
        Huesped menor = new Huesped("Mateo", "789", LocalDate.of(2024, 2, 15), "987", "mateo@gmail.com", gestorHuespedes);

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(adulto1);
        ocupantes.add(adulto2);
        ocupantes.add(menor);

        // Act
        boolean resultado = gestorReservas.validarCapacidadMaxima(apartamento, ocupantes);

        // Assert: 3 <= 3 -> true (el menor ocupa cupo)
        assertTrue(resultado, "Debería retornar true cuando el total de ocupantes (incluyendo menores) no supera la capacidad máxima");
    }

    @Test
    @DisplayName("Debe retornar false cuando el total de ocupantes excede la capacidad máxima por incluir menores de 6 años")
    void testValidarCapacidadMaximaExcedidaPorMenores() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

        // Apartamento con capacidad máxima de 2 personas (cuarto parámetro)
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 2, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // 2 adultos + 1 menor de 6 años = 3 ocupantes (> capacidad de 2)
        Huesped adulto1 = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10), "321", "kevin@gmail.com", gestorHuespedes);
        Huesped adulto2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20), "654", "laura@gmail.com", gestorHuespedes);
        Huesped menor = new Huesped("Mateo", "789", LocalDate.of(2024, 2, 15), "987", "mateo@gmail.com", gestorHuespedes);

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(adulto1);
        ocupantes.add(adulto2);
        ocupantes.add(menor);

        // Act
        boolean resultado = gestorReservas.validarCapacidadMaxima(apartamento, ocupantes);

        // Assert: 3 > 2 -> false
        assertFalse(resultado, "Debería retornar false porque los menores de 6 años también ocupan cupo y se excede la capacidad (3 > 2)");
    }

    @Test
    @DisplayName("Debe validar correctamente usando la sobrecarga por número entero de ocupantes")
    void testValidarCapacidadMaximaSobrecargaEntero() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Act
        boolean dentroCapacidad = gestorReservas.validarCapacidadMaxima(apartamento, 3);
        boolean limiteExacto = gestorReservas.validarCapacidadMaxima(apartamento, 4);
        boolean excedido = gestorReservas.validarCapacidadMaxima(apartamento, 5);

        // Assert
        assertTrue(dentroCapacidad, "3 ocupantes en capacidad de 4 debe retornar true");
        assertTrue(limiteExacto, "4 ocupantes en capacidad de 4 debe retornar true");
        assertFalse(excedido, "5 ocupantes en capacidad de 4 debe retornar false");
    }

    @Test
    @DisplayName("Debe retornar false cuando los parámetros son nulos o el número de ocupantes es negativo")
    void testValidarCapacidadMaximaParametrosInvalidos() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        ArrayList<Huesped> ocupantes = new ArrayList<>();

        // Act & Assert
        assertFalse(gestorReservas.validarCapacidadMaxima(null, ocupantes), "Debe retornar false si el apartamento es null");
        assertFalse(gestorReservas.validarCapacidadMaxima(apartamento, (ArrayList<Huesped>) null), "Debe retornar false si la lista de ocupantes es null");
        assertFalse(gestorReservas.validarCapacidadMaxima(null, 2), "Debe retornar false si el apartamento es null en la sobrecarga");
        assertFalse(gestorReservas.validarCapacidadMaxima(apartamento, -1), "Debe retornar false si el total de ocupantes es negativo");
    }

    // ========== PRUEBAS PARA TAREA 4.3: validarBloqueos() ========== //

    @Test
    @DisplayName("Debe retornar true cuando el apartamento no tiene bloqueos o las fechas no se cruzan con ningún bloqueo")
    void testValidarBloqueosSinConflicto() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Caso 1: Lista de bloqueos vacía
        LocalDate entrada1 = LocalDate.of(2026, 12, 10);
        LocalDate salida1 = LocalDate.of(2026, 12, 15);
        assertTrue(gestorReservas.validarBloqueos(apartamento, entrada1, salida1),
                "Debería retornar true cuando el apartamento no tiene bloqueos registrados");

        // Caso 2: Bloqueo existente del 20 al 25 de diciembre, reserva termina justo el 20 (cuando empieza el bloqueo)
        Bloqueo bloqueo = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 25), apartamento);
        apartamento.getListBloqueos().add(bloqueo);

        LocalDate entradaAntes = LocalDate.of(2026, 12, 15);
        LocalDate salidaAntes = LocalDate.of(2026, 12, 20);

        // Reserva entra después de que termina el bloqueo (26 al 30 de diciembre)
        LocalDate entradaDespues = LocalDate.of(2026, 12, 26);
        LocalDate salidaDespues = LocalDate.of(2026, 12, 30);

        // Act
        boolean disponibleAntes = gestorReservas.validarBloqueos(apartamento, entradaAntes, salidaAntes);
        boolean disponibleDespues = gestorReservas.validarBloqueos(apartamento, entradaDespues, salidaDespues);

        // Assert
        assertTrue(disponibleAntes, "Debería permitir reservar si el check-out es el mismo día que inicia el bloqueo");
        assertTrue(disponibleDespues, "Debería permitir reservar después de que finaliza el bloqueo");
    }

    @Test
    @DisplayName("Debe retornar false cuando la estancia se cruza con un bloqueo del apartamento")
    void testValidarBloqueosConSolapamiento() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Bloqueo del 10 al 15 de diciembre
        Bloqueo bloqueo = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);
        apartamento.getListBloqueos().add(bloqueo);

        // Reserva del 12 al 18 de diciembre (se solapa con el bloqueo)
        LocalDate entrada = LocalDate.of(2026, 12, 12);
        LocalDate salida = LocalDate.of(2026, 12, 18);

        // Act
        boolean disponible = gestorReservas.validarBloqueos(apartamento, entrada, salida);

        // Assert
        assertFalse(disponible, "Debería retornar false cuando las fechas de la reserva se cruzan con un bloqueo");
    }

    @Test
    @DisplayName("Debe detectar conflicto cuando existen múltiples bloqueos y uno de ellos se solapa")
    void testValidarBloqueosMultiplesBloqueos() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Dos bloqueos distintos en el apartamento
        Bloqueo bloqueo1 = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5), apartamento);
        Bloqueo bloqueo2 = new Bloqueo("Motivo 2", LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 24), apartamento);
        apartamento.getListBloqueos().add(bloqueo1);
        apartamento.getListBloqueos().add(bloqueo2);

        // Reserva del 18 al 22 de diciembre (no choca con bloqueo1, pero sí con bloqueo2)
        LocalDate entrada = LocalDate.of(2026, 12, 18);
        LocalDate salida = LocalDate.of(2026, 12, 22);

        // Act
        boolean disponible = gestorReservas.validarBloqueos(apartamento, entrada, salida);

        // Assert
        assertFalse(disponible, "Debería retornar false al detectar conflicto con el segundo bloqueo registrado");
    }

    @Test
    @DisplayName("Debe retornar false cuando los parámetros son nulos o el rango de fechas es inválido")
    void testValidarBloqueosParametrosInvalidos() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        LocalDate entrada = LocalDate.of(2026, 12, 15);
        LocalDate salida = LocalDate.of(2026, 12, 10); // Salida anterior a la entrada

        // Act & Assert
        assertFalse(gestorReservas.validarBloqueos(null, entrada, salida), "Debe fallar si apartamento es null");
        assertFalse(gestorReservas.validarBloqueos(apartamento, null, LocalDate.of(2026, 12, 20)), "Debe fallar si fechaEntrada es null");
        assertFalse(gestorReservas.validarBloqueos(apartamento, LocalDate.of(2026, 12, 10), null), "Debe fallar si fechaSalida es null");
        assertFalse(gestorReservas.validarBloqueos(apartamento, entrada, salida), "Debe fallar si fechaEntrada es posterior a fechaSalida");
        assertFalse(gestorReservas.validarBloqueos(apartamento, entrada, entrada), "Debe fallar si fechaEntrada es igual a fechaSalida");
    }

    // ========== PRUEBAS PARA TAREA 4.4: consultarApartamentosDisponibles() ========== //

    @Test
    @DisplayName("Debe retornar únicamente los apartamentos que cumplen con capacidad, sin doble venta y sin bloqueos")
    void testConsultarApartamentosDisponiblesFiltraCorrectamente() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");

        LocalDate entradaSolicitada = LocalDate.of(2026, 12, 10);
        LocalDate salidaSolicitada = LocalDate.of(2026, 12, 15);
        int capacidadRequerida = 4;

        // 1. Apto A1: Disponible y cumple capacidad (4 >= 4) -> DEBE APARECER
        Apartamento aptoDisponible = new Apartamento("A1", "Piso 1 apart 10", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // 2. Apto A2: Disponible pero capacidad insuficiente (2 < 4) -> NO DEBE APARECER
        Apartamento aptoPequeno = new Apartamento("A2", "Piso 1 apart 12", 1, 2, new ArrayList<String>(), 15.0, EstadoOperativo.PREPARADO, "Ninguno");

        // 3. Apto B1: Capacidad suficiente (6 >= 4) pero ocupado por reserva activa en esas fechas -> NO DEBE APARECER
        Apartamento aptoReservado = new Apartamento("B1", "Piso 2 apart 20", 3, 6, new ArrayList<String>(), 30.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estanciaOcupada = new Estancia(LocalDate.of(2026, 12, 12), LocalDate.of(2026, 12, 16), aptoReservado);
        Reserva reservaActiva = new Reserva("R1", null, EstadoActual.CONFIRMADA, folio, canalReserva, aptoReservado, estanciaOcupada, gestorReservas);
        gestorReservas.getListReservas().add(reservaActiva);

        // 4. Apto B2: Capacidad suficiente (4 >= 4) pero bloqueado en esas fechas -> NO DEBE APARECER
        Apartamento aptoBloqueado = new Apartamento("B2", "Piso 2 apart 22", 2, 4, new ArrayList<String>(), 25.0, EstadoOperativo.PREPARADO, "Ninguno");
        Bloqueo bloqueo = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 8), LocalDate.of(2026, 12, 12), aptoBloqueado);
        aptoBloqueado.getListBloqueos().add(bloqueo);

        // 5. Apto C1: Disponible, capacidad 5 >= 4, tiene una reserva pero CANCELADA -> DEBE APARECER
        Apartamento aptoConReservaCancelada = new Apartamento("C1", "Piso 3 apart 30", 2, 5, new ArrayList<String>(), 22.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estanciaCancelada = new Estancia(entradaSolicitada, salidaSolicitada, aptoConReservaCancelada);
        Reserva reservaCancelada = new Reserva("R2", null, EstadoActual.CANCELADA, folio, canalReserva, aptoConReservaCancelada, estanciaCancelada, gestorReservas);
        gestorReservas.getListReservas().add(reservaCancelada);

        // Registramos todos los apartamentos en el gestor
        gestorApartamentos.getListApartamentos().add(aptoDisponible);
        gestorApartamentos.getListApartamentos().add(aptoPequeno);
        gestorApartamentos.getListApartamentos().add(aptoReservado);
        gestorApartamentos.getListApartamentos().add(aptoBloqueado);
        gestorApartamentos.getListApartamentos().add(aptoConReservaCancelada);

        // Act
        ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(entradaSolicitada, salidaSolicitada, capacidadRequerida);

        // Assert
        assertEquals(2, disponibles.size(), "Debería retornar exactamente 2 apartamentos disponibles (A1 y C1)");
        assertTrue(disponibles.contains(aptoDisponible), "Debería incluir el apartamento A1");
        assertTrue(disponibles.contains(aptoConReservaCancelada), "Debería incluir el apartamento C1 cuya reserva previa fue cancelada");
        assertFalse(disponibles.contains(aptoPequeno), "No debería incluir A2 por capacidad insuficiente");
        assertFalse(disponibles.contains(aptoReservado), "No debería incluir B1 por doble venta");
        assertFalse(disponibles.contains(aptoBloqueado), "No debería incluir B2 por bloqueo de fechas");
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando ningún apartamento cumple con las condiciones")
    void testConsultarApartamentosDisponiblesSinResultados() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();

        Apartamento apto = new Apartamento("A1", "Piso 1 apart 10", 1, 2, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        gestorApartamentos.getListApartamentos().add(apto);

        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 15);

        // Act: Solicitamos capacidad para 6 personas cuando el único apartamento es para 2
        ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(entrada, salida, 6);

        // Assert
        assertNotNull(disponibles, "No debe retornar null, debe retornar una lista vacía");
        assertTrue(disponibles.isEmpty(), "Debería retornar una lista vacía cuando ningún apartamento cumple");
    }

    @Test
    @DisplayName("Debe retornar una lista vacía cuando el rango de fechas es inválido")
    void testConsultarApartamentosDisponiblesFechasInvalidas() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();

        Apartamento apto = new Apartamento("A1", "Piso 1 apart 10", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        gestorApartamentos.getListApartamentos().add(apto);

        LocalDate entrada = LocalDate.of(2026, 12, 20);
        LocalDate salida = LocalDate.of(2026, 12, 15); // Salida anterior a la entrada

        // Act
        ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(entrada, salida, 2);

        // Assert
        assertNotNull(disponibles, "La lista retornada no debe ser null");
        assertTrue(disponibles.isEmpty(), "Debería retornar lista vacía cuando la fecha de entrada no es anterior a la de salida");
    }

    // ========== PRUEBAS PARA TAREA 4.5: generarCodigoUnico() ========== //

    @Test
    @DisplayName("Debe generar un código con el formato exacto RES-<timestamp>-<numero4Digitos>")
    void testGenerarCodigoUnicoFormatoValido() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);

        // Act
        String codigo = gestorReservas.generarCodigoUnico();

        // Assert
        assertNotNull(codigo, "El código generado no debe ser null");
        assertTrue(codigo.startsWith("RES-"), "El código debe iniciar con el prefijo 'RES-'");
        assertTrue(codigo.matches("^RES-\\d+-\\d{4}$"),
                "El código debe cumplir con el formato RES-<timestamp>-<4 dígitos> (1000-9999), pero fue: " + codigo);
    }

    @Test
    @DisplayName("Debe generar códigos distintos en llamadas consecutivas y sin existir previamente en el sistema")
    void testGenerarCodigoUnicoNoSeRepite() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);

        // Act: Generamos varios códigos seguidos (incluso dentro del mismo milisegundo)
        String codigo1 = gestorReservas.generarCodigoUnico();
        String codigo2 = gestorReservas.generarCodigoUnico();
        String codigo3 = gestorReservas.generarCodigoUnico();

        // Assert
        assertNotEquals(codigo1, codigo2, "Dos códigos generados consecutivamente no deben ser iguales");
        assertNotEquals(codigo2, codigo3, "Dos códigos generados consecutivamente no deben ser iguales");
        assertNotEquals(codigo1, codigo3, "Dos códigos generados consecutivamente no deben ser iguales");
        assertNull(gestorReservas.consultar(codigo1), "El código generado no debe existir previamente en listReservas");
    }

    @Test
    @DisplayName("Debe evitar colisiones generando un nuevo código si ya existe una reserva con el código generado")
    void testGenerarCodigoUnicoEvitaColisionConExistente() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);

        // Registramos una reserva previa en el gestor
        String codigoPrevio = gestorReservas.generarCodigoUnico();
        Reserva reservaExistente = new Reserva(codigoPrevio, null, EstadoActual.PENDIENTE, folio, canalReserva, apartamento, estancia, gestorReservas);
        gestorReservas.getListReservas().add(reservaExistente);

        // Act
        String nuevoCodigo = gestorReservas.generarCodigoUnico();

        // Assert
        assertNotEquals(codigoPrevio, nuevoCodigo, "El nuevo código debe ser distinto al de la reserva ya registrada");
        assertNull(gestorReservas.consultar(nuevoCodigo), "El nuevo código no debe coincidir con ninguna reserva existente");
    }

    // ========== PRUEBAS PARA TAREA 4.6: existeCodigoExterno() ========== //

    @Test
    @DisplayName("Debe retornar true cuando ya existe una reserva de canal EXTERNA con el mismo código")
    void testExisteCodigoExternoCuandoYaExiste() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);

        CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, "BK-998877");
        Reserva reservaExterna = new Reserva("R1", null, EstadoActual.CONFIRMADA, folio, canalExterno, apartamento, estancia, gestorReservas);
        gestorReservas.getListReservas().add(reservaExterna);

        // Act
        boolean existe = gestorReservas.existeCodigoExterno("BK-998877");

        // Assert
        assertTrue(existe, "Debería retornar true porque ya existe una reserva con canal EXTERNA y código 'BK-998877'");
    }

    @Test
    @DisplayName("Debe retornar false cuando el código externo no coincide con ninguna reserva registrada")
    void testExisteCodigoExternoCuandoNoExiste() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);

        CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, "BK-998877");
        Reserva reservaExterna = new Reserva("R1", null, EstadoActual.CONFIRMADA, folio, canalExterno, apartamento, estancia, gestorReservas);
        gestorReservas.getListReservas().add(reservaExterna);

        // Act
        boolean existe = gestorReservas.existeCodigoExterno("AIR-112233");

        // Assert
        assertFalse(existe, "Debería retornar false cuando el código externo no está registrado en ninguna reserva");
    }

    @Test
    @DisplayName("Debe ignorar reservas cuyo canal NO sea EXTERNA (por ejemplo DIRECTA) aunque tengan el mismo código")
    void testExisteCodigoExternoIgnoraCanalDirecto() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
        Folio folio = new Folio();
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15), apartamento);

        // Reserva con canal DIRECTA pero con el código en el campo codigoCanal
        CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "DIR-5555");
        Reserva reservaDirecta = new Reserva("R1", null, EstadoActual.PENDIENTE, folio, canalDirecto, apartamento, estancia, gestorReservas);
        gestorReservas.getListReservas().add(reservaDirecta);

        // Act
        boolean existe = gestorReservas.existeCodigoExterno("DIR-5555");

        // Assert
        assertFalse(existe, "Debería retornar false porque la reserva pertenece al canal DIRECTA y no a EXTERNA");
    }

    @Test
    @DisplayName("Debe retornar false cuando el código externo es null, vacío o solo espacios")
    void testExisteCodigoExternoParametrosInvalidos() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = new GestorReservas(gestorGeneral);

        // Act & Assert
        assertFalse(gestorReservas.existeCodigoExterno(null), "Debería retornar false si codigoExterno es null");
        assertFalse(gestorReservas.existeCodigoExterno(""), "Debería retornar false si codigoExterno está vacío");
        assertFalse(gestorReservas.existeCodigoExterno("   "), "Debería retornar false si codigoExterno solo tiene espacios");
    }
}
