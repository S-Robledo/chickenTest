package com.spring.chickenTest.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
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

	private boolean inicio = true;

	@GetMapping("/listar")
	public String listar(Model model) {
		if (inicio) {
			iStatusService.inicializarCuenta(); //revisado
			inicio = false;
		}
		List<Gallina> totalGallinas = iGallinaService.listarProductos(); //revisado
		List<Gallina> gallinas = iGallinaService.listarGallinas(); //revisado
		List<Gallina> huevos = iGallinaService.listarHuevos();  //revisado

		model.addAttribute("gallinas", gallinas);
		model.addAttribute("huevos", huevos);
		model.addAttribute("fecha", LocalDate.now());
		model.addAttribute("totalGranja", iGallinaService.listarProductos().size()); //ok
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size()); //ok
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size()); //ok
		model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta()); // revisado
		model.addAttribute("pasarDeDia", iStatusService.obtenerDia()); //ok

		return "index";
	}
	//revisado
	@PostMapping("/comprarGallina")
	public String comprarGallina(@RequestParam(value = "cant", defaultValue = "1") int cant) {
		
		try {
			iGallinaService.comprarGallina(cant);
		} catch (SinDineroException | ProductoException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}
	//Revisado
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
		} catch (GallinaNotFoundException | SinDineroException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@PostMapping("/venderHuevo")
	public String venderhuevo(@RequestParam(value = "cant", defaultValue = "1") int cant) {
		try {
			iGallinaService.venderHuevo(cant);
		} catch (GallinaNotFoundException | SinDineroException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@GetMapping("/pasarDeDia")
	public String pasarDeDia() {
		iStatusService.pasarAotroDia();
		//iStatusService.pasarDeDia();
		return "redirect:/listar";
	}

	@GetMapping("/reporte")
	public String reporte(Model model) {
//		List<Gallina> totalProductos = iGallinaService.listarProductos();
//		model.addAttribute("totalProductos", totalProductos);
		// model.addAttribute("totalProductos",
		// iGallinaService.listarProductos().size());

		model.addAttribute("totalGranja", iGallinaService.listarProductos().size());
		model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size());
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size());
		// obtener de cuenta datos
		model.addAttribute("gallinasVendidas", iStatusService.plataEnCuenta(1).get().getGallinasVendidas());
		model.addAttribute("totalGastadoGallinas", (iStatusService.plataEnCuenta(1).get().getGallinasVendidas() * 500));

		model.addAttribute("huevosVendidos", iStatusService.plataEnCuenta(1).get().getHuevosVendidos());
		model.addAttribute("totalGastadoHuevos", (iStatusService.plataEnCuenta(1).get().getHuevosVendidos() * 15));

		model.addAttribute("costoUnitarioGallina", iStatusService.plataEnCuenta(1).get().getPrecioGallina());
		return "reporte";
	}
}
