Aquí tienes las **20 tareas** con el mismo formato, siguiendo el orden exacto de la lista anterior:

---

## 🔴 NullPointerException

### **Tarea 1: Apartamento — inicializar listas en el constructor**
**Archivo:** `com/ApartaSuitesUQ/Apartamento/Apartamento.java`

**Descripción:**
El constructor de `Apartamento` no inicializa `listNovedades`, `listBloqueos` ni `listReservas`, por lo que quedan en `null`. Al llamar a `anadirNovedad()`, `anadirBloqueo()`, `eliminarNovedad()` o `eliminarBloqueo()` se lanza un `NullPointerException`.

**Qué hacer:**
- Inicializar las tres listas en el constructor:
  - `this.listNovedades = new ArrayList<Novedad>();`
  - `this.listBloqueos = new ArrayList<Bloqueo>();`
  - `this.listReservas = new ArrayList<Reserva>();`
- Comprobar el resultado:
  - `anadirNovedad()` y `anadirBloqueo()` agregan elementos sin lanzar excepción
  - `eliminarNovedad()` y `eliminarBloqueo()` retornan `false` si el elemento no existe

**Notas:**
- No cambia la firma del constructor ni las llamadas existentes
- También evita el NPE al usar `Recepcionista.anadirNovedadEnApartamento()`

---

### **Tarea 2: GestorReservas.confirmarReserva() — usa la estancia equivocada**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
En `confirmarReserva()` se obtiene la estancia mediante `reserva.getTheApartamento().getTheEstancia()`, pero `Apartamento.theEstancia` nunca se asigna en el proyecto y siempre es `null`, lo que lanza un `NullPointerException`.

**Qué hacer:**
- Cambiar la obtención de la estancia:
  - De: `reserva.getTheApartamento().getTheEstancia().calcularPrecioEstadia()`
  - A: `reserva.getTheEstancia().calcularPrecioEstadia()`
- Validar que `reserva.getTheEstancia() != null` antes de llamar a `calcularPrecioEstadia()`
- Comprobar el resultado:
  - `confirmarReserva()` ya no lanza NPE al acceder a la estancia

**Notas:**
- La estancia pertenece a la `Reserva`, no al `Apartamento`

---

### **Tarea 3: Huesped.cotizarApartamentoPorCodigo() — NPE por estancia null**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
`cotizarApartamentoPorCodigo()` llama a `apartamento.getTheEstancia()`, que siempre retorna `null`, provocando NPE al llamar `calcularPrecioEstadia()`. Además, `estancia.getListNoches().getFirst()` lanza `NoSuchElementException` o NPE si `listNoches` está vacía o `null`.

**Qué hacer:**
- Dejar de usar `apartamento.getTheEstancia()`
- Crear una estancia temporal usando los parámetros `fechaEntrada`, `fechaSalida` y `apartamento`
- Validar que `estancia.getListNoches()` no sea `null` ni esté vacía antes de llamar a `getFirst()`
- Si `apartamento == null` o las fechas son inválidas, retornar `null` o un mensaje acorde sin lanzar excepción
- Comprobar el resultado:
  - Llamar a `cotizarApartamentoPorCodigo()` con un código válido y fechas válidas no lanza NPE

**Notas:**
- El método ya recibe `fechaEntrada` y `fechaSalida` en su firma, pero no los estaba usando

---

### **Tarea 4: GestorReservas.cancelarReserva() — sin null check**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`cancelarReserva()` llama a `consultar(codigoReserva)` y accede inmediatamente a `reserva.getEstadoActual()` sin verificar si `reserva` es `null`. Si el código no existe, se lanza un `NullPointerException`.

**Qué hacer:**
- Agregar validación después de `consultar(codigoReserva)`:
  - `if (reserva == null) return false;`
- Validar también que `reserva.getTheFolio() != null` antes de llamar a `registrarDevolucion()`
- Comprobar el resultado:
  - Llamar a `cancelarReserva()` con un código inexistente retorna `false`

