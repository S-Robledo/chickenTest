package com.spring.chickenTest.service;

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
	
	
	public StatusService(IGallina iGallinaData, ICuenta iCuentaData, IGallinaService iGallinaService) {
		super();
		this.iGallinaData = iGallinaData;
		this.iCuentaData = iCuentaData;
		this.iGallinaService = iGallinaService;
	}

	@Override
	public Gallina idGallina(boolean esGallina) throws GallinaNotFoundException { //era int idGallina
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();

		if (esGallina) {
			for (Gallina gallina : listaCompleta) {
				if (!gallina.isHuevo()) {
					//return gallina.getIdGallina();
					return gallina;
				}
			}
		} else if (!esGallina) {
			for (Gallina gallina : listaCompleta) {
				if (gallina.isHuevo()) {
					//return gallina.getIdGallina();
					return gallina;
				}
			}
		}
		throw new GallinaNotFoundException("No hay animales en la granja");
	}

	@Override
	public Optional<Cuenta> plataEnCuenta(int id){
		 return iCuentaData.findById(id);
	}

	@Override
	public int pasarDeDia() {
		// TODO Auto-generated method stub
		int dia = 1;
		return dia;
	}
}
