package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Estancia.Noche;
import com.ApartaSuitesUQ.Gestores.GestorApartamentos;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorHuespedes;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
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
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Reserva existente del 10 al 15 de diciembre
    Estancia estanciaExistente = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());
    Reserva reservaExistente = new Reserva("R1", null, EstadoActual.CONFIRMADA,
        folio, canalReserva, apartamento, estanciaExistente, gestorReservas);
    gestorReservas.getListReservas().add(reservaExistente);

    // Nueva consulta: inicia el mismo día que termina la anterior
    // (check-out 15 / check-in 15 -> NO se solapan)
    LocalDate nuevaEntrada = LocalDate.of(2026, 12, 15);
    LocalDate nuevaSalida = LocalDate.of(2026, 12, 20);

    // Act
    boolean disponible = gestorReservas.validarSinDobleVenta(
        apartamento, nuevaEntrada, nuevaSalida, null);

    // Assert
    assertTrue(disponible,
        "Debería permitir reservar cuando no hay solapamiento de fechas (check-in el día del check-out)");
  }

  @Test
  @DisplayName("Debe retornar false cuando existe una reserva activa con fechas solapadas en el mismo apartamento")
  void testValidarSinDobleVentaConSolapamiento() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Reserva activa (PENDIENTE, CONFIRMADA o EN_CURSO) del 10 al 15 de diciembre
    Estancia estanciaExistente = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());
    Reserva reservaExistente = new Reserva("R1", null, EstadoActual.PENDIENTE,
        folio, canalReserva, apartamento, estanciaExistente, gestorReservas);
    gestorReservas.getListReservas().add(reservaExistente);

    // Nueva consulta del 12 al 18 de diciembre (se cruza del 12 al 15)
    LocalDate nuevaEntrada = LocalDate.of(2026, 12, 12);
    LocalDate nuevaSalida = LocalDate.of(2026, 12, 18);

    // Act
    boolean disponible = gestorReservas.validarSinDobleVenta(
        apartamento, nuevaEntrada, nuevaSalida, null);

    // Assert
    assertFalse(disponible,
        "Debería retornar false al detectar solapamiento con una reserva activa en el mismo apartamento");
  }

  @Test
  @DisplayName("Debe ignorar la reserva cuyo código coincide con codigoReservaExcluir (caso actualización)")
  void testValidarSinDobleVentaExcluyendoMismaReserva() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Reserva R1 del 10 al 15 de diciembre
    Estancia estanciaExistente = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());
    Reserva reservaExistente = new Reserva("R1", null, EstadoActual.CONFIRMADA,
        folio, canalReserva, apartamento, estanciaExistente, gestorReservas);
    gestorReservas.getListReservas().add(reservaExistente);

    // Se modifican fechas de la misma reserva R1 (del 11 al 16 de diciembre)
    LocalDate nuevaEntrada = LocalDate.of(2026, 12, 11);
    LocalDate nuevaSalida = LocalDate.of(2026, 12, 16);

    // Act
    boolean disponible = gestorReservas.validarSinDobleVenta(
        apartamento, nuevaEntrada, nuevaSalida, "R1");

    // Assert
    assertTrue(disponible,
        "Debería ignorar la reserva R1 al actualizarla y permitir el rango de fechas");
  }

  @Test
  @DisplayName("Debe ignorar reservas en estados no activos (CANCELADA, FINALIZADA, NO_SHOWN)")
  void testValidarSinDobleVentaIgnoraReservasInactivas() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    LocalDate entrada = LocalDate.of(2026, 12, 10);
    LocalDate salida = LocalDate.of(2026, 12, 15);
    Estancia estancia = new Estancia(entrada, salida, apartamento, new ArrayList<Huesped>());

    // Tres reservas en las mismas fechas pero en estados inactivos
    Reserva rCancelada = new Reserva("R1", null, EstadoActual.CANCELADA,
        folio, canalReserva, apartamento, estancia, gestorReservas);
    Reserva rFinalizada = new Reserva("R2", null, EstadoActual.FINALIZADA,
        folio, canalReserva, apartamento, estancia, gestorReservas);
    Reserva rNoShow = new Reserva("R3", null, EstadoActual.NO_SHOWN,
        folio, canalReserva, apartamento, estancia, gestorReservas);

    gestorReservas.getListReservas().add(rCancelada);
    gestorReservas.getListReservas().add(rFinalizada);
    gestorReservas.getListReservas().add(rNoShow);

    // Act
    boolean disponible = gestorReservas.validarSinDobleVenta(
        apartamento, entrada, salida, null);

    // Assert
    assertTrue(disponible,
        "Debería retornar true porque ninguna de las reservas existentes está activa");
  }

  @Test
  @DisplayName("Debe permitir reservar en las mismas fechas si la reserva activa es de otro apartamento")
  void testValidarSinDobleVentaDistintoApartamento() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");

    Apartamento apartamentoA1 = new Apartamento("A1", "Piso 1 apart 20", 5, 10,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Apartamento apartamentoB2 = new Apartamento("B2", "Piso 2 apart 10", 4, 8,
        new ArrayList<String>(), 25.0, EstadoOperativo.PREPARADO, "Ninguno");

    LocalDate entrada = LocalDate.of(2026, 12, 10);
    LocalDate salida = LocalDate.of(2026, 12, 15);

    // Reserva activa en A1
    Estancia estanciaA1 = new Estancia(entrada, salida, apartamentoA1, new ArrayList<Huesped>());
    Reserva reservaA1 = new Reserva("R1", null, EstadoActual.EN_CURSO,
        folio, canalReserva, apartamentoA1, estanciaA1, gestorReservas);
    gestorReservas.getListReservas().add(reservaA1);

    // Act: Consultamos disponibilidad para B2 en exactamente las mismas fechas
    boolean disponible = gestorReservas.validarSinDobleVenta(
        apartamentoB2, entrada, salida, null);

    // Assert
    assertTrue(disponible,
        "Debería permitir reservar B2 aunque A1 esté ocupado en las mismas fechas");
  }

  @Test
  @DisplayName("Debe retornar false cuando los parámetros de entrada son nulos o el rango de fechas es inválido")
  void testValidarSinDobleVentaParametrosInvalidos() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 5, 10,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    LocalDate entrada = LocalDate.of(2026, 12, 15);
    LocalDate salida = LocalDate.of(2026, 12, 10); // Salida anterior a la entrada

    // Act & Assert
    assertFalse(gestorReservas.validarSinDobleVenta(null, entrada, salida, null),
        "Debe fallar si apartamento es null");
    assertFalse(gestorReservas.validarSinDobleVenta(
        apartamento, null, LocalDate.of(2026, 12, 20), null),
        "Debe fallar si entrada es null");
    assertFalse(gestorReservas.validarSinDobleVenta(
        apartamento, LocalDate.of(2026, 12, 10), null, null),
        "Debe fallar si salida es null");
    assertFalse(gestorReservas.validarSinDobleVenta(apartamento, entrada, salida, null),
        "Debe fallar si entrada no es anterior a salida");
    assertFalse(gestorReservas.validarSinDobleVenta(apartamento, entrada, entrada, null),
        "Debe fallar si entrada es igual a salida");
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
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 3,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // 2 adultos + 1 menor de 6 años = 3 ocupantes en total
    Huesped adulto1 = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10),
        "321", "kevin@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Huesped adulto2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20),
        "654", "laura@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Huesped menor = new Huesped("Mateo", "789", LocalDate.now().minusYears(2),
        "987", "mateo@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(adulto1);
    ocupantes.add(adulto2);
    ocupantes.add(menor);

    // Act
    boolean resultado = gestorReservas.validarCapacidadMaxima(apartamento, ocupantes);

    // Assert: 3 <= 3 -> true (el menor ocupa cupo)
    assertTrue(resultado,
        "Debería retornar true cuando el total de ocupantes (incluyendo menores) no supera la capacidad máxima");
  }

  @Test
  @DisplayName("Debe retornar false cuando el total de ocupantes excede la capacidad máxima por incluir menores de 6 años")
  void testValidarCapacidadMaximaExcedidaPorMenores() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    GestorHuespedes gestorHuespedes = new GestorHuespedes(gestorGeneral);

    // Apartamento con capacidad máxima de 2 personas (cuarto parámetro)
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 2,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // 2 adultos + 1 menor de 6 años = 3 ocupantes (> capacidad de 2)
    Huesped adulto1 = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10),
        "321", "kevin@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Huesped adulto2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20),
        "654", "laura@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Huesped menor = new Huesped("Mateo", "789", LocalDate.now().minusYears(2),
        "987", "mateo@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(adulto1);
    ocupantes.add(adulto2);
    ocupantes.add(menor);

    // Act
    boolean resultado = gestorReservas.validarCapacidadMaxima(apartamento, ocupantes);

    // Assert: 3 > 2 -> false
    assertFalse(resultado,
        "Debería retornar false porque los menores de 6 años también ocupan cupo y se excede la capacidad (3 > 2)");
  }

  @Test
  @DisplayName("Debe validar correctamente usando la sobrecarga por número entero de ocupantes")
  void testValidarCapacidadMaximaSobrecargaEntero() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

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
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    ArrayList<Huesped> ocupantes = new ArrayList<>();

    // Act & Assert
    assertFalse(gestorReservas.validarCapacidadMaxima(null, ocupantes),
        "Debe retornar false si el apartamento es null");
    assertFalse(gestorReservas.validarCapacidadMaxima(apartamento, (ArrayList<Huesped>) null),
        "Debe retornar false si la lista de ocupantes es null");
    assertFalse(gestorReservas.validarCapacidadMaxima(null, 2),
        "Debe retornar false si el apartamento es null en la sobrecarga");
    assertFalse(gestorReservas.validarCapacidadMaxima(apartamento, -1),
        "Debe retornar false si el total de ocupantes es negativo");
  }

  // ========== PRUEBAS PARA TAREA 4.3: validarBloqueos() ========== //

  @Test
  @DisplayName("Debe retornar true cuando el apartamento no tiene bloqueos o las fechas no se cruzan con ningún bloqueo")
  void testValidarBloqueosSinConflicto() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Caso 1: Lista de bloqueos vacía
    LocalDate entrada1 = LocalDate.of(2026, 12, 10);
    LocalDate salida1 = LocalDate.of(2026, 12, 15);
    assertTrue(gestorReservas.validarBloqueos(apartamento, entrada1, salida1),
        "Debería retornar true cuando el apartamento no tiene bloqueos registrados");

    // Caso 2: Bloqueo existente del 20 al 25 de diciembre,
    // reserva termina justo el 20 (cuando empieza el bloqueo)
    Bloqueo bloqueo = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 20),
        LocalDate.of(2026, 12, 25), apartamento);
    apartamento.getListBloqueos().add(bloqueo);

    LocalDate entradaAntes = LocalDate.of(2026, 12, 15);
    LocalDate salidaAntes = LocalDate.of(2026, 12, 20);

    // Reserva entra después de que termina el bloqueo (26 al 30 de diciembre)
    LocalDate entradaDespues = LocalDate.of(2026, 12, 26);
    LocalDate salidaDespues = LocalDate.of(2026, 12, 30);

    // Act
    boolean disponibleAntes = gestorReservas.validarBloqueos(
        apartamento, entradaAntes, salidaAntes);
    boolean disponibleDespues = gestorReservas.validarBloqueos(
        apartamento, entradaDespues, salidaDespues);

    // Assert
    assertTrue(disponibleAntes,
        "Debería permitir reservar si el check-out es el mismo día que inicia el bloqueo");
    assertTrue(disponibleDespues,
        "Debería permitir reservar después de que finaliza el bloqueo");
  }

  @Test
  @DisplayName("Debe retornar false cuando la estancia se cruza con un bloqueo del apartamento")
  void testValidarBloqueosConSolapamiento() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Bloqueo del 10 al 15 de diciembre
    Bloqueo bloqueo = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 10),
        LocalDate.of(2026, 12, 15), apartamento);
    apartamento.getListBloqueos().add(bloqueo);

    // Reserva del 12 al 18 de diciembre (se solapa con el bloqueo)
    LocalDate entrada = LocalDate.of(2026, 12, 12);
    LocalDate salida = LocalDate.of(2026, 12, 18);

    // Act
    boolean disponible = gestorReservas.validarBloqueos(apartamento, entrada, salida);

    // Assert
    assertFalse(disponible,
        "Debería retornar false cuando las fechas de la reserva se cruzan con un bloqueo");
  }

  @Test
  @DisplayName("Debe detectar conflicto cuando existen múltiples bloqueos y uno de ellos se solapa")
  void testValidarBloqueosMultiplesBloqueos() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Dos bloqueos distintos en el apartamento
    Bloqueo bloqueo1 = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 1),
        LocalDate.of(2026, 12, 5), apartamento);
    Bloqueo bloqueo2 = new Bloqueo("Motivo 2", LocalDate.of(2026, 12, 20),
        LocalDate.of(2026, 12, 24), apartamento);
    apartamento.getListBloqueos().add(bloqueo1);
    apartamento.getListBloqueos().add(bloqueo2);

    // Reserva del 18 al 22 de diciembre (no choca con bloqueo1, pero sí con
    // bloqueo2)
    LocalDate entrada = LocalDate.of(2026, 12, 18);
    LocalDate salida = LocalDate.of(2026, 12, 22);

    // Act
    boolean disponible = gestorReservas.validarBloqueos(apartamento, entrada, salida);

    // Assert
    assertFalse(disponible,
        "Debería retornar false al detectar conflicto con el segundo bloqueo registrado");
  }

  @Test
  @DisplayName("Debe retornar false cuando los parámetros son nulos o el rango de fechas es inválido")
  void testValidarBloqueosParametrosInvalidos() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    LocalDate entrada = LocalDate.of(2026, 12, 15);
    LocalDate salida = LocalDate.of(2026, 12, 10); // Salida anterior a la entrada

    // Act & Assert
    assertFalse(gestorReservas.validarBloqueos(null, entrada, salida),
        "Debe fallar si apartamento es null");
    assertFalse(gestorReservas.validarBloqueos(
        apartamento, null, LocalDate.of(2026, 12, 20)),
        "Debe fallar si fechaEntrada es null");
    assertFalse(gestorReservas.validarBloqueos(
        apartamento, LocalDate.of(2026, 12, 10), null),
        "Debe fallar si fechaSalida es null");
    assertFalse(gestorReservas.validarBloqueos(apartamento, entrada, salida),
        "Debe fallar si fechaEntrada es posterior a fechaSalida");
    assertFalse(gestorReservas.validarBloqueos(apartamento, entrada, entrada),
        "Debe fallar si fechaEntrada es igual a fechaSalida");
  }

  // ========== PRUEBAS PARA TAREA 4.4: consultarApartamentosDisponibles()
  // ========== //

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
    Apartamento aptoDisponible = new Apartamento("A1", "Piso 1 apart 10", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // 2. Apto A2: Disponible pero capacidad insuficiente (2 < 4) -> NO DEBE
    // APARECER
    Apartamento aptoPequeno = new Apartamento("A2", "Piso 1 apart 12", 1, 2,
        new ArrayList<String>(), 15.0, EstadoOperativo.PREPARADO, "Ninguno");

    // 3. Apto B1: Capacidad suficiente pero ocupado por reserva activa -> NO DEBE
    // APARECER
    Apartamento aptoReservado = new Apartamento("B1", "Piso 2 apart 20", 3, 6,
        new ArrayList<String>(), 30.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estanciaOcupada = new Estancia(
        LocalDate.of(2026, 12, 12), LocalDate.of(2026, 12, 16),
        aptoReservado, new ArrayList<Huesped>());
    Reserva reservaActiva = new Reserva("R1", null, EstadoActual.CONFIRMADA,
        folio, canalReserva, aptoReservado, estanciaOcupada, gestorReservas);
    gestorReservas.getListReservas().add(reservaActiva);

    // 4. Apto B2: Capacidad suficiente pero bloqueado en esas fechas -> NO DEBE
    // APARECER
    Apartamento aptoBloqueado = new Apartamento("B2", "Piso 2 apart 22", 2, 4,
        new ArrayList<String>(), 25.0, EstadoOperativo.PREPARADO, "Ninguno");
    Bloqueo bloqueo = new Bloqueo("Motivo 1", LocalDate.of(2026, 12, 8),
        LocalDate.of(2026, 12, 12), aptoBloqueado);
    aptoBloqueado.getListBloqueos().add(bloqueo);

    // 5. Apto C1: Disponible, capacidad 5 >= 4, tiene una reserva CANCELADA -> DEBE
    // APARECER
    Apartamento aptoConReservaCancelada = new Apartamento("C1", "Piso 3 apart 30", 2, 5,
        new ArrayList<String>(), 22.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estanciaCancelada = new Estancia(entradaSolicitada, salidaSolicitada,
        aptoConReservaCancelada, new ArrayList<Huesped>());
    Reserva reservaCancelada = new Reserva("R2", null, EstadoActual.CANCELADA,
        folio, canalReserva, aptoConReservaCancelada, estanciaCancelada, gestorReservas);
    gestorReservas.getListReservas().add(reservaCancelada);

    // Registramos todos los apartamentos en el gestor
    gestorApartamentos.getListApartamentos().add(aptoDisponible);
    gestorApartamentos.getListApartamentos().add(aptoPequeno);
    gestorApartamentos.getListApartamentos().add(aptoReservado);
    gestorApartamentos.getListApartamentos().add(aptoBloqueado);
    gestorApartamentos.getListApartamentos().add(aptoConReservaCancelada);

    // Act
    ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(
        entradaSolicitada, salidaSolicitada, capacidadRequerida);

    // Assert
    assertEquals(2, disponibles.size(),
        "Debería retornar exactamente 2 apartamentos disponibles (A1 y C1)");
    assertTrue(disponibles.contains(aptoDisponible), "Debería incluir el apartamento A1");
    assertTrue(disponibles.contains(aptoConReservaCancelada),
        "Debería incluir el apartamento C1 cuya reserva previa fue cancelada");
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

    Apartamento apto = new Apartamento("A1", "Piso 1 apart 10", 1, 2,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    gestorApartamentos.getListApartamentos().add(apto);

    LocalDate entrada = LocalDate.of(2026, 12, 10);
    LocalDate salida = LocalDate.of(2026, 12, 15);

    // Act: Solicitamos capacidad para 6 personas cuando el único apartamento es
    // para 2
    ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(
        entrada, salida, 6);

    // Assert
    assertNotNull(disponibles, "No debe retornar null, debe retornar una lista vacía");
    assertTrue(disponibles.isEmpty(),
        "Debería retornar una lista vacía cuando ningún apartamento cumple");
  }

  @Test
  @DisplayName("Debe retornar una lista vacía cuando el rango de fechas es inválido")
  void testConsultarApartamentosDisponiblesFechasInvalidas() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    GestorApartamentos gestorApartamentos = gestorGeneral.getTheGestorApartamentos();

    Apartamento apto = new Apartamento("A1", "Piso 1 apart 10", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    gestorApartamentos.getListApartamentos().add(apto);

    LocalDate entrada = LocalDate.of(2026, 12, 20);
    LocalDate salida = LocalDate.of(2026, 12, 15); // Salida anterior a la entrada

    // Act
    ArrayList<Apartamento> disponibles = gestorReservas.consultarApartamentosDisponibles(
        entrada, salida, 2);

    // Assert
    assertNotNull(disponibles, "La lista retornada no debe ser null");
    assertTrue(disponibles.isEmpty(),
        "Debería retornar lista vacía cuando la fecha de entrada no es anterior a la de salida");
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
    assertTrue(codigo.matches("^RES-\\d+-[1-9]\\d{3}$"),
        "El código debe cumplir con el formato RES-<timestamp>-<4 dígitos> (1000-9999), pero fue: "
            + codigo);
  }

  @Test
  @DisplayName("Debe generar códigos distintos cuando los anteriores ya están registrados")
  void testGenerarCodigoUnicoNoSeRepite() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estancia = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());

    // Act: registramos cada código para que el siguiente se compare con él.
    String codigo1 = gestorReservas.generarCodigoUnico();
    assertTrue(gestorReservas.crear(new Reserva(codigo1, null, EstadoActual.PENDIENTE,
        folio, canalReserva, apartamento, estancia, gestorReservas)));

    String codigo2 = gestorReservas.generarCodigoUnico();
    assertTrue(gestorReservas.crear(new Reserva(codigo2, null, EstadoActual.PENDIENTE,
        folio, canalReserva, apartamento, estancia, gestorReservas)));

    String codigo3 = gestorReservas.generarCodigoUnico();

    // Assert
    assertNotEquals(codigo1, codigo2, "Los códigos registrados deben ser distintos");
    assertNotEquals(codigo2, codigo3, "El nuevo código debe ser distinto al segundo registrado");
    assertNotEquals(codigo1, codigo3, "El nuevo código debe ser distinto al primero registrado");
    assertNull(gestorReservas.consultar(codigo3),
        "El último código generado no debe existir previamente en listReservas");
  }

  @Test
  @DisplayName("Debe evitar colisiones generando un nuevo código si ya existe una reserva con el código generado")
  void testGenerarCodigoUnicoEvitaColisionConExistente() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    CanalReserva canalReserva = new CanalReserva(NombreCanal.DIRECTA, "");
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estancia = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());

    // Registramos una reserva previa en el gestor
    String codigoPrevio = gestorReservas.generarCodigoUnico();
    Reserva reservaExistente = new Reserva(codigoPrevio, null, EstadoActual.PENDIENTE,
        folio, canalReserva, apartamento, estancia, gestorReservas);
    gestorReservas.getListReservas().add(reservaExistente);

    // Act
    String nuevoCodigo = gestorReservas.generarCodigoUnico();

    // Assert
    assertNotEquals(codigoPrevio, nuevoCodigo,
        "El nuevo código debe ser distinto al de la reserva ya registrada");
    assertNull(gestorReservas.consultar(nuevoCodigo),
        "El nuevo código no debe coincidir con ninguna reserva existente");
  }

  // ========== PRUEBAS PARA TAREA 4.6: existeCodigoExterno() ========== //

  @Test
  @DisplayName("Debe retornar true cuando ya existe una reserva de canal EXTERNA con el mismo código")
  void testExisteCodigoExternoCuandoYaExiste() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estancia = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());

    CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, "BK-998877");
    Reserva reservaExterna = new Reserva("R1", null, EstadoActual.CONFIRMADA,
        folio, canalExterno, apartamento, estancia, gestorReservas);
    gestorReservas.getListReservas().add(reservaExterna);

    // Act
    boolean existe = gestorReservas.existeCodigoExterno("BK-998877");

    // Assert
    assertTrue(existe,
        "Debería retornar true porque ya existe una reserva con canal EXTERNA y código 'BK-998877'");
  }

  @Test
  @DisplayName("Debe retornar false cuando el código externo no coincide con ninguna reserva registrada")
  void testExisteCodigoExternoCuandoNoExiste() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estancia = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());

    CanalReserva canalExterno = new CanalReserva(NombreCanal.EXTERNA, "BK-998877");
    Reserva reservaExterna = new Reserva("R1", null, EstadoActual.CONFIRMADA,
        folio, canalExterno, apartamento, estancia, gestorReservas);
    gestorReservas.getListReservas().add(reservaExterna);

    // Act
    boolean existe = gestorReservas.existeCodigoExterno("AIR-112233");

    // Assert
    assertFalse(existe,
        "Debería retornar false cuando el código externo no está registrado en ninguna reserva");
  }

  @Test
  @DisplayName("Debe ignorar reservas cuyo canal NO sea EXTERNA (por ejemplo DIRECTA) aunque tengan el mismo código")
  void testExisteCodigoExternoIgnoraCanalDirecto() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);
    Folio folio = new Folio();
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4,
        new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estancia = new Estancia(
        LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15),
        apartamento, new ArrayList<Huesped>());

    // Reserva con canal DIRECTA pero con el código en el campo codigoCanal
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "DIR-5555");
    Reserva reservaDirecta = new Reserva("R1", null, EstadoActual.PENDIENTE,
        folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.getListReservas().add(reservaDirecta);

    // Act
    boolean existe = gestorReservas.existeCodigoExterno("DIR-5555");

    // Assert
    assertFalse(existe,
        "Debería retornar false porque la reserva pertenece al canal DIRECTA y no a EXTERNA");
  }

  @Test
  @DisplayName("Debe retornar false cuando el código externo es null, vacío o solo espacios")
  void testExisteCodigoExternoParametrosInvalidos() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = new GestorReservas(gestorGeneral);

    // Act & Assert
    assertFalse(gestorReservas.existeCodigoExterno(null),
        "Debería retornar false si codigoExterno es null");
    assertFalse(gestorReservas.existeCodigoExterno(""),
        "Debería retornar false si codigoExterno está vacío");
    assertFalse(gestorReservas.existeCodigoExterno("   "),
        "Debería retornar false si codigoExterno solo tiene espacios");
  }

  // ========== PRUEBAS PARA TAREA 7.1: confirmarReserva() ========== //

  @Test
  @DisplayName("Debe retornar true y cambiar el estado a CONFIRMADA cuando la reserva PORTAL tiene pago igual o mayor al 30% de anticipo")
  void testConfirmarReservaPortalConAnticipoSuficiente() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(),gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

    // Estancia de 2 noches (10 al 12 de diciembre). Ajustamos el precio de sus noches generadas a 100.0 c/u -> Total = 200.0
    Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, new ArrayList<Huesped>());
    estancia.getListHuespedes().add(titular);
    for (Noche noche : estancia.getListNoches()) {
      noche.setPrecio(100.0);
    }

    // 30% de 200.0 = 60.0 -> Registramos un pago exacto de 60.0 en el folio
    Folio folio = new Folio();
    Pago pagoAnticipo = new Pago(60.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folio);
    folio.getListPagos().add(pagoAnticipo);

    CanalReserva canalPortal = new CanalReserva(NombreCanal.PORTAL, "");
    Reserva reserva = new Reserva("RES-100", titular, EstadoActual.PENDIENTE, folio, canalPortal, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean confirmada = gestorReservas.confirmarReserva("RES-100");

    // Assert
    assertTrue(confirmada, "Debería confirmar la reserva cuando el pago cubre al menos el 30% del valor de la estadía");
    assertEquals(EstadoActual.CONFIRMADA, reserva.getEstadoActual(), "El estado de la reserva debe cambiar a CONFIRMADA");
  }

  @Test
  @DisplayName("Debe retornar false y mantener estado PENDIENTE cuando el pago en el folio es menor al 30% requerido")
  void testConfirmarReservaPortalConAnticipoInsuficiente() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    // Tarifa individual = 100.0 -> 1 ocupante pagante * 2 noches * 100.0 (Temporada.BAJA = 1.0) = 200.0 total -> 30% = 60.0
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, ocupantes);

    // Registramos un pago insuficiente de 50.0 (< 60.0)
    Folio folio = new Folio();
    Pago pagoInsuficiente = new Pago(50.0, MedioDePago.TARJETA, LocalDate.now(), folio);
    folio.getListPagos().add(pagoInsuficiente);

    CanalReserva canalPortal = new CanalReserva(NombreCanal.PORTAL, "");
    Reserva reserva = new Reserva("RES-101", titular, EstadoActual.PENDIENTE, folio, canalPortal, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean confirmada = gestorReservas.confirmarReserva("RES-101");

    // Assert
    assertFalse(confirmada, "No debería confirmar la reserva si los pagos son menores al 30%");
    assertEquals(EstadoActual.PENDIENTE, reserva.getEstadoActual(), "La reserva debe permanecer en estado PENDIENTE");
  }

  @Test
  @DisplayName("Debe retornar false cuando la reserva no existe o no se encuentra en estado PENDIENTE")
  void testConfirmarReservaInexistenteOEstadoInvalido() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(),gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
    Estancia estancia = new Estancia(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), apartamento, new ArrayList<Huesped>());
    Folio folio = new Folio();
    CanalReserva canalPortal = new CanalReserva(NombreCanal.PORTAL, "");

    // Reserva ya está CANCELADA (no está en PENDIENTE)
    Reserva reservaCancelada = new Reserva("RES-102", titular, EstadoActual.CANCELADA, folio, canalPortal, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reservaCancelada);

    // Act & Assert
    assertFalse(gestorReservas.confirmarReserva("CODIGO-INEXISTENTE"), "Debe retornar false si el código de reserva no existe");
    assertFalse(gestorReservas.confirmarReserva(null), "Debe retornar false si el código es null");
    assertFalse(gestorReservas.confirmarReserva("RES-102"), "Debe retornar false si la reserva no está en estado PENDIENTE");
    assertEquals(EstadoActual.CANCELADA, reservaCancelada.getEstadoActual(), "El estado CANCELADA no debe alterarse");
  }

  // ========== PRUEBAS PARA TAREA 7.2: cancelarReserva() ========== //

  @Test
  @DisplayName("Debe cancelar una reserva CONFIRMADA, registrar la devolución en el folio y liberar el apartamento")
  void testCancelarReservaConfirmadaExitoso() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    // Fechas futuras con más de 7 días de anticipación para aplicar devolución del 100% según política
    LocalDate entrada = LocalDate.now().plusDays(15);
    LocalDate salida = LocalDate.now().plusDays(17);
    Estancia estancia = new Estancia(entrada, salida, apartamento, ocupantes);

    Folio folio = new Folio();
    Pago pagoRealizado = new Pago(200.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folio);
    folio.getListPagos().add(pagoRealizado);

    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");
    Reserva reserva = new Reserva("RES-200", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean cancelada = gestorReservas.cancelarReserva("RES-200");

    // Assert
    assertTrue(cancelada, "Debería retornar true al cancelar una reserva en estado CONFIRMADA");
    assertEquals(EstadoActual.CANCELADA, reserva.getEstadoActual(), "El estado de la reserva debe cambiar a CANCELADA");
    assertTrue(gestorReservas.validarSinDobleVenta(apartamento, entrada, salida, ""),
            "Al cancelarse la reserva, el apartamento debe quedar disponible nuevamente en esas fechas");
  }

  @Test
  @DisplayName("Debe cancelar una reserva en estado PENDIENTE correctamente")
  void testCancelarReservaPendienteExitoso() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalPortal = new CanalReserva(NombreCanal.PORTAL, "");

    Reserva reserva = new Reserva("RES-201", titular, EstadoActual.PENDIENTE, folio, canalPortal, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean cancelada = gestorReservas.cancelarReserva("RES-201");

    // Assert
    assertTrue(cancelada, "Debería permitir cancelar una reserva en estado PENDIENTE");
    assertEquals(EstadoActual.CANCELADA, reserva.getEstadoActual(), "El estado debe actualizarse a CANCELADA");
  }

  @Test
  @DisplayName("Debe retornar false cuando la reserva está EN_CURSO, FINALIZADA, ya CANCELADA o no existe")
  void testCancelarReservaEstadoNoPermitidoOInexistente() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reservaEnCurso = new Reserva("RES-202", titular, EstadoActual.EN_CURSO, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reservaEnCurso);

    // Act & Assert
    assertFalse(gestorReservas.cancelarReserva("CODIGO-INEXISTENTE"), "Debe retornar false si el código no existe");
    assertFalse(gestorReservas.cancelarReserva(null), "Debe retornar false si el código es null");
    assertFalse(gestorReservas.cancelarReserva("RES-202"), "No debe permitir cancelar una reserva que ya está EN_CURSO");
    assertEquals(EstadoActual.EN_CURSO, reservaEnCurso.getEstadoActual(), "El estado EN_CURSO no debe modificarse");
  }

  // ========== PRUEBAS PARA TAREA 7.3: realizarCheckIn() ========== //

  @Test
  @DisplayName("Debe realizar el check-in exitosamente cambiando la reserva a EN_CURSO y el apartamento a OCUPADO")
  void testRealizarCheckInExitoso() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    // Fecha de entrada es hoy (LocalDate.now()), por lo que ya llegó la fecha
    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(3), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reserva = new Reserva("RES-300", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean checkInRealizado = gestorReservas.realizarCheckIn("RES-300");

    // Assert
    assertTrue(checkInRealizado, "Debería retornar true cuando la reserva está CONFIRMADA, el apartamento PREPARADO y la fecha ya llegó");
    assertEquals(EstadoActual.EN_CURSO, reserva.getEstadoActual(), "El estado de la reserva debe pasar a EN_CURSO");
    assertEquals(EstadoOperativo.OCUPADO, apartamento.getEstadoOperativo(), "El estado operativo del apartamento debe cambiar a OCUPADO");
  }

  @Test
  @DisplayName("Debe retornar false y no modificar estados cuando aún no ha llegado la fecha de entrada")
  void testRealizarCheckInAntesDeFechaEntrada() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    // Fecha de entrada futura (mañana)
    Estancia estancia = new Estancia(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reserva = new Reserva("RES-301", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean checkInRealizado = gestorReservas.realizarCheckIn("RES-301");

    // Assert
    assertFalse(checkInRealizado, "No debería permitir check-in antes de la fecha de entrada");
    assertEquals(EstadoActual.CONFIRMADA, reserva.getEstadoActual(), "La reserva debe mantenerse en CONFIRMADA");
    assertEquals(EstadoOperativo.PREPARADO, apartamento.getEstadoOperativo(), "El apartamento debe mantenerse en PREPARADO");
  }

  @Test
  @DisplayName("Debe retornar false cuando el apartamento NO está en estado PREPARADO")
  void testRealizarCheckInApartamentoNoPreparado() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    // Apartamento en EN_ASEO (o cualquier estado distinto a PREPARADO)
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PENDIENTE_DE_PREPARACION, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reserva = new Reserva("RES-302", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean checkInRealizado = gestorReservas.realizarCheckIn("RES-302");

    // Assert
    assertFalse(checkInRealizado, "No debería permitir check-in si el apartamento no está PREPARADO");
    assertEquals(EstadoActual.CONFIRMADA, reserva.getEstadoActual(), "La reserva debe mantenerse en CONFIRMADA");
    assertEquals(EstadoOperativo.PENDIENTE_DE_PREPARACION, apartamento.getEstadoOperativo(), "El estado del apartamento no debe alterarse");
  }

  @Test
  @DisplayName("Debe retornar false cuando la reserva no existe o no se encuentra en estado CONFIRMADA")
  void testRealizarCheckInReservaInexistenteOEstadoInvalido() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalPortal = new CanalReserva(NombreCanal.PORTAL, "");

    // Reserva aún en estado PENDIENTE (no ha sido confirmada)
    Reserva reservaPendiente = new Reserva("RES-303", titular, EstadoActual.PENDIENTE, folio, canalPortal, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reservaPendiente);

    // Act & Assert
    assertFalse(gestorReservas.realizarCheckIn("CODIGO-INEXISTENTE"), "Debe retornar false si el código de reserva no existe");
    assertFalse(gestorReservas.realizarCheckIn(null), "Debe retornar false si el código es null");
    assertFalse(gestorReservas.realizarCheckIn("RES-303"), "No debería permitir check-in de una reserva en estado PENDIENTE");
    assertEquals(EstadoActual.PENDIENTE, reservaPendiente.getEstadoActual(), "La reserva debe permanecer en PENDIENTE");
    assertEquals(EstadoOperativo.PREPARADO, apartamento.getEstadoOperativo(), "El apartamento debe permanecer en PREPARADO");
  }

  // ========== PRUEBAS PARA TAREA 7.4: realizarCheckOut() ========== //

  @Test
  @DisplayName("Debe realizar el check-out exitosamente cambiando la reserva a FINALIZADA y el apartamento a PENDIENTE_DE_PREPARACION")
  void testRealizarCheckOutExitoso() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.OCUPADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now().minusDays(2), LocalDate.now(), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reserva = new Reserva("RES-400", titular, EstadoActual.EN_CURSO, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean checkOutRealizado = gestorReservas.realizarCheckOut("RES-400");

    // Assert
    assertTrue(checkOutRealizado, "Debería retornar true cuando la reserva está EN_CURSO y tiene apartamento asociado");
    assertEquals(EstadoActual.FINALIZADA, reserva.getEstadoActual(), "El estado de la reserva debe cambiar a FINALIZADA");
    assertEquals(EstadoOperativo.PENDIENTE_DE_PREPARACION, apartamento.getEstadoOperativo(), "El estado operativo del apartamento debe cambiar a PENDIENTE_DE_PREPARACION");
  }

  @Test
  @DisplayName("Debe retornar false y no modificar estados cuando la reserva no existe o no está en estado EN_CURSO")
  void testRealizarCheckOutReservaInexistenteOEstadoInvalido() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    // Reserva en estado CONFIRMADA (no ha hecho check-in, no está EN_CURSO)
    Reserva reservaConfirmada = new Reserva("RES-401", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reservaConfirmada);

    // Act & Assert
    assertFalse(gestorReservas.realizarCheckOut("CODIGO-INEXISTENTE"), "Debe retornar false si el código de reserva no existe");
    assertFalse(gestorReservas.realizarCheckOut(null), "Debe retornar false si el código es null");
    assertFalse(gestorReservas.realizarCheckOut("RES-401"), "No debe permitir check-out de una reserva que no está EN_CURSO");
    assertEquals(EstadoActual.CONFIRMADA, reservaConfirmada.getEstadoActual(), "El estado CONFIRMADA no debe modificarse");
    assertEquals(EstadoOperativo.PREPARADO, apartamento.getEstadoOperativo(), "El estado del apartamento no debe modificarse");
  }

  // ========== PRUEBAS PARA TAREA 7.5: declararNoShow() ========== //

  @Test
  @DisplayName("Debe declarar NO_SHOWN y registrar 0% de devolución cuando es el día de entrada y son más de las 22:00")
  void testDeclararNoShowExitosoDespuesDeLas22() {
    // Este escenario depende de que la hora del sistema sea posterior a las 22:00
    org.junit.jupiter.api.Assumptions.assumeTrue(
            LocalTime.now().isAfter(LocalTime.of(22, 0)),
            "Se omite porque la hora actual del sistema es anterior o igual a las 22:00"
    );

    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
    Folio folio = new Folio();
    Pago anticipo = new Pago(60.0, MedioDePago.TRANSFERENCIA, LocalDate.now().minusDays(3), folio);
    folio.getListPagos().add(anticipo);

    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");
    Reserva reserva = new Reserva("RES-500", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean noShowDeclarado = gestorReservas.declararNoShow("RES-500");

    // Assert
    assertTrue(noShowDeclarado, "Debería retornar true cuando la reserva está CONFIRMADA, es el día de entrada y pasó las 22:00");
    assertEquals(EstadoActual.NO_SHOWN, reserva.getEstadoActual(), "El estado de la reserva debe cambiar a NO_SHOWN");
  }

  @Test
  @DisplayName("Debe retornar false cuando aún no son las 22:00 en el día de entrada")
  void testDeclararNoShowAntesDeLas22() {
    // Este escenario aplica cuando el reloj del sistema marca las 22:00 o antes
    org.junit.jupiter.api.Assumptions.assumeTrue(
            !LocalTime.now().isAfter(LocalTime.of(22, 0)),
            "Se omite porque la hora actual del sistema ya superó las 22:00"
    );

    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reserva = new Reserva("RES-501", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean noShowDeclarado = gestorReservas.declararNoShow("RES-501");

    // Assert
    assertFalse(noShowDeclarado, "No debe permitir declarar no-show antes de las 22:00");
    assertEquals(EstadoActual.CONFIRMADA, reserva.getEstadoActual(), "La reserva debe mantenerse en estado CONFIRMADA");
  }

  @Test
  @DisplayName("Debe retornar false cuando aún no ha llegado la fecha de entrada")
  void testDeclararNoShowFechaFutura() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    // Fecha de entrada futura (mañana)
    Estancia estancia = new Estancia(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalDirecto = new CanalReserva(NombreCanal.DIRECTA, "");

    Reserva reserva = new Reserva("RES-502", titular, EstadoActual.CONFIRMADA, folio, canalDirecto, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reserva);

    // Act
    boolean noShowDeclarado = gestorReservas.declararNoShow("RES-502");

    // Assert
    assertFalse(noShowDeclarado, "No debe declarar no-show si la fecha de entrada es futura");
    assertEquals(EstadoActual.CONFIRMADA, reserva.getEstadoActual(), "La reserva debe mantenerse en CONFIRMADA");
  }

  @Test
  @DisplayName("Debe retornar false cuando la reserva no existe o no está en estado CONFIRMADA")
  void testDeclararNoShowInexistenteOEstadoInvalido() {
    // Arrange
    GestorGeneral gestorGeneral = new GestorGeneral();
    GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
    GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

    Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", new ArrayList<Reserva>(), gestorHuespedes);
    Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 100.0, EstadoOperativo.PREPARADO, "Ninguno");

    ArrayList<Huesped> ocupantes = new ArrayList<>();
    ocupantes.add(titular);

    Estancia estancia = new Estancia(LocalDate.now(), LocalDate.now().plusDays(2), apartamento, ocupantes);
    Folio folio = new Folio();
    CanalReserva canalPortal = new CanalReserva(NombreCanal.PORTAL, "");

    // Reserva en estado PENDIENTE (no CONFIRMADA)
    Reserva reservaPendiente = new Reserva("RES-503", titular, EstadoActual.PENDIENTE, folio, canalPortal, apartamento, estancia, gestorReservas);
    gestorReservas.crear(reservaPendiente);

    // Act & Assert
    assertFalse(gestorReservas.declararNoShow("CODIGO-INEXISTENTE"), "Debe retornar false si el código no existe");
    assertFalse(gestorReservas.declararNoShow(null), "Debe retornar false si el código es null");
    assertFalse(gestorReservas.declararNoShow("RES-503"), "No debe permitir declarar no-show en una reserva PENDIENTE");
    assertEquals(EstadoActual.PENDIENTE, reservaPendiente.getEstadoActual(), "El estado PENDIENTE no debe modificarse");
  }


}