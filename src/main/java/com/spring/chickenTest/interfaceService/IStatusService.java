package com.spring.chickenTest.interfaceService;

import java.util.Date;
import java.util.Optional;

import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;

public interface IStatusService {	
	
	public void inicializarCuenta();	
	public int obtenerDia();	
	public void pasarAotroDia() throws ProductoException;
	public Gallina idGallina (boolean esGallina) throws GallinaNotFoundException;
	public boolean convertirHuevo(Gallina gallina);	
	public boolean muerteGallina(Gallina gallina);
	public Optional<Cuenta> plataEnCuenta(int id);	 
	public void actualizarCuenta(double PRECIO_GALLINA, double PRECIO_HUEVO, boolean esGallina, int cant) throws SinDineroException;
	public Date obtenerFecha();	
}
