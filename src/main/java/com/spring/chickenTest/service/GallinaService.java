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

	private final double PRECIO_GALLINA = 200;

	private final double PRECIO_HUEVO = 20;

	private final double MINIMO_EN_CUENTA = 1100;  

	private final double MAXIMO_EN_CUENTA = 50000;

	private final int LIMITE_CANT_COMPRA = 15;

	private final int LIMITE_CANT_VENTA = 2;
	
	

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
		this.crearProducto(false, cant);		
	}

	@Override
	public void crearHuevo(int cant) throws ProductoException, SinDineroException {

		iStatusService.actualizarCuenta(PRECIO_GALLINA, PRECIO_HUEVO, false, cant);
		this.crearProducto(true, cant);

	}

	@Override
	public void crearProducto(boolean esHuevo, int cant) throws ProductoException {
		
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
			this.registrarCompra(g);
			if (gallina.equals(null)) {
				throw new ProductoException("No se pudo crear Producto");
			}
		}
	}

	@Override
	public void eliminarProducto(Gallina gallina) {
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		double dineroCuenta = 0;
		if (!gallina.isHuevo()) {
			cuenta.setPrecioGallina(gallina.getDinero() );
			dineroCuenta = gallina.getDinero();
			cuenta.setGallinasVendidas(cuenta.getGallinasVendidas() + 1);
		} else {
			cuenta.setPrecioHuevo(gallina.getDinero());
			dineroCuenta = gallina.getDinero();
			cuenta.setHuevosVendidos(cuenta.getHuevosVendidos() + 1);
		}
		try {
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
			iGallinaData.deleteById(gallina.getIdGallina());
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void eliminarGallina(Gallina gallina) throws GallinaNotFoundException {
		iGallinaData.deleteById(gallina.getIdGallina());
	}
	
	@Override
	public void comprarGallina(int cant) throws ProductoException, SinDineroException {

		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= MINIMO_EN_CUENTA) {
			if (this.listarGallinas().size() < LIMITE_CANT_COMPRA && cant < LIMITE_CANT_COMPRA) {

				this.crearGallina(cant);
			
			}else {
				throw new ProductoException("error cantidad de compra");
			}
		} else {
			throw new SinDineroException("error gasto de compra");
		}		
	}
 
	@Override
	public void comprarHuevo(int cant) throws ProductoException, SinDineroException {

		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= MINIMO_EN_CUENTA) {  		
			if ((this.listarHuevos().size()+ cant) < LIMITE_CANT_COMPRA) {

				this.crearHuevo(cant);

			} else {
				throw new ProductoException("error cantidad de compra");
			}
		} else {
			throw new SinDineroException("error gasto de compra");
		}
	}
	
	@Override
	public void venderGallina(int cant) throws GallinaNotFoundException, SinDineroException {

		try {
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < MAXIMO_EN_CUENTA) {
				if (this.listarGallinas().size() <= 0) {
					throw new GallinaNotFoundException("no hay producto para vender");
				} else if (this.listarGallinas().size() > LIMITE_CANT_VENTA) {
					for (int i = 0; i < cant; i++) {
						Gallina gallina = iStatusService.idGallina(true);
						this.eliminarProducto(gallina);
					}
				} else {
					throw new GallinaNotFoundException("No puede vender mas del limite");
				}
			} else {
				throw new SinDineroException("Alcanzo el limite ingresos de venta");
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void venderHuevo(int cant) throws GallinaNotFoundException, SinDineroException {
	
		try {
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < MAXIMO_EN_CUENTA) {
				if (this.listarHuevos().size() <= 0) {
					throw new GallinaNotFoundException("no hay producto para vender");
				} else if (this.listarHuevos().size() > LIMITE_CANT_VENTA) {
					for (int i = 0; i < cant; i++) {
					Gallina gallina = iStatusService.idGallina(false);
					this.eliminarProducto(gallina);
					}
				} else {
					throw new GallinaNotFoundException("No puede vender mas del limite");
				}
			} else {
				throw new SinDineroException("Alcanzo el limite ingresos de venta");
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
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
