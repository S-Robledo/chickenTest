package com.spring.chickenTest.modelo;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "cuenta")
public class Cuenta {
	@Id
	private int idCuenta;
	private double dineroCuenta;
	private int gallinasVendidas;
	private int huevosVendidos;
	@Min(value = 0, message = "debe ingresar un numero mayor")
	@Max(value = 50, message = "la cantidad maxima es 50")
	private int gallinasCompra;
	private int huevosCompra;
	private double precioGallinaCompra;
	private double precioHuevoCompra;
	private double precioGallinaVenta;
	private double precioHuevoVenta;
	private int gallinaNacimiento;
	private int gallinaMuerte;

	public Cuenta() {

	}

	public Cuenta(int idCuenta, double dineroCuenta, int gallinasVendidas, int huevosVendidos, int gallinasCompra,
			int huevosCompra, double precioGallinaCompra, double precioHuevoCompra, double precioGallinaVenta,
			double precioHuevoVenta) {
		this.idCuenta = idCuenta;
		this.dineroCuenta = dineroCuenta;
		this.gallinasVendidas = gallinasVendidas;
		this.huevosVendidos = huevosVendidos;
		this.gallinasCompra = gallinasCompra;
		this.huevosCompra = huevosCompra;
		this.precioGallinaCompra = precioGallinaCompra;
		this.precioHuevoCompra = precioHuevoCompra;
		this.precioGallinaVenta = precioGallinaVenta;
		this.precioHuevoVenta = precioHuevoVenta;

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

	public void setDineroCuenta(double dineroCuenta) throws SinDineroException {
		if (this.dineroCuenta < 0) {
			throw new SinDineroException("Error! no hay dinero en cuenta");
		}
		if ((this.dineroCuenta += dineroCuenta) < 0) {
			throw new SinDineroException("Error! no hay suficiente dinero en cuenta");
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

	public int getGallinasCompra() {
		return gallinasCompra;
	}

	public void setGallinasCompra(int gallinasCompra) {
		this.gallinasCompra = gallinasCompra;
	}

	public int getHuevosCompra() {
		return huevosCompra;
	}

	public void setHuevosCompra(int huevosCompra) {
		this.huevosCompra = huevosCompra;
	}

	public double getPrecioGallinaCompra() {
		return precioGallinaCompra;
	}

	public void setPrecioGallinaCompra(double precioGallina) {
		this.precioGallinaCompra = precioGallina;
	}

	public double getPrecioHuevoCompra() {
		return precioHuevoCompra;
	}

	public void setPrecioHuevoCompra(double precioHuevo) {
		this.precioHuevoCompra = precioHuevo;
	}

	public double getPrecioGallinaVenta() {
		return precioGallinaVenta;
	}

	public void setPrecioGallinaVenta(double precioGallinaVenta) {
		this.precioGallinaVenta = precioGallinaVenta;
	}

	public double getPrecioHuevoVenta() {
		return precioHuevoVenta;
	}

	public void setPrecioHuevoVenta(double precioHuevoVenta) {
		this.precioHuevoVenta = precioHuevoVenta;
	}

	public int getGallinaNacimiento() {
		return gallinaNacimiento;
	}

	public void setGallinaNacimiento(int gallinaNacimiento) {
		this.gallinaNacimiento = gallinaNacimiento;
	}

	public int getGallinaMuerte() {
		return gallinaMuerte;
	}

	public void setGallinaMuerte(int gallinaMuerte) {
		this.gallinaMuerte = gallinaMuerte;
	}

}
