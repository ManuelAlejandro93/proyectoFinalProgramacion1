package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Reserva.Cargo;
import com.ApartaSuitesUQ.Reserva.Folio;
import com.ApartaSuitesUQ.Reserva.MedioDePago;
import com.ApartaSuitesUQ.Reserva.Pago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;

class FolioTest {

    private Folio folio;

    @BeforeEach
    void setUp() {
        folio = new Folio();
    }

    // ========== PRUEBAS PARA TAREA 1.1: CalcularSaldoNormal() ========== //

    @Test
    @DisplayName("Debe calcular el saldo correcto con cargos y pagos válidos")
    void testCalcularSaldoNormal() {
        // Arrange (Organizar)
        ArrayList<Cargo> cargos = new ArrayList<>();
        cargos.add(new Cargo("Cargo 1" , 150.0, folio)); // Asumiendo que Cargo tiene un constructor o setter con precio
        cargos.add(new Cargo("Cargo 2" , 50.0, folio));
        folio.setListCargos(cargos);

        ArrayList<Pago> pagos = new ArrayList<>();
        pagos.add(new Pago(80.0, MedioDePago.EFECTIVO, LocalDate.now(), folio));    // Asumiendo que Pago tiene un constructor o setter con monto
        folio.setListPagos(pagos);

        // Act (Actuar)
        double saldoCalculado = folio.calcularSaldo();

        // Assert (Afirmar) -> (150 + 50) - 80 = 120.0
        assertEquals(120.0, saldoCalculado, 0.001, "El saldo debería ser 120.0 (deuda del cliente)");
    }

    @Test
    @DisplayName("Debe dar saldo 0 si las listas listCargos y listPagos son null")
    void testCalcularSaldoConListasNull() {
        // Arrange
        folio.setListCargos(null);
        folio.setListPagos(null);

        // Act
        double saldoCalculado = folio.calcularSaldo();

        // Assert
        assertEquals(0.0, saldoCalculado, 0.001, "Si las listas son null, el saldo debe ser 0.0");
    }

    @Test
    @DisplayName("Debe calcular saldo negativo cuando los pagos superan a los cargos (crédito a favor)")
    void testCalcularSaldoCreditoAFavor() {
        // Arrange
        ArrayList<Cargo> cargos = new ArrayList<>();
        cargos.add(new Cargo("Cargo 1", 100.0, folio));
        folio.setListCargos(cargos);

        ArrayList<Pago> pagos = new ArrayList<>();
        pagos.add(new Pago(150.0, MedioDePago.TARJETA, LocalDate.of(2026, 11, 25), folio));
        folio.setListPagos(pagos);

        // Act
        double saldoCalculado = folio.calcularSaldo();

        // Assert -> 100 - 150 = -50.0
        assertEquals(-50.0, saldoCalculado, 0.001, "El saldo debería ser negativo (-50.0)");
    }

    @Test
    @DisplayName("Debe dar saldo 0 si ambas listas están vacías")
    void testCalcularSaldoListasVacias() {
        // Arrange
        folio.setListCargos(new ArrayList<>());
        folio.setListPagos(new ArrayList<>());

        // Act
        double saldoCalculado = folio.calcularSaldo();

        // Assert
        assertEquals(0.0, saldoCalculado, 0.001, "Listas vacías deben dar saldo 0.0");
    }

    // ========== PRUEBAS PARA TAREA 1.2: anadirPago() ========== //

    @Test
    @DisplayName("Debe añadir el pago exitosamente y retornar true cuando el pago es válido")
    void testAnadirPagoExitoso() {
        // Arrange
        Pago pagoValido = new Pago(100.0, MedioDePago.EFECTIVO, LocalDate.now(), folio);

        // Act
        boolean resultado = folio.anadirPago(pagoValido);

        // Assert
        assertTrue(resultado, "El método debe retornar true al registrar un pago válido");
        assertNotNull(folio.getListPagos(), "La lista de pagos no debe ser null");
        assertEquals(1, folio.getListPagos().size(), "La lista de pagos debe contener 1 elemento");
        assertTrue(folio.getListPagos().contains(pagoValido), "El pago añadido debe estar en la lista");
    }

