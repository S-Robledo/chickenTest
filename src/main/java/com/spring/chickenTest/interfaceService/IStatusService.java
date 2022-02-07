package com.spring.chickenTest.interfaceService;

import java.util.List;
import java.util.Optional;

import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.Status;

public interface IStatusService {

	public int contarGallinas();
	public int contarHuevos();
	public void dineroDisponible();
	public Optional<Status> findById(int id);
	
	//vender gallina
	public int idGallina (boolean esGallina) throws GallinaNotFoundException;
	public void vender(int idGallina);
	//listados
	
	

}
