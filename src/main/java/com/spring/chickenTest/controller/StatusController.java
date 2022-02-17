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
import com.spring.chickenTest.modelo.ExceedsLimitException;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
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
		List<Gallina> gallinas = iGallinaService.listarProductos();

		model.addAttribute("gallinas", gallinas);
		model.addAttribute("fecha", LocalDate.now());
		model.addAttribute("totalGranja", gallinas.size());
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size());
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size());
		model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		model.addAttribute("pasarDeDia", iStatusService.pasarDeDia());

		return "index";
	}

	@PostMapping("/comprarGallina")
	public String comprarGallina() {
		try {
			iGallinaService.comprarGallina();
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/comprarHuevo")
	public String comprarHuevo() {
		try {
			iGallinaService.comprarHuevo();
		} catch (SinDineroException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderGallina")
	public String venderGallina() {
		try {
			iGallinaService.venderGallina();
		} catch (GallinaNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderHuevo")
	public String venderhuevo() {
		try {
			iGallinaService.venderHuevo();
		} catch (GallinaNotFoundException e) {
			// TODO Auto-generated catch block
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
