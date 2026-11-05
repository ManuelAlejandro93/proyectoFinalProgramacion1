Hasta ahora hemos modificado **tres clases**:

### `Folio.java`
- `calcularSaldo()` suma los cargos, resta los pagos, guarda el resultado en `saldo` y lo retorna. Admite listas `null`.
- `anadirPago()` agrega pagos con monto positivo, medio de pago y fecha; inicializa la lista si hace falta.
- `anadirCargo()` agrega cargos con nombre y precio no negativo; permite cargos de valor `0`.
- `registrarDevolicion()` rechaza montos negativos, acepta `0` sin registrar nada y, para montos positivos, crea un `Pago` asociado al folio mediante `this`. Conservamos ese nombre de método tal como venía en la tarea.

### `Estancia.java`
- `asignarNoches()` crea una noche por cada fecha desde la entrada **hasta el día anterior a la salida**, y actualiza el contador `noches`.
- Añadimos `listHuespedes` y un constructor que la recibe, conservando el constructor anterior.
- `calcularPrecioEstadia()` ahora devuelve `double`. Cuenta como pagantes a los huéspedes de **6 años o más** y suma el costo noche por noche.

### `Noche.java`
- Añadimos `precioPorTemporada` como multiplicador: **BAJA `1.0`**, **MEDIA `1.5`**, **ALTA `2.0`**.
- El multiplicador se establece según la temporada y se actualiza si esta cambia; no se recibe como parámetro del constructor.

**Regla de precio actual:** por cada noche, `Estancia` calcula `tarifaIndividual del apartamento × huéspedes pagantes × multiplicador de la noche`. La tarifa guardada en `Apartamento` no cambia, y **no se cobra el día de salida**.