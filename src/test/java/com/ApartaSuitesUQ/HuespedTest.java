package com.ApartaSuitesUQ;

import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Gestores.GestorGeneral;
import com.ApartaSuitesUQ.Gestores.GestorHuespedes;
import com.ApartaSuitesUQ.Gestores.GestorReservas;
import com.ApartaSuitesUQ.Reserva.EstadoActual;
import com.ApartaSuitesUQ.Reserva.NombreCanal;
import com.ApartaSuitesUQ.Reserva.Reserva;
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

        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", gestorHuespedes);
        Huesped acompanante = new Huesped("Laura", "456", LocalDate.of(2002, 3, 18), "654", "laura@gmail.com", gestorHuespedes);

        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);
        ocupantes.add(acompanante);

        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");
        LocalDate entrada = LocalDate.of(2026, 12, 10);
        LocalDate salida = LocalDate.of(2026, 12, 15);

        // Act
        boolean creada = titular.crearReservaEnPortal(ocupantes, apartamento, entrada, salida);

        // Assert
        assertTrue(creada, "El método debería retornar true cuando la reserva en el portal se crea correctamente");
        assertEquals(1, gestorReservas.getListReservas().size(), "Debería haberse registrado exactamente 1 reserva en el gestor");

        Reserva reservaCreada = gestorReservas.getListReservas().get(0);
        assertNotNull(reservaCreada.getCodigo(), "La reserva debe tener un código único generado");
        assertTrue(reservaCreada.getCodigo().startsWith("RES-"), "El código debe iniciar con 'RES-'");
        assertEquals(EstadoActual.PENDIENTE, reservaCreada.getEstadoActual(), "Una reserva creada por el portal debe nacer en estado PENDIENTE");
        assertEquals(NombreCanal.PORTAL, reservaCreada.getTheCanalReserva().nombreCanal(), "El canal de la reserva debe ser PORTAL");
        assertEquals(titular, reservaCreada.getTheHuesped(), "El titular de la reserva debe ser el mismo huésped que invocó el método");
        assertEquals(apartamento, reservaCreada.getTheApartamento(), "El apartamento debe coincidir con el enviado");
        assertEquals(2, reservaCreada.getTheEstancia().getListHuespedes().size(), "La estancia debe contener los 2 ocupantes registrados");
    }

    @Test
    @DisplayName("Debe retornar false y no registrar la reserva cuando el número de ocupantes excede la capacidad máxima")
    void testCrearReservaEnPortalExcedeCapacidad() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular = new Huesped("Kevin", "123", LocalDate.of(2000, 5, 10), "321", "kevin@gmail.com", gestorHuespedes);
        Huesped h2 = new Huesped("Laura", "456", LocalDate.of(1998, 8, 20), "654", "laura@gmail.com", gestorHuespedes);
        Huesped h3 = new Huesped("Mateo", "789", LocalDate.of(2024, 2, 15), "987", "mateo@gmail.com", gestorHuespedes);

        // 3 ocupantes (incluyendo menor de 6 años)
        ArrayList<Huesped> ocupantes = new ArrayList<>();
        ocupantes.add(titular);
        ocupantes.add(h2);
        ocupantes.add(h3);

        // Apartamento con capacidad máxima de 2 personas (cuarto parámetro = 2)
        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 1, 2, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Act
        boolean creada = titular.crearReservaEnPortal(ocupantes, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Assert
        assertFalse(creada, "Debe retornar false cuando la cantidad de ocupantes supera la capacidad máxima");
        assertTrue(gestorReservas.getListReservas().isEmpty(), "No debería haberse registrado ninguna reserva en el gestor");
    }

    @Test
    @DisplayName("Debe retornar false y no registrar la reserva cuando hay doble venta en el mismo apartamento y fechas")
    void testCrearReservaEnPortalConDobleVenta() {
        // Arrange
        GestorGeneral gestorGeneral = new GestorGeneral();
        GestorReservas gestorReservas = gestorGeneral.getTheGestorReservas();
        GestorHuespedes gestorHuespedes = gestorGeneral.getTheGestorHuespedes();

        Huesped titular1 = new Huesped("Kevin", "123", LocalDate.of(2007, 11, 5), "321", "kevin67@gmail.com", gestorHuespedes);
        Huesped titular2 = new Huesped("Carlos", "789", LocalDate.of(1990, 8, 22), "987", "carlos@gmail.com", gestorHuespedes);

        ArrayList<Huesped> ocupantes1 = new ArrayList<>();
        ocupantes1.add(titular1);

        ArrayList<Huesped> ocupantes2 = new ArrayList<>();
        ocupantes2.add(titular2);

        Apartamento apartamento = new Apartamento("A1", "Piso 1 apart 20", 2, 4, new ArrayList<String>(), 20.0, EstadoOperativo.PREPARADO, "Ninguno");

        // Primera reserva exitosa del 10 al 15 de diciembre
        boolean primeraCreada = titular1.crearReservaEnPortal(ocupantes1, apartamento,
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 15));

        // Act: Segundo huésped intenta reservar el mismo apartamento del 12 al 18 de diciembre
        boolean segundaCreada = titular2.crearReservaEnPortal(ocupantes2, apartamento,
                LocalDate.of(2026, 12, 12), LocalDate.of(2026, 12, 18));

        // Assert
        assertTrue(primeraCreada, "La primera reserva debe crearse exitosamente");
        assertFalse(segundaCreada, "La segunda reserva debe retornar false por solapamiento de fechas");
        assertEquals(1, gestorReservas.getListReservas().size(), "Solo debe existir la primera reserva en el sistema");
        assertEquals(titular1, gestorReservas.getListReservas().get(0).getTheHuesped(), "Debe conservarse la reserva del primer titular");
    }
}
