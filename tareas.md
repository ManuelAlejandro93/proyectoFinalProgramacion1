

---

# **DESCRIPCIONES DETALLADAS DE CADA TAREA (Sin Pseudocódigo)**

---

## **LOTE 1: Lógica Financiera Base (Tareas 1-4)**

---

### **Tarea 1.1: Folio.calcularSaldo()**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`

**Descripción:**
Implementar un método que calcule el saldo actual del folio. El saldo es la diferencia entre la suma de todos los cargos y la suma de todos los pagos.

**Qué hacer:**
- Crear dos variables: `sumaCargos` y `sumaPagos`, inicializadas en 0
- Validar que `listCargos` no sea null:
  - Si es null: usar 0 como suma
  - Si no es null: recorrer la lista y sumar todos los `precio` de cada cargo
- Validar que `listPagos` no sea null:
  - Si es null: usar 0 como suma
  - Si no es null: recorrer la lista y sumar todos los `monto` de cada pago
- Calcular: `saldo = sumaCargos - sumaPagos`
- Retornar el saldo calculado

**Notas:**
- Un saldo positivo significa que el cliente debe dinero
- Un saldo negativo significa que tiene crédito a favor
- Este es el método más crítico del sistema

---

### **Tarea 1.2: Folio.anadirPago()**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`

**Descripción:**
Agregar un pago a la lista de pagos del folio. Este método se llama cuando el huésped realiza un pago (anticipo, abono, o pago completo).

**Qué hacer:**
- Recibir un objeto `Pago` como parámetro
- Validar que el pago NO sea null:
  - Si es null: retornar `false` y no hacer nada más
- Validar que el monto del pago sea mayor a 0:
  - Si es <= 0: retornar `false` y no hacer nada más
- Si todas las validaciones pasan:
  - Agregar el pago a `listPagos` usando el método `add()`
  - Retornar `true`

**Notas:**
- No permitir pagos con monto 0 o negativo
- No permitir objetos Pago null
- Cada pago debe estar completo (monto, medio, fecha)

---

### **Tarea 1.3: Folio.anadirCargo()**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`

**Descripción:**
Agregar un cargo a la lista de cargos del folio. Los cargos incluyen: alojamiento, servicios adicionales (desayuno, lavandería), penalidades, etc.

**Qué hacer:**
- Recibir un objeto `Cargo` como parámetro
- Validar que el cargo NO sea null:
  - Si es null: retornar `false` y no hacer nada más
- Validar que el precio del cargo sea >= 0:
  - Si es negativo: retornar `false` y no hacer nada más
  - Si es 0: permitir (puede haber cargos de $0)
- Si todas las validaciones pasan:
  - Agregar el cargo a `listCargos` usando el método `add()`
  - Retornar `true`

**Notas:**
- Los cargos pueden ser 0 (por ejemplo, servicios gratuitos)
- Los cargos NO pueden ser negativos (usar devoluciones para eso)
- Cada cargo debe tener nombre y precio

---

### **Tarea 1.4: Folio.registrarDevolicion()**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`

**Descripción:**
Registrar una devolución cuando se cancela una reserva. La devolución se registra como un movimiento (no mueve dinero real), solo para auditoría y cálculo de saldo.

**Qué hacer:**
- Recibir un `double monto` como parámetro (monto a devolver)
- Validar que el monto NO sea negativo:
  - Si es negativo: retornar `false` y no hacer nada más
- Si el monto es exactamente 0:
  - No registrar nada (cliente no recibe devolución)
  - Retornar `true` (operación válida, pero sin efecto)
- Si el monto es positivo:
  - Crear un nuevo objeto `Pago` con:
    - `monto`: el monto a devolver
    - `medioDePago`: usar `MedioDePago.TRANSFERENCIA` (por defecto)
    - `fecha`: la fecha actual (`LocalDate.now()`)
    - `theEstancia`: pasar `null` (es una devolución, no un pago real)
  - Llamar a `anadirPago()` con este nuevo pago
  - Retornar el resultado de `anadirPago()` (true o false)

**Notas:**
- La devolución se registra como un "pago a favor" del cliente
- No mueve dinero real, solo registra en el sistema
- Es importante para auditoría y cálculo correcto del saldo
- Una devolución de $0 es válida (significa no hay devolución)

---

## **LOTE 2: Lógica de Estancia y Precios (Tareas 5-7)**

---

### **Tarea 2.1: Estancia.asignarNoches()**
**Archivo:** `com/ApartaSuitesUQ/Estancia/Estancia.java`

**Descripción:**
Crear un objeto `Noche` por cada día de la estancia (entre fechaEntrada y fechaSalida, sin incluir fechaSalida). Cada noche debe tener fecha, temporada y precio.

**Qué hacer:**
- Validar que `fechaEntrada` sea menor que `fechaSalida`:
  - Si no: no hacer nada (la estancia es inválida)
- Validar que `listNoches` esté vacío o null:
  - Si no está vacío: limpiar la lista primero
- Crear un contador `nocheCount` inicializado en 0
- Crear una variable `fechaActual` igual a `fechaEntrada`
- Hacer un loop mientras `fechaActual` sea menor que `fechaSalida`:
  - Crear un nuevo objeto `Noche` con:
    - `fecha`: `fechaActual`
    - `temporada`: `Temporada.BAJA` (por defecto)
    - `precio`: `0.0` (se calculará después)
    - `theEstancia`: `this` (referencia a la estancia actual)
  - Agregar la noche a `listNoches`
  - Incrementar `nocheCount` en 1
  - Avanzar `fechaActual` un día usando `plusDays(1)`
