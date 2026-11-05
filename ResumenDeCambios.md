### 1 revisar.

### 2. `Folio`
- `calcularSaldo()` calcula **suma de cargos − suma de pagos** y actualiza `saldo`.
- `anadirPago()` valida el pago antes de agregarlo.
- `anadirCargo()` admite cargos de valor cero, pero no negativos.
- `registrarDevolicion()` registra una devolución positiva como un `Pago` asociado al folio con `this`; si el monto es cero, no registra nada.

### 3. `Estancia` y `Noche`
- `asignarNoches()` crea una noche por cada fecha desde la entrada **hasta el día anterior a la salida**. Confirmamos que se cobra la última noche de esa lista, pero **no el día de salida**.
- `Estancia` tiene una lista de huéspedes y `calcularPrecioEstadia()` devuelve el total. Cuentan como pagantes los huéspedes de **6 años o más**.
- `Noche` tiene `precioPorTemporada` como **multiplicador**: BAJA `1.0`, MEDIA `1.5` y ALTA `2.0`. Se actualiza según su temporada.
- El precio se calcula noche por noche: **tarifa individual del apartamento × ocupantes pagantes × multiplicador de la noche**. La tarifa base del apartamento no cambia.

### 4. Política de cancelación
- Añadimos `GestorPoliticaCancelacion.calcularDevolicion()`: aplica el porcentaje de la política según los días de antelación a la fecha de entrada.
- Acordamos manejar la política desde el gestor, **sin guardar una copia de sus valores en cada reserva**.

### 5. `Reserva`
- `puedePasarA()` valida las transiciones del ciclo de vida de la reserva.
- Tú cambiaste `setEstadoActual()` para que use esa validación y devuelva `true` o `false`.
- Añadimos las referencias a `Estancia` y `GestorPoliticaCancelacion`, sin hacer que `Reserva` dependa de `Empresa`.
- En **tu última versión**, dejaste un solo constructor, con asignaciones línea por línea, y el método `Reserva.calcularDevolucion()` delega el cálculo al gestor.

**Pendiente que dejamos para después:** revisar `GestorReservas.actualizarInformacionReserva()`, porque aún no utiliza el resultado booleano de `setEstadoActual()`. 