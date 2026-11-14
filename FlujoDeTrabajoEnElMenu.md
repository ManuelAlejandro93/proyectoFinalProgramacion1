# Guion de Demostración - ApartaSuitesUQ

> **Datos precargados al iniciar:**
> - **Apartamentos:** A1 (Villa Juliana, cap 6), A2 (Suite Pequeña, cap 2, con bloqueo de hoy+10 a hoy+15), B1 (Suite Familiar, cap 4)
> - **Huéspedes:** KEV001 (Kevin, adulto), LAU002 (Laura, adulta), MAT003 (Mateo, 4 años - menor de 6)
> - **Reservas:** R-PEND1 (A1, PENDIENTE por Portal, dentro de 30 días), R-CHK01 (B1, CONFIRMADA con entrada HOY), R-EXT01 (A1, EXTERNA con código BK-998877)
> - **Empleados:** Ana (Recepcionista)

---

### Prueba 1: Consultar disponibilidad y cotizar (Valida 4 reglas a la vez)
**Función:** `1. Consultar disponibilidad y cotizar` -> `gestorReservas.consultarApartamentosDisponibles()`
**Datos a ingresar:**
- Fecha entrada: `[HOY + 11 días]` (ej: si hoy es 2026-05-13, poner 2026-05-24)
- Fecha salida: `[HOY + 12 días]` (ej: 2026-05-25)
- Personas de 6 años o más: `1`
- Menores de 6 años: `0`
**Objetivo:** Comprobar que el sistema filtra por bloqueos.
**Respuesta esperada:** Solo deben aparecer A1 y B1. **A2 NO debe aparecer.**
**Razón:** A2 tiene un bloqueo precargado de `hoy+10 a hoy+15`. La fecha solicitada cae dentro de ese bloqueo, por lo que `validarBloqueos()` debe retornar `false` y excluirlo.

### Prueba 2: Cotización con menores de 6 años
**Función:** `1. Consultar disponibilidad y cotizar`
**Datos a ingresar:**
- Fecha entrada: `[HOY + 30 días]` (misma que R-PEND1, ej: 2026-06-12)
- Fecha salida: `[HOY + 31 días]` (ej: 2026-06-13)
- Personas de 6 años o más: `2`
- Menores de 6 años: `1` (Total = 3)
**Objetivo:** Comprobar que los menores ocupan cupo pero NO pagan.
**Respuesta esperada:** Debe aparecer A1 (cap 6) y B1 (cap 4), pero **NO A2 (cap 2)**. El precio total mostrado debe ser por 2 personas, no por 3.
**Razón:** A2 tiene capacidad 2 y se solicitan 3 personas. Aunque Mateo es menor, `validarCapacidadMaxima()` cuenta a todos los ocupantes. La cotización demuestra que el cálculo de precio los excluye.

### Prueba 3: Intentar crear reserva que excede capacidad
**Función:** `2. Crear reserva` -> Tipo `PORTAL`
**Datos a ingresar:**
- Tipo: `PORTAL`
- Código apartamento: `A2`
- Fecha entrada: `[HOY + 40 días]`
- Fecha salida: `[HOY + 42 días]`
- Código huésped titular: `KEV001`
- Acompañantes: `LAU002,MAT003` (Total 3 personas para A2)
**Objetivo:** Comprobar validación de capacidad máxima.
**Respuesta esperada:** Mensaje de error: `Se supera la capacidad del apartamento (3 personas para una capacidad de 2)`.
**Razón:** A2 es para 2 personas. El sistema no debe permitir la creación.

### Prueba 4: Intentar crear reserva en fecha bloqueada
**Función:** `2. Crear reserva` -> Tipo `DIRECTA`
**Datos a ingresar:**
- Tipo: `DIRECTA`
- Código apartamento: `A2`
- Fecha entrada: `[HOY + 11 días]`
- Fecha salida: `[HOY + 12 días]`
- Código huésped titular: `KEV001`
- Acompañantes: `(vacío)`
**Objetivo:** Comprobar validación de bloqueos al crear.
**Respuesta esperada:** Mensaje de error: `El apartamento tiene un bloqueo en esas fechas.`
**Razón:** Son las mismas fechas del bloqueo precargado de A2.

### Prueba 5: Intentar doble venta (solapamiento)
**Función:** `2. Crear reserva` -> Tipo `DIRECTA`
**Datos a ingresar:**
- Tipo: `DIRECTA`
- Código apartamento: `A1`
- Fecha entrada: `[HOY + 31 días]` (Se solapa con R-PEND1 que es de +30 a +35)
- Fecha salida: `[HOY + 33 días]`
- Código huésped titular: `LAU002`
- Acompañantes: `(vacío)`
**Objetivo:** Comprobar `validarSinDobleVenta()`.
**Respuesta esperada:** Mensaje de error: `El apartamento ya tiene una reserva activa en esas fechas.`
**Razón:** R-PEND1 ya ocupa A1 del día 30 al 35 en estado PENDIENTE (activo).

### Prueba 6: Crear reserva externa con código duplicado
**Función:** `2. Crear reserva` -> Tipo `EXTERNA`
**Datos a ingresar:**
- Tipo: `EXTERNA`
- Código externo: `BK-998877`
**Objetivo:** Comprobar `existeCodigoExterno()`.
**Respuesta esperada:** Al ingresar el código externo, debe aparecer inmediatamente el error: `Ya existe una reserva externa con el código BK-998877` y no deja continuar.
**Razón:** R-EXT01 ya fue precargada con ese mismo código externo y canal EXTERNA.

