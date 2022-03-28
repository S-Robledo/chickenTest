package com.spring.chickenTest.service;

import java.util.Calendar;
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

	Calendar calendar = Calendar.getInstance();

	private final double MINIMO_EN_CUENTA = 900;

	private final double INICIALIZAR_SALDO = 30000;

	private final int MUERTE_GALLINA = 30;

	private final int CONVERTIR_HUEVO_EDAD = 21;

	private final int CREAR_HUEVO = 2;

	private final boolean MUERTE_BOOLEAN_PASAR_DIA = true;

	private final boolean NACIMIENTO_BOOLEAN_PASAR_DIA = false;

	private final double PRECIO_GALLINA = 500;

	private final double PRECIO_HUEVO = 20;

	private final double GANANCIA_GALLINA = 1.30;

	private final double GANANCIA_HUEVO = 1.30;

	public StatusService(IGallina iGallinaData, ICuenta iCuentaData, IGallinaService iGallinaService) {
		super();
		this.iGallinaData = iGallinaData;
		this.iCuentaData = iCuentaData;
		this.iGallinaService = iGallinaService;
	}

	@Override
	public void inicializarCuenta() {
		Cuenta cuenta = new Cuenta(1, INICIALIZAR_SALDO, 0, 0, 0, 0, (PRECIO_GALLINA), (PRECIO_HUEVO),
				(PRECIO_GALLINA * GANANCIA_GALLINA), (PRECIO_HUEVO * GANANCIA_HUEVO));
		iCuentaData.save(cuenta);
	}

	@Override
	public Date obtenerFecha() {
		return calendar.getTime();
	}

	@Override
	public void pasarAotroDia() throws ProductoException {

		List<Gallina> listaCompleta = iGallinaService.listarProductos();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.getTime();
		for (Gallina gallina : listaCompleta) {
			gallina.setFechaPasarDeDia(calendar.getTime());
			if (convertirHuevo(gallina)) {
				actualizarGallina(NACIMIENTO_BOOLEAN_PASAR_DIA);
			}
			iGallinaData.save(gallina);
			if (muerteGallina(gallina)) {
				actualizarGallina(MUERTE_BOOLEAN_PASAR_DIA);
			}
			crearHuevos(gallina);
		}
	}

	@Override
	public boolean convertirHuevo(Gallina gallina) {

		if ((gallina.edadGallina() % CONVERTIR_HUEVO_EDAD) == 0 && gallina.isHuevo()) {
			if (gallina.isHuevo()) {
				gallina.setHuevo(false);
				gallina.setFechaCreacion(calendar.getTime());
				iGallinaData.save(gallina);
				return true;
			}
		}
		return false;
	}

	public void actualizarGallina(boolean muerte) {
		Cuenta cuenta = plataEnCuenta(1).get();
		if (muerte) {
			cuenta.setGallinaMuerte(cuenta.getGallinaMuerte() + 1);

		} else {
			cuenta.setGallinaNacimiento(cuenta.getGallinaNacimiento() + 1);
		}
		iCuentaData.save(cuenta);
	}

	@Override
	public boolean muerteGallina(Gallina gallina) {

		if (gallina.edadGallina() >= MUERTE_GALLINA) {
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

	private void crearHuevos(Gallina gallina) throws ProductoException {

		if (gallina.edadGallina() > 4 && (gallina.edadGallina() % CREAR_HUEVO == 0) && !gallina.isHuevo()) {
			if (gallina.edadGallina() != 0) {
				iGallinaService.crearProducto(true, 1, false);
			}
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
	public Optional<Cuenta> plataEnCuenta(int id) {
		return iCuentaData.findById(id);
	}

	@Override
	public void actualizarCuenta(boolean esGallina, int cant)
			throws SinDineroException {
		Cuenta cuenta = plataEnCuenta(1).get();
		double dineroCuenta = 0;
		if (esGallina) {
			dineroCuenta = -(cuenta.getPrecioGallinaCompra() * cant);
		} else {
			dineroCuenta = -(cuenta.getPrecioHuevoCompra() * cant);
		}
		if (!(plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta < MINIMO_EN_CUENTA)) {
			cuenta.setDineroCuenta(dineroCuenta);
			iCuentaData.save(cuenta);
		} else {
			throw new SinDineroException("Error! No se puede gastar mas del limite");
		}
	}
}
