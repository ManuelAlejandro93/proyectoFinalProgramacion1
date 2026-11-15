# Instructivo de demostración — ApartaSuitesUQ

## 1. Antes de comenzar

1. Ejecuta la clase `App`.
2. Realiza las demostraciones en el orden de este documento y **una sola vez** para que coincidan los resultados.
3. Cada operación importante tiene una reserva precargada propia: no necesitas crear, pagar o confirmar otra reserva para demostrarla.
4. Para repetir todo desde el estado inicial, cierra y vuelve a ejecutar `App`. Los cambios se conservan únicamente durante la ejecución.

### Cómo ingresar fechas y montos

- **Hoy** significa la fecha del equipo cuando inicia el programa.
- Donde diga **hoy + 5 días**, debes escribir la fecha resultante en formato `aaaa-mm-dd`. **No escribas literalmente “hoy + 5”.**
- Escribe los montos sin símbolos ni separadores. Ejemplo: `60000`.
- Para dejar los acompañantes vacíos, no escribas nada y pulsa **Aceptar**. **Cancelar** interrumpe la operación.
- La presentación de los montos puede variar según la configuración regional del equipo.

> Los valores de este instructivo corresponden a la implementación actual: las noches se crean en temporada `BAJA`, con multiplicador `1.0`.

---

## 2. Datos precargados de referencia

### Apartamentos

| Código | Nombre | Capacidad | Tarifa individual por noche | Estado inicial | Particularidad |
|---|---|---:|---:|---|---|
| `A1` | Villa Juliana | 6 | $150.000 | PREPARADO | Tiene reservas futuras |
| `A2` | Suite Pequeña | 2 | $80.000 | PREPARADO | Bloqueado desde hoy + 20 hasta hoy + 25 |
| `B1` | Suite Familiar | 4 | $100.000 | PREPARADO | Tiene una reserva confirmada que entra hoy |
| `B2` | Suite Doble | 4 | $110.000 | PREPARADO | Tiene reservas para confirmación y cancelación |
| `C1` | Penthouse Vista | 4 | $200.000 | OCUPADO | Tiene una reserva en curso |
| `C2` | Apartaestudio | 2 | $90.000 | PENDIENTE_DE_PREPARACION | Tiene la reserva para demostrar el folio |
| `D1` | Casa Campestre | 8 | $250.000 | FUERA_DE_SERVICIO | Remodelación de cocina |

### Huéspedes

| Código | Nombre | Uso |
|---|---|---|
| `KEV001` | Kevin | Adulto pagante |
| `LAU002` | Laura | Adulta pagante |
| `MAT003` | Mateo | Tiene 4 años: ocupa cupo, pero no paga |
| `SOF004` | Sofía | Adulta pagante |

### Reservas

| Código | Apartamento | Estado inicial | Fechas | Valor de estadía | Pagos precargados | Saldo inicial |
|---|---|---|---|---:|---:|---:|
| `R-CONF01` | A1 | PENDIENTE | Hoy + 10 a hoy + 13 | $900.000 | $270.000 | $630.000 |
| `R-PEND01` | B2 | PENDIENTE | Hoy + 10 a hoy + 13 | $330.000 | $0 | $330.000 |
| `R-CHK01` | B1 | CONFIRMADA | Hoy a hoy + 3 | $300.000 | $90.000 | $210.000 |
| `R-OUT01` | C1 | EN_CURSO | Hoy − 2 a hoy + 2 | $800.000 | $800.000 | $0 |
| `R-CAN01` | B2 | CONFIRMADA | Hoy + 30 a hoy + 33 | $330.000 | $99.000 | $231.000 |
| `R-FOL01` | C2 | CONFIRMADA | Hoy + 40 a hoy + 42 | $180.000 | $0 | $180.000 |
| `R-EXT01` | A1 | CONFIRMADA | Hoy + 60 a hoy + 62 | $300.000 | $0 | $300.000 |

> `R-EXT01` pertenece al canal `EXTERNA` y tiene el código externo **`BK-998877`**.