**Notas:**
- Solo agrega validación defensiva al inicio del método

---

### **Tarea 5: GestorReservas.declararNoShow() — sin null check**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`declararNoShow()` llama a `consultar(codigoReserva)` y accede directamente a `reserva.getEstadoActual()` sin verificar si `reserva` es `null`. También accede a `getTheEstancia()` y `getTheFolio()` sin validar nulls.

**Qué hacer:**
- Agregar validación después de `consultar(codigoReserva)`:
  - `if (reserva == null) return false;`
- Validar que `reserva.getTheEstancia() != null`, `fechaEntrada != null` y `reserva.getTheFolio() != null` antes de usarlos
- Comprobar el resultado:
  - Llamar a `declararNoShow()` con un código inexistente retorna `false` sin lanzar NPE

**Notas:**
- Mantiene intacta la lógica de cambio de estado a `NO_SHOWN`

---

### **Tarea 6: Huesped.cancelarReservaPorCodigo() — sin null check**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
`cancelarReservaPorCodigo()` llama a `gestorReservas.consultar(codigoReserva)` y accede directamente a `reserva.getTheHuesped()` sin comprobar si `reserva` es `null`. Si el código no existe, se lanza un `NullPointerException`.

**Qué hacer:**
- Agregar validación después de `consultar(codigoReserva)`:
  - `if (reserva == null) return false;`
- Comprobar el resultado:
  - Llamar con un código inexistente retorna `false` en lugar de lanzar NPE

**Notas:**
- Solo se agrega una línea de validación

---

### **Tarea 7: Huesped.consultarReservas() — acumula duplicados y NPE**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
`consultarReservas()` agrega a `this.listReservas` todas las reservas del titular encontradas en `gestorReservas` sin limpiar la lista previamente, duplicando los registros en cada llamada. Además, si `listReservas` llega como `null` en el constructor o `gestorReservas` es `null`, lanza `NullPointerException`.

**Qué hacer:**
- Inicializar `this.listReservas` si es `null` y limpiarla antes de llenarla:
  - `if (this.listReservas == null) this.listReservas = new ArrayList<Reserva>();`
  - `this.listReservas.clear();`
- Validar que `gestorReservas != null && gestorReservas.getListReservas() != null` antes de recorrerla
- Comprobar el resultado:
  - Llamar dos veces seguidas a `consultarReservas()` retorna la misma lista sin duplicados
  - `consultarSaldoEnFolios()` deja de multiplicar el saldo

**Notas:**
- Corrige directamente la corrupción de saldo en `consultarSaldoEnFolios()`

---

### **Tarea 8: Huesped constructor — theGestorHuespedes sin null check**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
El constructor de `Huesped` ejecuta `theGestorHuespedes.getTheGestorGeneral()` sin validar si `theGestorHuespedes` (o el `GestorGeneral` retornado) es `null`, lo que causa un `NullPointerException` al instanciar. Además, permite que `listReservas` quede en `null`.

**Qué hacer:**
- Validar `theGestorHuespedes != null` antes de acceder a sus gestores (o lanzar `IllegalArgumentException` si es obligatorio)
- Inicializar `this.listReservas`:
  - `this.listReservas = (listReservas != null) ? listReservas : new ArrayList<Reserva>();`
- Comprobar el resultado:
  - Construir un `Huesped` con `listReservas` en `null` o sin gestor no produce NPE inesperado

**Notas:**
- Si los gestores pueden ser `null` en pruebas unitarias, conviene evitar el NPE en el constructor asignándolos solo cuando `theGestorHuespedes != null` y `gestorGeneral != null`

---

### **Tarea 9: Persona.calcularEdad() — NPE si fechaNacimiento es null**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Persona.java`

**Descripción:**
`calcularEdad()` accede a `this.fechaNacimiento.getYear()` sin validar si `this.fechaNacimiento` es `null`. Si en algún flujo se construye o manipula una `Persona` con fecha de nacimiento `null`, lanzará un `NullPointerException`.