    @Test
    @DisplayName("Debe retornar false al intentar añadir un objeto Pago null")
    void testAnadirPagoNull() {
        // Act
        boolean resultado = folio.anadirPago(null);

        // Assert
        assertFalse(resultado, "Debe retornar false si el pago es null");
    }

    @Test
    @DisplayName("Debe retornar false si el monto del pago es menor o igual a cero")
    void testAnadirPagoMontoInvalido() {
        // Arrange
        Pago pagoMontoCero = new Pago(0.0, MedioDePago.EFECTIVO, LocalDate.now(), folio);
        Pago pagoMontoNegativo = new Pago(-50.0, MedioDePago.TARJETA, LocalDate.now(), folio);

        // Act & Assert
        assertFalse(folio.anadirPago(pagoMontoCero), "Debe retornar false si el monto es 0");
        assertFalse(folio.anadirPago(pagoMontoNegativo), "Debe retornar false si el monto es negativo");
    }

    @Test
    @DisplayName("Debe retornar false si el medio de pago o la fecha son null")
    void testAnadirPagoDatosIncompletos() {
        // Arrange
        Pago pagoSinMedio = new Pago(100.0, null, LocalDate.now(), folio);
        Pago pagoSinFecha = new Pago(100.0, MedioDePago.EFECTIVO, null, folio);

        // Act & Assert
        assertFalse(folio.anadirPago(pagoSinMedio), "Debe retornar false si el medio de pago es null");
        assertFalse(folio.anadirPago(pagoSinFecha), "Debe retornar false si la fecha es null");
    }

    @Test
    @DisplayName("Debe inicializar listPagos si es null y añadir el pago correctamente")
    void testAnadirPagoInicializaListaNull() {
        // Arrange: Aseguramos que la lista sea null al inicio
        folio.setListPagos(null);
        Pago pagoValido = new Pago(200.0, MedioDePago.TRANSFERENCIA, LocalDate.now(), folio);

        // Act
        boolean resultado = folio.anadirPago(pagoValido);

        // Assert
        assertTrue(resultado, "Debe retornar true tras inicializar la lista y agregar el pago");
        assertNotNull(folio.getListPagos(), "La lista debió inicializarse automáticamente");
        assertEquals(1, folio.getListPagos().size(), "La lista debe contener exactamente 1 pago");
    }

    // ========== PRUEBAS PARA TAREA 1.3: anadirCargo() ========== //

    @Test
    @DisplayName("Debe añadir el cargo exitosamente y retornar true cuando el cargo es válido")
    void testAnadirCargoExitoso() {
        // Arrange
        Cargo cargoValido = new Cargo("Desayuno", 25.0, folio);

        // Act
        boolean resultado = folio.anadirCargo(cargoValido);

        // Assert
        assertTrue(resultado, "Debe retornar true al registrar un cargo válido");
        assertNotNull(folio.getListCargos(), "La lista de cargos no debe ser null");
        assertEquals(1, folio.getListCargos().size(), "La lista debe contener 1 elemento");
        assertTrue(folio.getListCargos().contains(cargoValido), "El cargo añadido debe estar en la lista");
    }

    @Test
    @DisplayName("Debe permitir añadir un cargo con precio igual a 0.0 (Servicio gratuito)")
    void testAnadirCargoPrecioCero() {
        // Arrange
        Cargo cargoGratis = new Cargo("Parqueadero Gratuito", 0.0, folio);

        // Act
        boolean resultado = folio.anadirCargo(cargoGratis);

        // Assert
        assertTrue(resultado, "Debe permitir registrar un cargo con precio 0");
        assertEquals(1, folio.getListCargos().size(), "La lista debe contener el cargo gratuito");
    }

