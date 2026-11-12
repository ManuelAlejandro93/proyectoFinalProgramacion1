package com.ApartaSuitesUQ.Gestores;

import com.ApartaSuitesUQ.Reserva.Reserva;
import com.ApartaSuitesUQ.Apartamento.Apartamento;
import com.ApartaSuitesUQ.Apartamento.Bloqueo;
import com.ApartaSuitesUQ.Apartamento.EstadoOperativo;
import com.ApartaSuitesUQ.Estancia.Estancia;
import com.ApartaSuitesUQ.Reserva.*;
import com.ApartaSuitesUQ.Usuarios.Huesped;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class GestorReservas implements IGestor<Reserva, String> {
	private ArrayList<Reserva> listReservas = new ArrayList<Reserva>();

	private Random generadorAleatorio = new Random();
	private GestorGeneral theGestorGeneral;

	//Gestores
	private GestorApartamentos gestorApartamentos;


    public GestorReservas(GestorGeneral theGestorGeneral) {
		this.theGestorGeneral = theGestorGeneral;
		this.gestorApartamentos = this.theGestorGeneral.getTheGestorApartamentos();

    }

    // ========== INICIO DE CRUD ========== //

    // CREATE - añadir cualquiera.
    @Override
    public boolean crear(Reserva reserva) {
        if (reserva == null || reserva.getCodigo() == null ||
                reserva.getCodigo().trim().isEmpty()) {
            return false; // Código inválido
        }
        if (consultar(reserva.getCodigo()) != null) {
            return false; // Código duplicado
        }

        listReservas.add(reserva);
        return true;
    }

    // READ (listar todas como texto, para mostrar en JOptionPane)
    // Leer
    @Override
    public String consultarTodos() {
        String resultado = "";
        if (listReservas == null || listReservas.isEmpty()) {
            return "No hay reservas registradas.";
        }

        for (int i = 0; i < listReservas.size(); i++) {
            resultado = resultado + listReservas.get(i).toString() + "\n";
        }
        return resultado;
    }

    // READ (buscar por id) devuelve objeto o null
    @Override
    public Reserva consultar(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return null; // Código inválido
        }

        Reserva resultado = null;
        for (int i = 0; i < listReservas.size(); i++) {
            if (codigo.equalsIgnoreCase(listReservas.get(i).getCodigo())) {
                resultado = listReservas.get(i);
            }
        }
        return resultado;
    }

    // UPDATE - actualiza objeto
    @Override
    public boolean actualizar(String codigo, Reserva reserva) {

//		Huesped nuevoHuespedEncargado, EstadoActual nuevoEstadoActual,
//				Folio nuevoFolio,
//				CanalReserva nuevoCanalReserva

        if (codigo == null || codigo.trim().isEmpty()) {
            return false; // Código inválido
        }

        if (reserva.getTheHuesped() == null ||
                reserva.getEstadoActual() == null || reserva.getTheFolio() == null ||
                reserva.getTheCanalReserva() == null) {
            return false; // Parámetros inválidos
        }

        Reserva reservaRecuperada = consultar(codigo);
        if (reservaRecuperada == null) {
            return false;
        }
        if (!reservaRecuperada.puedePasarA(reserva.getEstadoActual())){
            return false;
        }

        reservaRecuperada.setTheHuesped(reserva.getTheHuesped());
        reservaRecuperada.setEstadoActual(reserva.getEstadoActual());
        reservaRecuperada.setTheFolio(reserva.getTheFolio());
        reservaRecuperada.setTheCanalReserva(reserva.getTheCanalReserva());

        return true;
    }

    // DELETE objeto
    @Override
    public boolean borrar(String codigo) {

        if (codigo == null || codigo.trim().isEmpty()) {
            return false; // Código inválido
        }

        Reserva reservaRecuperada = consultar(codigo);
        if (reservaRecuperada == null) {
            return false;
        } else {
            listReservas.remove(reservaRecuperada);
            return true;
        }
    }

    // ========== FIN DE CRUD ========== //

    // ========== INICIO DE FUNCIONES DE VALIDACION ========== //

	// Genera un código con formato RES-timestamp-aleatorio que no exista en el
	// sistema.
	public String generarCodigoUnico() {
		while (true) {
			long marcaDeTiempo = System.currentTimeMillis();
			int numeroAleatorio = this.generadorAleatorio.nextInt(9000) + 1000;
			String codigoGenerado = "RES-" + marcaDeTiempo + "-" + numeroAleatorio;

			if (this.consultar(codigoGenerado) == null) {
				return codigoGenerado;
			}
		}
	}

	// Retorna true si el código externo ya está siendo utilizado
	// por una reserva cuyo canal sea EXTERNA.
	public boolean existeCodigoExterno(String codigoExterno) {
		if (codigoExterno == null || codigoExterno.trim().isEmpty()
				|| this.listReservas == null) {
			return false;
		}

		for (Reserva reserva : this.listReservas) {
			if (reserva == null) {
				continue;
			}

			CanalReserva canal = reserva.getTheCanalReserva();

			if (canal == null || canal.nombreCanal() != NombreCanal.EXTERNA) {
				continue;
			}

			String codigoDelCanal = canal.codigoCanal();

			if (codigoExterno.equals(codigoDelCanal)) {
				return true;
			}
		}

		return false;
	}

	// Valida que todos los ocupantes, incluidos los menores de 6 años,
	// no superen la capacidad máxima del apartamento.
	public boolean validarCapacidadMaxima(Apartamento apartamento, ArrayList<Huesped> ocupantes) {
		if (apartamento == null || ocupantes == null) {
			return false;
		}

		return validarCapacidadMaxima(apartamento, ocupantes.size());
	}

	// Sobrecarga para validar por número de personas, sin necesidad de crear la
	// lista.
	public boolean validarCapacidadMaxima(Apartamento apartamento, int totalOcupantes) {
		if (apartamento == null || totalOcupantes < 0) {
			return false;
		}

		return totalOcupantes <= apartamento.getCapacidad();
	}

    // Valida que una reserva no comparta noches con otra reserva activa
    // del mismo apartamento.
    public boolean validarSinDobleVenta(Apartamento apartamento, LocalDate entrada, LocalDate salida, String codigoReservaExcluir) {
        if (apartamento == null || apartamento.getCodigo() == null ||
                apartamento.getCodigo().trim().isEmpty() ||
                entrada == null || salida == null || !entrada.isBefore(salida)) {
            return false;
        }

        if (this.listReservas == null) {
            return true;
        }

        for (Reserva reserva : this.listReservas) {
            if (reserva == null) {
                continue;
            }

            if (codigoReservaExcluir != null &&
                    codigoReservaExcluir.equalsIgnoreCase(reserva.getCodigo())) {
                continue;
            }

            EstadoActual estado = reserva.getEstadoActual();

            if (estado != EstadoActual.PENDIENTE &&
                    estado != EstadoActual.CONFIRMADA &&
                    estado != EstadoActual.EN_CURSO) {
                continue;
            }

            Apartamento apartamentoReserva = reserva.getTheApartamento();

            if (apartamentoReserva == null ||
                    !apartamento.getCodigo().equalsIgnoreCase(apartamentoReserva.getCodigo())) {
                continue;
            }

            Estancia estancia = reserva.getTheEstancia();

            // Si faltan fechas de una reserva activa del mismo apartamento,
            // no se puede garantizar que esté disponible.
            if (estancia == null || estancia.getFechaEntrada() == null ||
                    estancia.getFechaSalida() == null) {
                return false;
            }

            LocalDate entradaReserva = estancia.getFechaEntrada();
            LocalDate salidaReserva = estancia.getFechaSalida();

            if (!entradaReserva.isBefore(salidaReserva)) {
                return false;
            }

            // Comprueba si los periodos se solapan.
            if (entrada.isBefore(salidaReserva) && entradaReserva.isBefore(salida)) {
                return false;
            }
        }

        return true;
    }

    // Valida que el rango solicitado no se cruce con ningún bloqueo del
    // apartamento.
    // La reserva ocupa [fechaEntrada, fechaSalida): el día de salida ya no se
    // ocupa.
    // El bloqueo ocupa [inicioBloqueo, finBloqueo]: su último día sí está
    // bloqueado.
    public boolean validarBloqueos(Apartamento apartamento, LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (apartamento == null || fechaEntrada == null || fechaSalida == null
                || !fechaEntrada.isBefore(fechaSalida)) {
            return false;
        }

        ArrayList<Bloqueo> bloqueosDelApartamento = apartamento.getListBloqueos();

        if (bloqueosDelApartamento == null || bloqueosDelApartamento.isEmpty()) {
            return true;
        }

        for (Bloqueo bloqueo : bloqueosDelApartamento) {
            if (bloqueo == null) {
                continue;
            }

            LocalDate inicioBloqueo = bloqueo.getFechaInicio();
            LocalDate finBloqueo = bloqueo.getFechafinal();

            if (inicioBloqueo == null || finBloqueo == null || finBloqueo.isBefore(inicioBloqueo)) {
                continue;
            }

            boolean reservaSaleAntesOElDiaQueEmpiezaElBloqueo = fechaSalida.isBefore(inicioBloqueo)
                    || fechaSalida.isEqual(inicioBloqueo);

            boolean reservaEntraDespuesDeQueTerminaElBloqueo = fechaEntrada.isAfter(finBloqueo);

            boolean hayCruceConElBloqueo = !reservaSaleAntesOElDiaQueEmpiezaElBloqueo
                    && !reservaEntraDespuesDeQueTerminaElBloqueo;

            if (hayCruceConElBloqueo) {
                return false;
            }
        }

        return true;
    }

	// Retorna apartamentos con capacidad suficiente, sin doble venta,
	// sin bloqueos y que no estén fuera de servicio.
	public ArrayList<Apartamento> consultarApartamentosDisponibles(LocalDate entrada, LocalDate salida,
			int capacidadMinima) {
		if (this.gestorApartamentos == null ||
				this.gestorApartamentos.getListApartamentos() == null) {
			return new ArrayList<Apartamento>();
		}

		return consultarApartamentosDisponibles(
				entrada,
				salida,
				capacidadMinima,
				this.gestorApartamentos.getListApartamentos());
	}
    public boolean declararNoShow(String codigoReserva){
        boolean res = false;
        Reserva reserva = consultar(codigoReserva);

        if (reserva.getEstadoActual() == EstadoActual.CONFIRMADA){
            LocalDate fechaEntrada = reserva.getTheEstancia().getFechaEntrada();
            if(LocalDate.now().isEqual(fechaEntrada) || LocalDate.now().isAfter(fechaEntrada)){
                LocalTime horaActual = LocalTime.now();
                if(horaActual.isAfter(LocalTime.of(22,0))){
                    if(reserva.puedePasarA(EstadoActual.NO_SHOWN)){
                        reserva.setEstadoActual(EstadoActual.NO_SHOWN);
                        Folio folio = reserva.getTheFolio();
                        folio.registrarDevolucion(0.0);
                        res = true;
                    }
                }
            }
        }

        return res;
    }

    // ========== FIN DE FUNCIONES DE VALIDACION ========== //

    // ========== INICIO DE FUNCIONES AUXILIARES ========== //

    public boolean confirmarReserva(String codigoReserva) {
        boolean res = false;

        Reserva reserva = consultar(codigoReserva);
        if (reserva != null) {
            if (reserva.getEstadoActual() == EstadoActual.PENDIENTE) {
                if (reserva.getTheCanalReserva().nombreCanal() == NombreCanal.PORTAL) {
                    double valorTotal = reserva.getTheApartamento().getTheEstancia().calcularPrecioEstadia();
                    double anticipoNecesario = valorTotal * 0.30;
                    double saldoFolio = reserva.getTheFolio().calcularSaldo();

                    if (saldoFolio >= anticipoNecesario) {
                        if (reserva.puedePasarA(EstadoActual.CONFIRMADA)) {
                            reserva.setEstadoActual(EstadoActual.CONFIRMADA);
                            res = true;
                        }
                    }
                }
            }
        }

        return res;
    }

    public boolean cancelarReserva(String codigoReserva) {
        boolean res = false;
        Reserva reserva = consultar(codigoReserva);

        if (reserva.getEstadoActual() == EstadoActual.PENDIENTE || reserva.getEstadoActual() == EstadoActual.CONFIRMADA) {
            if (reserva.puedePasarA(EstadoActual.CANCELADA)) {
                double montoDevolucion = reserva.calcularDevolucion();
                boolean resDevolucion = reserva.getTheFolio().registrarDevolucion(montoDevolucion);

                if (resDevolucion) {
                    reserva.setEstadoActual(EstadoActual.CANCELADA);
                    res = true;
                }
            }
        }

        return res;
    }

    // Sobrecarga que recibe la lista para no depender de la inyección.
    public ArrayList<Apartamento> consultarApartamentosDisponibles(LocalDate entrada, LocalDate salida, int capacidadMinima, ArrayList<Apartamento> todos) {
        ArrayList<Apartamento> disponibles = new ArrayList<Apartamento>();

		if (entrada == null || salida == null || !entrada.isBefore(salida)) {
			return disponibles;
		}

		if (capacidadMinima <= 0 || todos == null) {
			return disponibles;
		}

		for (Apartamento apartamento : todos) {
			if (apartamento == null) {
				continue;
			}

			// 1. Capacidad mínima.
			if (!validarCapacidadMaxima(apartamento, capacidadMinima)) {
				continue;
			}

			// 2. No debe estar fuera de servicio.
			if (apartamento.getEstadoOperativo() == EstadoOperativo.FUERA_DE_SERVICIO) {
				continue;
			}

			// 3. Sin doble venta.
			if (!validarSinDobleVenta(apartamento, entrada, salida, null)) {
				continue;
			}

			// 4. Sin bloqueos.
			if (!validarBloqueos(apartamento, entrada, salida)) {
				continue;
			}

			disponibles.add(apartamento);
		}

        return disponibles;
    }

    public boolean realizarCheckIn(String codigoReserva) {
        boolean res = false;

        Reserva reserva = consultar(codigoReserva);
        if (reserva != null) {
            if (reserva.getEstadoActual() == EstadoActual.CONFIRMADA) {
                LocalDate fechaEntrada = reserva.getTheEstancia().getFechaEntrada();
                if (LocalDate.now().isAfter(fechaEntrada)) {
                    Apartamento apartamento = reserva.getTheApartamento();
                    if (apartamento.getEstadoOperativo() == EstadoOperativo.PREPARADO) {
                        if (reserva.puedePasarA(EstadoActual.EN_CURSO)) {
                            reserva.setEstadoActual(EstadoActual.EN_CURSO);
                            apartamento.setEstadoOperativo(EstadoOperativo.OCUPADO);
                        }
                    }
                }
            }
        }

        return res;
    }

    public boolean realizarCheckOut(String codigoReserva) {
        boolean res = false;

        Reserva reserva = consultar(codigoReserva);
        if (reserva != null) {
            if (reserva.getEstadoActual() == EstadoActual.EN_CURSO) {
                if (reserva.puedePasarA(EstadoActual.FINALIZADA)) {
                    Apartamento apartamento = reserva.getTheApartamento();
                    reserva.setEstadoActual(EstadoActual.FINALIZADA);
                    apartamento.setEstadoOperativo(EstadoOperativo.PENDIENTE_DE_PREPARACION);
                }
            }
        }
        return res;
    }

    // ========== FIN DE FUNCIONES AUXILIARES ========== //

    // ========== INICIO DE GETS Y SETS ========== //

    public ArrayList<Reserva> getListReservas() {
        return this.listReservas;
    }

    public void setListReservas(ArrayList<Reserva> listReservas) {
        this.listReservas = listReservas;
    }

	public GestorGeneral getTheGestorGeneral() {
		return this.theGestorGeneral;
	}

	public void setTheGestorGeneral(GestorGeneral theGestorGeneral) {
		this.theGestorGeneral = theGestorGeneral;
	}

	public GestorApartamentos getGestorApartamentos() {
		return gestorApartamentos;
	}

	// ========== FIN DE GETS Y SETS ========== //

}
