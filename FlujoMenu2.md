# Instructivo de Demostración - ApartaSuitesUQ

> **Nota para el presentador:** Todas las fechas son relativas al día de la presentación. Donde dice `[HOY]` debes ingresar la fecha actual (ej: `2026-11-18`), donde dice `[HOY + 10]` debes sumarle 10 días a la fecha actual.

---

## Opción 1: Consultar disponibilidad y cotizar
**Función:** Validar que el sistema filtra por bloqueos y reglas de negocio.

### Prueba 1.1: Exclusión por bloqueo
* **Qué hay antes de ejecutarla:** A2 tiene un bloqueo programado de `[HOY+20]` a `[HOY+25]` por mantenimiento.
* **Los inputs importantes:** 
  * Fecha entrada: `[HOY + 20]`
  * Fecha salida: `[HOY + 22]`
  * Personas (adultos): `1`
  * Menores de 6: `0`
* **Respuesta esperada:** En la lista aparecen A1, B1, B2. **A2 NO aparece**. C1, C2 y D1 no aparecen.
* **Razón:** A2 está bloqueado por esas fechas (`validarBloqueos` retorna false). C1 está OCUPADO, C2 está PENDIENTE_DE_PREPARACION y D1 está FUERA_DE_SERVICIO, por lo que son excluidos.

### Prueba 1.2: Capacidad y menores de edad
* **Qué hay antes de ejecutarla:** Varios apartamentos tienen distintas capacidades.
* **Los inputs importantes:**
  * Fecha entrada: `[HOY + 100]` (una fecha libre)
  * Fecha salida: `[HOY + 102]`
  * Personas (adultos): `2`
  * Menores de 6: `1`
* **Respuesta esperada:** Aparecen solo A1, B1 y B2. Se calcula el total cotizado por 2 personas pagantes.
* **Razón:** Buscas 3 personas en total. A2 y C2 (capacidad 2) quedan excluidos porque se suma a los adultos y menores (el menor ocupa cupo). En el cálculo del precio, el menor NO paga; el precio total refleja solo a los 2 adultos.

---

## Opción 2: Crear reserva
**Función:** Validar las reglas de creación, código externo duplicado y estados de apartamento.

### Prueba 2.1: Fallo por código externo duplicado
* **Qué hay antes de ejecutarla:** Existe una reserva externa precargada con el código `BK-998877` (`R-EXT01`).
* **Los inputs importantes:**
  * Canal: `EXTERNA`
  * Código externo: `BK-998877`
* **Respuesta esperada:** Error instantáneo: "Ya existe una reserva externa con el código BK-998877".
* **Razón:** El sistema llama a `existeCodigoExterno()` y detecta la colisión con `R-EXT01`.

### Prueba 2.2: Fallo por doble venta
* **Qué hay antes de ejecutarla:** La reserva `R-CONF01` ocupa el apartamento A1 de `[HOY+10]` a `[HOY+13]`.
* **Los inputs importantes:**
  * Canal: `PORTAL`
  * Huésped titular: `SOF004`
  * Apartamento: `A1`
  * Fechas: `[HOY + 10]` a `[HOY + 12]`
  * Acompañantes: *(Dejar vacío)*
* **Respuesta esperada:** Error: "El apartamento ya tiene una reserva activa en esas fechas".
* **Razón:** El método `validarSinDobleVenta()` detecta que `R-CONF01` está activa (PENDIENTE) y se solapa con las fechas ingresadas.

### Prueba 2.3: Creación exitosa
* **Qué hay antes de ejecutarla:** El apartamento B1 está libre y el huésped `LAU002` existe.
* **Los inputs importantes:**
  * Canal: `PORTAL`
  * Huésped titular: `LAU002`
  * Apartamento: `B1`
  * Fechas: `[HOY + 50]` a `[HOY + 52]`
  * Acompañantes: `MAT003`
* **Respuesta esperada:** "Reserva creada correctamente". Estado: PENDIENTE. Valor total calculado.
* **Razón:** No hay bloqueos ni solapamientos. Al ser canal Portal, nace como PENDIENTE y muestra el valor que debe pagar como anticipo (30%).

---

## Opción 3: Confirmar reserva pendiente
**Función:** Validar el pago del anticipo del 30% para confirmar reservas de portal.

### Prueba 3.1: Éxito al confirmar (Ya tiene pago)
* **Qué hay antes de ejecutarla:** `R-CONF01` está PENDIENTE y ya tiene registrado en su folio el pago del 30% de anticipo.
* **Los inputs importantes:**
  * Código de reserva: `R-CONF01`
* **Respuesta esperada:** "Reserva R-CONF01 confirmada correctamente".
* **Razón:** El sistema suma los pagos del folio y como ya alcanzó el `PORCENTAJE_ANTICIPO` (0.30), permite el cambio a estado CONFIRMADA.

