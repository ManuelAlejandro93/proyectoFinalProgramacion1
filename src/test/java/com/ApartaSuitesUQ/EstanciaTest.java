package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Estancia.Noche;
import com.ApartaSuitesUQ.Estancia.Temporada;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorHuespedes;
import com.ApartaSuitesUQ.Usuarios.Huesped;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;

public class EstanciaTest {

    // ========== PRUEBAS REFORMULADAS PARA TAREA 2.1: asignarNoches() ========== //

    @Test
    @DisplayName("Debe asignar las noches correctamente para un rango válido de fechas")
    void testAsignarNochesValido() {
        // Arrange
        Apartamento apto = new Apartamento("APT101", "Suite Premium", 2, 4, null, 150.0, EstadoOperativo.PREPARADO, null);
        LocalDate entrada = LocalDate.of(2026, 3, 10);
        LocalDate salida = LocalDate.of(2026, 3, 12);

        // Act: El constructor ejecuta asignarNoches() automáticamente
        Estancia estancia = new Estancia(entrada, salida, apto);

        // Assert
        assertNotNull(estancia.getListNoches(), "La lista de noches no debe ser nula");
        assertEquals(2, estancia.getNoches(), "El atributo noches debe ser exactamente 2");
        assertEquals(2, estancia.getListNoches().size(), "Deben haberse agregado 2 objetos Noche a la lista");

        // Verificar los datos de la primera noche
        Noche primeraNoche = estancia.getListNoches().get(0);
        assertEquals(LocalDate.of(2026, 3, 10), primeraNoche.getFecha());
        assertEquals(Temporada.BAJA, primeraNoche.getTemporada());
        assertEquals(0.0, primeraNoche.getPrecio(), 0.001);

        // Verificar la segunda noche
        Noche segundaNoche = estancia.getListNoches().get(1);
        assertEquals(LocalDate.of(2026, 3, 11), segundaNoche.getFecha());
    }

    @Test
    @DisplayName("Debe limpiar las noches previas si la lista ya contenía elementos")
    void testAsignarNochesLimpiaListaPrevia() {
        // Arrange
        Apartamento apto = new Apartamento("APT101", "Suite Premium", 2, 4, null, 150.0, EstadoOperativo.PREPARADO, null);
        LocalDate entrada = LocalDate.of(2026, 3, 10);
        LocalDate salida = LocalDate.of(2026, 3, 12);

        // Instanciamos la estancia (esto crea 2 noches inicialmente)
        Estancia estancia = new Estancia(entrada, salida, apto);

        // Simulamos un escenario donde añadimos manualmente noches viejas/ajenas a la lista
        ArrayList<Noche> nochesViejas = new ArrayList<>();
        nochesViejas.add(new Noche(LocalDate.of(2026, 1, 1), Temporada.BAJA, 0.0, estancia));
        estancia.setListNoches(nochesViejas);

        // Act: Forzamos la ejecución manual para verificar que limpie lo anterior
        estancia.asignarNoches();

        // Assert: Debe haber borrado la noche de enero y tener solo las 2 correctas
        assertEquals(2, estancia.getListNoches().size(), "Debe limpiar los registros previos antes de generar los nuevos");
        assertEquals(LocalDate.of(2026, 3, 10), estancia.getListNoches().get(0).getFecha());
    }

    @Test
    @DisplayName("No debe hacer nada si la fecha de entrada no es menor a la de salida")
    void testAsignarNochesFechasInvalidas() {
        // Arrange: Entrada posterior a la salida
        Apartamento apto = new Apartamento("APT101", "Suite Premium", 2, 4, null, 150.0, EstadoOperativo.PREPARADO, null);
        LocalDate entrada = LocalDate.of(2026, 3, 15);
        LocalDate salida = LocalDate.of(2026, 3, 12);

        // Act
        Estancia estancia = new Estancia(entrada, salida, apto);

        // Assert: No debió sumar noches ni inicializar la lista
        assertEquals(0, estancia.getNoches(), "No debe contar noches si el rango es inválido");
        if (estancia.getListNoches() != null) {
            assertTrue(estancia.getListNoches().isEmpty(), "La lista de noches debe permanecer vacía");
        }
    }

    // ========== PRUEBAS PARA TAREA 2.2: calcularPrecioEstadia() ========== //

    @Test
    @DisplayName("Debe calcular el precio correctamente incluyendo solo mayores de 6 años")
    void testCalcularPrecioEstadiaConFiltroEdad() {
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorHuespedes gestor = new GestorHuespedes(gestorGeneral);

        // Arrange: Apartamento con tarifa de 100.0
        Apartamento apto = new Apartamento("APT101", "Suite", 2, 4, null, 100.0, EstadoOperativo.PREPARADO, null);
        LocalDate entrada = LocalDate.of(2026, 3, 10);
        LocalDate salida = LocalDate.of(2026, 3, 13); // 3 noches (10, 11, 12)

        // Crear huéspedes: uno de 25 años (paga) y uno de 4 años (no paga)
        // Se usa la fecha actual de 2026 para calcular los nacimientos
        Huesped adulto = new Huesped("Juan", "123", LocalDate.of(2001, 1, 1), "300", "juan@mail.com", gestor);
        Huesped nino = new Huesped("Lina", "456", LocalDate.of(2022, 1, 1), "300", "lina@mail.com", gestor);

        ArrayList<Huesped> huespedes = new ArrayList<>();
        huespedes.add(adulto);
        huespedes.add(nino);

        // Act: Al crear la estancia se autogeneran las 3 noches
        Estancia estancia = new Estancia(entrada, salida, apto);
        estancia.setListHuespedes(huespedes);
        double precioCalculado = estancia.calcularPrecioEstadia();

        // Assert: 1 pagante x Tarifa 100.0 x 3 noches = 300.0
        assertEquals(300.0, precioCalculado, 0.001, "El total debería ser 300.0 ya que el menor de 6 años no paga");
    }

    @Test
    @DisplayName("Debe retornar 0.0 si no hay noches o el apartamento es nulo")
    void testCalcularPrecioEstadiaCasosVacios() {
        // Arrange
        Apartamento apto = new Apartamento("APT101", "Suite", 2, 4, null, 100.0, EstadoOperativo.PREPARADO, null);

        // Instancia válida pero forzamos apartamento a nulo
        Estancia estanciaSinApto = new Estancia(LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 12), apto);
        estanciaSinApto.setTheApartamento(null);

        // Assert
        assertEquals(0.0, estanciaSinApto.calcularPrecioEstadia(), "Debe retornar 0 si el apartamento es nulo");
    }
}
