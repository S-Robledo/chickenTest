package com.spring.chickenTest.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "gallina")
public class Gallina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_GALLINA")
	private int idGallina;
	
	@Column(name = "IS_HUEVO")
	private boolean isHuevo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
	
	private Date fechaPasarDeDia;

	public Gallina() {

	}

	public Gallina(boolean b) {
		this.isHuevo = b;
		this.fechaCreacion = null;
		this.fechaPasarDeDia = null;
	}

	public int getIdGallina() {
		return idGallina;
	}

	public void setIdGallina(int idGallina) {
		this.idGallina = idGallina;
	}

	public boolean isHuevo() {
		return isHuevo;
	}

	public void setHuevo(boolean isHuevo) {
		this.isHuevo = isHuevo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaPasarDeDia() {
		return fechaPasarDeDia;
	}

	public void setFechaPasarDeDia(Date pasarDeDia) {
		this.fechaPasarDeDia = pasarDeDia;
	}

	public int edadGallina() {
		int milisecondsByDay = 86400000;
		int edadProducto = (int) ((this.getFechaPasarDeDia().getTime() - this.getFechaCreacion().getTime())
				/ milisecondsByDay);
		return edadProducto;
	}

}
