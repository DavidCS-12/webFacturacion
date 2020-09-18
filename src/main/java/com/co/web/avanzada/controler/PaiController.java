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

import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.repository.IPaiRepo;

@Controller
@RequestMapping("/admind")
public class PaiController {
	private final IPaiRepo iPaiRepo;

    @Autowired
    public PaiController(IPaiRepo iPaiRepo) {
        this.iPaiRepo = iPaiRepo;
    }
    
    @GetMapping("/addpai")
    public String showSignUpForm(Model model) {
    	model.addAttribute("pai", new Pai());
        return "add-pai";
    }
    
    @PostMapping("/add_pai")
    public String addPais(@Validated Pai pai, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-pai";
        }
        
        iPaiRepo.save(pai);
        model.addAttribute("pai", iPaiRepo.findAll());
        return "redirect:/admind/listarPaises";
    }
    
    @GetMapping("/editPai/{idPais}")
    public String showUpdateForm(@PathVariable("idPais") int idPais, Model model) {
    	Pai pai = iPaiRepo.findById(idPais).orElseThrow(() -> new IllegalArgumentException("Invalid pais id:" + idPais));
        model.addAttribute("pai", pai);
        return "update-pai";
    }
    
    @PostMapping("/updatePai/{idPais}")
    public String updatePais(@PathVariable("idPais") int idPais, @Validated Pai pai, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	pai.setIdPais(idPais);
            return "update-pai";
        }
        
        iPaiRepo.save(pai);
        model.addAttribute("pai", iPaiRepo.findAll());
        return "redirect:/admind/listarPaises";
    }
    
    @GetMapping("/deletePai/{idPais}")
    public String deletePais(@PathVariable("idPais") int idPais, Model model) {
        Pai pai = iPaiRepo.findById(idPais).orElseThrow(() -> new IllegalArgumentException("Invalid pais id:" + idPais));
        iPaiRepo.delete(pai);
        model.addAttribute("pai", iPaiRepo.findAll());
        return "redirect:/admind/listarPaises";
    }
    @GetMapping("/listarPaises")
    public String ListarPais(Model model) {
        model.addAttribute("pai", iPaiRepo.findAll());
		return "listarPai";
    }

}