- Actualizar `this.noches` con el valor de `nocheCount`

**Notas:**
- Del 10 al 12 de marzo son 2 noches (10 y 11), no 3
- La fecha de salida NO se incluye
- Cada noche empieza con temporada BAJA y precio 0
- El contador de noches debe actualizar el atributo `noches`

---

### **Tarea 2.2: Estancia.calcularPrecioEstadia()**
**Archivo:** `com/ApartaSuitesUQ/Estancia/Estancia.java`

**Descripción:**
Calcular el precio total de la estancia sumando noche por noche. Para cada noche, multiplicar la tarifa de su temporada por el número de ocupantes que pagan (mayores de 6 años).

**Qué hacer:**
- Validar que `listNoches` NO esté vacío:
  - Si está vacío: retornar 0
- Validar que `theApartamento` NO sea null:
  - Si es null: retornar 0
- Crear una variable `ocupantesPagantes` inicializada en 0
- Recorrer la lista de ocupantes/huéspedes de la estancia:
  - Para cada huésped: llamar a `getEdad()`
  - Si la edad es >= 6: incrementar `ocupantesPagantes`
  - Los menores de 6 años no se cuentan en pagantes (pero SÍ ocupan cupo)
- Crear una variable `precioTotal` inicializada en 0
- Recorrer cada `Noche` en `listNoches`:
  - Obtener la `temporada` de la noche
  - Obtener la tarifa del apartamento para esa temporada:
    - Esto puede estar en el apartamento directamente o en un gestor
    - Usar `apartamento.getTarifaIndividual()` (asumiendo que es la tarifa base)
  - Calcular: `precioPorNoche = tarifa × ocupantesPagantes`
  - Sumar al `precioTotal`: `precioTotal += precioPorNoche`
- Retornar `precioTotal`

**Notas:**
- La edad se calcula automáticamente desde la fecha de nacimiento
- Menores de 6 años ocupan cupo pero no pagan
- Cada noche puede tener una tarifa diferente según temporada
- El cálculo es noche por noche, no global

---

### **Tarea 2.3: GestorPoliticaCancelacion.calcularDevolicion()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorPoliticaCancelacion.java`

**Descripción:**
Calcular el porcentaje de devolución según los días de antelación desde hoy hasta la fecha de entrada. Este método encapsula la lógica de la política.

**Qué hacer:**
- Recibir dos parámetros:
  - `double valorOriginal` (valor total de la estancia)
  - `LocalDate fechaEntrada` (fecha de entrada de la reserva)
- Obtener la fecha actual: `LocalDate hoy = LocalDate.now()`
- Calcular días de antelación usando `ChronoUnit.DAYS.between(hoy, fechaEntrada)`:
  - Esto da un número positivo si la entrada está en el futuro
  - Esto da un número negativo si la entrada ya pasó
- Crear una variable `porcentajeDevolución` inicializada en 0
- Aplicar la política de cancelación:
  - Si `diasAntelacion > 7`: 
    - `porcentajeDevolución = this.devolucionCompleta` (1.0 = 100%)
  - Si `diasAntelacion >= 2` y `diasAntelacion <= 7`:
    - `porcentajeDevolución = this.devolucionParcial` (0.5 = 50%)
  - Si `diasAntelacion < 2`:
    - `porcentajeDevolución = this.sinDevolucion` (0.0 = 0%)
- Calcular el monto a devolver: `montoDevolucion = valorOriginal × porcentajeDevolución`
- Retornar `montoDevolucion`

**Notas:**
- Más de 7 días: devolución completa
- Entre 2 y 7 días: devolución del 50%
- Menos de 48 horas (2 días): sin devolución
- Los valores están congelados al crear la reserva

---

## **LOTE 3: Validaciones Base de Reservas (Tareas 8-9)**

---

### **Tarea 3.1: Reserva.puedePasarA()**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Reserva.java`

**Descripción:**
Validar si una reserva puede cambiar de su estado actual a un nuevo estado. Esto se hace según la tabla de ciclo de vida de las reservas.

**Qué hacer:**
- Recibir como parámetro: `EstadoActual nuevoEstado`
- Validar según el estado actual (`this.estadoActual`):
  - Si estado actual es **PENDIENTE**:
    - Permitir cambiar SOLO a: CONFIRMADA o CANCELADA
    - Cualquier otro estado: retornar false
  - Si estado actual es **CONFIRMADA**:
    - Permitir cambiar SOLO a: EN_CURSO, CANCELADA o NO_SHOW
    - Cualquier otro estado: retornar false
  - Si estado actual es **EN_CURSO**:
    - Permitir cambiar SOLO a: FINALIZADA
    - Cualquier otro estado: retornar false
  - Si estado actual es **FINALIZADA**:
    - No permitir cambios a ningún estado
    - Retornar false para cualquier cambio
  - Si estado actual es **CANCELADA**:
    - No permitir cambios a ningún estado
    - Retornar false para cualquier cambio
  - Si estado actual es **NO_SHOW**:
    - No permitir cambios a ningún estado
    - Retornar false para cualquier cambio
- Si la transición es permitida: retornar `true`
- Si la transición NO es permitida: retornar `false`

**Notas:**
- Finalizada, Cancelada y No-show son estados finales (no se puede salir de ellos)
- Solo se puede confirmar desde Pendiente
- Solo se puede hacer check-in desde Confirmada
- Solo se puede hacer check-out desde En curso

---

### **Tarea 3.2: Reserva.calcularDevolicion()**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Reserva.java`

