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

### hasta la tarea 4.1 todo hecho.

# Resumen de cambios: `GestorReservas.java`

**Archivo modificado:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

---

## 1. Tarea 4.2: `validarCapacidadMaxima()`

Valida que el número total de ocupantes no supere la capacidad máxima del apartamento.

- **Firma:** `validarCapacidadMaxima(Apartamento apartamento, ArrayList<Huesped> ocupantes)`
- Cuenta **todos** los ocupantes, incluidos los menores de 6 años (ocupan cupo aunque no pagan).
- Retorna `true` si `totalOcupantes <= capacidadMaxima` y `false` si la excede.
- Retorna `false` si el apartamento o la lista de ocupantes son `null`.

**Mejora:** se añadió una sobrecarga que recibe un número de personas en lugar de una lista:

```java
validarCapacidadMaxima(Apartamento apartamento, int totalOcupantes)
```

La versión con lista reutiliza esta sobrecarga internamente.

---

## 2. Mejora en `anadirReserva()`

Antes solo validaba que el código no fuera nulo, vacío ni duplicado. Ahora, si la reserva tiene **apartamento y estancia** asociados, también rechaza la reserva cuando:

1. El apartamento está `FUERA_DE_SERVICIO`.
2. Los huéspedes de la estancia superan la capacidad (`validarCapacidadMaxima`).
3. Las fechas se solapan con otra reserva activa del mismo apartamento (`validarSinDobleVenta`).
4. Las fechas se cruzan con un bloqueo del apartamento (`validarBloqueos`).

> Si la reserva no tiene apartamento o estancia, se permite agregarla para no romper otras pruebas. Se puede endurecer más adelante.

---

## 3. Nuevo método: `validarBloqueos()`

La tarea 4.4 lo usaba, pero no existía, así que se creó.

- **Firma:** `validarBloqueos(Apartamento apartamento, LocalDate fechaEntrada, LocalDate fechaSalida)`
- Recorre los bloqueos del apartamento y retorna `false` si alguno se cruza con el rango solicitado.
- **Criterio de fechas:**
  - La reserva ocupa `[fechaEntrada, fechaSalida)`: el día de salida no se ocupa.
  - El bloqueo ocupa `[inicioBloqueo, finBloqueo]`: su último día sí está bloqueado.
  - Si la salida de la reserva coincide con el inicio del bloqueo, **se permite**.
  - Si la entrada de la reserva coincide con el fin del bloqueo, **no se permite**.
- Ignora los bloqueos nulos o con fechas inválidas.
- Variables con nombres descriptivos para seguir la lógica:
  - `reservaSaleAntesOElDiaQueEmpiezaElBloqueo`
  - `reservaEntraDespuesDeQueTerminaElBloqueo`
  - `hayCruceConElBloqueo`

---

## 4. Tarea 4.4: `consultarApartamentosDisponibles()`

Retorna los apartamentos disponibles para un rango de fechas y una capacidad mínima.

- **Firma:** `consultarApartamentosDisponibles(LocalDate entrada, LocalDate salida, int capacidadMinima)`
- Para cada apartamento, lo descarta si:
  1. No tiene capacidad suficiente.
  2. Está `FUERA_DE_SERVICIO` (mejora añadida).
  3. Tiene conflicto con otra reserva (`validarSinDobleVenta`).
  4. Tiene un bloqueo en esas fechas (`validarBloqueos`).
- Siempre retorna una lista (vacía si los datos son inválidos), **nunca `null`**.

**Sobrecarga:** existe una versión de 4 parámetros que recibe la lista de apartamentos directamente:

```java
consultarApartamentosDisponibles(entrada, salida, capacidadMinima, ArrayList<Apartamento> todos)
```

> **Nota:** la tarea proponía `validarSinDobleVenta(apartamento, entrada, salida, "")`. Se usó `null`, que produce el mismo resultado porque ninguna reserva tiene código vacío.

---

## 5. Corrección: acceso a los apartamentos

La tarea indicaba usar `GestorApartamentos.getListApartamentos()`, pero ese método **no es estático**, así que no compila.

**Solución:** se añadió a `GestorReservas` una referencia a `GestorApartamentos`:

- Atributo: `private GestorApartamentos theGestorApartamentos;`
- Nuevo constructor: `GestorReservas(GestorApartamentos theGestorApartamentos)`
- Getter y setter: `getTheGestorApartamentos()` / `setTheGestorApartamentos(...)`

**Conexión necesaria** (por ejemplo, en `GestorGeneral` o en el main):

```java
gestorGeneral.getTheGestorReservas()
        .setTheGestorApartamentos(gestorGeneral.getTheGestorApartamentos());
```

---
