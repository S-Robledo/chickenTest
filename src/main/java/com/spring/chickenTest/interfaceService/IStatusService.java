package com.spring.chickenTest.interfaceService;

import java.util.Date;
import java.util.Optional;

import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;

public interface IStatusService {	
	public Gallina idGallina (boolean esGallina) throws GallinaNotFoundException;//era int idGallina
	public Optional<Cuenta> plataEnCuenta(int id);
	
	public void pasarDeDia();
	//public Date obtenerDia();
	public int obtenerDia();
}
