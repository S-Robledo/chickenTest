package com.spring.chickenTest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaces.IGallina;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.ProductoException;

@Service
public class GallinaService implements IGallinaService {

	@Autowired
	private IGallina iGallinaData;
		
	@Override public List<Gallina> listarProductos() {
	    return (List<Gallina>) iGallinaData.findAll(); 
	}  

	@Override
	public List<Gallina> listarGallinas() {
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
	public List<Gallina> listarHuevos() {		
		List<Gallina> listaCompleta = (List<Gallina>) iGallinaData.findAll();
		List<Gallina> listaHuevos = new ArrayList<Gallina>();
		
		for(Gallina gallina : listaCompleta) {
			if(gallina.isHuevo()) {
				listaHuevos.add(gallina);
			}
		}	
		return listaHuevos;
	}
	
	@Override
	public int crearProducto(Gallina g) throws ProductoException  {
		int res=0;
		Gallina gallina = iGallinaData.save(g);
		if(!gallina.equals(null)) {
			throw new ProductoException("No se pudo crear el Producto");
		}
		return res;
	}

	@Override
	public void eliminarProducto(int idGallina) {		
		iGallinaData.deleteById(idGallina);
		}
}
