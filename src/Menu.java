import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Menu {
	private Empresa empresa;
	private DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public Menu(Empresa empresa) {
		this.empresa = empresa;
	}

	// ==================== MENÚ PRINCIPAL ====================
	public void mostrarMenuPrincipal() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== SISTEMA ===\n\n"
					+ "1. Gestionar Politicas de Cancelacion\n"
					+ "2. Gestionar Empleados\n"
					+ "3. Gestionar Apartamentos\n"
					+ "4. Gestionar Reservas\n"
					+ "5. Gestionar Huespedes\n"
					+ "6. Gestionar Noches\n"
					+ "0. Salir\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}
			switch (opcion) {
				case "1":
					menuPoliticasDeCancelacion();
					break;
				case "2":
					menuEmpleados();
					break;
				case "3":
					menuApartamentos();
					break;
				case "4":
					menuReservas();
					break;
				case "5":
					menuHuespedes();
					break;
				case "6":
					menuNoches();
					break;
				case "0":
					JOptionPane.showMessageDialog(null, "¡Gracias por usar el sistema!");
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	// ==================== MENÚ EQUIPOS ====================
	private void menuPoliticasDeCancelacion() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== GESTIÓN DE EQUIPOS ===\n\n"
					+ "1. Crear Equipo\n"
					+ "2. Listar Equipos\n"
					+ "3. Buscar Equipos por codigo\n"
					+ "4. Actualizar Equipo\n"
					+ "5. Eliminar Equipos\n"
					+ "6. Volver al Menú Principal\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}

			switch (opcion) {
				case "1":
					crearEquipos();
					break;
				case "2":
					listarEquipos();
					break;
				case "3":
					buscarEquipoPorCodigo();
					break;
				case "4":
					actualizarEquipo();
					break;
				case "5":
					eliminarEquipo();
					break;
				case "6":
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void crearEquipos() {
		// String codigo,✅
		// String marca,✅
		// String modelo,✅
		// String costoPorHora,✅
		// CondicionEquipo condicionEquipo✅

		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo:");
		if (codigo == null)
			return;

		String marca = JOptionPane.showInputDialog(null, "Ingrese marca:");
		if (marca == null)
			return;

		String modelo = JOptionPane.showInputDialog(null, "Ingrese modelo:");
		if (modelo == null)
			return;

		String costoPorHora = JOptionPane.showInputDialog(null, "Ingrese costo por hora:");

		if (costoPorHora == null || costoPorHora.trim().isEmpty()) {
			return;
		}

		long costoPorHoraLong = Long.parseLong(costoPorHora);

		CondicionEquipo condicionEquipo = ingresarCondicionEquipo();

		Equipo nuevoEquipo = new Equipo(codigo, marca, modelo, costoPorHoraLong, condicionEquipo);

		boolean resultado = empresa.getGestorGeneral().getGestorEquipos().anadirEquipo(nuevoEquipo);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Equipo creado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo crear el equipo");
		}
	}

	// Ingresar estado operativo
	public CondicionEquipo ingresarCondicionEquipo() {
		String mensaje = "Seleccione una condicion de equipo posible: \n\n"
				+ "1. UTILIZADO\n"
				+ "2. EN_MANTENIMIENTO\n"
				+ "3. FUERA_DE_SERVICIO\n\n";
		String condicionEquipo = JOptionPane.showInputDialog(null, mensaje);

		switch (condicionEquipo) {
			case "1":
				return CondicionEquipo.DISPONIBLE;
			case "2":
				return CondicionEquipo.EN_MANTENIMIENTO;
			case "3":
				return CondicionEquipo.FUERA_DE_SERVICIO;
			default:
				return CondicionEquipo.DISPONIBLE;
		}
	}

	private void listarEquipos() {
		String equipos = empresa.getGestorGeneral().getGestorEquipos().mostrarTodosEquipos();
		JOptionPane.showMessageDialog(null, equipos);
	}

	private void buscarEquipoPorCodigo() {
		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo:");
		if (codigo == null)
			return;

		Equipo equipo = empresa.getGestorGeneral().getGestorEquipos().buscarEquipoPorCodigo(codigo);
		if (equipo != null) {
			JOptionPane.showMessageDialog(null, equipo.toString());
		} else {
			JOptionPane.showMessageDialog(null, "Equipo no encontrado.");
		}
	}

	private void actualizarEquipo() {
		// String codigo,✅
		// String marca,✅
		// String modelo,✅
		// long costoPorHora,
		// CondicionEquipo condicionEquipo

		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo del equipo a actualizar:");
		if (codigo == null)
			return;

		String nuevaMarca = JOptionPane.showInputDialog(null, "Ingrese nuevo marca:");
		if (nuevaMarca == null)
			return;

		String nuevoModelo = JOptionPane.showInputDialog(null, "Ingrese nuevo modelo:");
		if (nuevoModelo == null)
			return;

		String nuevoCostoPorHora = JOptionPane.showInputDialog(null, "Ingrese nuevo costo por hora:");

		if (nuevoCostoPorHora == null || nuevoCostoPorHora.trim().isEmpty()) {
			return;
		}

		long nuevoCostoPorHoraLong = Long.parseLong(nuevoCostoPorHora);

		CondicionEquipo nuevaCondicionEquipo = ingresarCondicionEquipo();

		boolean resultado = empresa.getGestorGeneral().getGestorEquipos().actualizarInformacionEquipo(codigo, nuevaMarca,
				nuevoModelo, nuevoCostoPorHoraLong, nuevaCondicionEquipo);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Equipo actualizado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Equipo no encontrado.");
		}
	}

	private void eliminarEquipo() {
		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo del equipo a eliminar:");
		if (codigo == null)
			return;

		boolean resultado = empresa.getGestorGeneral().getGestorEquipos().eliminarEquipo(codigo);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Equipo eliminado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Equipo no encontrado.");
		}
	}

	// ==================== MENÚ OPERADORES ====================
	private void menuEmpleados() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== GESTIÓN DE OPERADORES ===\n\n"
					+ "1. Crear Operador\n"
					+ "2. Listar Operadores\n"
					+ "3. Buscar Operador por Código\n"
					+ "4. Actualizar Estado Operador\n"
					+ "5. Eliminar Operador\n"
					+ "6. Volver al Menú Principal\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}

			switch (opcion) {
				case "1":
					crearOperador();
					break;
				case "2":
					listarOperador();
					break;
				case "3":
					buscarOperadorPorCodigo();
					break;
				case "4":
					actualizarEstadoOperador();
					break;
				case "5":
					eliminarOperador();
					break;
				case "6":
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void crearOperador() {
		// private String nombre;✅
		// private String identificacion;✅
		// private String certificacion;✅
		// private int experienciaAnios;

		String nombre = JOptionPane.showInputDialog(null, "Ingrese nombre:");
		if (nombre == null)
			return;

		String identificacion = JOptionPane.showInputDialog(null, "Ingrese identificacion:");
		if (identificacion == null)
			return;

		String certificacion = JOptionPane.showInputDialog(null, "Ingrese certificacion:");
		if (certificacion == null)
			return;

		String aniosExperiencia = JOptionPane.showInputDialog(null, "Ingrese años de experiencia:");

		if (aniosExperiencia == null || aniosExperiencia.trim().isEmpty()) {
			return;
		}

		Integer aniosExperienciaInt = Integer.parseInt(aniosExperiencia);

		Operador nuevoOperador = new Operador(nombre, identificacion, certificacion, aniosExperienciaInt);

		boolean resultado = empresa.getGestorGeneral().getGestorOperadores().anadirOperador(nuevoOperador);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Operador creado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo crear el Operador");
		}
	}

	private void listarOperador() {
		String operadores = empresa.getGestorGeneral().getGestorOperadores().mostrarTodosOperadores();
		JOptionPane.showMessageDialog(null, operadores);
	}

	private void buscarOperadorPorCodigo() {
		String identificacion = JOptionPane.showInputDialog(null, "Ingrese identificacion a buscar: ");
		if (identificacion == null)
			return;

		Operador operador = empresa.getGestorGeneral().getGestorOperadores()
				.buscarOperadorPorIdentificacion(identificacion);

		if (operador != null) {
			JOptionPane.showMessageDialog(null, operador.toString());
		} else {
			JOptionPane.showMessageDialog(null, "Operador no encontrado.");
		}
	}

	private void actualizarEstadoOperador() {
		// String identificacionx
		// String nombrex
		// String certificacion x
		// int experienciaAnios

		String identificacion = JOptionPane.showInputDialog(null, "Ingrese identificacion del operador a actualizar:");
		if (identificacion == null)
			return;

		String nuevaNombre = JOptionPane.showInputDialog(null, "Ingrese nuevo nombre:");
		if (nuevaNombre == null)
			return;

		String nuevoCertificacion = JOptionPane.showInputDialog(null, "Ingrese nuevo certificacion:");
		if (nuevoCertificacion == null)
			return;

		String nuevoExperienciaAnios = JOptionPane.showInputDialog(null, "Ingrese nueva experiencia en anios:");

		if (nuevoExperienciaAnios == null || nuevoExperienciaAnios.trim().isEmpty()) {
			return;
		}

		int nuevaExperienciaInt = Integer.parseInt(nuevoExperienciaAnios);

		boolean resultado = empresa.getGestorGeneral().getGestorOperadores()
				.actualizarInformacionOperador(nuevaNombre, identificacion, nuevoCertificacion, nuevaExperienciaInt);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Operario actualizado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Operario no encontrado.");
		}
	}

	private void eliminarOperador() {
		String identificacion = JOptionPane.showInputDialog(null, "Ingrese código del operario a eliminar:");
		if (identificacion == null)
			return;

		boolean resultado = empresa.getGestorGeneral().getGestorOperadores().eliminarOperador(identificacion);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Operador eliminado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Operador no encontrado.");
		}
	}

	// ==================== MENÚ CLIENTES ====================
	private void menuApartamentos() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== GESTIÓN DE CLIENTES ===\n\n"
					+ "1. Crear Cliente\n"
					+ "2. Listar Clientes\n"
					+ "3. Buscar Cliente por Código\n"
					+ "4. Eliminar Cliente\n"
					+ "6. Volver al Menú Principal\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}

			switch (opcion) {
				case "1":
					crearCliente();
					break;
				case "2":
					listarClientes();
					break;
				case "3":
					buscarClientePorCodigo();
					break;
				case "4":
					eliminarCliente();
					break;
				case "6":
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void crearCliente() {

		// String nombre x
		// String cedula x
		// String telefono x
		// String correo x

		String nombre = JOptionPane.showInputDialog(null, "Ingrese nombre:");
		if (nombre == null)
			return;

		String cedula = JOptionPane.showInputDialog(null, "Ingrese cedula:");
		if (cedula == null)
			return;

		String telefono = JOptionPane.showInputDialog(null, "Ingrese telefono:");
		if (telefono == null)
			return;

		String correo = JOptionPane.showInputDialog(null, "Ingrese correo:");
		if (correo == null)
			return;

		Cliente nuevoCliente = new Cliente(nombre, cedula, telefono, correo);

		boolean resultado = empresa.getGestorGeneral().getGestorClientes().anadirCliente(nuevoCliente);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Cliente creado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo crear el Cliente.");
		}

	}

	private void listarClientes() {
		String proyectos = empresa.getGestorGeneral().getGestorClientes().mostrarTodosClientes();
		JOptionPane.showMessageDialog(null, proyectos);
	}

	// No se puede actualizar cliente por requisito del sistema
	private void buscarClientePorCodigo() {
		String cedula = JOptionPane.showInputDialog(null, "Ingrese cédula del cliente a buscar:");
		if (cedula == null)
			return;

		Cliente cliente = empresa.getGestorGeneral().getGestorClientes().buscarClientePorCedula(cedula);
		if (cliente != null) {
			JOptionPane.showMessageDialog(null, cliente.toString());
		} else {
			JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
		}
	}

	private void eliminarCliente() {
		String cedula = JOptionPane.showInputDialog(null, "Ingrese cédula del cliente a eliminar:");
		if (cedula == null)
			return;

		boolean resultado = empresa.getGestorGeneral().getGestorClientes().eliminarCliente(cedula);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
		}
	}

	// ==================== MENÚ FUNCIONES ESPECIALES ====================
	private void menuReservas() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== GESTIÓN DE FUNCIONES ESPECIALES ===\n\n"
					+ "1. Configurar Turnos en reservas\n"
					+ "2. Consultar Equipos Disponibles para Fecha y Turno\n"
					+ "0. Volver al Menú Principal\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}

			switch (opcion) {
				case "1":
					configurarTurnosEnReserva();
					break;
				case "2":
					consultarEquiposDisponiblesParaFechaYTurno();
					break;
				case "0":
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	// todo: hacer esto
	private void configurarTurnosEnReserva() {
		empresa.getGestorGeneral().getGestorFuncionesEspeciales().configurarTurnosEnReserva(null);
		JOptionPane.showMessageDialog(null, "Opción no creada todavía.");
	}

	private void consultarEquiposDisponiblesParaFechaYTurno() {
		String fechaStr = JOptionPane.showInputDialog(null, "Ingrese fecha (yyyy-MM-dd):");
		if (fechaStr == null)
			return;

		LocalDate fecha = LocalDate.parse(fechaStr, formato);

		TurnoNombre turno = ingresarTurno();

		String equiposDisponibles = empresa.getGestorGeneral().getGestorEquipos()
				.obtenerEquiposDisponiblesParaFechaYTurno(fecha, turno,
						empresa.getGestorGeneral().getGestorReservas());

		JOptionPane.showMessageDialog(null, equiposDisponibles);
	}

	// ==================== MENÚ RESERVAS ====================

	private void menuHuespedes() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== GESTIÓN DE RESERVAS ===\n\n"
					+ "1. Crear Reserva\n"
					+ "2. Listar Reservas\n"
					+ "3. Buscar reservas por codigo\n"
					+ "4. Actualizar reserva\n"
					+ "5. Eliminar reserva\n"
					+ "6. Confirmar reserva\n"
					+ "7. Asignar cliente a reserva\n"
					+ "8. Asignar operador a reserva\n"
					+ "9. Cambiar estado de reserva\n"
					+ "10. Consultar Reservas Confirmadas por Cliente\n"
					+ "11. Consultar Valor Total de Reserva\n"
					+ "0. Volver al Menú Principal\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}

			switch (opcion) {
				case "1":
					crearReserva();
					break;
				case "2":
					listarReservas();
					break;
				case "3":
					buscarReservaPorCodigo();
					break;
				case "4":
					actualizarReserva();
					break;
				case "5":
					eliminarReserva();
					break;
				case "6":
					confirmarReserva();
					break;
				case "7":
					asignarClienteAReserva();
					break;
				case "8":
					asignarOperadorAReserva();
					break;
				case "9":
					cambiarEstadoReserva();
					break;
				case "10":
					consultarReservasConfirmadasCliente();
					break;
				case "11":
					consultarValorTotalReserva();
					break;
				case "0":
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void crearReserva() {
		// String codigo;✅
		// Cliente cliente; ❌se puede crear una reserva sin cliente.
		// LocalDate fechaReserva;✅
		// TurnoNombre turno;
		// EstadoActualProceso estadoActualProceso;✅

		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo:");
		if (codigo == null)
			return;

		String fechaDeReserva = JOptionPane.showInputDialog(null, "Ingrese fecha de reserva (yyyy-MM-dd):");
		if (fechaDeReserva == null)
			return;

		LocalDate fechaReservaDate = LocalDate.parse(fechaDeReserva, formato);

		EstadoActualProceso estadoActualProceso = ingresarEstadoActualProceso();

		TurnoNombre turno = ingresarTurno();

		Reserva nuevaReserva = new Reserva(codigo, fechaReservaDate, turno, estadoActualProceso);

		boolean resultado = empresa.getGestorGeneral().getGestorReservas().anadirReserva(nuevaReserva);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva creado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo crear la Reserva");
		}
	}

	// Ingresar estado actual proceso
	public TurnoNombre ingresarTurno() {
		String mensaje = "Seleccione un turno de reserva posible: \n\n"
				+ "1. MAÑANA\n"
				+ "2. TARDE\n"
				+ "3. NOCHE\n\n";
		String turno = JOptionPane.showInputDialog(null, mensaje);

		switch (turno) {
			case "1":
				return TurnoNombre.MANANIA;
			case "2":
				return TurnoNombre.TARDE;
			case "3":
				return TurnoNombre.NOCHE;

			default:
				return TurnoNombre.MANANIA;
		}
	}

	// Ingresar estado actual del proceso
	public EstadoActualProceso ingresarEstadoActualProceso() {
		String mensaje = "Seleccione un estado actual de proceso de reserva posible: \n\n"
				+ "1. SOLICITADA\n"
				+ "2. CONFIRMADA\n"
				+ "3. CANCELADA\n"
				+ "4. FINALIZADA\n\n";
		String estadoActual = JOptionPane.showInputDialog(null, mensaje);

		switch (estadoActual) {
			case "1":
				return EstadoActualProceso.SOLICITADA;
			case "2":
				return EstadoActualProceso.CONFIRMADA;
			case "3":
				return EstadoActualProceso.CANCELADA;
			case "4":
				return EstadoActualProceso.FINALIZADA;
			default:
				return EstadoActualProceso.SOLICITADA;
		}
	}

	private void listarReservas() {
		String reservas = empresa.getGestorGeneral().getGestorReservas().mostrarTodasReservas();
		JOptionPane.showMessageDialog(null, reservas);
	}

	private void buscarReservaPorCodigo() {
		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo:");
		if (codigo == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigo);
		if (reserva != null) {
			JOptionPane.showMessageDialog(null, reserva.toString());
		} else {
			JOptionPane.showMessageDialog(null, "Reserva no encontrado.");
		}
	}

	private void actualizarReserva() {
		// String Codigo✅
		// LocalDate nuevaFechaReserva✅
		// TurnoNombre nuevoTurno✅
		// EstadoActualProceso nuevoEstadoActualProceso

		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo de la reserva a actualizar:");
		if (codigo == null)
			return;

		String nuevaFechaDeReserva = JOptionPane.showInputDialog(null, "Ingrese nueva fecha de reserva (yyyy-MM-dd):");
		if (nuevaFechaDeReserva == null)
			return;

		LocalDate nuevaFechaReservaDate = LocalDate.parse(nuevaFechaDeReserva, formato);

		TurnoNombre nuevoTurno = ingresarTurno();

		EstadoActualProceso nuevoEstadoActualProceso = ingresarEstadoActualProceso();

		boolean resultado = empresa.getGestorGeneral().getGestorReservas().actualizarInformacionReserva(codigo,
				nuevaFechaReservaDate, nuevoTurno, nuevoEstadoActualProceso);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva actualizada exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
		}
	}

	private void eliminarReserva() {
		String codigo = JOptionPane.showInputDialog(null, "Ingrese codigo del reserva a eliminar:");
		if (codigo == null)
			return;

		boolean resultado = empresa.getGestorGeneral().getGestorReservas().eliminarReserva(codigo);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva eliminado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Reserva no encontrado.");
		}
	}

	private void confirmarReserva() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva a confirmar:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		boolean resultado = empresa.getGestorGeneral().getGestorReservas().confirmarReserva(codigoReserva);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva confirmada exitosamente.");
		} else {
			if (!reserva.tieneEquipos()) {
				JOptionPane.showMessageDialog(null, "La reserva debe tener al menos un equipo para ser confirmada.");
			} else if (!reserva.tieneOperadorSiTieneDron()) {
				JOptionPane.showMessageDialog(null, "La reserva contiene un dron pero no tiene operador asignado.");
			} else if (!reserva.puedeSerConfirmada()) {
				JOptionPane.showMessageDialog(null, "La reserva ya fue cancelada o finalizada y no puede ser confirmada.");
			} else {
				JOptionPane.showMessageDialog(null, "No se pudo confirmar la reserva.");
			}
		}
	}

	// ==================== MENÚ RESERVAS INDIVIDUALES ====================
	private void menuNoches() {
		boolean continuar = true;
		while (continuar) {
			String opciones = "=== GESTIÓN DE RESERVAS INDIVIDUALES POR EQUIPO ===\n\n"
					+ "1. Crear Reserva Individual\n"
					+ "2. Listar Reservas Individuales\n"
					+ "3. Buscar Reserva Individual por Código\n"
					+ "4. Actualizar Reserva Individual\n"
					+ "5. Eliminar Reserva Individual\n"
					+ "6. Volver al Menú Principal\n\n"
					+ "Seleccione una opción:";

			String opcion = JOptionPane.showInputDialog(null, opciones);

			if (opcion == null) {
				continuar = false;
				break;
			}

			switch (opcion) {
				case "1":
					crearReservaIndividual();
					break;
				case "2":
					listarReservasIndividuales();
					break;
				case "3":
					buscarReservaIndividualPorCodigo();
					break;
				case "4":
					actualizarReservaIndividual();
					break;
				case "5":
					eliminarReservaIndividual();
					break;
				case "6":
					continuar = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Opción inválida. Intente nuevamente.");
			}
		}
	}

	private void crearReservaIndividual() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String codigo = JOptionPane.showInputDialog(null, "Ingrese código de la reserva individual:");
		if (codigo == null)
			return;

		String codigoEquipo = JOptionPane.showInputDialog(null, "Ingrese código del equipo:");
		if (codigoEquipo == null)
			return;

		Equipo equipo = empresa.getGestorGeneral().getGestorEquipos().buscarEquipoPorCodigo(codigoEquipo);
		if (equipo == null) {
			JOptionPane.showMessageDialog(null, "Equipo no encontrado.");
			return;
		}

		String horasDeTrabajo = JOptionPane.showInputDialog(null, "Ingrese horas de trabajo:");
		if (horasDeTrabajo == null || horasDeTrabajo.trim().isEmpty()) {
			return;
		}

		long horasDeTrabajoLong = Long.parseLong(horasDeTrabajo);

		ReservaIndividualPorEquipo nuevaReservaIndividual = new ReservaIndividualPorEquipo(codigo, equipo,
				horasDeTrabajoLong);

		boolean resultado = reserva.getGestorReservasIndividuales()
				.anadirReservaIndividual(nuevaReservaIndividual, reserva,
						empresa.getGestorGeneral().getGestorReservas());

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva individual creada exitosamente.");
		} else if (!equipo.estaDisponible()) {
			JOptionPane.showMessageDialog(null, "El equipo no está disponible.");
		} else if (reserva.equipoYaExisteEnReserva(codigoEquipo)) {
			JOptionPane.showMessageDialog(null, "El equipo ya existe en esta reserva.");
		} else if (empresa.getGestorGeneral().getGestorReservas()
				.equipoConfirmadoEnOtraReserva(codigoEquipo, codigoReserva)) {
			JOptionPane.showMessageDialog(null, "El equipo ya está confirmado en otra reserva para esa fecha y turno.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo crear la reserva individual.");
		}

	}

	private void listarReservasIndividuales() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String reservasIndividuales = reserva.getGestorReservasIndividuales().mostrarTodasReservasIndividuales();
		JOptionPane.showMessageDialog(null, reservasIndividuales);
	}

	private void buscarReservaIndividualPorCodigo() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String codigo = JOptionPane.showInputDialog(null, "Ingrese código de la reserva individual:");
		if (codigo == null)
			return;

		ReservaIndividualPorEquipo reservaIndividual = reserva.getGestorReservasIndividuales()
				.buscarReservaIndividualPorCodigo(codigo);
		if (reservaIndividual != null) {
			JOptionPane.showMessageDialog(null, reservaIndividual.toString());
		} else {
			JOptionPane.showMessageDialog(null, "Reserva individual no encontrada.");
		}
	}

	private void actualizarReservaIndividual() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String codigo = JOptionPane.showInputDialog(null, "Ingrese código de la reserva individual a actualizar:");
		if (codigo == null)
			return;

		String codigoEquipo = JOptionPane.showInputDialog(null, "Ingrese nuevo código del equipo:");
		if (codigoEquipo == null)
			return;

		Equipo equipo = empresa.getGestorGeneral().getGestorEquipos().buscarEquipoPorCodigo(codigoEquipo);
		if (equipo == null) {
			JOptionPane.showMessageDialog(null, "Equipo no encontrado.");
			return;
		}

		String nuevasHoras = JOptionPane.showInputDialog(null, "Ingrese nuevas horas de trabajo:");
		if (nuevasHoras == null || nuevasHoras.trim().isEmpty()) {
			return;
		}

		long nuevasHorasLong = Long.parseLong(nuevasHoras);

		boolean resultado = reserva.getGestorReservasIndividuales()
				.actualizarInformacionReservaIndividual(codigo, equipo, nuevasHorasLong);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva individual actualizada exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Reserva individual no encontrada.");
		}
	}

	private void eliminarReservaIndividual() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String codigo = JOptionPane.showInputDialog(null, "Ingrese código de la reserva individual a eliminar:");
		if (codigo == null)
			return;

		boolean resultado = reserva.getGestorReservasIndividuales().eliminarReservaIndividual(codigo);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Reserva individual eliminada exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "Reserva individual no encontrada.");
		}
	}

	private void asignarClienteAReserva() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String cedulaCliente = JOptionPane.showInputDialog(null, "Ingrese cédula del cliente:");
		if (cedulaCliente == null)
			return;

		Cliente cliente = empresa.getGestorGeneral().getGestorClientes().buscarClientePorCedula(cedulaCliente);
		if (cliente == null) {
			JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
			return;
		}

		boolean resultado = empresa.getGestorGeneral().getGestorReservas().asignarClienteAReserva(codigoReserva, cliente);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Cliente asignado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo asignar el cliente.");
		}
	}

	private void asignarOperadorAReserva() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);

		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		String identificacionOperador = JOptionPane.showInputDialog(null, "Ingrese identificación del operador:");
		if (identificacionOperador == null)
			return;

		Operador operador = empresa.getGestorGeneral().getGestorOperadores()
				.buscarOperadorPorIdentificacion(identificacionOperador);

		if (operador == null) {
			JOptionPane.showMessageDialog(null, "Operador no encontrado.");
			return;
		}

		boolean resultado = empresa.getGestorGeneral().getGestorReservas().asignarOperadorAReserva(codigoReserva, operador);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Operador asignado exitosamente.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo asignar el operador.");
		}
	}

	private void cambiarEstadoReserva() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		// Mini Menu para cambiar de estado actual.
		String mensaje = "Estado actual: " + reserva.getEstadoActualProceso() + "\n\n"
				+ "Seleccione nuevo estado:\n\n"
				+ "1. CANCELADA\n"
				+ "2. FINALIZADA\n\n";
		String opcion = JOptionPane.showInputDialog(null, mensaje);

		if (opcion == null)
			return;

		EstadoActualProceso nuevoEstado = null;

		switch (opcion) {
			case "1":
				nuevoEstado = EstadoActualProceso.CANCELADA;
				break;
			case "2":
				nuevoEstado = EstadoActualProceso.FINALIZADA;
				break;
			default:
				JOptionPane.showMessageDialog(null, "Opción inválida.");
				return;
		}

		boolean resultado = empresa.getGestorGeneral().getGestorReservas()
				.cambiarEstadoReserva(codigoReserva, nuevoEstado);

		if (resultado) {
			JOptionPane.showMessageDialog(null, "Estado de la reserva cambiado exitosamente a " + nuevoEstado + ".");
		} else if (reserva.getEstadoActualProceso() == EstadoActualProceso.CANCELADA ||
				reserva.getEstadoActualProceso() == EstadoActualProceso.FINALIZADA) {
			JOptionPane.showMessageDialog(null,
					"La reserva ya está " + reserva.getEstadoActualProceso() + " y no puede cambiar de estado.");
		} else {
			JOptionPane.showMessageDialog(null, "No se pudo cambiar el estado de la reserva.");
		}
	}

	private void consultarReservasConfirmadasCliente() {
		String cedulaCliente = JOptionPane.showInputDialog(null, "Ingrese cédula del cliente:");
		if (cedulaCliente == null)
			return;

		String reservasConfirmadas = empresa.getGestorGeneral().getGestorReservas()
				.obtenerReservasConfirmadasPorCliente(cedulaCliente);

		JOptionPane.showMessageDialog(null, reservasConfirmadas);
	}

	private void consultarValorTotalReserva() {
		String codigoReserva = JOptionPane.showInputDialog(null, "Ingrese código de la reserva:");
		if (codigoReserva == null)
			return;

		Reserva reserva = empresa.getGestorGeneral().getGestorReservas().buscarReservaPorCodigo(codigoReserva);
		if (reserva == null) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		long valorTotal = empresa.getGestorGeneral().getGestorReservas().obtenerValorTotalReserva(codigoReserva);

		if (valorTotal == 0) {
			JOptionPane.showMessageDialog(null, "Reserva no encontrada.");
			return;
		}

		JOptionPane.showMessageDialog(null, "Valor total de la reserva: $" + valorTotal);
	}

}