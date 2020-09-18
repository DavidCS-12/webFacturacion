package com.co.web.avanzada.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.co.web.avanzada.entity.Categoria;
import com.co.web.avanzada.repository.ICategoriaRepo;
@Controller
@RequestMapping("/admind")
public class CategoriaController {
	@Autowired
	private ICategoriaRepo iCategoriaRepo;	
	
    @GetMapping("/addCategoria")
    public String showSignUpForm(Model model) {
     	 model.addAttribute("categoria", new Categoria());
        return "add-categoria";
    }
    
    @PostMapping("/add_categoria")
    public String addCategoria(@Validated Categoria categoria, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	 model.addAttribute("categorias", iCategoriaRepo.findAll());
            return "add-categoria";
        }
        
        iCategoriaRepo.save(categoria);
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "redirect:/admind/listarCategorias";
    }
    
    @GetMapping("/editCategoria/{idCategoria}")
    public String showUpdateForm(@PathVariable("idCategoria") int idCategoria, Model model) {
    	Categoria categoria = iCategoriaRepo.findById(idCategoria).orElseThrow(() -> new IllegalArgumentException("Invalid categoria id:" + idCategoria));
        model.addAttribute("categorias", categoria);
        return "update-categoria";
    }
    
    @PostMapping("/updateCategoria/{idCategoria}")
    public String updateCategoria(@PathVariable("idCategoria") int idCategoria, @Validated Categoria categoria, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	
        	 model.addAttribute("categorias", iCategoriaRepo.findAll());
        	categoria.setIdCategoria(idCategoria);
            return "update-categoria";
        }
        
        iCategoriaRepo.save(categoria);
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "redirect:/admind/listarCategorias";
    }
    
    @GetMapping("/deleteCategoria/{idCategoria}")
    public String deleteCategoria(@PathVariable("idCategoria") int idCategoria, Model model) {
        Categoria categoria = iCategoriaRepo.findById(idCategoria).orElseThrow(() -> new IllegalArgumentException("Invalid categoria id:" + idCategoria));
        iCategoriaRepo.delete(categoria);
    	model.addAttribute("categorias", iCategoriaRepo.findAll());
    	return "redirect:/admind/listarCategorias";
    }
    @GetMapping("/listarcategorias")
    public String ListarCate(Model model) {
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "listarcategoria";
    }
     

}