package com.spring.chickenTest.interfaceService;

import java.util.Date;
import java.util.Optional;

import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.SinDineroException;

public interface IStatusService {	
	public Gallina idGallina (boolean esGallina) throws GallinaNotFoundException;//era int idGallina
	public Optional<Cuenta> plataEnCuenta(int id);
	
	//public void pasarDeDia();
	//public Date obtenerDia();
	public int obtenerDia();
	//agregado 24-02
	
	public void pasarAotroDia();
	public void muerteGallina(Gallina gallina);
	public void convertirHuevo(Gallina gallina, int dia);
	
	public void inicializarCuenta();
	
	public void actualizarCuenta(boolean esGallina, int cant) throws SinDineroException;
	
}
