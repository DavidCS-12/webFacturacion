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
	
	/*Con esta anotación se crea la instancia del been cada vez que el sistema lo requiera.*/
	/*se instancian los repositorios, en este caso de usuarioRepo, paiRepo, departamentoRepo, municipioRepo, authorityRepository y passgenerator, para poder hacer uso de las funciones del repositorio, 
	 * las cuales son la iteración entre la información obtenida en el sistema y la base de datos.*/
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
	

	/*Método que carga la plantilla de inicio de usuario.*/
	@GetMapping("/")
	public String login() {
		return "login";
	}
	
	
	 /*Método que mapea el formulario necesario para hacer el registro de un usuario, el cuál recibe como parametro el modelo a cargar
	 * en la plantilla con el que se va a interactuar.*/
    @GetMapping("/addusuario")
    public String showSignUpForm(Usuario usuario, Model model) {
    	Modelos(model);
    	 /*Se retorna la plantilla o formulario html para el registro del usuario*/
        return "add-usuario";
    }
    

	 /*Método que direcciona al inicio del menu de un administrador.*/
    @GetMapping("/index")
	public String InicioAdmin(Model model) {
		return "index";
	}
    
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/   
    @PostMapping("/add_usuario")
    public String addusuario(@Validated Usuario usuario, BindingResult result, Model model) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar usuarios, de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de usuarios.*/
    	if (result.hasErrors()) {
    		model.addAttribute("usuario", usuario);
    		Modelos(model);
            return "add-usuario";
        }
        
        System.out.println("este es el usuario que vamos aregistrar"+ usuario.toString());
        
        Authority autorizacion= authorityRepository.findByAuthority("ROLE_CLIENTE");
               
        
        Set<Authority> authority= new HashSet<Authority>();
        authority.add(autorizacion);
        
        usuario.setAuthority(authority);
        usuario.setEnabled(true);
        usuario.setRol("CLIENTE");

        // El String que mandamos al metodo encode es el password que queremos
		// encriptar.
		usuario.setPassword((passgenerator.enciptarPassword(usuario.getPassword())));
        
		/*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iUsuarioRepo.save(usuario);
        /* Se cargan todas los productos existentes en la base de datos al modelo para poder listarlas.*/
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
    /*En este método se recibe del formulario en donde se listan los usuarios el id del usuario a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del usuario.*/
    @GetMapping("/edit/{dni}")
    public String showUpdateForm(@PathVariable("dni") int dni, Model model) {
    	/*En esta parte se crea un bean de tipo usuario y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el usuario arroja el
    	 * mensaje de error.*/
    	Usuario usuario = iUsuarioRepo.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + dni));
        Modelos(model);
        /*Carga en el modelo los datos del usuario buscado,los usuarios disponibles para poder hacer la modificación.*/
        model.addAttribute("usuario", usuario);
        return "update-usuario";
    }
    
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/update/{dni}")
    public String updateUsuario(@PathVariable("dni") int dni, @Validated Usuario usuario, BindingResult result, Model model) {
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
        	Modelos(model);
        	usuario.setDni(dni);
            return "redirect:/edit/"+dni;
        }
        Authority autorizacion= authorityRepository.findByAuthority("ROLE_CLIENTE");
               
        
        Set<Authority> authority= new HashSet<Authority>();
        authority.add(autorizacion);
        usuario.setRol("CLIENTE");
        usuario.setAuthority(authority);
        usuario.setEnabled(true);
        usuario.setPassword((passgenerator.enciptarPassword(usuario.getPassword())));
        /*la condición se modifica los datos cambiados en dicho usuario. */
        iUsuarioRepo.save(usuario);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de usuarios*/
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "redirect:/index";
    }
    
    
    /*En este método se recibe como parametro de la lista de usuarios el id del usuario seleccionado*/
    @GetMapping("/delete/{dni}")
    public String deleteUsuario(@PathVariable("dni") int dni, Model model) {
    	/*Se instancia un bean tipo usuario y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el usuario dado el id recibido*/
        Usuario usuario = iUsuarioRepo.findById(dni).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + dni));
        /* Si encuentra el usuario carga el bean y medianted el método delete del repositorio se envia y se elimina el usuario 
         * buscado anteriormente.*/
        iUsuarioRepo.delete(usuario);
        /* Se carga una lista actualiza de usuarios al modelo y se redirige a la página de listar.*/
        model.addAttribute("usuario", iUsuarioRepo.findAll());
        return "redirect:/listarUsuario";
    }
    
    /*Método encargado de enviar al modelo o plantilla la lista de usuarios existentes en la base de datos.*/
    @GetMapping("/listarUsuarios")
    public String listarUsuario(Model model) {
    	/*Se buscan los usuarios mediante el método del repositorio findbyid y se cargan en la variable 'usuario' 
    	 * a la plantilla o medelo de la plantilla.*/
        model.addAttribute("usuario", iUsuarioRepo.findByRoleCliente());
        return "listarUsuario";
    }    	
}