**Descripción:**
Calcular cuánto dinero se devuelve al cliente cuando cancela una reserva, según la política de cancelación y los días de antelación.

**Qué hacer:**
- Obtener la fecha de entrada de la estancia:
  - `LocalDate fechaEntrada = this.theEstancia.getFechaEntrada()`
- Validar que `theEstancia` NO sea null:
  - Si es null: retornar 0
- Calcular el valor total de la estancia:
  - Llamar a `this.theEstancia.calcularPrecioEstadia()`
  - Guardar el resultado en `double valorTotal`
- Obtener el gestor de política de cancelación:
  - `GestorPoliticaCancelacion gestor = empresa.getTheGestorGeneral().getTheGestorPoliticaCancelacion()`
- Llamar a `gestor.calcularDevolicion(valorTotal, fechaEntrada)`:
  - Esto retorna el monto exacto a devolver
- Retornar el resultado

**Notas:**
- Este método usa el método de GestorPoliticaCancelacion
- Es la "cara" de la reserva que expone la devolución
- El cálculo depende de cuándo se cancele

---

## **LOTE 4: Validaciones de Disponibilidad (Tareas 10-15)**

---

### **Tarea 4.1: GestorReservas.validarSinDobleVenta()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Validar que un apartamento NO tenga dos reservas activas que compartan noches. Esta es la regla más importante del sistema.

**Qué hacer:**
- Recibir cuatro parámetros:
  - `Apartamento apartamento` (el apartamento a validar)
  - `LocalDate entrada` (fecha de entrada de la nueva reserva)
  - `LocalDate salida` (fecha de salida de la nueva reserva)
  - `String codigoReservaExcluir` (código de reserva a ignorar, para updates)
- Recorrer TODAS las reservas en `this.listReservas`:
  - Para cada reserva:
    - Si su código es igual a `codigoReservaExcluir`: saltar (ignorar)
    - Si su estado NO es activo (es decir, NO es Pendiente, Confirmada o En curso): saltar
    - Si su apartamento NO es el mismo que el parámetro: saltar
    - Si llegamos aquí: es una reserva activa del MISMO apartamento
      - Obtener fechas de entrada y salida de esa reserva
      - Verificar si hay solapamiento:
        - Dos rangos de fechas se solapan si: `entrada1 < salida2 AND entrada2 < salida1`
        - Si hay solapamiento: retornar `false` INMEDIATAMENTE (hay conflicto)
- Si termina el loop sin encontrar conflictos: retornar `true`

**Notas:**
- Esta es la validación más crítica del sistema
- Impide que el mismo apartamento se venda dos veces
- Usa lógica de solapamiento de fechas
- El parámetro `codigoReservaExcluir` permite actualizar reservas existentes

---

### **Tarea 4.2: GestorReservas.validarCapacidadMaxima()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Validar que el número total de ocupantes no supere la capacidad máxima del apartamento. Los menores de 6 años ocupan cupo pero no pagan.

**Qué hacer:**
- Recibir dos parámetros:
  - `Apartamento apartamento` (el apartamento a validar)
  - `ArrayList<Huesped> ocupantes` (lista de ocupantes)
- Contar cuántos ocupantes hay: `int totalOcupantes = ocupantes.size()`
- Obtener la capacidad máxima del apartamento: `int capacidadMaxima = apartamento.getCapacidad()`
- Comparar:
  - Si `totalOcupantes <= capacidadMaxima`: retornar `true` (cumple)
  - Si `totalOcupantes > capacidadMaxima`: retornar `false` (excede)

**Notas:**
- Todos los huéspedes cuentan, incluso menores de 6 años
- Los menores ocupan cupo pero no pagan
- Es una validación simple pero crítica

---

### **Tarea 4.3: GestorReservas.validarBloqueos()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Validar que el apartamento NO tenga bloqueos registrados en ninguna de las noches de la estancia.

**Qué hacer:**
- Recibir tres parámetros:
  - `Apartamento apartamento` (el apartamento a validar)
  - `LocalDate entrada` (fecha de entrada)
  - `LocalDate salida` (fecha de salida)
- Obtener la lista de bloqueos del apartamento: `apartamento.getListBloqueos()`
- Recorrer cada bloqueo en esa lista:
  - Para cada bloqueo:
    - Obtener fecha de inicio: `LocalDate fechaInicioBloqueo = bloqueo.getFechaInicio()`
    - Obtener fecha final: `LocalDate fechaFinBloqueo = bloqueo.getFechafinal()`
    - Verificar si hay solapamiento entre el bloqueo y la estancia:
      - Usar la misma lógica de solapamiento: `entrada < fechaFinBloqueo AND fechaInicioBloqueo < salida`
      - Si hay solapamiento: retornar `false` INMEDIATAMENTE (hay bloqueo)
- Si termina el loop sin encontrar bloqueos: retornar `true`

**Notas:**
- Un bloqueo impide que se venda ese rango de fechas
- Puede haber múltiples bloqueos
- Usa la misma lógica de solapamiento que validarSinDobleVenta

---

### **Tarea 4.4: GestorReservas.consultarApartamentosDisponibles()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Retornar una lista de apartamentos que están disponibles para un rango de fechas y una capacidad mínima. Esto es usado por Huésped para ver qué puede reservar.

**Qué hacer:**
- Recibir tres parámetros:
  - `LocalDate entrada` (fecha de entrada solicitada)
  - `LocalDate salida` (fecha de salida solicitada)
  - `int capacidadMinima` (mínimo de personas)