---

# Demostración de la opción 1: Consultar disponibilidad y cotizar

## Prueba 1.1 — Capacidad y cotización con un menor

### Qué hay antes de ejecutarla

- No hay reservas que ocupen los apartamentos entre hoy + 5 y hoy + 7.
- A2 y C2 tienen capacidad para dos personas.
- D1 está fuera de servicio.

### Qué vamos a añadir

Nada. La consulta crea ocupantes temporales para cotizar, pero no registra reservas ni huéspedes nuevos en el gestor.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `1` |
| 2 | Fecha de entrada | Hoy + 5 días |
| 3 | Fecha de salida | Hoy + 7 días |
| 4 | Personas de 6 años o más | `2` |
| 5 | Menores de 6 años | `1` |

### Inputs importantes y razón

- **Dos pagantes y un menor:** son tres ocupantes para validar capacidad, pero solo dos para calcular el precio.
- **Dos noches:** la fecha de salida no se cobra como otra noche.

### Respuesta esperada

Deben aparecer estos cuatro apartamentos:

| Apartamento | Cálculo | Total |
|---|---|---:|
| A1 | $150.000 × 2 pagantes × 2 noches | $600.000 |
| B1 | $100.000 × 2 pagantes × 2 noches | $400.000 |
| B2 | $110.000 × 2 pagantes × 2 noches | $440.000 |
| C1 | $200.000 × 2 pagantes × 2 noches | $800.000 |

No deben aparecer:

- **A2 y C2:** tres ocupantes superan su capacidad de dos.
- **D1:** está fuera de servicio.

> C1 aparece aunque actualmente esté ocupado porque su reserva termina en hoy + 2. La consulta es para hoy + 5. El método actual descarta `FUERA_DE_SERVICIO`, pero no excluye automáticamente `OCUPADO` o `PENDIENTE_DE_PREPARACION` para todas las fechas.

---

## Prueba 1.2 — Exclusión por bloqueo

### Qué hay antes de ejecutarla

A2 tiene un bloqueo precargado desde **hoy + 20 hasta hoy + 25**, incluyendo el último día.

### Qué vamos a añadir

Nada. Solo consultaremos disponibilidad.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `1` |
| 2 | Fecha de entrada | Hoy + 21 días |
| 3 | Fecha de salida | Hoy + 22 días |
| 4 | Personas de 6 años o más | `1` |
| 5 | Menores de 6 años | `0` |

### Input importante y razón

El rango solicitado está dentro del bloqueo de A2. `validarBloqueos()` debe retornar `false` para ese apartamento.

### Respuesta esperada

- Aparecen: **A1, B1, B2, C1 y C2**.
- A2 no aparece por el bloqueo.
- D1 no aparece por estar fuera de servicio.

---

## Prueba 1.3 — Exclusión por reservas activas

### Qué hay antes de ejecutarla

- `R-CONF01` ocupa A1 desde hoy + 10 hasta hoy + 13.
- `R-PEND01` ocupa B2 durante el mismo rango.
- Ambas están inicialmente pendientes y ese estado cuenta como activo.

### Qué vamos a añadir

Nada. Solo consultaremos disponibilidad.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `1` |
| 2 | Fecha de entrada | Hoy + 11 días |
| 3 | Fecha de salida | Hoy + 12 días |
| 4 | Personas de 6 años o más | `1` |
| 5 | Menores de 6 años | `0` |

### Input importante y razón

El rango consultado se cruza con las dos reservas pendientes. `validarSinDobleVenta()` debe retornar `false` para A1 y B2.

### Respuesta esperada

- Aparecen: **A2, B1, C1 y C2**.
- A1 y B2 no aparecen por sus reservas activas.
- D1 tampoco aparece por estar fuera de servicio.

---

# Demostración de la opción 2: Crear reserva

## Prueba 2.1 — Rechazo por capacidad

### Qué hay antes de ejecutarla

A2 tiene capacidad máxima de **dos personas**.

### Qué vamos a añadir

