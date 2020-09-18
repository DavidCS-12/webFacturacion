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

import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.repository.IDepartamentoRepo;
import com.co.web.avanzada.repository.IMunicipioRepo;
import com.co.web.avanzada.repository.IPaiRepo;

@Controller
@RequestMapping("/admind")
public class DepartamentoController {
	@Autowired
	private  IDepartamentoRepo iDepartamentoRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;
	@Autowired
	private  IMunicipioRepo iMunicipioRepo;  
	
    
    @GetMapping("/addDepartamento")
    public String showSignUpForm(Model model) {
        model.addAttribute("departamento",new Departamento());
    	model.addAttribute("paises", iPaiRepo.findAll());
    	return "add-departamento";
    }
    
    @PostMapping("/add_departamento")
    public String addDepartamento(@Validated Departamento departamento, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
            model.addAttribute("municipios", iMunicipioRepo.findAll());
           return "add-departamento";
        }
        
        
        iDepartamentoRepo.save(departamento);
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "redirect:/admind/listarDepartamentos";
    }
    
    @GetMapping("/editDepartamento/{idDepartamento}")
    public String showUpdateForm(@PathVariable("idDepartamento") int idDepartamento, Model model) {
    	Departamento departamento = iDepartamentoRepo.findById(idDepartamento).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + idDepartamento));
    	model.addAttribute("departamento", departamento);
    	model.addAttribute("paises", iPaiRepo.findAll());
        return "update-departamento";
    }
    
    @PostMapping("/updateDepartamento/{idDepartamento}")
    public String updateDepartamento(@PathVariable("idDepartamento") int idDepartamento, @Validated Departamento departamento, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
            model.addAttribute("municipios", iMunicipioRepo.findAll());
        	model.addAttribute("departamentos", departamento);
        	departamento.setIdDepartamento(idDepartamento); 
            return "update-departamento";
        }
        
        iDepartamentoRepo.save(departamento);
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "redirect:/admind/listarDepartamentos";
    }
    
    @GetMapping("/deleteDepartamento/{idDepartamento}")
    public String deleteDepartamento(@PathVariable("idDepartamento") int idDepartamento, Model model) {
        Departamento departamento = iDepartamentoRepo.findById(idDepartamento).orElseThrow(() -> new IllegalArgumentException("Invalid categoria id:" + idDepartamento));
        iDepartamentoRepo.delete(departamento);
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "redirect:/admind/listarDepartamentos";
    }
    
    @GetMapping("/listarDepartamentos")
    public String ListarDepa(Model model) {
    	model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "listarDepartamento";
    }
  
}
