package com.spring.chickenTest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.interfaces.IStatus;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.Status;

@Service
public class StatusService implements IStatusService {

	@Autowired
	private IGallina iGallinaData;
	
	@Autowired 
	private IStatus iStatusData;
	
	@Override
	public List<Gallina> listarGallinas(){
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();
		List<Gallina> listaGallinas = new ArrayList<Gallina>();
		for(Gallina gallina : listaCompleta) {			
			if(!gallina.isHuevo()) {
					listaGallinas.add(gallina);
			}							
	}		
		return listaGallinas;
		
	}
	@Override
	public int contarGallinas() {		//
		//int cantidadGallinas = contar(true);		
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();
		int contar =0;
		for(Gallina gallina : listaCompleta) {			
				if(!gallina.isHuevo()) {
					contar++;
				}							
		}		
		return contar;
	}

	@Override
	public int contarHuevos() {		
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();
		int contar =0;
		for(Gallina gallina : listaCompleta) {			
				if(gallina.isHuevo()) {
					contar++;
				}							
		}						
		return contar;
	}
	
	@Override
	public void dineroDisponible() {
		
	}
	
	@Override
	public Optional<Status> findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int idGallina (boolean idGallina) throws GallinaNotFoundException {			
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();		
		for(Gallina gallina : listaCompleta) {			
				if(!gallina.isHuevo()) {					
					return gallina.getIdGallina();
				}
				else if(gallina.isHuevo()) {
					return gallina.getIdGallina();
				}
		}		
		throw new GallinaNotFoundException("No hay animales en la granja");
	}

	@Override
	public void vender(int idGallina) {		 
		iGallinaData.deleteById(idGallina);	 
	}

	
}