**Qué hacer:**
- Agregar validación al inicio de `calcularEdad()`:
  - `if (this.fechaNacimiento == null) return 0;`
- Comprobar el resultado:
  - Llamar a `calcularEdad()` con `fechaNacimiento == null` retorna `0` sin lanzar NPE

**Notas:**
- No altera el cálculo normal cuando `fechaNacimiento` sí está presente

---

### **Tarea 10: Estancia.asignarNoches() — deja listNoches en null**
**Archivo:** `com/ApartaSuitesUQ/Estancia/Estancia.java`

**Descripción:**
En `asignarNoches()`, si `fechaEntrada` o `fechaSalida` son `null` o `fechaEntrada` no es anterior a `fechaSalida`, el método hace `return` antes de inicializar `this.listNoches`. Esto deja `listNoches` en `null`, provocando NPE en quien llame a `getListNoches()`.

**Qué hacer:**
- Mover la inicialización (y limpieza) de `this.listNoches` antes de la validación de fechas:
  - `if (this.listNoches == null) this.listNoches = new ArrayList<Noche>();`
  - `else this.listNoches.clear();`
  - `this.noches = 0;`
- Después de eso, validar fechas y hacer `return` si no son válidas
- Comprobar el resultado:
  - `getListNoches()` nunca retorna `null`, incluso si las fechas de entrada/salida son inválidas

**Notas:**
- Evita también que queden noches viejas si se cambia la fecha y se vuelve a llamar a `asignarNoches()` con fechas inválidas

---

### **Tarea 11: Gestores CRUD — actualizar() sin null check en parámetro**
**Archivos:** `com/ApartaSuitesUQ/Gestores/GestorApartamentos.java`, `GestorEmpleados.java`, `GestorHuespedes.java`, `GestorReservas.java`

**Descripción:**
Los métodos `actualizar(String codigo, T entidad)` en los cuatro gestores acceden a los getters de `entidad` sin comprobar si `entidad == null`. Pasar `null` como segundo parámetro lanza un `NullPointerException`.

**Qué hacer:**
- Agregar validación al inicio de `actualizar()` en los cuatro gestores:
  - `if (apartamento == null) return false;` en `GestorApartamentos`
  - `if (empleado == null) return false;` en `GestorEmpleados`
  - `if (huesped == null) return false;` en `GestorHuespedes`
  - `if (reserva == null) return false;` al inicio de `GestorReservas.actualizar()` (antes de `reserva.getTheHuesped()`)
- Comprobar el resultado:
  - Llamar a `actualizar(codigo, null)` retorna `false` en todos los gestores

**Notas:**
- Es un cambio idéntico en los cuatro archivos

---

### **Tarea 12: Reserva constructor — sin validaciones básicas**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Reserva.java`

**Descripción:**
El constructor de `Reserva` llama directamente a `theEstancia.getFechaEntrada()` y `theGestorReservas.getTheGestorGeneral()` sin validar si `theEstancia`, `theGestorReservas` o `gestorGeneral` son `null`, generando `NullPointerException`.

**Qué hacer:**
- Validar `theEstancia` antes de extraer `fechaEntrada` y `fechaSalida`:
  - `if (theEstancia != null) { ... }` (o lanzar `IllegalArgumentException`)
- Validar `theGestorReservas` y `gestorGeneral` antes de obtener `gestorPoliticaCancelacion`
- Comprobar el resultado:
  - Construir una `Reserva` con parámetros nulos no produce NPE sin control

**Notas:**
- Si la estancia es obligatoria para una reserva, lanzar `IllegalArgumentException` falla rápido y con un mensaje claro; si se quiere permitir `null`, usar `if != null`

---

