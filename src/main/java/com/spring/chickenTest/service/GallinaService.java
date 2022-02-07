package com.spring.chickenTest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.interfaces.IStatus;
import com.spring.chickenTest.modelo.Gallina;

@Service
public class GallinaService implements IGallinaService {

	@Autowired
	private IGallina iGallinadata;
	
	@Autowired
	private IStatus iStatusData;

	
	@Override public List<Gallina> listarProductos() {
	    return (List<Gallina>) iGallinadata.findAll(); 
	}  
	  
	@Override
	public List<Gallina> listarHuevos() {		
		List<Gallina> listaCompleta = (List<Gallina>) iGallinadata.findAll();
		List<Gallina> listaHuevos = new ArrayList<Gallina>();
		
		for(Gallina gallina : listaCompleta) {
			if(gallina.isHuevo()) {
				listaHuevos.add(gallina);
			}
		}	
		return listaHuevos;
	}
	
	@Override
	public int save(Gallina g) {
		int res=0;
		Gallina gallina = iGallinadata.save(g);
		if(!gallina.equals(null)) {
			res=1;
		}
		return res;
	}

	@Override
	public List<Gallina> listarGallinas() {
		// TODO Auto-generated method stub
		return null;
	}

	
 
}
