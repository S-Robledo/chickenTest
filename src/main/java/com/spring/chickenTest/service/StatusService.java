package com.spring.chickenTest.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.interfaces.ICuenta;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;

@Service
public class StatusService implements IStatusService {

	@Autowired
	private IGallina iGallinaData;

	@Autowired
	private ICuenta iCuentaData;

	@Autowired
	private IGallinaService iGallinaService;

	private int diaAgregado = 0;

	private int acumulador = 0;

	private final double MINIMO_EN_CUENTA = 900;

	private final double INICIALIZAR_SALDO = 6000;

	private final int MUERTE_GALLINA = 20;

	private final int CONVERTIR_HUEVO = 10;
	
	private final boolean MUERTE_BOOLEAN = true;
	
	private final boolean NACIMIENTO_BOOLEAN = false;
	

	public StatusService(IGallina iGallinaData, ICuenta iCuentaData, IGallinaService iGallinaService) {
		super();
		this.iGallinaData = iGallinaData;
		this.iCuentaData = iCuentaData;
		this.iGallinaService = iGallinaService;
	}

	@Override
	public void inicializarCuenta() {
		Cuenta cuenta = new Cuenta(1, INICIALIZAR_SALDO, 0, 0, 0, 0, 0, 0);
		iCuentaData.save(cuenta);
	}

	@Override
	public int obtenerDia() {
		return diaAgregado;
	}

	@Override
	public void pasarAotroDia() throws ProductoException {
		diaAgregado++;
		acumulador++;

		Date fecha = new Date();
		Date mañana = new Date(fecha.getTime() + (1000 * 60 * 60 * 24));

		List<Gallina> listaCompleta = iGallinaService.listarProductos();
		int dia = 0;
		for (Gallina gallina : listaCompleta) {
			dia = gallina.getPasarDia() + 1;
			gallina.setPasarDia(dia);
			if (this.convertirHuevo(gallina, dia)) {
				actualizarGallina(NACIMIENTO_BOOLEAN);
			}

			iGallinaData.save(gallina);

			if (this.muerteGallina(gallina)) {
				actualizarGallina(MUERTE_BOOLEAN);
			}

			this.crearHuevos();
		}
	}
//

	public void actualizarGallina(boolean muerte) {
		Cuenta cuenta = this.plataEnCuenta(1).get();
		if (muerte) {
			cuenta.setGallinaMuerte(cuenta.getGallinaMuerte() + 1);

		} else {
			cuenta.setGallinaNacimiento(cuenta.getGallinaNacimiento() + 1);
		}
		iCuentaData.save(cuenta);
	}

//
	private void crearHuevos() throws ProductoException {
		List<Gallina> listaCompleta = iGallinaService.listarProductos();
		if (acumulador == 4) {
			for (Gallina gallina : listaCompleta) {

				if (!gallina.isHuevo()) {
					iGallinaService.crearProducto(true, 2, false);
				}
			}
			acumulador = 0;
		}
	}

	@Override
	public Gallina idGallina(boolean esGallina) throws GallinaNotFoundException {

		List<Gallina> listaCompleta = iGallinaService.listarProductos();

		if (esGallina) {
			for (Gallina gallina : listaCompleta) {
				if (!gallina.isHuevo()) {
					return gallina;
				}
			}
		} else if (!esGallina) {
			for (Gallina gallina : listaCompleta) {
				if (gallina.isHuevo()) {
					return gallina;
				}
			}
		}
		throw new GallinaNotFoundException("No hay animales en la granja");
	}

	@Override
	public boolean convertirHuevo(Gallina gallina, int dia) {

		if ((gallina.getPasarDia() - gallina.getCreacion()) >= CONVERTIR_HUEVO) {
			if (gallina.isHuevo()) {
				gallina.setHuevo(false);
				gallina.setCreacion(dia);
				gallina.setDinero(500);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean muerteGallina(Gallina gallina) {
		if ((gallina.getPasarDia() - gallina.getCreacion()) >= MUERTE_GALLINA) {
			if (!gallina.isHuevo()) {
				try {
					iGallinaService.eliminarGallina(gallina);
					return true;
				} catch (GallinaNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public Optional<Cuenta> plataEnCuenta(int id) {
		return iCuentaData.findById(id);
	}

	@Override
	public void actualizarCuenta(double PRECIO_GALLINA, double PRECIO_HUEVO, boolean esGallina, int cant)
			throws SinDineroException {
		double dineroCuenta = 0;
		if (esGallina) {
			dineroCuenta = -(PRECIO_GALLINA) * cant;
		} else {
			dineroCuenta = -(PRECIO_HUEVO) * cant;
		}
		Cuenta cuenta = this.plataEnCuenta(1).get();
		System.out.println(this.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta);// sacar
		if (!(this.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta < MINIMO_EN_CUENTA)) {
			cuenta.setPrecioGallina(PRECIO_GALLINA);
			cuenta.setPrecioHuevo(PRECIO_HUEVO);
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
		} else {
			throw new SinDineroException("Error! No se puede gastar mas del limite");
		}
	}

}