### **Tarea 13: GestorReservas.confirmarReserva() — sin null check en canal**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`confirmarReserva()` llama a `reserva.getTheCanalReserva().nombreCanal()`, `reserva.getTheFolio().calcularSaldo()` y `reserva.getTheEstancia()` sin verificar que esos objetos no sean `null`. Si alguno falta, lanza `NullPointerException`.

**Qué hacer:**
- Verificar antes de usar sus métodos:
  - `reserva.getTheCanalReserva() != null`
  - `reserva.getTheEstancia() != null`
  - `reserva.getTheFolio() != null`
- Si alguno es `null`, retornar `false`
- Comprobar el resultado:
  - Confirmar una reserva con canal, estancia o folio en `null` retorna `false` sin lanzar excepción

**Notas:**
- Complementa la Tarea 2 dentro del mismo método `confirmarReserva()`

---

## 🟠 Bugs de lógica

### **Tarea 14: GestorReservas.realizarCheckIn() — siempre retorna false**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`realizarCheckIn()` inicializa `boolean res = false;`, realiza la transición de estado de la reserva y cambia el estado operativo del apartamento a `OCUPADO`, pero nunca asigna `res = true;`. El método siempre retorna `false` aunque el check-in sea exitoso.

**Qué hacer:**
- Agregar `res = true;` después de actualizar el estado de la reserva y del apartamento
- Verificar que `apartamento != null` y `reserva.getTheEstancia() != null` antes de usarlos
- Comprobar el resultado:
  - Cuando el check-in se realiza correctamente, el método retorna `true`

**Notas:**
- Asegúrate de comprobar también que `reserva.setEstadoActual(EstadoActual.EN_CURSO)` haya retornado `true`

---

### **Tarea 15: GestorReservas.realizarCheckOut() — siempre retorna false**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`realizarCheckOut()` inicializa `boolean res = false;`, cambia el estado de la reserva a `FINALIZADA` y el del apartamento a `PENDIENTE_DE_PREPARACION`, pero nunca asigna `res = true;`. El método siempre retorna `false` aunque el check-out sea exitoso.

**Qué hacer:**
- Agregar `res = true;` después de actualizar el estado de la reserva y del apartamento
- Verificar que `apartamento != null` antes de actualizar su estado operativo
- Comprobar el resultado:
  - Cuando el check-out se realiza correctamente, el método retorna `true`

**Notas:**
- Mismo bug que en `realizarCheckIn()` (Tarea 14)

---

### **Tarea 16: GestorReservas.realizarCheckIn() — no permite check-in el día de llegada**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`realizarCheckIn()` usa la condición `LocalDate.now().isAfter(fechaEntrada)`. Como `isAfter` excluye el mismo día, no permite realizar el check-in en la fecha de entrada programada, sino únicamente a partir del día siguiente.

**Qué hacer:**
- Cambiar la condición de fecha:
  - De: `if (LocalDate.now().isAfter(fechaEntrada))`
  - A: `if (fechaEntrada != null && !LocalDate.now().isBefore(fechaEntrada))`
- Comprobar el resultado:
  - Si `fechaEntrada` es hoy (`LocalDate.now()`), la validación de fecha pasa y permite hacer check-in

**Notas:**
- `!LocalDate.now().isBefore(fechaEntrada)` equivale a hoy >= `fechaEntrada` (mismo día o posterior)

---

### **Tarea 17: Folio.getSaldo() — devuelve valor obsoleto**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`

**Descripción:**
El atributo `saldo` únicamente se calcula en el constructor. Los métodos `anadirPago()` y `anadirCargo()` modifican las listas pero no recalculan `this.saldo`, de modo que `getSaldo()` retorna un valor desactualizado distinto al de `calcularSaldo()`.

**Qué hacer:**
- Llamar a `this.calcularSaldo();` al final de `anadirPago()` y `anadirCargo()` cuando el elemento se agrega con éxito
- O hacer que `getSaldo()` retorne `this.calcularSaldo();`
- Comprobar el resultado:
  - Después de agregar un pago o un cargo, `folio.getSaldo()` coincide exactamente con `folio.calcularSaldo()`