### Prueba 7: Confirmar reserva sin anticipo (Debe fallar)
**Función:** `3. Confirmar reserva pendiente`
**Datos a ingresar:**
- Código de reserva: `R-PEND1`
**Objetivo:** Comprobar que no se confirma sin el 30% de anticipo.
**Respuesta esperada:** Mensaje de error mostrando `Anticipo requerido (30%): $...` y `Total pagado: $0.00`.
**Razón:** R-PEND1 es de Portal y nació PENDIENTE. Su folio solo tiene el cargo de la estadía, ningún pago. `confirmarReserva()` debe fallar porque `totalPagos < anticipo`.

### Prueba 8: Registrar pago del anticipo
**Función:** `4. Gestionar folio` -> `PAGO`
**Datos a ingresar:**
- Código de reserva: `R-PEND1`
- Acción: `PAGO`
- Monto: `135000` (Es el 30% de 3 noches en A1 para 2 pagantes: 150000 * 2 * 3 noches * 0.3)
- Medio de pago: `TRANSFERENCIA`
**Objetivo:** Demostrar que el folio registra pagos y actualiza saldo.
**Respuesta esperada:** Mensaje `Pago registrado` y el folio mostrado debe tener `Saldo` reducido y `Total de pagos: $135,000.00`.
**Razón:** El recepcionista usa `anadirPagoPorFolioID()` que suma a la lista de pagos.

### Prueba 9: Confirmar reserva con anticipo (Debe tener éxito)
**Función:** `3. Confirmar reserva pendiente`
**Datos a ingresar:**
- Código de reserva: `R-PEND1`
**Objetivo:** Comprobar que ahora sí confirma.
**Respuesta esperada:** Mensaje `Reserva R-PEND1 confirmada correctamente.`
**Razón:** Ahora `totalPagos (135000) >= anticipoNecesario (135000)` es `true`, por lo que cambia a CONFIRMADA.

### Prueba 10: Realizar Check-In
**Función:** `5. Realizar check-in`
**Datos a ingresar:**
- Código de reserva: `R-CHK01`
**Objetivo:** Comprobar cambio de estado de Reserva y Apartamento.
**Respuesta esperada:** Mensaje `Check-in realizado. Reserva: EN_CURSO / Apartamento: OCUPADO`.
**Razón:** R-CHK01 fue precargada CONFIRMADA con fecha de entrada HOY y su apartamento B1 está PREPARADO. Cumple las 3 condiciones.

### Prueba 11: Realizar Check-Out
**Función:** `6. Realizar check-out`
**Datos a ingresar:**
- Código de reserva: `R-CHK01` (La misma de la prueba anterior)
**Objetivo:** Comprobar finalización.
**Respuesta esperada:** Mensaje `Check-out realizado. Reserva: FINALIZADA / Apartamento: PENDIENTE_DE_PREPARACION / Saldo final: $...`.
**Razón:** Solo reservas en EN_CURSO pueden pasar a FINALIZADA.

### Prueba 12: Cancelar reserva y ver devolución
**Función:** `7. Cancelar reserva`
**Datos a ingresar:**
- Código de reserva: `R-PEND1` (Ahora está CONFIRMADA)
**Objetivo:** Comprobar `cancelarReserva()` y política de cancelación.
**Respuesta esperada:** Mensaje `Reserva cancelada. Devolución según política: $...` Como falta más de 7 días para la entrada (es dentro de 30 días), la devolución debe ser del 100%.
**Razón:** R-PEND1 está en estado cancelable y `calcularDevolucion()` aplica `GestorPoliticaCancelacion`.

### Prueba 13: Gestionar apartamento (Novedad y Estado)
**Función:** `8. Gestionar apartamento` -> `NOVEDAD` y luego `ESTADO`
**Datos a ingresar (Parte A - Novedad):**
- Código apartamento: `B1`
- Acción: `NOVEDAD`
- Descripción: `Lámpara rota`
**Datos a ingresar (Parte B - Estado):**
- Código apartamento: `B1`
- Acción: `ESTADO`
- Nuevo estado: `PREPARADO`
**Objetivo:** Demostrar `anadirNovedad()` y cambio de estado operativo.
**Respuesta esperada:** Mensaje `Novedad registrada` y luego `Estado actualizado a PREPARADO`.
**Razón:** B1 quedó en PENDIENTE_DE_PREPARACION tras el check-out. Hay que prepararlo de nuevo para que sea utilizable. La novedad se añade a su `listNovedades`.

### Prueba 14: Ver estado final del sistema
**Función:** `9. Ver estado del sistema`
**Objetivo:** Verificación visual final.
**Respuesta esperada:** Un listado donde se vea:
- A1: PREPARADO
- A2: PREPARADO (con 1 bloqueo)
- B1: PREPARADO (con 1 novedad)
- R-PEND1: CANCELADA
- R-CHK01: FINALIZADA
- R-EXT01: CONFIRMADA
**Razón:** Resume todas las transiciones de estado que ocurrieron durante la demo.