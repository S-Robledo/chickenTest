package com.spring.chickenTest.interfaceService;

import java.util.Optional;

import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.GallinaNotFoundException;

public interface IStatusService {	
	//vender gallina
	public int idGallina (boolean esGallina) throws GallinaNotFoundException;		
	//public void dineroDisponible();
	public Optional<Cuenta> plataEnCuenta(int id);
	public void ActualizarSaldo(Cuenta cuenta);
}
