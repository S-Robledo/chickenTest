package com.spring.chickenTest.interfaceService;

import java.util.Optional;

import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;

public interface IStatusService {	
	//vender gallina
	public Gallina idGallina (boolean esGallina) throws GallinaNotFoundException;//era int idGallina
	public Optional<Cuenta> plataEnCuenta(int id);
	public int pasarDeDia();
}
