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

	// revisado
	@Override
	public List<Gallina> listarProductos() {
		return (List<Gallina>) iGallinaData.findAll();
	}

	// revisado, se cambio (List<Gallina>) iGallinaData.findAll(); por el nombre de
	// la funcion
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

	// se cambio (List<Gallina>) iGallinaData.findAll(); por el nombre de la funcion
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
	//Revisado
	@Override
	public void crearProducto(boolean esHuevo, int cant) throws ProductoException {
		int dinero = 0;
		if(esHuevo) {
			dinero = 20;
		} else {
			dinero = 500;
		}
		for (int i = 0; i < cant; i++) {

			Gallina g = new Gallina(esHuevo);
			g.setCreacion(iStatusService.obtenerDia());
			g.setPasarDia(iStatusService.obtenerDia());
			g.setDinero(dinero);
			Gallina gallina = iGallinaData.save(g);
			
			if (gallina.equals(null)) {
				throw new ProductoException("No se pudo crear Gallina");
			}
		}		
	}
	
	//Revisado
	@Override
	public void crearGallina(int cant) throws ProductoException, SinDineroException { // funciona
//		double dineroCuenta = -100 * cant;
//		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
//			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta <= 1100) {
//				throw new SinDineroException("crear Gallina. no se puede gastar mas del limite: ");
//			}
//			cuenta.setDineroCuenta(dineroCuenta);
//			iCuentaData.save(cuenta);
			
			iStatusService.actualizarCuenta(true, cant); //revisado
			this.crearProducto(false, cant);
//			for (int i = 0; i < cant; i++) {
//
//				Gallina g = new Gallina(false);
//				g.setCreacion(iStatusService.obtenerDia());
//				g.setPasarDia(iStatusService.obtenerDia());
//				g.setDinero(500);
//				Gallina gallina = iGallinaData.save(g);
//				if (gallina.equals(null)) {
//					throw new ProductoException("No se pudo crear Gallina");
//				}
//			}		
		} catch (ProductoException | SinDineroException e) {
			e.printStackTrace();
		}
	}
	//Revisado
	@Override
	public void crearHuevo(int cant) throws ProductoException {
//		double dineroCuenta = -50 * cant;
//		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
//			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta <= 1100) {
//				throw new SinDineroException("crear Gallina. no se puede gastar mas del limite: "
//						+ iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta);
//			}
//			cuenta.setDineroCuenta(dineroCuenta);
//			iCuentaData.save(cuenta);
			iStatusService.actualizarCuenta(false, cant);
			this.crearProducto(true, cant);
//			for (int i = 0; i < cant; i++) {
//				Gallina g = new Gallina(true);
//				g.setCreacion(iStatusService.obtenerDia());
//				g.setPasarDia(iStatusService.obtenerDia());
//				g.setDinero(20);
//				Gallina gallina = iGallinaData.save(g);
//				if (gallina.equals(null)) {
//					throw new ProductoException("No se pudo crear Huevo");
//				}
//			}
		} catch (SinDineroException e) {
			e.printStackTrace();
		} catch (ProductoException e) {
			e.printStackTrace();
		}
	}
	//Revisado ver como simplificar
	@Override
	public void eliminarProducto(Gallina gallina) {
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		double dineroCuenta = 0;
		if (!gallina.isHuevo()) {
			cuenta.setPrecioGallina(gallina.getDinero());
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
	//revisado
	@Override
	public void comprarGallina(int cant) throws ProductoException, SinDineroException {
		int limite = 15;
		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= 1100) {
			if (this.listarGallinas().size() < limite && cant < limite) {
				try {
					this.crearGallina(cant);
				} catch (SinDineroException | ProductoException e) {
					e.printStackTrace();
				}
			} else {
				throw new ProductoException("Se llego al limite de cantidad de compra");
			}
		} else {
			throw new SinDineroException("Alcanzo el limite de gasto de compra");
		}
	}
	//Reviado
	@Override
	public void comprarHuevo(int cant) throws ProductoException, SinDineroException {
		int limite = 15;
		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= 1100) {
			if (this.listarHuevos().size() < limite && cant < limite) {
				try {
					this.crearHuevo(cant);
				} catch (ProductoException e) {
					e.printStackTrace();
				}
			} else {
				throw new ProductoException("Se llego al limite de cantidad de compra");
			}
		} else {
			throw new SinDineroException("Alcanzo el limite de gasto de compra");
		}
	}
	//Revisado
	@Override
	public void venderGallina(int cant) throws GallinaNotFoundException, SinDineroException {

		try {
			int limite = 3;
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < 50000) {
				// hay gallinas ver como mostrar la excepcion
				if (this.listarGallinas().size() <= 0) {
					throw new GallinaNotFoundException("no hay producto para vender");
				} else if (this.listarGallinas().size() > limite) {
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
	//Revisado
	@Override
	public void venderHuevo(int cant) throws GallinaNotFoundException, SinDineroException {
		try {
			int limite = 2;
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < 50000) {
				if (this.listarHuevos().size() <= 0) {
					throw new GallinaNotFoundException("no hay producto para vender");
				} else if (this.listarHuevos().size() > limite) {
					Gallina gallina = iStatusService.idGallina(false);
					this.eliminarProducto(gallina);
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
	//Revisado, lo usa Status service para matar gallina
	@Override
	public void eliminarGallina(Gallina gallina) throws GallinaNotFoundException {
		iGallinaData.deleteById(gallina.getIdGallina());
	}

}
