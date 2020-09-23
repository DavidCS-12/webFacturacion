package com.co.web.avanzada.controler;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.Authority;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.AuthorityRepository;
import com.co.web.avanzada.repository.IPaiRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;
import com.co.web.avanzada.util.Passgenerator;

@Controller
public class VendedorController {
	@Autowired
	private  IUsuarioRepo iUsuarioRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;	
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private Passgenerator passgenerator;

    @GetMapping("/addvendedor")
    public String addVendedor(Usuario usuario, Model model) {
    	Modelos(model);
        return "add-usuario-vendedor";
    }
    
    @PostMapping("/add_vendedor")
    public String addvendedor(@Validated Usuario usuario, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		model.addAttribute("usuario", usuario);
    		
    		Modelos(model);
            return "add-usuario-vendedor";
        }
        
        System.out.println("Este es el usuario que vamos a registrar"+ usuario.toString());
        
        Authority autorizacion= authorityRepository.findByAuthority("ROLE_VENDEDOR");
               
        
        Set<Authority> authority= new HashSet<Authority>();
        authority.add(autorizacion);
        
        usuario.setAuthority(authority);
        usuario.setEnabled(true);
        usuario.setRol("VENDEDOR");
        // El String que mandamos al metodo encode es el password que queremos
		// encriptar.
		usuario.setPassword((passgenerator.enciptarPassword(usuario.getPassword())));
        
        iUsuarioRepo.save(usuario);
        model.addAttribute("usuarios", iUsuarioRepo.findAll());
        return "redirect:/listarVendedores";
    }
    public void Modelos(Model model) {
        model.addAttribute("paises", iPaiRepo.findAll());
       

    	model.addAttribute("municipios", new Municipio());
    	model.addAttribute("departamentos", new Departamento());
 
    }
    @GetMapping("/editVendedor/{dni}")
    public String editVendedor(@PathVariable("dni") int dni, Model model) {
    	Usuario usuario = iUsuarioRepo.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + dni));
        Modelos(model);
    	model.addAttribute("usuario", usuario);
        return "update-usuario-vendedor";
    }
    
    @PostMapping("/updateVendedor/{dni}")
    public String updateVendedor(@PathVariable("dni") int dni, @Validated Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	Modelos(model);
        	usuario.setDni(dni);
            return "redirect:/editVendedor/"+dni;
        }
        
        Authority autorizacion= authorityRepository.findByAuthority("ROLE_VENDEDOR");
        Set<Authority> authority= new HashSet<Authority>();
        authority.add(autorizacion);
        usuario.setAuthority(authority);
        usuario.setEnabled(true);
        usuario.setRol("VENDEDOR");
        usuario.setPassword((passgenerator.enciptarPassword(usuario.getPassword())));
        iUsuarioRepo.save(usuario);
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "redirect:/listarVendedores/";
    }
    
    @GetMapping("/deleteVendedor/{dni}")
    public String deleteVendedor(@PathVariable("dni") int dni, Model model) {
        Usuario usuario = iUsuarioRepo.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + dni));
        iUsuarioRepo.delete(usuario);
    	
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "redirect:/listarVendedores";
    }
    
    @GetMapping("/listarVendedores")
    public String listarVendedor(Model model) {
        model.addAttribute("usuario", iUsuarioRepo.findByRoleVendedor());
        return "listarVendedor";
    }
}
