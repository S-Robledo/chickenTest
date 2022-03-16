package com.spring.chickenTest.controller;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
import com.spring.chickenTest.modelo.Cuenta;
import com.spring.chickenTest.modelo.Gallina;
import com.spring.chickenTest.modelo.GallinaNotFoundException;
import com.spring.chickenTest.modelo.ProductoException;
import com.spring.chickenTest.modelo.SinDineroException;
import com.spring.chickenTest.service.GallinaService;

@Controller
@RequestMapping
public class StatusController {

	@Autowired
	private IGallinaService iGallinaService;
	@Autowired
	private IStatusService iStatusService;

	@Autowired
	public GallinaService gallinaService;

	private boolean inicio = true;

	private boolean muestra = false;
	
	private final double CANTIDAD_MIN = 0;
	
	private final double CANTIDAD_MAX = 100;
	
	private final String MSG_WARNING = "Cuidado! Ingresar valor entre '1' y '";

	@GetMapping("/listar")
	public String listar(Model model) {
		if (inicio) {
			iStatusService.inicializarCuenta();
			inicio = false;
		}
		List<Gallina> totalGallinas = iGallinaService.listarProductos();
		List<Gallina> gallinas = iGallinaService.listarGallinas();
		List<Gallina> huevos = iGallinaService.listarHuevos();
		model.addAttribute("gallinas", gallinas);
		model.addAttribute("huevos", huevos);
		model.addAttribute("fecha", LocalDate.now());
		model.addAttribute("totalGranja", iGallinaService.listarProductos().size());
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size());
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size());
		model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());
		model.addAttribute("pasarDeDia", iStatusService.obtenerDia());

		return "index";
	}

	@PostMapping("/comprarGallina")
	public String comprarGallina(@RequestParam(value = "cant", defaultValue = "0") int cant,
			RedirectAttributes attribute) {

		try {
			if (cant > CANTIDAD_MIN && cant < CANTIDAD_MAX) {
				
				iGallinaService.comprarGallina(cant);
				attribute.addFlashAttribute("mensaje", "Compra exitosa!").addFlashAttribute("clase", "success");
			} else {
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX +"'")
						.addFlashAttribute("clase", "warning");
			}
		} catch (SinDineroException | ProductoException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "redirect:/listar";

	}

	@PostMapping("/comprarHuevo")
	public String comprarHuevo(@RequestParam(value = "cant", defaultValue = "0") int cant,
			RedirectAttributes attribute) {

		try {
			if (cant > CANTIDAD_MIN && cant < CANTIDAD_MAX) {
				
				iGallinaService.comprarHuevo(cant);
				attribute.addFlashAttribute("mensaje", "Compra exitosa!").addFlashAttribute("clase", "success");
			} else {
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX +"'")
						.addFlashAttribute("clase", "warning");
			}
		} catch (SinDineroException | ProductoException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "redirect:/listar";
		
	}

	@PostMapping("/venderGallina")
	public String venderGallina(@RequestParam(value = "cant", defaultValue = "0") int cant,
			RedirectAttributes attribute) {
		
		try {
			if (cant > CANTIDAD_MIN && cant < CANTIDAD_MAX) {
				
				iGallinaService.venderGallina(cant);
				attribute.addFlashAttribute("mensaje", "Venta exitosa!").addFlashAttribute("clase", "success");
			} else {
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX +"'")
						.addFlashAttribute("clase", "warning");
			}
		} catch (GallinaNotFoundException | SinDineroException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "redirect:/listar";
		
	}

	@PostMapping("/venderHuevo")
	public String venderhuevo(@RequestParam(value = "cant", defaultValue = "0") int cant,
			RedirectAttributes attribute) {
		try {
			if (cant > CANTIDAD_MIN && cant < CANTIDAD_MAX) {
				
				iGallinaService.venderHuevo(cant);
				attribute.addFlashAttribute("mensaje", "Venta exitosa!").addFlashAttribute("clase", "success");
			} else {
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX +"'")
				.addFlashAttribute("clase", "warning");
			}
		} catch (GallinaNotFoundException | SinDineroException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "redirect:/listar";
	}

	@GetMapping("/pasarDeDia")
	public String pasarDeDia() {
		try {
			iStatusService.pasarAotroDia();
		} catch (ProductoException e) {
			e.printStackTrace();
		}
		return "redirect:/listar";
	}

	@GetMapping("/reporte")
	public String reporte(Model model, RedirectAttributes attribute) {
		try {
			model.addAttribute("totalGranja", iGallinaService.listarProductos().size());
			model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());

			model.addAttribute("costoUnitarioGallina", gallinaService.getPRECIO_GALLINA());
			model.addAttribute("totalCantGallinas", iGallinaService.listarGallinas().size());
			model.addAttribute("costoUnitarioHuevo", gallinaService.getPRECIO_HUEVO());
			model.addAttribute("totalCantHuevos", iGallinaService.listarHuevos().size());

			model.addAttribute("gallinasVendidas", iStatusService.plataEnCuenta(1).get().getGallinasVendidas());
			model.addAttribute("totalVentaGallinas",
					(iStatusService.plataEnCuenta(1).get().getGallinasVendidas() * gallinaService.getPRECIO_GALLINA()));
			model.addAttribute("huevosVendidos", iStatusService.plataEnCuenta(1).get().getHuevosVendidos());
			model.addAttribute("totalVentaHuevos",
					(iStatusService.plataEnCuenta(1).get().getHuevosVendidos() * gallinaService.getPRECIO_HUEVO()));

			model.addAttribute("gallinasCompra", iStatusService.plataEnCuenta(1).get().getGallinasCompra());
			model.addAttribute("huevosCompra", iStatusService.plataEnCuenta(1).get().getHuevosCompra());
			model.addAttribute("totalCompraGallinas",
					(iStatusService.plataEnCuenta(1).get().getGallinasCompra() * gallinaService.getPRECIO_GALLINA()));
			model.addAttribute("totalCompraHuevos",
					(iStatusService.plataEnCuenta(1).get().getHuevosCompra() * gallinaService.getPRECIO_HUEVO()));
			
			//natimiento gallinas muerte gallinas
			model.addAttribute("totalNacimientoGallinas", "24" );
			model.addAttribute("totalMuerteGallinas", "2");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "reporte";
	}
}
