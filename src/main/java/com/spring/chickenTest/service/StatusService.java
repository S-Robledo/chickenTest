package com.spring.chickenTest.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.PrimaryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.interfaces.ICuenta;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.interfaces.IStatus;
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

	public StatusService(IGallina iGallinaData, ICuenta iCuentaData, IGallinaService iGallinaService) {
		super();
		this.iGallinaData = iGallinaData;
		this.iCuentaData = iCuentaData;
		this.iGallinaService = iGallinaService;
	}
	//Revisado
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

	// revisado
	@Override
	public Optional<Cuenta> plataEnCuenta(int id) {
		return iCuentaData.findById(id);
	}

	/*
	 * Metodo que suma un dia de vida a cada objeto. y pasa huevos a gallinas y
	 * elimina gallinas - si al llegar al dia 5 de vida y si es huevo, se cambia su
	 * estado isHuevo a false pasando a ser gallina, se pasa el dia actual como
	 * creacion y se cambia el valor.
	 * 
	 * - si al llegar al dia 10 de vida, y si no es huevo,
	 * 
	 */
	//Revisado
	@Override
	public void pasarAotroDia() { // antes era void se cambio para mostrar dia por pantalla
		diaAgregado++;
		Date fecha = new Date();
		Date mañana = new Date(fecha.getTime() + (1000 * 60 * 60 * 24));

		List<Gallina> listaCompleta = iGallinaService.listarProductos();
		int dia = 0;
		for (Gallina gallina : listaCompleta) {
			dia = gallina.getPasarDia() + 1;
			gallina.setPasarDia(dia);
			this.convertirHuevo(gallina, dia);
			iGallinaData.save(gallina);
			this.muerteGallina(gallina);
		}
	}
	//Revisado
	@Override
	public void convertirHuevo(Gallina gallina, int dia) {

		if ((gallina.getPasarDia() - gallina.getCreacion()) >= 5) {
			if (gallina.isHuevo()) {
				gallina.setHuevo(false);
				gallina.setCreacion(dia);
				gallina.setDinero(500);
				// gallina.setFechaCreacion(mañana);
			}
		}
	}
	//Revisado
	@Override
	public void muerteGallina(Gallina gallina) {
		if ((gallina.getPasarDia() - gallina.getCreacion()) >= 10) {
			if (!gallina.isHuevo()) {
				try {
					iGallinaService.eliminarGallina(gallina);
				} catch (GallinaNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	@Override
//	public void pasarDeDia() {
//		List<Gallina> listaCompleta = iGallinaService.listarProductos();
//		int dia = 0;
//		Date fecha = new Date();
//		Date mañana = new Date(fecha.getTime() + (1000 * 60 * 60 * 24));
//		for (Gallina gallina : listaCompleta) {
//			dia = gallina.getPasarDia() + 1;
//			fecha = gallina.getFechaCreacion();
//			gallina.setPasarDeDia(mañana);
//			gallina.setPasarDia(dia);
//			if ((gallina.getPasarDia() - gallina.getCreacion()) >= 5) {
//				if (gallina.isHuevo()) {
//					gallina.setHuevo(false);
//					gallina.setCreacion(dia);
//					gallina.setDinero(500);
//					gallina.setFechaCreacion(mañana);
//				}
//			}
//			iGallinaData.save(gallina);
//			if ((gallina.getPasarDia() - gallina.getCreacion()) >= 10) {
//				if (!gallina.isHuevo()) {
//					try {
//						iGallinaService.eliminarGallina(gallina);
//					} catch (GallinaNotFoundException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
	//Revisado
	@Override
	public int obtenerDia() {
		return diaAgregado;
	}

	// revisado
	@Override
	public void inicializarCuenta() {
		Cuenta cuenta = new Cuenta(1, 20000);
		iCuentaData.save(cuenta);
	}

	//revisado
	@Override
	public void actualizarCuenta(boolean esGallina, int cant) throws SinDineroException {
		double dineroCuenta =0;
		if(esGallina) {
			dineroCuenta = -500 * cant;			
		} else {
			dineroCuenta = -20 * cant;
		}
		Cuenta cuenta = this.plataEnCuenta(1).get();		
		if (this.plataEnCuenta(1).get().getDineroCuenta() + dineroCuenta <= 1100) {
			throw new SinDineroException("crear Gallina. no se puede gastar mas del limite: ");
		}
		cuenta.setDineroCuenta(dineroCuenta);
		iCuentaData.save(cuenta);		
	}	
	
	
}
