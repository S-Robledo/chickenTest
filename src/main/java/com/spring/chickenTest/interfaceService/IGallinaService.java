package com.spring.chickenTest.interfaceService;

import java.util.List;

import com.spring.chickenTest.modelo.Gallina;

public interface IGallinaService {

	public List<Gallina> listarProductos();
	public List<Gallina> listarGallinas();
	public List<Gallina> listarHuevos();
 	public int save(Gallina g);
	
	
}
