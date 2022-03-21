package com.spring.chickenTest.controller;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.chickenTest.interfaceService.IGallinaService;
import com.spring.chickenTest.interfaceService.IStatusService;
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

	private final double CANTIDAD_MIN = 0;

	private final double CANTIDAD_MAX = 100;

	private final String MSG_WARNING = "Cuidado! Ingresar valor entre '1' y '";

	SimpleDateFormat fe = new SimpleDateFormat("YYYY-MM-dd");

	@GetMapping("/listar")
	public String listar(Model model) {
		if (inicio) {
			iStatusService.inicializarCuenta();
			inicio = false;
		}
		model.addAttribute("fechaFormat", fe.format(iStatusService.obtenerFecha()));
		model.addAttribute("pasarDeDia", iStatusService.obtenerDia());
		model.addAttribute("totalGranja", iGallinaService.listarProductos().size());
		model.addAttribute("totalGallinas", iGallinaService.listarGallinas().size());
		model.addAttribute("totalHuevos", iGallinaService.listarHuevos().size());
		model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());

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
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX + "'").addFlashAttribute("clase",
						"warning");
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
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX + "'").addFlashAttribute("clase",
						"warning");
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
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX + "'").addFlashAttribute("clase",
						"warning");
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
				attribute.addFlashAttribute("mensaje", MSG_WARNING + CANTIDAD_MAX + "'").addFlashAttribute("clase",
						"warning");
			}
		} catch (GallinaNotFoundException | SinDineroException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "redirect:/listar";
	}

	@GetMapping("/pasarDeDia")
	public String pasarDeDia(RedirectAttributes attribute) {
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

			model.addAttribute("totalCantGallinas", iGallinaService.listarGallinas().size());
			model.addAttribute("costoUnitarioGallina", gallinaService.getPRECIO_GALLINA());
			model.addAttribute("totalCantHuevos", iGallinaService.listarHuevos().size());
			model.addAttribute("costoUnitarioHuevo", gallinaService.getPRECIO_HUEVO());

			model.addAttribute("gallinasVendidas", iStatusService.plataEnCuenta(1).get().getGallinasVendidas());
			model.addAttribute("totalVentaGallinas", (iStatusService.plataEnCuenta(1).get().getGallinasVendidas()
					* iStatusService.plataEnCuenta(1).get().getPrecioGallina()));
			model.addAttribute("huevosVendidos", iStatusService.plataEnCuenta(1).get().getHuevosVendidos());
			model.addAttribute("totalVentaHuevos", (iStatusService.plataEnCuenta(1).get().getHuevosVendidos()
					* iStatusService.plataEnCuenta(1).get().getPrecioHuevo()));

			model.addAttribute("gallinasCompra", iStatusService.plataEnCuenta(1).get().getGallinasCompra());
			model.addAttribute("totalCompraGallinas",
					(iStatusService.plataEnCuenta(1).get().getGallinasCompra() * gallinaService.getPRECIO_GALLINA()));
			model.addAttribute("huevosCompra", iStatusService.plataEnCuenta(1).get().getHuevosCompra());
			model.addAttribute("totalCompraHuevos",
					(iStatusService.plataEnCuenta(1).get().getHuevosCompra() * gallinaService.getPRECIO_HUEVO()));

			model.addAttribute("totalGranja", iGallinaService.listarProductos().size());
			model.addAttribute("dineroEnCuenta", iStatusService.plataEnCuenta(1).get().getDineroCuenta());
			model.addAttribute("totalNacimientoGallinas", iStatusService.plataEnCuenta(1).get().getGallinaNacimiento());
			model.addAttribute("totalMuerteGallinas", iStatusService.plataEnCuenta(1).get().getGallinaMuerte());

		} catch (RuntimeException e) {
			e.printStackTrace();
			attribute.addFlashAttribute("mensaje", e.getMessage()).addFlashAttribute("clase", "danger");
		}
		return "reporte";
	}
}