Intentaremos registrar una reserva para tres personas. **No debe guardarse.**

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `PORTAL` |
| 3 | Código del titular | `KEV001` |
| 4 | Código del apartamento | `A2` |
| 5 | Fecha de entrada | Hoy + 8 días |
| 6 | Fecha de salida | Hoy + 9 días |
| 7 | Acompañantes | `LAU002,MAT003` |

### Inputs importantes y razón

El titular ya está incluido. Con Laura y Mateo se completan **tres ocupantes**. Mateo no paga, pero sí ocupa cupo.

### Respuesta esperada

Un mensaje indicando:

> Se supera la capacidad del apartamento: 3 personas para una capacidad de 2.

No se crea la reserva.

---

## Prueba 2.2 — Rechazo por bloqueo

### Qué hay antes de ejecutarla

A2 está bloqueado desde hoy + 20 hasta hoy + 25.

### Qué vamos a añadir

Intentaremos crear una reserva dentro del bloqueo. **No debe guardarse.**

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `DIRECTA` |
| 3 | Código del titular | `KEV001` |
| 4 | Código del apartamento | `A2` |
| 5 | Fecha de entrada | Hoy + 21 días |
| 6 | Fecha de salida | Hoy + 22 días |
| 7 | Acompañantes | Dejar vacío y aceptar |

### Input importante y razón

La capacidad es suficiente, pero las fechas coinciden con el bloqueo.

### Respuesta esperada

> El apartamento tiene un bloqueo en esas fechas.

No se crea la reserva.

---

## Prueba 2.3 — Rechazo por doble venta

### Qué hay antes de ejecutarla

`R-CONF01` ya reserva A1 desde hoy + 10 hasta hoy + 13.

### Qué vamos a añadir

Intentaremos crear otra reserva sobre parte de ese rango. **No debe guardarse.**

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `DIRECTA` |
| 3 | Código del titular | `LAU002` |
| 4 | Código del apartamento | `A1` |
| 5 | Fecha de entrada | Hoy + 11 días |
| 6 | Fecha de salida | Hoy + 12 días |
| 7 | Acompañantes | Dejar vacío y aceptar |

### Input importante y razón

Las fechas se solapan con una reserva activa del mismo apartamento.

### Respuesta esperada

> El apartamento ya tiene una reserva activa en esas fechas.

No se crea la reserva.

---

## Prueba 2.4 — Rechazo por código externo repetido

### Qué hay antes de ejecutarla

`R-EXT01` tiene el código externo **`BK-998877`**.

### Qué vamos a añadir

Intentaremos utilizar nuevamente ese código. **No debe guardarse otra reserva.**

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `EXTERNA` |
| 3 | Código externo | `BK-998877` |

### Input importante y razón

El código coincide exactamente con el de una reserva externa precargada. `existeCodigoExterno()` debe retornar `true`.

### Respuesta esperada

> Ya existe una reserva externa con el código BK-998877.

La operación termina sin pedir titular, apartamento ni fechas.

---

## Prueba 2.5 — Rechazo por apartamento fuera de servicio

### Qué hay antes de ejecutarla

D1 está en `FUERA_DE_SERVICIO` por **Remodelación de cocina**.

### Qué vamos a añadir

Intentaremos crear una reserva en ese apartamento. **No debe guardarse.**

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `DIRECTA` |
| 3 | Código del titular | `SOF004` |
| 4 | Código del apartamento | `D1` |
| 5 | Fecha de entrada | Hoy + 5 días |
| 6 | Fecha de salida | Hoy + 7 días |
| 7 | Acompañantes | Dejar vacío y aceptar |

### Input importante y razón

El estado operativo de D1 impide crear la reserva, aunque tenga capacidad suficiente.

### Respuesta esperada

> El apartamento está fuera de servicio: Remodelación de cocina.

---

## Prueba 2.6 — Crear correctamente una reserva por portal

### Qué hay antes de ejecutarla

A2 está preparado y libre entre hoy + 5 y hoy + 7. Su bloqueo comienza después.

### Qué vamos a añadir

