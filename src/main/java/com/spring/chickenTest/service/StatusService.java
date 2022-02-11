package com.spring.chickenTest.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.PrimaryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.interfaces.ICuenta;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.interfaces.IStatus;
import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;

@Service
public class StatusService implements IStatusService {

	@Autowired
	private IGallina iGallinaData;

	@Autowired
	private IStatus iStatusData;
	
	@Autowired
	private ICuenta iCuentaData;

	@Override
	public int idGallina(boolean esGallina) throws GallinaNotFoundException {
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();

		if (esGallina) {
			for (Gallina gallina : listaCompleta) {
				if (!gallina.isHuevo()) {
					return gallina.getIdGallina();
				}
			}
		} else if (!esGallina) {
			for (Gallina gallina : listaCompleta) {
				if (gallina.isHuevo()) {
					return gallina.getIdGallina();
				}
			}
		}
		throw new GallinaNotFoundException("No hay animales en la granja");
	}

	/*
	 * @Override public int idGallina(boolean esGallina) { List<Gallina>
	 * listaCompleta = (List<Gallina>) iGallinaData.findAll(); for (Gallina gallina
	 * : listaCompleta) { if (esGallina && !gallina.isHuevo()) { return
	 * gallina.getIdGallina(); } else if (!esGallina && gallina.isHuevo()) { return
	 * gallina.getIdGallina(); } } return null; }
	 */

	/*
	 * @Override public void dineroDisponible() { iCuenta.findById(1);
	 * //iStatusData.findById(1); }
	 */
	@Override
	public Optional<Cuenta> plataEnCuenta(int id){
		 return iCuentaData.findById(id);
	}

	@Override
	public void ActualizarSaldo(Cuenta cuenta) {
		iCuentaData.save(cuenta);
		//iCuenta.save(null)
		
	}

	@Override
	public int pasarDeDia() {
		// TODO Auto-generated method stub
		int dia = 1;
		return dia;
	}
}