- Crear un `ArrayList<Apartamento>` vacío llamado `disponibles`
- Obtener todos los apartamentos: `ArrayList<Apartamento> todos = GestorApartamentos.getListApartamentos()`
- Recorrer cada apartamento en `todos`:
  - Para cada apartamento:
    - Si su capacidad es menor que `capacidadMinima`: saltar
    - Llamar a `validarSinDobleVenta(apartamento, entrada, salida, "")`:
      - Si retorna `false` (hay conflicto): saltar
    - Llamar a `validarBloqueos(apartamento, entrada, salida)`:
      - Si retorna `false` (hay bloqueo): saltar
    - Si llegamos aquí: el apartamento está disponible
      - Agregar a `disponibles`
- Retornar la lista `disponibles`

**Notas:**
- Usa las tres validaciones anteriores
- Es la combinación de todas ellas
- Retorna una lista completa de apartamentos disponibles

---

### **Tarea 4.5: GestorReservas.generarCodigoUnico()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Generar un código único para cada reserva. El código debe ser diferente cada vez y no debe existir ya en el sistema.

**Qué hacer:**
- Crear un loop infinito (while true):
  - Generar un código usando este formato:
    - Concatenar "RES-" (prefijo)
    - Concatenar `System.currentTimeMillis()` (timestamp actual en milisegundos)
    - Concatenar un número aleatorio entre 1000 y 9999 usando `new Random().nextInt(9000) + 1000`
    - Ejemplo: "RES-1698753421500-5672"
  - Buscar si este código ya existe en `listReservas`:
    - Recorrer todas las reservas
    - Si encontramos una con el mismo código: continuar el loop (generar otro)
    - Si NO encontramos: salir del loop
  - Retornar el código único generado

**Notas:**
- El timestamp garantiza que sea diferente cada milisegundo
- El número aleatorio agrega más variedad
- Es prácticamente imposible que se repita

---

### **Tarea 4.6: GestorReservas.existeCodigoExterno()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Validar que un código externo NO exista ya en el sistema. Esto previene que la misma plataforma externa cree dos reservas con el mismo código.

**Qué hacer:**
- Recibir un parámetro: `String codigoExterno`
- Recorrer TODAS las reservas en `this.listReservas`:
  - Para cada reserva:
    - Obtener su canal: `CanalReserva canal = reserva.getTheCanalReserva()`
    - Validar que el canal NO sea null
    - Si el nombre del canal es **EXTERNA**:
      - Obtener el código del canal: `String codigoDelCanal = canal.getCodigoCanal()`
      - Si es igual a `codigoExterno`: retornar `true` INMEDIATAMENTE (existe)
- Si termina el loop sin encontrar coincidencias: retornar `false` (no existe)

**Notas:**
- Solo busca en reservas con canal EXTERNA
- Retorna true si YA existe (conflicto)
- Retorna false si NO existe (puede crearse)

---

## **LOTE 5: Creación de Reservas - Primera Parte (Tarea 16)**

---

### **Tarea 5.1: Recepcionista.registrarReservasPorCanalDirecto()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Recepcionista.java`

**Descripción:**
Registrar una reserva recibida por teléfono, mensajería o presencia (canal Directo). La reserva nace en estado Confirmada (no requiere anticipo).

**Qué hacer:**
- Recibir cinco parámetros:
  - `Huesped titular` (el huésped que hace la reserva)
  - `ArrayList<Huesped> ocupantes` (lista de todos los ocupantes)
  - `Apartamento apartamento` (el apartamento a reservar)
  - `LocalDate entrada` (fecha de entrada)
  - `LocalDate salida` (fecha de salida)
- Validar sin doble venta:
  - Llamar a `GestorReservas.validarSinDobleVenta(apartamento, entrada, salida, "")`
  - Si retorna `false`: retornar `false` (hay conflicto)
- Validar capacidad máxima:
  - Llamar a `GestorReservas.validarCapacidadMaxima(apartamento, ocupantes)`
  - Si retorna `false`: retornar `false` (excede capacidad)
- Generar código único:
  - Llamar a `GestorReservas.generarCodigoUnico()`
  - Guardar el resultado en `String codigo`
- Crear objeto Estancia:
  - `Estancia estancia = new Estancia(entrada, salida, new ArrayList<>(), apartamento)`
  - Llamar a `estancia.asignarNoches()` para crear las noches
- Crear objeto Folio:
  - `Folio folio = new Folio(new ArrayList<>(), new ArrayList<>(), 0.0)`
  - Vacío de cargos y pagos
- Crear objeto Reserva:
  - Parámetros:
    - `codigo`: el generado
    - `titular`: el parámetro recibido
    - `estadoActual`: **CONFIRMADA** (no Pendiente)
    - `folio`: el creado
    - `canalReserva`: `new CanalReserva(NombreCanal.DIRECTA, "")`
    - `apartamento`: el parámetro recibido
- Agregar la reserva:
  - Llamar a `GestorReservas.anadirReserva(reserva)`
  - Retornar el resultado (true o false)

**Notas:**
- Es para reservas recibidas directamente (teléfono, presencia, etc.)
- Nace confirmada, no pendiente
- No requiere anticipo
- Es la forma más simple de crear una reserva

---

## **LOTE 6: Creación de Reservas - Segunda Parte (Tareas 17-18)**

---

### **Tarea 6.1: Recepcionista.recibirReservasDelCanalExterno()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Recepcionista.java`

**Descripción:**
Recibir una reserva de una plataforma externa (Airbnb, Booking, etc.). Validar que el código externo sea único y que no choque con reservas vigentes.