Una reserva nueva para Kevin y Mateo.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `PORTAL` |
| 3 | Código del titular | `KEV001` |
| 4 | Código del apartamento | `A2` |
| 5 | Fecha de entrada | Hoy + 5 días |
| 6 | Fecha de salida | Hoy + 7 días |
| 7 | Acompañantes | `MAT003` |

### Inputs importantes y razón

- Kevin y Mateo completan dos ocupantes: no superan la capacidad.
- Solo Kevin paga.
- El canal `PORTAL` establece el estado inicial `PENDIENTE`.

### Respuesta esperada

- Reserva creada correctamente.
- Código generado con formato `RES-...`.
- Estado: **PENDIENTE**.
- Canal: **PORTAL**.
- Noches: **2**.
- Valor total: **$160.000**.
- Anticipo requerido: **$48.000**.

**Cálculo:** $80.000 × 1 pagante × 2 noches = $160.000.

No necesitas utilizar esta reserva para las siguientes demostraciones.

---

## Prueba 2.7 — Crear correctamente una reserva directa

### Qué hay antes de ejecutarla

B2 está libre entre hoy + 5 y hoy + 7. Sus reservas precargadas corresponden a otras fechas.

### Qué vamos a añadir

Una reserva directa para Sofía.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `DIRECTA` |
| 3 | Código del titular | `SOF004` |
| 4 | Código del apartamento | `B2` |
| 5 | Fecha de entrada | Hoy + 5 días |
| 6 | Fecha de salida | Hoy + 7 días |
| 7 | Acompañantes | Dejar vacío y aceptar |

### Input importante y razón

El canal `DIRECTA` crea la reserva directamente en estado `CONFIRMADA`.

### Respuesta esperada

- Reserva creada correctamente.
- Estado: **CONFIRMADA**.
- Canal: **DIRECTA**.
- Noches: **2**.
- Valor total: **$220.000**.

**Cálculo:** $110.000 × 1 pagante × 2 noches.

---

## Prueba 2.8 — Crear correctamente una reserva externa

### Qué hay antes de ejecutarla

A1 está libre entre hoy + 5 y hoy + 7. El código externo que utilizaremos no está registrado.

### Qué vamos a añadir

Una reserva externa para Kevin, Laura y Mateo.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `2` |
| 2 | Canal | `EXTERNA` |
| 3 | Código externo | `BK-DEMO-001` |
| 4 | Código del titular | `KEV001` |
| 5 | Código del apartamento | `A1` |
| 6 | Fecha de entrada | Hoy + 5 días |
| 7 | Fecha de salida | Hoy + 7 días |
| 8 | Acompañantes | `LAU002,MAT003` |

### Inputs importantes y razón

- `BK-DEMO-001` es diferente al código precargado.
- Son tres ocupantes, pero solamente dos pagantes.
- El canal `EXTERNA` crea la reserva en `CONFIRMADA`.

### Respuesta esperada

- Reserva creada correctamente.
- Estado: **CONFIRMADA**.
- Canal: **EXTERNA**.
- Noches: **2**.
- Valor total: **$600.000**.

**Cálculo:** $150.000 × 2 pagantes × 2 noches.

---

# Demostración de la opción 3: Confirmar reserva pendiente

## Prueba 3.1 — Confirmación exitosa con anticipo precargado

### Qué hay antes de ejecutarla

`R-CONF01` tiene:

- Estado `PENDIENTE`.
- Canal `PORTAL`.
- Estadía por $900.000.
- Un pago precargado de $270.000: exactamente el 30%.

### Qué vamos a añadir

No se añaden entidades ni pagos. Se cambia el estado de la reserva.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `3` |
| 2 | Código de reserva | `R-CONF01` |

### Input importante y razón

Esta reserva está preparada específicamente para confirmar sin registrar un pago durante la demostración.

La comparación es:

**$270.000 pagados ≥ $270.000 requeridos → `true`.**

### Respuesta esperada

> Reserva R-CONF01 confirmada correctamente.

