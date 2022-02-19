package com.spring.chickenTest.interfaceService;

import java.util.List;

import com.spring.chickenTest.modelo.ExceedsLimitException;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;

public interface IGallinaService {

	public List<Gallina> listarProductos();
	public List<Gallina> listarGallinas();
	public List<Gallina> listarHuevos();
 	
 	//cambiar crearProducto por crearGallina y crearHuevo  
	//public void crearProducto(Gallina g) throws ProductoException;
 	public void crearGallina(int cant) throws ProductoException, SinDineroException;
 	public void crearHuevo(int cant) throws ProductoException;
 	
 	public void eliminarProducto(Gallina gallina); 	
 	public void comprarGallina(int cant) throws SinDineroException, ProductoException; 	
 	public void comprarHuevo(int cant) throws SinDineroException, ProductoException;
 	public void venderGallina(int cant) throws GallinaNotFoundException;
 	public void venderHuevo(int cant) throws GallinaNotFoundException;
 	//pasar huevo a gallina 	
}
