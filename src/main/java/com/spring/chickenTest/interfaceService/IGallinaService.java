package com.spring.chickenTest.interfaceService;

import java.util.List;

import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;

public interface IGallinaService {

	public List<Gallina> listarProductos();
	public List<Gallina> listarGallinas();
	public List<Gallina> listarHuevos(); 	
 	public void crearGallina(int cant) throws ProductoException, SinDineroException;
 	public void crearHuevo(int cant) throws ProductoException, SinDineroException; 		 	
 	public void crearProducto(boolean esGallina, int cant, boolean esCompra) throws ProductoException; 	
 	public void eliminarProducto(Gallina gallina) throws SinDineroException; 	
 	public void eliminarGallina(Gallina gallina) throws GallinaNotFoundException; 	
 	public void comprarGallina(int cant) throws SinDineroException, ProductoException; 	
 	public void comprarHuevo(int cant) throws SinDineroException, ProductoException;
 	public void venderGallina(int cant) throws GallinaNotFoundException, SinDineroException;
 	public void venderHuevo(int cant) throws GallinaNotFoundException, SinDineroException; 	
}
