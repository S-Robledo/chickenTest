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
		int contarGallinas = iStatusService.contarGallinas();
		int limite = 5;
		if(contarGallinas < limite) {
			Gallina g = new Gallina(false);
			iGallinaService.save(g);			
		}
		return "redirect:/listar";
	}

	@PostMapping("/comprarHuevo")
	public String comprarHuevo() {
		int contarHuevos = iStatusService.contarHuevos();
		int limite = 7;
		if(contarHuevos < limite) {
			Gallina g = new Gallina(true);
			iGallinaService.save(g);			
		}
		return "redirect:/listar";
	}	
	
	@PostMapping("/venderGallina")
	public String venderGallina() {
		try {
			int contarGallinas = iStatusService.contarGallinas();
			int idGallina = iStatusService.idGallina(true);
			int limite = 2;
			if (contarGallinas > limite) {
				iStatusService.vender(idGallina);
			}
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderHuevo")
	public String venderhuevo() {
		try {
			int contarHuevos = iStatusService.contarHuevos();
			int idGallina = iStatusService.idGallina(false);
			int limite = 1;
			if (contarHuevos > limite) {
				iStatusService.vender(idGallina);
			}
		} catch (GallinaNotFoundException e) {

			e.printStackTrace();
		}
		return "redirect:/listar";
	}

}
