package Clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Estudiantes {
	private int codestudiante;
	private String nombre;
	private String direccion;
	private String tlf;
	private Date fechaalta;
	private List <Participa> participaen;
	public int getCodestudiante() {
		return codestudiante;
	}
	public void setCodestudiante(int codestudiante) {
		this.codestudiante = codestudiante;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTlf() {
		return tlf;
	}
	public void setTlf(String tlf) {
		this.tlf = tlf;
	}
	public Date getFechaalta() {
		return fechaalta;
	}
	public void setFechaalta(Date fechaalta) {
		this.fechaalta = fechaalta;
	}
	public List<Participa> getParticipaen() {
		return participaen;
	}
	public void setParticipaen(List<Participa> participaen) {
		this.participaen = participaen;
	}
	public Estudiantes(int codestudiante, String nombre, String direccion, String tlf, Date fechaalta,
			List<Participa> participaen) {
		super();
		this.codestudiante = codestudiante;
		this.nombre = nombre;
		this.direccion = direccion;
		this.tlf = tlf;
		this.fechaalta = fechaalta;
		this.participaen = participaen;
	} 
	public Estudiantes(int codestudiante, String nombre, String direccion, String tlf, Date fechaalta) {
		super();
		this.codestudiante = codestudiante;
		this.nombre = nombre;
		this.direccion = direccion;
		this.tlf = tlf;
		this.fechaalta = fechaalta;
		this.participaen = new ArrayList<Participa>();
	} 
	public Estudiantes() {
		super();
	}
	@Override
	public String toString() {
		return "Estudiantes [codestudiante=" + codestudiante + ", nombre=" + nombre + ", direccion=" + direccion
				+ ", tlf=" + tlf + ", fechaalta=" + fechaalta + ", participaen=" + participaen + "]";
	} 
	
	
	
	
	
	

}
