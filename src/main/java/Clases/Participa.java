package Clases;

public class Participa {
	private int codparticipacion;
	private Estudiantes estudiante;
	// Objeto estudiante
	private Proyectos proyecto;
	// Objeto proyecto
	private String tipoparticipacion;
	private int numaportaciones;
	public int getCodparticipacion() {
		return codparticipacion;
	}
	public void setCodparticipacion(int codparticipacion) {
		this.codparticipacion = codparticipacion;
	}
	public Estudiantes getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiantes estudiante) {
		this.estudiante = estudiante;
	}
	public Proyectos getProyecto() {
		return proyecto;
	}
	public void setProyecto(Proyectos proyecto) {
		this.proyecto = proyecto;
	}
	public String getTipoparticipacion() {
		return tipoparticipacion;
	}
	public void setTipoparticipacion(String tipoparticipacion) {
		this.tipoparticipacion = tipoparticipacion;
	}
	public int getNumaportaciones() {
		return numaportaciones;
	}
	public void setNumaportaciones(int numaportaciones) {
		this.numaportaciones = numaportaciones;
	}
	public Participa(int codparticipacion, Estudiantes estudiante, Proyectos proyecto, String tipoparticipacion,
			int numaportaciones) {
		super();
		this.codparticipacion = codparticipacion;
		this.estudiante = estudiante;
		this.proyecto = proyecto;
		this.tipoparticipacion = tipoparticipacion;
		this.numaportaciones = numaportaciones;
	}
	public Participa() {
		super();
	}
	@Override
	public String toString() {
		return "Participación:  [Código: " + codparticipacion + ", Estudiante: " + estudiante.getNombre() + "\n,Proyecto: "
				+ proyecto.getNombre() + ", Tipo de participación: " + tipoparticipacion + ", Número de aportaciones: " + numaportaciones + "]";
	}
	
	
	
	
}
