package com.spring.chickenTest.interfaceService;

import com.spring.chickenTest.modelo.GallinaNotFoundException;

public interface IStatusService {	
	//vender gallina
	public int idGallina (boolean esGallina) throws GallinaNotFoundException;		
	public void dineroDisponible();
}
