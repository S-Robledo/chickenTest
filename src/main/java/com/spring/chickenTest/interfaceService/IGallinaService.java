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
 	public void crearGallina() throws ProductoException;
 	public void crearHuevo() throws ProductoException;
 	
 	public void eliminarProducto(Gallina gallina); 	
 	public void comprarGallina() throws SinDineroException; 	
 	public void comprarHuevo() throws SinDineroException;
 	public void venderGallina() throws GallinaNotFoundException;
 	public void venderHuevo() throws GallinaNotFoundException;
 	//pasar huevo a gallina 	
}
