package com.co.web.avanzada.controler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import com.co.web.avanzada.config.CloudinaryConfig;
import com.co.web.avanzada.entity.Bodega;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.repository.IBodegaRepo;
import com.co.web.avanzada.repository.ICategoriaRepo;
import com.co.web.avanzada.repository.IDepartamentoRepo;
import com.co.web.avanzada.repository.IMunicipioRepo;
import com.co.web.avanzada.repository.IPaiRepo;
import com.co.web.avanzada.repository.IProveedorRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;

@Controller

public class BodegaController {

	@Autowired
	private IMunicipioRepo iMunicipioRepo;
	@Autowired
	private IDepartamentoRepo iDepartamentoRepo;
	@Autowired
	private IPaiRepo iPaiRepo;
	@Autowired
	private IUsuarioRepo iUsuarioRepo;
	@Autowired
	private IBodegaRepo iBodegaRepo;
	@Autowired
	private CloudinaryConfig cloudc;

	@GetMapping("/addbodega")
	public String showSignUpForm(Model model) {
		model.addAttribute("bodega", new Bodega());
		model.addAttribute("usuarios", iUsuarioRepo.findByRoleVendedor());
		model.addAttribute("paises", iPaiRepo.findAll());
		model.addAttribute("municipios", new Municipio());
		model.addAttribute("departamentos", new Departamento());
		return "add-bodega";
	}

	@PostMapping("/add_bodega")
	public String addProducto(@Validated Bodega bodega, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("bodega", new Bodega());
			model.addAttribute("paises", iPaiRepo.findAll());
			model.addAttribute("municipios", new Municipio());
			model.addAttribute("departamentos", new Departamento());
			return "add-bodega";
		}

		iBodegaRepo.save(bodega);
		model.addAttribute("bodegas", iBodegaRepo.findAll());
		return "redirect:/listarBodega";
	}

	@GetMapping("/editBodega/{idBodega}")
	public String showUpdateForm(@PathVariable("idBodega") int idBodega, Model model) {
		Bodega bodega = iBodegaRepo.findById(idBodega)
				.orElseThrow(() -> new IllegalArgumentException("Invalid bodega id:" + idBodega));
		model.addAttribute("bodega", bodega);
		model.addAttribute("usuarios", iUsuarioRepo.findByRoleVendedor());
		model.addAttribute("paises", iPaiRepo.findAll());
		model.addAttribute("municipios", new Municipio());
		model.addAttribute("departamentos", new Departamento());

		return "update-bodega";
	}

	@PostMapping("/updateBodega/{idBodega}")
	public String updateBodega(@PathVariable("idBodega") int idBodega, @Validated Bodega bodega, BindingResult result,
			Model model, @RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl) {
		if (result.hasErrors()) {

			model.addAttribute("bodega", bodega);
			return "update-bodega";
		}

		iBodegaRepo.save(bodega);
		model.addAttribute("bodegas", iBodegaRepo.findAll());
		return "redirect:/listarBodega";
	}

	@GetMapping("/deleteBodega/{idBodega}")
	public String deleteProducto(@PathVariable("idBodega") int idBodega, Model model) {
		Bodega bodega = iBodegaRepo.findById(idBodega)
				.orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + idBodega));
		iBodegaRepo.delete(bodega);
		model.addAttribute("bodega", iBodegaRepo.findAll());
		return "redirect:/listarBodega";
	}

	@GetMapping("/listarBodega")
	public String ListarDepa(Model model) {
		model.addAttribute("bodegas", iBodegaRepo.findAll());
		return "listarBodega";
	}

}