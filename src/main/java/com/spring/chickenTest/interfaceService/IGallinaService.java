package com.spring.chickenTest.interfaceService;

import java.util.List;

import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.ProductoException;

public interface IGallinaService {

	public List<Gallina> listarProductos();
	public List<Gallina> listarGallinas();
	public List<Gallina> listarHuevos();
 	public void crearProducto(Gallina g) throws ProductoException;
 	public void eliminarProducto(int idGallina);
}
