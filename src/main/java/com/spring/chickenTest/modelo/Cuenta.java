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

	public Cuenta() {
		// TODO Auto-generated constructor stub
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
		this.dineroCuenta += dineroCuenta;
	}

	//crear metodo get cuenta
	
	

	public void venderGallina() throws SinDineroException {
		this.setDineroCuenta(this.getDineroCuenta() + 100);
	}

	public void comprarHuevo() throws SinDineroException {
		this.setDineroCuenta(this.getDineroCuenta() - 30);
	}

	public void venderHuevo() throws SinDineroException {
		this.setDineroCuenta(this.getDineroCuenta() + 80);
	}

}
