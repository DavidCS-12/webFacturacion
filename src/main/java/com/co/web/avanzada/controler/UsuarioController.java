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
import org.springframework.web.bind.annotation.RequestParam;

import com.co.web.avanzada.entity.Authority;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.AuthorityRepository;
import com.co.web.avanzada.repository.IDepartamentoRepo;
import com.co.web.avanzada.repository.IMunicipioRepo;
import com.co.web.avanzada.repository.IPaiRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;
import com.co.web.avanzada.util.Passgenerator;

@Controller
public class UsuarioController {
	@Autowired
	private  IUsuarioRepo iUsuarioRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;
	@Autowired
	private IDepartamentoRepo iDepartamentoRepo;
	@Autowired
	private IMunicipioRepo iMunicipioRepo;	
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private Passgenerator passgenerator;
	@Autowired
	

	@GetMapping("/")
	public String login() {
		return "login";
	}
    @GetMapping("/addusuario")
    public String showSignUpForm(Usuario usuario, Model model) {
    	Modelos(model);
        return "add-usuario";
    }
    @GetMapping("/admin")
	public String InicioAdmin(Model model) {
		return "index-admin";
	}
    @PostMapping("/add_usuario")
    public String addusuario(@Validated Usuario usuario, BindingResult result, Model model) {
    	if (result.hasErrors()) {
    		model.addAttribute("usuario", usuario);
    		Modelos(model);
            return "add-usuario";
        }
        
        System.out.println("este es el usuario que vamos aregistrar"+ usuario.toString());
        
        Authority autorizacion= authorityRepository.findByAuthority("ROLE_ADMIN");
               
        
        Set<Authority> authority= new HashSet<Authority>();
        authority.add(autorizacion);
        
        usuario.setAuthority(authority);
        usuario.setEnabled(true);
        
        // El String que mandamos al metodo encode es el password que queremos
		// encriptar.
		usuario.setPassword((passgenerator.enciptarPassword(usuario.getPassword())));
        
        iUsuarioRepo.save(usuario);
        model.addAttribute("usuarios", iUsuarioRepo.findAll());
        return "redirect:/";
    }
    public void Modelos(Model model) {
        model.addAttribute("paises", iPaiRepo.findAll());
    	model.addAttribute("municipios", new Municipio());
    	model.addAttribute("departamentos", new Departamento());
    }
    @GetMapping("/ajax/departamentos")
	public String ajaxDepartamentos(@RequestParam("id_pais") String id_pais, Model model) {
    	int id = Integer.parseInt(id_pais);
		model.addAttribute("departamentos", iDepartamentoRepo.ListarDeartamentosPais(id));
		return "add-usuario :: departamentos";
	}
    @GetMapping("/ajax/municipios")
	public String ajaxMunicipios(@RequestParam("id_departamento") String id_departamento, Model model) {
    	int id = Integer.parseInt(id_departamento);
		model.addAttribute("municipios", iMunicipioRepo.ListarMunicipioDeartamento(id));
		return "add-usuario :: municipios";
	}
    @GetMapping("/admind/edit/{dni}")
    public String showUpdateForm(@PathVariable("dni") int dni, Model model) {
    	Usuario usuario = iUsuarioRepo.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + dni));
        model.addAttribute("usuario", usuario);
        return "update-usuario";
    }
    
    @PostMapping("/admind/update/{dni}")
    public String updateUsuario(@PathVariable("dni") int dni, @Validated Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	usuario.setDni(dni);
            return "redirect:/usuario/edit/"+dni;
        }
        
        iUsuarioRepo.save(usuario);
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "redirect:/usuario/edit/"+dni;
    }
    
    @GetMapping("/admind/delete/{dni}")
    public String deleteUsuario(@PathVariable("dni") int dni, Model model) {
        Usuario usuario = iUsuarioRepo.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + dni));
        iUsuarioRepo.delete(usuario);
    	
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "listarUsuario";
    }
    
    @GetMapping("/admind/listarUsuario")
    public String ListarCate(@PathVariable ("dni")String dni,Model model) {
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "listarUsuario";
    }
        	
}


