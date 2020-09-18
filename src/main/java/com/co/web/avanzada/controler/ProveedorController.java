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
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Proveedor;
import com.co.web.avanzada.repository.IPaiRepo;
import com.co.web.avanzada.repository.IProveedorRepo;


@Controller
@RequestMapping("/admind")
public class ProveedorController {
	@Autowired
	private  IProveedorRepo iProveedorRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;

    @GetMapping("/addproveedor")
    public String addProveedor(Proveedor proveedor, Model model) {
    	Modelos(model);
        return "add-proveedor";
    }
    @PostMapping("/add_proveedor")
    public String add_proveedor(@Validated Proveedor proveedor, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		model.addAttribute("proveedor", proveedor);
    		Modelos(model);
            return "add-proveedor";
        }
        iProveedorRepo.save(proveedor);
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "redirect:/admind/listarProveedor";
    }
    public void Modelos(Model model) {
        model.addAttribute("paises", iPaiRepo.findAll());
    	model.addAttribute("municipios", new Municipio());
    	model.addAttribute("departamentos", new Departamento());
    }
    
    @GetMapping("/editProveedor/{nit}")
    public String showUpdateForm(@PathVariable("nit") int nit, Model model) {
    	Proveedor proveedor = iProveedorRepo.findById(nit).orElseThrow(() -> new IllegalArgumentException("Invalid proveedor id:" + nit));
    	
        model.addAttribute("proveedor",proveedor);
        model.addAttribute("paises",iPaiRepo.findAll());
        return "update-proveedor";
    }
    
    @PostMapping("/updateProveedor/{nit}")
    public String updateProveedor(@PathVariable("nit") int nit, @Validated Proveedor proveedor, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	proveedor.setNit(nit);
            return "redirect:/admind/updateProveedor/"+nit;
        }
        
        iProveedorRepo.save(proveedor);
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "redirect:/admind/listarProveedor";
    }
    
    @GetMapping("/deleteProveedor/{nit}")
    public String deleteProveedor(@PathVariable("nit") int nit, Model model) {
        Proveedor proveedor = iProveedorRepo.findById(nit).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + nit));
        iProveedorRepo.delete(proveedor);
    	
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "redirect:/admind/listarProveedor";
    }
    
    @GetMapping("/listarProveedor")
    public String ListarProveedor(Model model) {
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "listarProveedor";
    }

}
