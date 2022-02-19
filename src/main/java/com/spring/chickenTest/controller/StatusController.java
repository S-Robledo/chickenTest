package com.spring.chickenTest.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.modelo.ExceedsLimitException;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;

@Controller
@RequestMapping
public class StatusController {

	@Autowired
	private IGallinaService iGallinaService;
	@Autowired
	private IStatusService iStatusService;

	@GetMapping("/listar")
	public String listar(Model model) {
		// List<Gallina> gallinas = iGallinaService.listarProductos();
		List<Gallina> gallinas = iGallinaService.listarGallinas();
		List<Gallina> huevos = iGallinaService.listarHuevos();

		model.addAttribute("gallinas", gallinas);
		model.addAttribute("huevos", huevos);
		model.addAttribute("fecha", LocalDate.now());
		model.addAttribute("totalGranja", gallinas.size());
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size());
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size());
		model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		model.addAttribute("pasarDeDia", iStatusService.pasarDeDia());

		return "index";
	}
	
	@PostMapping("/comprarGallina")
	public String comprarGallina(@RequestParam(value = "cant", defaultValue = "1") int cant) {
		try {
			iGallinaService.comprarGallina(cant);
		} catch (SinDineroException | ProductoException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/comprarHuevo")
	public String comprarHuevo(@RequestParam(value = "cant", defaultValue = "1") int cant) {
		try {
			iGallinaService.comprarHuevo(cant);
		} catch (SinDineroException | ProductoException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderGallina")
	public String venderGallina(@RequestParam(value = "cant", defaultValue = "1") int cant) {
		try {
			iGallinaService.venderGallina(cant);
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderHuevo")
	public String venderhuevo(@RequestParam(value = "cant", defaultValue = "1") int cant) {
		try {
			iGallinaService.venderHuevo(cant);
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@GetMapping("/pasarDeDia")
	public String pasarDeDia() {
		iStatusService.pasarDeDia();
		return "redirect:/listar";
	}
}