Su estado pasa a **CONFIRMADA**. El saldo permanece en **$630.000**, porque confirmar no registra otro movimiento financiero.

---

## Prueba 3.2 — Rechazo por falta de anticipo

### Qué hay antes de ejecutarla

`R-PEND01` está pendiente por portal y no tiene pagos.

### Qué vamos a añadir

Nada. El intento debe rechazarse.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `3` |
| 2 | Código de reserva | `R-PEND01` |

### Input importante y razón

Su estadía cuesta $330.000. El 30% requerido es $99.000, pero tiene $0 pagados.

### Respuesta esperada

Un mensaje con:

- No se pudo confirmar la reserva.
- Anticipo requerido: **$99.000**.
- Total pagado: **$0**.

La reserva permanece en **PENDIENTE**.

---

# Demostración de la opción 4: Gestionar folio

> Estas tres pruebas usan `R-FOL01`. Ejecútalas en el orden indicado para que coincidan los saldos.

## Prueba 4.1 — Consultar el folio

### Qué hay antes de ejecutarla

`R-FOL01` tiene un cargo de estadía de $180.000 y ningún pago.

### Qué vamos a añadir

Nada. Solo mostraremos su resumen.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `4` |
| 2 | Código de reserva | `R-FOL01` |
| 3 | Acción | `VER` |

### Respuesta esperada y razón

| Dato | Valor |
|---|---:|
| Total de cargos | $180.000 |
| Total de pagos | $0 |
| Total de devoluciones | $0 |
| Saldo | **$180.000** |

El saldo corresponde a cargos menos pagos más devoluciones.

---

## Prueba 4.2 — Registrar un pago

### Qué hay antes de ejecutarla

El folio sigue con saldo de $180.000.

### Qué vamos a añadir

Un pago de $60.000 mediante tarjeta.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `4` |
| 2 | Código de reserva | `R-FOL01` |
| 3 | Acción | `PAGO` |
| 4 | Monto | `60000` |
| 5 | Medio de pago | `TARJETA` |

### Input importante y razón

El pago es positivo y tiene medio y fecha válidos. `registrarPagoPorFolioID()` debe retornar `true`.

### Respuesta esperada

> Pago registrado.

El resumen debe mostrar:

- Total de cargos: **$180.000**.
- Total de pagos: **$60.000**.
- Saldo: **$120.000**.

**Cálculo:** $180.000 − $60.000 = $120.000.

---

## Prueba 4.3 — Registrar un cargo adicional

### Qué hay antes de ejecutarla

Después de la prueba anterior:

- Cargos: $180.000.
- Pagos: $60.000.
- Saldo: $120.000.

### Qué vamos a añadir

Un cargo de lavandería por $25.000.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `4` |
| 2 | Código de reserva | `R-FOL01` |
| 3 | Acción | `CARGO` |
| 4 | Nombre del cargo | `Lavandería` |
| 5 | Valor | `25000` |

### Input importante y razón

El cargo tiene nombre y precio válidos. `registrarCargoPorFolioID()` debe retornar `true`.

### Respuesta esperada

> Cargo registrado.

El resumen debe mostrar:

- Total de cargos: **$205.000**.
- Total de pagos: **$60.000**.
- Saldo: **$145.000**.

**Cálculo:** $180.000 + $25.000 − $60.000 = $145.000.

---

# Demostración de la opción 5: Realizar check-in

### Qué hay antes de ejecutarla

`R-CHK01` tiene:

- Estado `CONFIRMADA`.
- Fecha de entrada: hoy.
- Apartamento B1 en estado `PREPARADO`.

### Qué vamos a añadir

Nada. Se actualizarán los estados de la reserva y del apartamento.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `5` |
| 2 | Código de reserva | `R-CHK01` |

### Input importante y razón

La reserva cumple las condiciones que revisa el método:

1. Está confirmada.
2. Ya llegó la fecha de entrada.
3. Su apartamento está preparado.
4. Puede pasar a `EN_CURSO`.

### Respuesta esperada

> Check-in realizado.

