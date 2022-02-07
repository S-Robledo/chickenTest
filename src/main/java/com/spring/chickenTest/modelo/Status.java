package com.spring.chickenTest.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="status")
public class Status {
	
	@Id
	private int idStatus;
	private Double dinero;
	private int cantidadGallinas;
	private int cantidadHuevos;

	public Status() {
		
	}
	
	public Status(Double dinero, int cantidadHuevos, int cantidadGallinas) {
		super();
		this.dinero = dinero;
		this.cantidadHuevos = cantidadHuevos;
		this.cantidadGallinas = cantidadGallinas;
	}

	public int getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}

	public Double getDinero() {
		return dinero;
	}

	public void setDinero(Double dinero) {
		this.dinero = dinero;
	}

	public int getCantidadHuevos() {
		return cantidadHuevos;
	}

	public void setCantidadHuevos(int cantidadHuevos) {
		this.cantidadHuevos = cantidadHuevos;
	}

	public void setCantidadGallinas(int cantidadGallinas) {
		this.cantidadGallinas = cantidadGallinas;
	}

	public int getCantidadGallinas() {
		return cantidadGallinas;
	}
}