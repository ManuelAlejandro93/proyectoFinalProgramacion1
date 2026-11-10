package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Gestores.GestorPoliticaCancelacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GestorPoliticaCancelacionTest {

    // ========== PRUEBAS PARA TAREA 2.3: calcularDevolucion() ========== //

    @Test
    @DisplayName("Debe devolver el 100% si la cancelación es con más de 7 días de anticipación")
    void testCalcularDevolucionCompleta() {
        // Pasamos la política al gestor (asumiendo este constructor o método set)
        GestorPoliticaCancelacion gestor = new GestorPoliticaCancelacion();

        LocalDate fechaEntrada = LocalDate.now().plusDays(10); // 10 días de antelación (> 7)
        double valorOriginal = 500.0;

        // Act
        double devolucion = gestor.calcularDevolucion(valorOriginal, fechaEntrada);

        // Assert: 500.0 * 1.0 = 500.0
        assertEquals(500.0, devolucion, 0.001, "Debería retornar el valor completo (100%)");
    }

    @Test
    @DisplayName("Debe devolver el 50% si la cancelación es entre 2 y 7 días de anticipación")
    void testCalcularDevolucionParcial() {
        GestorPoliticaCancelacion gestor = new GestorPoliticaCancelacion();

        LocalDate fechaEntrada = LocalDate.now().plusDays(5); // 5 días de antelación (entre 2 y 7)
        double valorOriginal = 400.0;

        // Act
        double devolucion = gestor.calcularDevolucion(valorOriginal, fechaEntrada);

        // Assert: 400.0 * 0.5 = 200.0
        assertEquals(200.0, devolucion, 0.001, "Debería retornar la mitad del valor (50%)");
    }

    @Test
    @DisplayName("Debe retornar 0.0 si la cancelación se hace con menos de 2 días de anticipación")
    void testCalcularSinDevolucion() {
        GestorPoliticaCancelacion gestor = new GestorPoliticaCancelacion();

        LocalDate fechaEntrada = LocalDate.now().plusDays(1); // 1 día de antelación (< 2)
        double valorOriginal = 300.0;

        // Act
        double devolucion = gestor.calcularDevolucion(valorOriginal, fechaEntrada);

        // Assert: 300.0 * 0.0 = 0.0
        assertEquals(0.0, devolucion, 0.001, "No debería retornar nada si falta menos de 48 horas");
    }
}