- Reserva: **EN_CURSO**.
- Apartamento B1: **OCUPADO**.

`realizarCheckIn()` retorna `true`.

> No necesitas confirmar esta reserva previamente: ya estaba confirmada en la precarga.

---

# Demostración de la opción 6: Realizar check-out

### Qué hay antes de ejecutarla

`R-OUT01` ya tiene:

- Estado `EN_CURSO`.
- Apartamento C1 en `OCUPADO`.
- Estadía por $800.000.
- Pagos por $800.000.
- Saldo de $0.

### Qué vamos a añadir

Nada. Se finalizará la reserva y se actualizará el apartamento.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `6` |
| 2 | Código de reserva | `R-OUT01` |

### Input importante y razón

La reserva ya está en curso y puede pasar a `FINALIZADA`. No depende del check-in realizado con `R-CHK01`.

### Respuesta esperada

> Check-out realizado.

- Reserva: **FINALIZADA**.
- Apartamento C1: **PENDIENTE_DE_PREPARACION**.
- Saldo final: **$0**.

`realizarCheckOut()` retorna `true`.

> Su salida programada es hoy + 2. La implementación actual permite finalizar una reserva en curso sin esperar esa fecha, por lo que esta demostración representa una salida anticipada.

---

# Demostración de la opción 7: Cancelar reserva

> **Advertencia:** esta opción permite demostrar la cancelación y el registro separado de devoluciones, pero conserva un error conocido en el cálculo del importe a devolver. Los resultados siguientes describen lo que hace actualmente el código, no un resultado contable correcto.

### Qué hay antes de ejecutarla

`R-CAN01` tiene:

- Estado `CONFIRMADA`.
- Entrada en hoy + 30.
- Estadía por $330.000.
- Pago precargado de $99.000.
- Saldo inicial de $231.000.

### Qué vamos a añadir

Se registrará una devolución y la reserva pasará a cancelada.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `7` |
| 2 | Código de reserva | `R-CAN01` |

### Input importante y razón

- La reserva está en un estado cancelable.
- Faltan más de siete días para su entrada.
- La política actual aplica el 100% **sobre el valor total de la estadía**, no sobre lo pagado.

### Respuesta esperada con el código actual

- Mensaje: **Reserva cancelada**.
- Estado: **CANCELADA**.
- Devolución registrada: **$330.000**.
- Total de pagos: **$99.000**.
- Saldo mostrado: **$561.000**.

**Cálculo que realiza el folio:**

$330.000 de cargos − $99.000 de pagos + $330.000 de devoluciones = **$561.000**.

### Qué demuestra y qué no

- Sí demuestra el cambio de estado y que la devolución se registra aparte de los pagos.
- **No demuestra una devolución financieramente correcta:** se están devolviendo $330.000 cuando solo se habían recibido $99.000. Este comportamiento requiere una corrección adicional.

---

# Demostración de la opción 8: Gestionar apartamento

Las tres acciones se demostrarán con **C2**, sin alterar los apartamentos dedicados al check-in y al check-out.

## Prueba 8.1 — Registrar un bloqueo

### Qué hay antes de ejecutarla

C2 no tiene bloqueos. Su reserva `R-FOL01` corresponde a hoy + 40 hasta hoy + 42.

### Qué vamos a añadir

Un bloqueo posterior a esa reserva.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `8` |
| 2 | Código del apartamento | `C2` |
| 3 | Acción | `BLOQUEO` |
| 4 | Motivo | `Mantenimiento de pintura` |
| 5 | Fecha de inicio | Hoy + 50 días |
| 6 | Fecha final | Hoy + 52 días |

### Inputs importantes y razón

La fecha final es posterior a la inicial. El rango no se cruza con la reserva precargada de C2.

### Respuesta esperada

Un mensaje indicando que el bloqueo fue registrado en Apartaestudio.

C2 queda con **un bloqueo**.

---

## Prueba 8.2 — Registrar una novedad

### Qué hay antes de ejecutarla

C2 no tiene novedades precargadas.

