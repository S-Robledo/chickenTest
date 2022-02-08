package com.spring.chickenTest.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;

@Controller
@RequestMapping
public class StatusController {

	@Autowired
	private IGallinaService iGallinaService;
	@Autowired
	private IStatusService iStatusService;

	@GetMapping("/listar")
	public String listar(Model model) {
		List<Gallina> gallinas = iGallinaService.listarProductos();

		model.addAttribute("gallinas", gallinas);
		model.addAttribute("fecha", LocalDate.now());
		model.addAttribute("totalGranja", gallinas.size());
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size());
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size());

		return "index";
	}

	@PostMapping("/comprarGallina")
	public String comprarGallina() {
		int limite = 3;
		try {
			if (iGallinaService.listarGallinas().size() < limite) {
				Gallina g = new Gallina(false);
				iGallinaService.crearProducto(g);
			}
		} catch (ProductoException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/comprarHuevo")
	public String comprarHuevo() {
		int limite = 5;
		try {
			if (iGallinaService.listarHuevos().size() < limite) {
				Gallina g = new Gallina(true);
				iGallinaService.crearProducto(g);
			}
		} catch (ProductoException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderGallina")
	public String venderGallina() {
		int limite = 2;
		try {
			if (iGallinaService.listarGallinas().size() > limite) {
				iGallinaService.eliminarProducto(iStatusService.idGallina(true));
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderHuevo")
	public String venderhuevo() {
		int limite = 1;
		try {
			if (iGallinaService.listarHuevos().size() > limite) {
				iGallinaService.eliminarProducto(iStatusService.idGallina(false));
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}
}