**Qué hacer:**
- Recibir seis parámetros:
  - `String codigoExterno` (código de la plataforma)
  - `Huesped titular` (huésped que hace la reserva)
  - `ArrayList<Huesped> ocupantes` (lista de ocupantes)
  - `Apartamento apartamento` (apartamento a reservar)
  - `LocalDate entrada` (fecha de entrada)
  - `LocalDate salida` (fecha de salida)
- Validar que el código externo NO exista:
  - Llamar a `GestorReservas.existeCodigoExterno(codigoExterno)`
  - Si retorna `true` (ya existe): retornar `false` (conflicto)
- Validar sin doble venta:
  - Llamar a `GestorReservas.validarSinDobleVenta(apartamento, entrada, salida, "")`
  - Si retorna `false`: retornar `false` (hay conflicto)
- Validar capacidad máxima:
  - Llamar a `GestorReservas.validarCapacidadMaxima(apartamento, ocupantes)`
  - Si retorna `false`: retornar `false` (excede capacidad)
- Generar código único:
  - Llamar a `GestorReservas.generarCodigoUnico()`
  - Guardar en `String codigo`
- Crear objeto Estancia:
  - `Estancia estancia = new Estancia(entrada, salida, new ArrayList<>(), apartamento)`
  - Llamar a `estancia.asignarNoches()`
- Crear objeto Folio:
  - `Folio folio = new Folio(new ArrayList<>(), new ArrayList<>(), 0.0)`
- Crear objeto Reserva:
  - Parámetros:
    - `codigo`: el generado
    - `titular`: el parámetro
    - `estadoActual`: **CONFIRMADA** (igual que Directo)
    - `folio`: el creado
    - `canalReserva`: `new CanalReserva(NombreCanal.EXTERNA, codigoExterno)`
    - `apartamento`: el parámetro
- Agregar la reserva:
  - Llamar a `GestorReservas.anadirReserva(reserva)`
  - Retornar el resultado (true o false)

**Notas:**
- Es para reservas de plataformas externas
- Nace confirmada como Directo
- El código externo debe ser ÚNICO
- Si hay conflicto: rechazar completamente

---

### **Tarea 6.2: Huesped.crearReservaEnPortal()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
El huésped crea una reserva por el Portal web. La reserva nace en estado Pendiente y requiere anticipo del 30% para confirmar.

**Qué hacer:**
- Recibir dos parámetros:
  - `ArrayList<Huesped> ocupantes` (lista de ocupantes, incluyendo el titular)
  - Nota: El titular es `this` (el huésped que llama el método)
- Nota: El método también necesita `Apartamento apartamento`, `LocalDate entrada`, `LocalDate salida`
  - Estos pueden ser parámetros adicionales o solicitarse de otro lado
- Validar sin doble venta:
  - Llamar a `GestorReservas.validarSinDobleVenta(apartamento, entrada, salida, "")`
  - Si retorna `false`: retornar `false`
- Validar capacidad máxima:
  - Llamar a `GestorReservas.validarCapacidadMaxima(apartamento, ocupantes)`
  - Si retorna `false`: retornar `false`
- Generar código único:
  - Llamar a `GestorReservas.generarCodigoUnico()`
  - Guardar en `String codigo`
- Crear objeto Estancia:
  - `Estancia estancia = new Estancia(entrada, salida, ocupantes, apartamento)`
  - Llamar a `estancia.asignarNoches()`
- Crear objeto Folio:
  - `Folio folio = new Folio(new ArrayList<>(), new ArrayList<>(), 0.0)`
- Crear objeto Reserva:
  - Parámetros:
    - `codigo`: el generado
    - `titular`: `this` (el huésped actual)
    - `estadoActual`: **PENDIENTE** (diferencia con Directo/Externa)
    - `folio`: el creado
    - `canalReserva`: `new CanalReserva(NombreCanal.PORTAL, "")`
    - `apartamento`: el parámetro
- Agregar la reserva:
  - Llamar a `GestorReservas.anadirReserva(reserva)`
  - Retornar el resultado (true o false)

**Notas:**
- Es para reservas del Portal web
- Nace en estado PENDIENTE (diferencia clave)
- Requiere anticipo del 30% para confirmar
- El titular es el huésped que crea la reserva

---

## **LOTE 7: Transiciones de Estado (Tareas 19-23)**

---

### **Tarea 7.1: GestorReservas.confirmarReserva()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Cambiar una reserva de estado Pendiente a Confirmada. Si es del canal Portal, validar que se haya pagado el anticipo del 30%.

**Qué hacer:**
- Recibir un parámetro: `String codigoReserva`
- Buscar la reserva:
  - Llamar a `buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false` (no existe)
  - Guardar en variable `Reserva reserva`
- Validar que esté en estado Pendiente:
  - Si `reserva.getEstadoActual() != EstadoActual.PENDIENTE`: retornar `false`
- Si el canal es Portal:
  - Obtener canal: `CanalReserva canal = reserva.getTheCanalReserva()`
  - Si `canal.getNombreCanal() == NombreCanal.PORTAL`:
    - Calcular valor total:
      - `double valorTotal = reserva.getTheEstancia().calcularPrecioEstadia()`
    - Calcular anticipo requerido:
      - `double anticipoRequerido = valorTotal × 0.30`
    - Calcular pagos realizados:
      - `double saldoFolio = reserva.getTheFolio().calcularSaldo()`
      - Nota: El saldo es cargos - pagos, así que si hay pagos, el saldo será negativo
      - Los pagos son negativos en el saldo, así que sumar su valor absoluto
    - Validar anticipo:
      - Si pagos < anticipoRequerido: retornar `false` (insuficiente)