### Qué vamos a añadir

La novedad **Lámpara rota**.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `8` |
| 2 | Código del apartamento | `C2` |
| 3 | Acción | `NOVEDAD` |
| 4 | Descripción | `Lámpara rota` |

### Input importante y razón

C2 existe y el objeto `Novedad` tiene una descripción. El recepcionista debe encontrar el apartamento y agregarla a su lista.

### Respuesta esperada

> Novedad registrada.

La lista mostrada incluye:

> [Lámpara rota]

C2 queda con **una novedad**. Esta acción no cambia automáticamente su estado operativo.

---

## Prueba 8.3 — Cambiar el estado operativo

### Qué hay antes de ejecutarla

C2 está en `PENDIENTE_DE_PREPARACION` desde la precarga.

### Qué vamos a añadir

Nada. Se modificará su estado operativo.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `8` |
| 2 | Código del apartamento | `C2` |
| 3 | Acción | `ESTADO` |
| 4 | Nuevo estado | `PREPARADO` |

### Input importante y razón

`PREPARADO` es una opción válida del menú y del enum `EstadoOperativo`.

### Respuesta esperada

> Estado de Apartaestudio actualizado a PREPARADO.

El bloqueo y la novedad registrados anteriormente permanecen guardados.

---

# Demostración de la opción 9: Ver estado del sistema

### Qué hay antes de ejecutarla

Si realizaste todas las pruebas anteriores una sola vez:

- Se crearon tres reservas nuevas.
- Se confirmó `R-CONF01`.
- Se realizó check-in de `R-CHK01`.
- Se finalizó `R-OUT01`.
- Se canceló `R-CAN01`.
- Se registraron movimientos en `R-FOL01`.
- Se modificó C2.

### Qué vamos a añadir

Nada. Solo se mostrarán los datos actuales.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `9` |

### Respuesta esperada: apartamentos

| Apartamento | Estado final | Bloqueos | Novedades |
|---|---|---:|---:|
| A1 | PREPARADO | 0 | 0 |
| A2 | PREPARADO | 1 | 0 |
| B1 | OCUPADO | 0 | 0 |
| B2 | PREPARADO | 0 | 0 |
| C1 | PENDIENTE_DE_PREPARACION | 0 | 0 |
| C2 | PREPARADO | 1 | 1 |
| D1 | FUERA_DE_SERVICIO | 0 | 0 |

### Respuesta esperada: reservas precargadas

| Reserva | Estado final | Saldo |
|---|---|---:|
| `R-CONF01` | CONFIRMADA | $630.000 |
| `R-PEND01` | PENDIENTE | $330.000 |
| `R-CHK01` | EN_CURSO | $210.000 |
| `R-OUT01` | FINALIZADA | $0 |
| `R-CAN01` | CANCELADA | $561.000 — resultado del error explicado |
| `R-FOL01` | CONFIRMADA | $145.000 |
| `R-EXT01` | CONFIRMADA | $300.000 |

También deben aparecer las tres reservas nuevas, con códigos generados:

| Reserva creada | Apartamento | Estado | Saldo |
|---|---|---|---:|
| Portal | A2 | PENDIENTE | $160.000 |
| Directa | B2 | CONFIRMADA | $220.000 |
| Externa | A1 | CONFIRMADA | $600.000 |

### Razón

Esta opción consulta las mismas listas utilizadas por las demás operaciones. Por eso permite comprobar que los cambios realmente quedaron registrados durante la ejecución.

---

# Demostración de la opción 0: Salir

### Qué hay antes de ejecutarla

El programa está mostrando el menú principal.

### Qué vamos a añadir

Nada.

### Pasos e inputs

| Paso | Dato solicitado | Valor |
|---|---|---|
| 1 | Opción del menú | `0` |

### Respuesta esperada

> Hasta luego.

El menú termina. Al ejecutar nuevamente `App`, se cargan otra vez los datos iniciales.

> Cerrar o cancelar el diálogo del menú principal también sale. Cancelar un diálogo dentro de una operación vuelve al menú.