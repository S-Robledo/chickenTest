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

	@Override
	public List<Gallina> listarProductos() {
		return (List<Gallina>) iGallinaData.findAll();
	}

	@Override
	public List<Gallina> listarGallinas() {
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();
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
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();
		List<Gallina> listaHuevos = new ArrayList<Gallina>();

		for (Gallina gallina : listaCompleta) {
			if (gallina.isHuevo()) {
				listaHuevos.add(gallina);
			}
		}
		return listaHuevos;
	}

	@Override
	public void crearGallina() throws ProductoException {
		double dineroCuenta = -100;
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
		try {
			Gallina g = new Gallina(false);
			Gallina gallina = iGallinaData.save(g);
			if (gallina.equals(null)) {
				throw new ProductoException("No se pudo crear Gallina");
			}
		} catch (ProductoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void crearHuevo() throws ProductoException {
		double dineroCuenta = -50;
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
		try {
			Gallina g = new Gallina(true);
			Gallina gallina = iGallinaData.save(g);
			if (gallina.equals(null)) {
				throw new ProductoException("No se pudo crear Huevo");
			}
		} catch (ProductoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void eliminarProducto(Gallina gallina) {
		double dineroCuenta = 0;
		if (!gallina.isHuevo()) {
			dineroCuenta = 100;
		} else {
			dineroCuenta = 40;
		}
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
		iGallinaData.deleteById(gallina.getIdGallina());
	}

	@Override
	public void comprarGallina() throws SinDineroException {
		int limite = 15;
		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= 2100) {
			if (this.listarGallinas().size() < limite) {
				try {
					this.crearGallina();
				} catch (ProductoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Alcanzo el limite de compra de gallinas: " + this.listarGallinas().size());
			}
		} else {
			System.out.println("Alcanzo el limite de gasto de compra de gallina: "
					+ iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		}
	}

	@Override
	public void comprarHuevo() throws SinDineroException {
		int limite = 5;
		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= 2050) {
			if (this.listarHuevos().size() < limite) {
				try {
					this.crearHuevo();
				} catch (ProductoException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Alcanzo el limite de compra de huevos: " + this.listarHuevos().size());
			}
		} else {
			System.out.println("Alcanzo el limite de gasto de compra de huevos: "
					+ iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		}
	}

	@Override
	public void venderGallina() throws GallinaNotFoundException {
		try {
			int limite = 3;
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < 10000) { // si no
				// hay gallinas ver como mostrar la excepcion
				if (this.listarGallinas().size() <= 0) {
					System.out.println("no hay nada para vender");
				} else if (this.listarGallinas().size() > limite) {
					// int idGallina = iStatusService.idGallina(true);
					Gallina gallina = iStatusService.idGallina(true);
					// this.eliminarProducto(idGallina);
					this.eliminarProducto(gallina);
					// Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
					// cuenta.venderGallina();
					// iStatusService.ActualizarSaldo(cuenta);
				} else {
					System.out.println("ya no puede vender mas del limite");
				}
			} else {
				System.out.println("Se llego al limite de dinero ingresado");
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void venderHuevo() throws GallinaNotFoundException {		
		try {
			int limite = 2;
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < 10000) {
				if (this.listarHuevos().size() <= 0) {
					System.out.println("no hay nada para vender");
				} else if (this.listarHuevos().size() > limite) {
					Gallina gallina = iStatusService.idGallina(false);
					this.eliminarProducto(gallina); // Cuenta
					//cuenta = iStatusService.plataEnCuenta(1).get(); // cuenta.venderHuevo();
					// iStatusService.ActualizarSaldo(cuenta); } else {
					System.out.println("ya no puede vender mas del limite");
				}
			} else {
				System.out.println("Se llego al limite de dinero ingresado");
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		 
	}
}