- Cambiar estado:
  - Llamar a `reserva.puedePasarA(EstadoActual.CONFIRMADA)`
  - Si retorna `false`: retornar `false`
  - Si retorna `true`: llamar a `reserva.setEstadoActual(EstadoActual.CONFIRMADA)`
- Retornar `true` (éxito)

**Notas:**
- Portal requiere 30% de anticipo
- Directo y Externa ya nacen confirmadas
- Validar transición con puedePasarA

---

### **Tarea 7.2: GestorReservas.cancelarReserva()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Cambiar una reserva a estado Cancelada, calcular la devolución según la política y registrarla en el folio.

**Qué hacer:**
- Recibir un parámetro: `String codigoReserva`
- Buscar la reserva:
  - Llamar a `buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en variable `Reserva reserva`
- Validar que esté en estado Pendiente o Confirmada:
  - Si `estadoActual != Pendiente AND estadoActual != Confirmada`: retornar `false`
- Validar transición:
  - Llamar a `reserva.puedePasarA(EstadoActual.CANCELADA)`
  - Si retorna `false`: retornar `false`
- Calcular devolución:
  - Llamar a `reserva.calcularDevolicion()`
  - Guardar en `double montoDevolucion`
- Registrar devolución:
  - Obtener folio: `Folio folio = reserva.getTheFolio()`
  - Llamar a `folio.registrarDevolicion(montoDevolucion)`
- Cambiar estado:
  - Llamar a `reserva.setEstadoActual(EstadoActual.CANCELADA)`
- Retornar `true`

**Notas:**
- Solo se puede cancelar desde Pendiente o Confirmada
- La devolución se calcula automáticamente
- Se registra en el folio para auditoría
- Libera el apartamento para otras reservas

---

### **Tarea 7.3: GestorReservas.realizarCheckIn()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Registrar la llegada del grupo (check-in). Cambiar reserva de Confirmada a En curso y apartamento a Ocupado.

**Qué hacer:**
- Recibir un parámetro: `String codigoReserva`
- Buscar la reserva:
  - Llamar a `buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en `Reserva reserva`
- Validar que esté en estado Confirmada:
  - Si `estadoActual != Confirmada`: retornar `false`
- Validar que la fecha de entrada ya llegó:
  - Obtener fecha entrada: `LocalDate fechaEntrada = reserva.getTheEstancia().getFechaEntrada()`
  - Si `LocalDate.now() < fechaEntrada`: retornar `false` (aún no es la fecha)
- Validar que el apartamento esté Preparado:
  - Obtener apartamento: `Apartamento apto = reserva.getTheApartamento()`
  - Si `apto.getEstadoOperativo() != EstadoOperativo.PREPARADO`: retornar `false`
- Validar transición:
  - Llamar a `reserva.puedePasarA(EstadoActual.EN_CURSO)`
  - Si retorna `false`: retornar `false`
- Cambiar estados:
  - Reserva: `reserva.setEstadoActual(EstadoActual.EN_CURSO)`
  - Apartamento: `apto.setEstadoOperativo(EstadoOperativo.OCUPADO)`
- Retornar `true`

**Notas:**
- Solo desde Confirmada
- Solo si es la fecha (o después)
- Solo si el apartamento está Preparado
- Cambia dos cosas: reserva y apartamento

---

### **Tarea 7.4: GestorReservas.realizarCheckOut()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Registrar la salida del grupo (check-out). Cambiar reserva a Finalizada y apartamento a Pendiente de preparación.

**Qué hacer:**
- Recibir un parámetro: `String codigoReserva`
- Buscar la reserva:
  - Llamar a `buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en `Reserva reserva`
- Validar que esté en estado En curso:
  - Si `estadoActual != EN_CURSO`: retornar `false`
- Validar transición:
  - Llamar a `reserva.puedePasarA(EstadoActual.FINALIZADA)`
  - Si retorna `false`: retornar `false`
- Cambiar estados:
  - Reserva: `reserva.setEstadoActual(EstadoActual.FINALIZADA)`
  - Apartamento: `apto.setEstadoOperativo(EstadoOperativo.PENDIENTE_DE_PREPARACION)`
- Retornar `true`

**Notas:**
- Solo desde En curso
- Cambia dos cosas: reserva y apartamento
- El apartamento queda pendiente de preparación (limpieza, revisión, etc.)

---

### **Tarea 7.5: GestorReservas.declararNoShow()**
**Archivo:** `com/ApartaSuitesUQ/Gestores/GestorReservas.java`

**Descripción:**
Declarar que el titular de una reserva no llegó antes de las 10:00 p.m. de la fecha de entrada. Cambiar estado a No-show y aplicar 0% de devolución.

**Qué hacer:**
- Recibir un parámetro: `String codigoReserva`
- Buscar la reserva:
  - Llamar a `buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en `Reserva reserva`
- Validar que esté en estado Confirmada:
  - Si `estadoActual != CONFIRMADA`: retornar `false`
- Validar que sea el día de entrada:
  - Obtener fecha entrada: `LocalDate fechaEntrada = reserva.getTheEstancia().getFechaEntrada()`
  - Si `LocalDate.now() != fechaEntrada`: retornar `false`
- Validar que sea después de las 22:00 (10:00 p.m.):
  - Obtener hora actual: `LocalTime horaActual = LocalTime.now()`
  - Si `horaActual < LocalTime.of(22, 0)`: retornar `false`
