import java.util.ArrayList;

public class Apartamento {
	private String codigo;
	private String nombre;
	private int dormitorios;
	private int capacidad;
	private ArrayList<String> caracteristicas = new ArrayList<String>();
	private int tarifaIndividual;
	private EstadoOperativo estadoOperativo;
	private String motivoFueraDeServicio;
	private ArrayList<Novedad> novedades = new ArrayList<Novedad>();
	private ArrayList<Bloqueo> bloqueos = new ArrayList<Bloqueo>();

	public Apartamento(String codigo, String nombre, int dormitorios, int capacidad, ArrayList<String> caracteristicas,
			int tarifaIndividual, EstadoOperativo estadoOperativo, String motivoFueraDeServicio, ArrayList<Novedad> novedades,
			ArrayList<Bloqueo> bloqueos) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.dormitorios = dormitorios;
		this.capacidad = capacidad;
		this.caracteristicas = caracteristicas;
		this.tarifaIndividual = tarifaIndividual;
		this.estadoOperativo = estadoOperativo;
		this.motivoFueraDeServicio = motivoFueraDeServicio;
		this.novedades = novedades;
		this.bloqueos = bloqueos;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getDormitorios() {
		return dormitorios;
	}

	public void setDormitorios(int dormitorios) {
		this.dormitorios = dormitorios;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public ArrayList<String> getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(ArrayList<String> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public int getTarifaIndividual() {
		return tarifaIndividual;
	}

	public void setTarifaIndividual(int tarifaIndividual) {
		this.tarifaIndividual = tarifaIndividual;
	}

	public EstadoOperativo getEstadoOperativo() {
		return estadoOperativo;
	}

	public void setEstadoOperativo(EstadoOperativo estadoOperativo) {
		this.estadoOperativo = estadoOperativo;
	}

	public String getMotivoFueraDeServicio() {
		return motivoFueraDeServicio;
	}

	public void setMotivoFueraDeServicio(String motivoFueraDeServicio) {
		this.motivoFueraDeServicio = motivoFueraDeServicio;
	}

	public ArrayList<Novedad> getNovedades() {
		return novedades;
	}

	public void setNovedades(ArrayList<Novedad> novedades) {
		this.novedades = novedades;
	}

	public ArrayList<Bloqueo> getBloqueos() {
		return bloqueos;
	}

	public void setBloqueos(ArrayList<Bloqueo> bloqueos) {
		this.bloqueos = bloqueos;
	}

	public void anadirNovedad(Novedad novedad) {
		this.novedades.add(novedad);
	}

	public void anadirBloqueo(Bloqueo bloqueo) {
		this.bloqueos.add(bloqueo);
	}
}