**Notas:**
- Hacer ambas cosas garantiza que tanto el campo `saldo` como `getSaldo()` estén siempre sincronizados

---

### **Tarea 18: GestorReservas.actualizar() — ignora resultado de setEstadoActual()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`actualizar()` invoca `reservaRecuperada.setEstadoActual(reserva.getEstadoActual())`, cuyo valor `boolean` es ignorado y el método siempre retorna `true`. Si la transición de estado es inválida (por ejemplo, de `FINALIZADA` a `PENDIENTE`), la reserva cambia sus demás campos pero el estado no se actualiza, reportando éxito falsamente.

**Qué hacer:**
- Verificar si el estado cambió (`reserva.getEstadoActual() != reservaRecuperada.getEstadoActual()`):
  - Si cambió y `!reservaRecuperada.puedePasarA(reserva.getEstadoActual())`, retornar `false` antes de modificar los demás campos
- Si es el mismo estado actual, no rechazar la actualización de los otros campos
- Comprobar el resultado:
  - Intentar actualizar una reserva con una transición de estado inválida retorna `false` sin corromper los otros campos
  - Actualizar otros campos manteniendo el mismo estado sigue retornando `true`

**Notas:**
- Es importante verificar si el estado cambió, porque `Reserva.puedePasarA()` retorna `false` cuando `nuevoEstado == estadoActual`

---

### **Tarea 19: GestorReservas.confirmarReserva() — lógica de anticipo invertida**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
`confirmarReserva()` compara `saldoFolio >= anticipoNecesario` usando `folio.calcularSaldo()`. Como `calcularSaldo()` calcula `sumaCargos - sumaPagos`, cada pago del huésped **disminuye** el saldo (incluso volviéndolo negativo si aún no se han agregado cargos). La condición queda invertida: pagar el anticipo impide confirmar la reserva.

**Qué hacer:**
- Calcular el total pagado sumando los montos de `folio.getListPagos()` (validando que no sea `null`)
- Cambiar la comparación:
  - De: `if (saldoFolio >= anticipoNecesario)`
  - A: `if (totalPagos >= anticipoNecesario)`
- Comprobar el resultado:
  - Si la estadía cuesta $1,000,000 y el huésped registra un pago de $300,000 (30%), `confirmarReserva()` pasa la reserva a `CONFIRMADA`

**Notas:**
- El problema ocurre sin importar si la reserva tiene o no cargos registrados previamente en el folio

---

### **Tarea 20: Folio.registrarDevolucion() — cuenta devolución como ingreso**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`

**Descripción:**
`registrarDevolucion()` crea un `Pago` normal con el monto de la devolución y lo añade a `listPagos`. Como `calcularSaldo()` hace `sumaCargos - sumaPagos`, la devolución se suma a los pagos recibidos del huésped, reduciendo su deuda o haciéndolo parecer que pagó más dinero en vez de registrar dinero devuelto por el hotel.

**Qué hacer:**
- Separar las devoluciones de los pagos recibidos (por ejemplo, creando una lista `listDevoluciones` con los montos devueltos, o un tipo/campo que distinga devoluciones)
- Actualizar `calcularSaldo()` para que descuente las devoluciones de lo pagado por el huésped (`sumaCargos - (sumaPagos - sumaDevoluciones)`, es decir, `sumaCargos - sumaPagos + sumaDevoluciones`)
- Recalcular el saldo después de registrar la devolución
- Comprobar el resultado:
  - Si un huésped pagó $300,000 (saldo -$300,000) y se registra una devolución de $300,000, el saldo queda en $0.00 en lugar de -$600,000

**Notas:**
- También evita que `GestorReservas.confirmarReserva()` (Tarea 19) cuente una devolución como parte del pago de un anticipo

---

Cuando quieras empezar, dime qué tarea quieres que implemente primero y te entrego la clase completa modificada.