- Validar transición:
  - Llamar a `reserva.puedePasarA(EstadoActual.NO_SHOW)`
  - Si retorna `false`: retornar `false`
- Cambiar estado:
  - `reserva.setEstadoActual(EstadoActual.NO_SHOW)`
- Registrar 0% de devolución:
  - Obtener folio: `Folio folio = reserva.getTheFolio()`
  - Llamar a `folio.registrarDevolicion(0.0)` (no devuelve nada)
- Retornar `true`

**Notas:**
- Solo desde Confirmada
- Solo el mismo día de entrada
- Solo después de las 22:00 (10:00 p.m.)
- El cliente pierde todo lo pagado (0% devolución)

---

## **LOTE 8: Métodos Complementarios de Huésped (Tareas 24-27)**

---

### **Tarea 8.1: Huesped.cotizarApartamentoPorCodigo()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
Calcular y retornar el precio total de una estancia en un apartamento específico. El resultado incluye desglose noche por noche según temporada.

**Qué hacer:**
- Recibir cuatro parámetros:
  - `String codigoApartamento` (código del apartamento)
  - `LocalDate entrada` (fecha de entrada)
  - `LocalDate salida` (fecha de salida)
  - Nota: Estos pueden ser parámetros adicionales o solicitarse desde la UI
- Buscar el apartamento:
  - Llamar a `GestorApartamentos.buscarApartamentoPorCodigo(codigoApartamento)`
  - Si retorna null: retornar "Apartamento no encontrado"
- Crear objeto Estancia temporal:
  - `Estancia estancia = new Estancia(entrada, salida, new ArrayList<>(), apartamento)`
  - Llamar a `estancia.asignarNoches()` para crear las noches
- Calcular precio:
  - Llamar a `estancia.calcularPrecioEstadia()`
  - Guardar en `double precioTotal`
- Crear string de respuesta:
  - Incluir: Nombre del apartamento
  - Incluir: Fechas (entrada y salida)
  - Incluir: Número de noches
  - Incluir: Precio por noche (si aplica)
  - Incluir: Precio total
  - Formato: legible para el usuario
- Retornar el string

**Notas:**
- Es para consultas, no crea nada
- Usa objetos temporales
- Retorna información formateada para mostrar

---

### **Tarea 8.2: Huesped.consultarReservas()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
Retornar una lista de todas las reservas del huésped actual. Se deben incluir reservas en cualquier estado.

**Qué hacer:**
- Crear un `ArrayList<Reserva>` vacío llamado `misReservas`
- Obtener todas las reservas del sistema:
  - Llamar a `GestorReservas.getListReservas()`
  - Guardar en `ArrayList<Reserva> todas`
- Recorrer cada reserva en `todas`:
  - Para cada reserva:
    - Obtener el titular: `Huesped titular = reserva.getTheHuesped()`
    - Si `titular == this` (es el huésped actual):
      - Agregar a `misReservas`
- Retornar `misReservas`

**Notas:**
- Retorna TODAS las reservas del huésped (cualquier estado)
- No filtra por estado
- Retorna lista, no string

---

### **Tarea 8.3: Huesped.consultarSaldoEnFolios()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
Calcular y retornar el saldo total adeudado por el huésped en todos sus folios activos (reservas no finalizadas).

**Qué hacer:**
- Crear variable `double saldoTotal = 0`
- Obtener todas las reservas del huésped:
  - Llamar a `consultarReservas()` (usa la tarea anterior)
  - Guardar en `ArrayList<Reserva> misReservas`
- Recorrer cada reserva en `misReservas`:
  - Para cada reserva:
    - Validar que sea ACTIVA (Pendiente, Confirmada, En curso):
      - Si `estado != Pendiente AND estado != Confirmada AND estado != EN_CURSO`: saltar
    - Obtener folio: `Folio folio = reserva.getTheFolio()`
    - Calcular saldo: `double saldo = folio.calcularSaldo()`
    - Sumar: `saldoTotal += saldo`
- Retornar `saldoTotal`

**Notas:**
- Solo suma saldos de reservas activas
- Las finalizadas, canceladas y no-show no cuentan
- Es la deuda actual del huésped

---

### **Tarea 8.4: Huesped.cancelarReservaPorCodigo()**
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Huesped.java`

**Descripción:**
El huésped cancela una de sus reservas (que esté en estado Pendiente o Confirmada). Se calcula y registra la devolución.

**Qué hacer:**
- Recibir un parámetro: `String codigoReserva`
- Buscar la reserva:
  - Llamar a `GestorReservas.buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en `Reserva reserva`
- Validar que pertenezca al huésped actual:
  - Si `reserva.getTheHuesped() != this`: retornar `false` (no es de este huésped)
- Validar que esté en estado Pendiente o Confirmada:
  - Si `estado != Pendiente AND estado != Confirmada`: retornar `false`
- Llamar a cancelar:
  - Llamar a `GestorReservas.cancelarReserva(codigoReserva)`
  - Retornar el resultado (true o false)

**Notas:**
- El huésped solo puede cancelar sus propias reservas
- Solo desde Pendiente o Confirmada
- La lógica de cancelación está en el Gestor

---

## **LOTE 9: Métodos Complementarios de Recepcionista (Tareas 28-30)**

---

