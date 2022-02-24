package com.spring.chickenTest.modelo;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cuenta")
public class Cuenta {
	@Id
	private int idCuenta;
	private double dineroCuenta;
	private int gallinasVendidas;
	private int huevosVendidos;
	private double precioGallina;
	private double precioHuevo;

	public Cuenta() {
		
	}
	
	public Cuenta(int idCuenta, double dineroCuenta) {
		this.idCuenta = idCuenta;
		this.dineroCuenta = dineroCuenta;
	}

	public int getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	public double getDineroCuenta() {
		return dineroCuenta;
	}

	public void setDineroCuenta(double dineroCuenta)  throws SinDineroException {
		if(this.dineroCuenta <  0) {
			throw new SinDineroException("no hay dinero en cuenta");
		}
		if((this.dineroCuenta += dineroCuenta) < 0) {
			throw new SinDineroException("no hay suficiente dinero en cuenta");
		}
	}

	public int getGallinasVendidas() {
		return gallinasVendidas;
	}

	public void setGallinasVendidas(int gallinasVendidas) {
		this.gallinasVendidas = gallinasVendidas;
	}

	public int getHuevosVendidos() {
		return huevosVendidos;
	}

	public void setHuevosVendidos(int huevosVendidos) {
		this.huevosVendidos = huevosVendidos;
	}

	public double getPrecioGallina() {
		return precioGallina;
	}

	public void setPrecioGallina(double precioGallina) {
		this.precioGallina = precioGallina;
	}

	public double getPrecioHuevo() {
		return precioHuevo;
	}

	public void setPrecioHuevo(double precioHuevo) {
		this.precioHuevo = precioHuevo;
	}


}
