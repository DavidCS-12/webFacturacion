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

import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.repository.IDepartamentoRepo;
import com.co.web.avanzada.repository.IMunicipioRepo;
import com.co.web.avanzada.repository.IPaiRepo;
@Controller
@RequestMapping("/admind")
public class MunicipioController {
    @Autowired
	private IMunicipioRepo iMunicipioRepo;
    @Autowired
	private IDepartamentoRepo iDepartamentoRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;

    @GetMapping("/addMunicipio")
    public String showSignUpForm(Model model) {
    	model.addAttribute("municipio", new Municipio());
    	model.addAttribute("paises", iPaiRepo.findAll());
    	model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "add-municipio";
    }
    
    @PostMapping("/add_municipio")
    public String addMunicipio(@Validated Municipio municipio, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
        	model.addAttribute("departamentos", iDepartamentoRepo.findAll());
            return "add-municipio";
        }
        
        iMunicipioRepo.save(municipio);
        model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "redirect:/admind/listarMunicipios";
    }
    
    @GetMapping("/editMunicipio/{idMunicipio}")
    public String showUpdateForm(@PathVariable("idMunicipio") int idMunicipio, Model model) {
    	Municipio municipio = iMunicipioRepo.findById(idMunicipio).orElseThrow(() -> new IllegalArgumentException("Invalid municipio id:" + idMunicipio));
    	model.addAttribute("municipio", municipio);
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "update-municipio";
    }
    
    @PostMapping("/updateMunicipio/{idMunicipio}")
    public String updateMunicipio(@PathVariable("idMunicipio") int idMunicipio, @Validated Municipio municipio, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
        	model.addAttribute("municipio", municipio);
            model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        	municipio.setIdMunicipio(idMunicipio);
            return "update-municipio";
        }
        
        iMunicipioRepo.save(municipio);
        model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "redirect:/admind/listarMunicipios";
    }
    
    @GetMapping("/deleteMunicipio/{idMunicipio}")
    public String deleteMunicipio(@PathVariable("idMunicipio") int idMunicipio, Model model) {
        Municipio municipio = iMunicipioRepo.findById(idMunicipio).orElseThrow(() -> new IllegalArgumentException("Invalid municipio id:" + idMunicipio));
        iMunicipioRepo.delete(municipio);
        model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "redirect:/admind/listarMunicipios";
    }
    
    @GetMapping("/listarMunicipios")
    public String ListarMunicipio(Model model) {
    	model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "listarMunicipio";
    }
     

}