### **Tarea 9.1: Recepcionista.registrarPagoPorFolioID()**✅
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Recepcionista.java`

**Descripción:**
La recepción registra un pago que realiza el huésped (efectivo, tarjeta, transferencia) en el folio de su reserva.

**Qué hacer:**
- Recibir dos parámetros:
  - `String codigoReserva` (código de la reserva)
  - `Pago pago` (objeto con datos del pago)
- Buscar la reserva:
  - Llamar a `GestorReservas.buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en `Reserva reserva`
- Obtener folio:
  - `Folio folio = reserva.getTheFolio()`
  - Si es null: retornar `false`
- Agregar pago:
  - Llamar a `folio.anadirPago(pago)`
  - Retornar el resultado (true o false)

**Notas:**
- Solo registra, no valida montos
- El pago debe ser un objeto válido
- Se agrega al folio de la reserva

---

### **Tarea 9.2: Recepcionista.registrarCargoPorFolioID()**✅
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Recepcionista.java`

**Descripción:**
La recepción registra un cargo adicional en el folio (servicios extra como desayuno, lavandería, minibar, etc.).

**Qué hacer:**
- Recibir dos parámetros:
  - `String codigoReserva` (código de la reserva)
  - `Cargo cargo` (objeto con datos del cargo)
- Buscar la reserva:
  - Llamar a `GestorReservas.buscarReservaPorCodigo(codigoReserva)`
  - Si retorna null: retornar `false`
  - Guardar en `Reserva reserva`
- Obtener folio:
  - `Folio folio = reserva.getTheFolio()`
  - Si es null: retornar `false`
- Agregar cargo:
  - Llamar a `folio.anadirCargo(cargo)`
  - Retornar el resultado (true o false)

**Notas:**
- Solo registra, no valida montos
- El cargo debe ser un objeto válido
- Se agrega al folio de la reserva

---

### **Tarea 9.3: Recepcionista.anadirNovedadEnApartamento()**✅
**Archivo:** `com/ApartaSuitesUQ/Usuarios/Recepcionista.java`

**Descripción:**
La recepción registra una novedad en un apartamento (daño, faltante, rotura, suciedad, etc.). Esto es importante para auditoría y mantenimiento.

**Qué hacer:**
- Recibir dos parámetros:
  - `String codigoApartamento` (código del apartamento)
  - `Novedad novedad` (objeto con descripción de la novedad)
- Buscar el apartamento:
  - Llamar a `GestorApartamentos.buscarApartamentoPorCodigo(codigoApartamento)`
  - Si retorna null: retornar `false`
  - Guardar en `Apartamento apartamento`
- Agregar novedad:
  - Llamar a `apartamento.anadirNovedad(novedad)`
  - Este método ya existe en la clase Apartamento
- Retornar `true`

**Notas:**
- Solo registra, no filtra
- La novedad debe ser un objeto válido
- Se agrega a la lista de novedades del apartamento

---

## **LOTE 10: Métodos de Display (Tareas 31-32)**

---

### **Tarea 10.1: Implementar toString() en clases financieras**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Folio.java`, `com/ApartaSuitesUQ/Reserva/Pago.java`, `com/ApartaSuitesUQ/Estancia/Estancia.java`

**Descripción:**
Implementar métodos `toString()` en clases financieras para mostrar información formateada en alertas y listas de la UI.

**Qué hacer en Folio.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Separadores visuales (===)
  - Saldo actual (llamar a `calcularSaldo()`)
  - Total de cargos (sumar todos)
  - Total de pagos (sumar todos)
  - Formato legible con saltos de línea (\n)

**Qué hacer en Pago.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Monto (con formato $)
  - Medio de pago
  - Fecha del pago
  - Formato simple, sin separadores

**Qué hacer en Estancia.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Separadores visuales
  - Fecha de entrada
  - Fecha de salida
  - Número de noches
  - Nombre del apartamento
  - Formato legible con saltos de línea

**Notas:**
- Usar \n para saltos de línea
- Usar $ para montos
- Validar nulls antes de llamar métodos
- Formato debe ser legible para UI

---

### **Tarea 10.2: Implementar toString() en clases de reservas**
**Archivo:** `com/ApartaSuitesUQ/Reserva/Reserva.java`, `com/ApartaSuitesUQ/Usuarios/Huesped.java`, `com/ApartaSuitesUQ/Apartamento/Bloqueo.java`, `com/ApartaSuitesUQ/Apartamento/Novedad.java`

**Descripción:**
Implementar métodos `toString()` en clases de dominio para mostrar información formateada en alertas y listas de la UI.

**Qué hacer en Reserva.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Separadores visuales
  - Código de la reserva
  - Nombre del huésped titular
  - Estado actual
  - Canal de reserva
  - Nombre del apartamento
  - Saldo del folio
  - Formato legible con saltos de línea
  - Validar nulls

**Qué hacer en Huesped.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Separadores visuales
  - Nombre
  - Código/documento
  - Fecha de nacimiento
  - Edad
  - Teléfono
  - Correo
  - Foto (si existe)
  - Titular (Sí/No)
  - Formato legible con saltos de línea
  - Validar nulls

**Qué hacer en Bloqueo.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Separadores visuales
  - Motivo del bloqueo
  - Fecha de inicio
  - Fecha final
  - Formato legible

**Qué hacer en Novedad.java:**
- Crear método `toString()` que retorne un String
- Incluir:
  - Simple: solo el nombre de la novedad
  - Sin separadores necesarios

**Notas:**
- Todos deben usar \n para formato
- Validar nulls antes de mostrar
- Formato debe ser legible para UI
- Son para display, no para lógica

---

**FIN DE LAS 32 TAREAS** ✅

