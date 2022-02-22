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
	public void crearGallina(int cant) throws ProductoException, SinDineroException { // funciona
		double dineroCuenta = -100 * cant;
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta <= 1100) {
				throw new SinDineroException("crear Gallina. no se puede gastar mas del limite: "
						+ iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta);
			}
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
			for (int i = 0; i < cant; i++) {

				Gallina g = new Gallina(false);
				g.setCreacion(iStatusService.obtenerDia());
				g.setPasarDia(iStatusService.obtenerDia());
				Gallina gallina = iGallinaData.save(g);
				if (gallina.equals(null)) {
					throw new ProductoException("No se pudo crear Gallina");
				}
			}
		} catch (SinDineroException e) {
			e.printStackTrace();
		} catch (ProductoException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void crearHuevo(int cant) throws ProductoException {
		double dineroCuenta = -50 * cant;
		Cuenta cuenta = iStatusService.plataEnCuenta(1).get();
		try {
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta <= 1100) {
				throw new SinDineroException("crear Gallina. no se puede gastar mas del limite: "
						+ iStatusService.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta);
			}
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
			for (int i = 0; i < cant; i++) {
				Gallina g = new Gallina(true);
				g.setCreacion(iStatusService.obtenerDia());
				g.setPasarDia(iStatusService.obtenerDia());
				Gallina gallina = iGallinaData.save(g);
				if (gallina.equals(null)) {
					throw new ProductoException("No se pudo crear Huevo");
				}
			}
		} catch (SinDineroException e) {
			e.printStackTrace();
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
			iGallinaData.deleteById(gallina.getIdGallina());
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
	}

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
				System.out.println("Alcanzo el limite de compra de gallinas: " + this.listarGallinas().size());
			}
		} else {
			System.out.println("Alcanzo el limite de gasto de compra de gallina: "
					+ iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		}
	}

	@Override
	public void comprarHuevo(int cant) throws ProductoException, SinDineroException {
		int limite = 5;
		if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() >= 1100) {
			if (this.listarHuevos().size() < limite && cant < limite) {
				try {
					this.crearHuevo(cant);
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
	public void venderGallina(int cant) throws GallinaNotFoundException {

		try {
			int limite = 3;
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < 10000) {
				// hay gallinas ver como mostrar la excepcion
				if (this.listarGallinas().size() <= 0) {
					System.out.println("no hay nada para vender");
				} else if (this.listarGallinas().size() > limite) {
					for (int i = 0; i < cant; i++) {
						Gallina gallina = iStatusService.idGallina(true);
						this.eliminarProducto(gallina);
					}

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
	public void venderHuevo(int cant) throws GallinaNotFoundException {
		try {
			int limite = 2;
			if (iStatusService.plataEnCuenta(1).get().getDineroCuenta() < 10000) {
				if (this.listarHuevos().size() <= 0) {
					System.out.println("no hay nada para vender");
				} else if (this.listarHuevos().size() > limite) {
					Gallina gallina = iStatusService.idGallina(false);
					this.eliminarProducto(gallina);
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
	public void inicializarSaldo() {
		Cuenta cuenta = new Cuenta(1, 2200);
		iCuentaData.save(cuenta);

	}

	@Override
	public void eliminarGallina(Gallina gallina) throws GallinaNotFoundException {
		iGallinaData.deleteById(gallina.getIdGallina());

	}
}