### Prueba 3.2: Fallo al confirmar (Sin pago)
* **Qué hay antes de ejecutarla:** `R-PEND01` está PENDIENTE y no tiene ningún pago registrado.
* **Los inputs importantes:**
  * Código de reserva: `R-PEND01`
* **Respuesta esperada:** Error mostrando el anticipo requerido y el total pagado ($0.00).
* **Razón:** El saldo pagado (0) es menor al anticipo necesario, por lo que `confirmarReserva()` retorna false.

---

## Opción 4: Gestionar folio
**Función:** Mostrar cómo se añaden movimientos financieros a una reserva.

### Prueba 4.1: Registrar pago o cargo
* **Qué hay antes de ejecutarla:** `R-FOL01` es una reserva confirmada que debe el 100% de la estadía.
* **Los inputs importantes (Parte 1 - Pago):**
  * Código: `R-FOL01`
  * Acción: `PAGO`
  * Monto: `100000`
  * Medio: `TRANSFERENCIA`
  * *(Parte 2 - Cargo):* Repetir, elegir `CARGO`, nombre `Minibar`, valor `15000`.
* **Respuesta esperada:** Mensajes de "Pago registrado" y "Cargo registrado" con el resumen del folio actualizado.
* **Razón:** El sistema usa `registrarPagoPorFolioID` y `registrarCargoPorFolioID`, guardándolos en el folio y recalculando el saldo final a pagar.

---

## Opción 5: Realizar Check-In
**Función:** Validar el ingreso del huésped.

### Prueba 5.1: Check-In exitoso
* **Qué hay antes de ejecutarla:** `R-CHK01` está CONFIRMADA, el apartamento B1 está PREPARADO, y su fecha de entrada es hoy.
* **Los inputs importantes:**
  * Código de reserva: `R-CHK01`
* **Respuesta esperada:** "Check-in realizado. Reserva: EN_CURSO / Apartamento: OCUPADO".
* **Razón:** Cumple las 3 condiciones: reserva CONFIRMADA, fecha actual es igual o posterior a la entrada, y el apartamento está PREPARADO. El sistema cambia el estado de la reserva y del apartamento automáticamente.

---

## Opción 6: Realizar Check-Out
**Función:** Validar la salida del huésped.

### Prueba 6.1: Check-Out exitoso
* **Qué hay antes de ejecutarla:** `R-OUT01` ya está EN_CURSO y su apartamento C1 está OCUPADO.
* **Los inputs importantes:**
  * Código de reserva: `R-OUT01`
* **Respuesta esperada:** "Check-out realizado. Reserva: FINALIZADA / Apartamento: PENDIENTE_DE_PREPARACION".
* **Razón:** Cumple la regla de estar EN_CURSO. Al finalizar, libera el apartamento pero lo marca como pendiente de aseo (PENDIENTE_DE_PREPARACION).

---

## Opción 7: Cancelar reserva
**Función:** Validar la cancelación y cálculo de devolución según la política.

### Prueba 7.1: Cancelación exitosa
* **Qué hay antes de ejecutarla:** `R-CAN01` está CONFIRMADA y falta un mes para su fecha de entrada.
* **Los inputs importantes:**
  * Código de reserva: `R-CAN01`
* **Respuesta esperada:** "Reserva cancelada. Devolución según la política de cancelación: [Monto]".
* **Razón:** El sistema detecta que faltan más de 7 días (en este caso 30) para la entrada, por lo que aplica la devolución del 100% según la política configurada. Pasa a estado CANCELADA.

---

## Opción 8: Gestionar apartamento
**Función:** Demostrar cómo se añaden bloqueos y novedades.

### Prueba 8.1: Añadir una novedad
* **Qué hay antes de ejecutarla:** El apartamento B1 está operativo.
* **Los inputs importantes:**
  * Código apartamento: `B1`
  * Acción: `NOVEDAD`
  * Descripción: `Foco del baño quemado`
* **Respuesta esperada:** "Novedad registrada".
* **Razón:** Llama al método de recepcionista que añade el objeto Novedad a la lista del apartamento.

### Prueba 8.2: Cambiar estado operativo
* **Qué hay antes de ejecutarla:** El apartamento C2 está PENDIENTE_DE_PREPARACION (por un check-out anterior).
* **Los inputs importantes:**
  * Código apartamento: `C2`
  * Acción: `ESTADO`
  * Nuevo estado: `PREPARADO`
* **Respuesta esperada:** "Estado de Apartaestudio actualizado a PREPARADO".
* **Razón:** Permite a recepción marcar el apartamento como listo para una nueva reserva.

---

## Opción 9: Ver estado del sistema
**Función:** Auditoría visual de todo lo ocurrido.

* **Qué hay antes de ejecutarla:** Todas las acciones anteriores.
* **Los inputs importantes:** *(Ninguno, se abre directamente)*
* **Respuesta esperada:** Listado completo de apartamentos (con sus estados y novedades) y todas las reservas (con sus saldos y estados actuales).
* **Razón:** Hace uso de los métodos `toString()` corregidos y recorre los gestores para mostrar el estado consolidado.