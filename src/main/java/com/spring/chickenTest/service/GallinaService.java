package com.spring.chickenTest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.interfaces.ICuenta;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.interfaces.IStatus;
import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.ExceedsLimitException;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;

@Service
public class GallinaService implements IGallinaService {

	@Autowired
	private IGallina iGallinaData;

	@Autowired
	private ICuenta iCuentaData;

	@Autowired
	private IStatusService iStatusService;

	private final double PRECIO_GALLINA = 500;

	private final double PRECIO_HUEVO = 20;

	private final double MAXIMO_EN_CUENTA = 100000;// sugiere 100000

	private final int LIMITE_CANT_COMPRA = 30000;

	private final int LIMITE_CANT_VENTA = 2;
	
	private final double GANANCIA_GALLINA = 1.30;
	
	private final double GANANCIA_HUEVO = 1.30;

	public double getPRECIO_GALLINA() {
		return PRECIO_GALLINA;
	}

	public double getPRECIO_HUEVO() {
		return PRECIO_HUEVO;
	}

	@Override
	public List<Gallina> listarProductos() {
		return (List<Gallina>) iGallinaData.findAll();
	}

	@Override
	public List<Gallina> listarGallinas() {
		List<Gallina> listaCompleta = this.listarProductos();
		List<Gallina> listaGallinas = new ArrayList<Gallina>();

		for (Gallina gallina : listaCompleta) {
			if (!gallina.isHuevo()) {
				listaGallinas.add(gallina);
			}
		}
		return listaGallinas;
	}

	@Override
	public List<Gallina> listarHuevos() {
		List<Gallina> listaCompleta = this.listarProductos();
		List<Gallina> listaHuevos = new ArrayList<Gallina>();

		for (Gallina gallina : listaCompleta) {
			if (gallina.isHuevo()) {
				listaHuevos.add(gallina);
			}
		}
		return listaHuevos;
	}

	@Override
	public void crearGallina(int cant) throws ProductoException, SinDineroException { // funciona

		iStatusService.actualizarCuenta(PRECIO_GALLINA, PRECIO_HUEVO, true, cant); // revisado
		this.crearProducto(false, cant, true);
	}

	@Override
	public void crearHuevo(int cant) throws ProductoException, SinDineroException {

		iStatusService.actualizarCuenta(PRECIO_GALLINA, PRECIO_HUEVO, false, cant);
		this.crearProducto(true, cant, true);

	}

	@Override
	public void crearProducto(boolean esHuevo, int cant, boolean esCompra) throws ProductoException {

		double dinero = 0;
		if (esHuevo) {
			dinero = PRECIO_HUEVO;
		} else {
			dinero = PRECIO_GALLINA;
		}
		for (int i = 0; i < cant; i++) {

			Gallina g = new Gallina(esHuevo);
			g.setCreacion(iStatusService.obtenerDia());
			g.setPasarDia(iStatusService.obtenerDia());
			g.setDinero(dinero);
			Gallina gallina = iGallinaData.save(g);
			if (esCompra) {
				this.registrarCompra(g);
			}
			if (gallina.equals(null)) {
				throw new ProductoException("Error! No se pudo crear Producto");
			}
		}
	}

	@Override
	public void eliminarProducto(Gallina gallina) throws SinDineroException {
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		double dineroCuenta = 0;
		if (!gallina.isHuevo()) {
			cuenta.setPrecioGallina(gallina.getDinero() * GANANCIA_GALLINA);
			dineroCuenta = gallina.getDinero();
			cuenta.setGallinasVendidas(cuenta.getGallinasVendidas() + 1);
		} else {
			cuenta.setPrecioHuevo(gallina.getDinero() * GANANCIA_HUEVO);
			dineroCuenta = gallina.getDinero();
			cuenta.setHuevosVendidos(cuenta.getHuevosVendidos() + 1);
		}
		if (!(iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta > MAXIMO_EN_CUENTA)) {
			System.out.println(iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta);
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
			iGallinaData.deleteById(gallina.getIdGallina());
		} else {
			throw new SinDineroException("Error! limite de ingresos");
		}
	}

	@Override
	public void eliminarGallina(Gallina gallina) throws GallinaNotFoundException {
		iGallinaData.deleteById(gallina.getIdGallina());
	}

	@Override
	public void comprarGallina(int cant) throws ProductoException, SinDineroException {

		if (this.listarGallinas().size() + cant < LIMITE_CANT_COMPRA) {

			this.crearGallina(cant);

		} else {
			throw new ProductoException("Error! Supera limite de compra");
		}
	}

	@Override
	public void comprarHuevo(int cant) throws ProductoException, SinDineroException {

		if ((this.listarHuevos().size() + cant) < LIMITE_CANT_COMPRA) {

			this.crearHuevo(cant);

		} else {
			throw new ProductoException("Error! Supera limite de compra");
		}
	}

	@Override
	public void venderGallina(int cant) throws GallinaNotFoundException, SinDineroException {

		if (this.listarGallinas().size() <= 0) {
			throw new GallinaNotFoundException("Error! no hay producto para vender");
		} else if (this.listarGallinas().size() - cant >= LIMITE_CANT_VENTA) {

			for (int i = 0; i < cant; i++) {
				Gallina gallina = iStatusService.idGallina(true);
				
				this.eliminarProducto(gallina);
			}
		} else {
			throw new GallinaNotFoundException("Error! no puede vender mas del limite");
		}
	}

	@Override
	public void venderHuevo(int cant) throws GallinaNotFoundException, SinDineroException {

		if (this.listarHuevos().size() <= 0) {
			throw new GallinaNotFoundException("Error! no hay producto para vender");
		} else if (this.listarHuevos().size() - cant >= LIMITE_CANT_VENTA) {

			for (int i = 0; i < cant; i++) {
				Gallina gallina = iStatusService.idGallina(false);
				
				this.eliminarProducto(gallina);
			}
		} else {
			throw new GallinaNotFoundException("Error! no puede vender mas del limite");
		}

	}

	public void registrarCompra(Gallina gallina) {
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		if (!gallina.isHuevo()) {
			cuenta.setGallinasCompra(cuenta.getGallinasCompra() + 1);
		} else {
			cuenta.setHuevosCompra(cuenta.getHuevosCompra() + 1);
		}
		iCuentaData.save(cuenta);
	}

}