    @Test
    @DisplayName("Debe retornar false al intentar añadir un objeto Cargo null")
    void testAnadirCargoNull() {
        // Act
        boolean resultado = folio.anadirCargo(null);

        // Assert
        assertFalse(resultado, "Debe retornar false si el cargo es null");
    }

    @Test
    @DisplayName("Debe retornar false si el precio del cargo es negativo")
    void testAnadirCargoPrecioNegativo() {
        // Arrange
        Cargo cargoNegativo = new Cargo("Descuento Inválido", -15.0, folio);

        // Act
        boolean resultado = folio.anadirCargo(cargoNegativo);

        // Assert
        assertFalse(resultado, "Debe retornar false si el precio del cargo es negativo");
    }

    @Test
    @DisplayName("Debe retornar false si el nombre del cargo es null o está vacío")
    void testAnadirCargoNombreInvalido() {
        // Arrange
        Cargo cargoNombreNull = new Cargo(null, 50.0, folio);
        Cargo cargoNombreVacio = new Cargo("   ", 50.0, folio);

        // Act & Assert
        assertFalse(folio.anadirCargo(cargoNombreNull), "Debe retornar false si el nombre del cargo es null");
        assertFalse(folio.anadirCargo(cargoNombreVacio), "Debe retornar false si el nombre está compuesto por espacios en blanco");
    }

    @Test
    @DisplayName("Debe inicializar listCargos si es null y añadir el cargo correctamente")
    void testAnadirCargoInicializaListaNull() {
        // Arrange
        folio.setListCargos(null);
        Cargo cargoValido = new Cargo("Lavandería", 10.0, folio);

        // Act
        boolean resultado = folio.anadirCargo(cargoValido);

        // Assert
        assertTrue(resultado, "Debe retornar true tras inicializar la lista de cargos");
        assertNotNull(folio.getListCargos(), "La lista de cargos debió inicializarse");
        assertEquals(1, folio.getListCargos().size());
    }

    // ========== PRUEBAS PARA TAREA 1.4: registrarDevolucion() ========== //

    @Test
    @DisplayName("Debe retornar false si el monto de devolución es negativo")
    void testRegistrarDevolucionMontoNegativo() {
        boolean resultado = folio.registrarDevolucion(-50.0);
        assertFalse(resultado, "No se deberían permitir devoluciones con montos negativos");
    }

    @Test
    @DisplayName("Debe retornar true sin registrar nada si el monto es exactamente cero")
    void testRegistrarDevolucionMontoCero() {
        boolean resultado = folio.registrarDevolucion(0.0);
        assertTrue(resultado, "Una devolución de 0 es válida pero no debe registrar nada");

        // Validamos que la lista de pagos siga vacía o sea nula
        if (folio.getListPagos() != null) {
            assertEquals(0, folio.getListPagos().size(), "No se debió añadir ningún pago a la lista");
        }
    }

    @Test
    @DisplayName("Debe registrar exitosamente una devolución con monto positivo")
    void testRegistrarDevolucionMontoPositivo() {
        // Nota: Esta prueba asume que tu método 'anadirPago' inicializa la lista e inserta el pago retornando true
        boolean resultado = folio.registrarDevolucion(100.0);

        assertTrue(resultado, "Debería retornar true si anadirPago registra la devolución con éxito");
        assertNotNull(folio.getListPagos(), "La lista de pagos no debería ser nula tras registrar una devolución");
        assertEquals(1, folio.getListPagos().size(), "Debería haber exactamente 1 registro en la lista de pagos");

        Pago pagoRegistrado = folio.getListPagos().get(0);
        assertEquals(100.0, pagoRegistrado.getMonto(), 0.001);
        assertEquals(MedioDePago.TRANSFERENCIA, pagoRegistrado.getMedioDePago());
    }
}
