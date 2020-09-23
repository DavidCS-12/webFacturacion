package com.co.web.avanzada.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.repository.IDepartamentoRepo;
import com.co.web.avanzada.repository.IMunicipioRepo;
import com.co.web.avanzada.repository.IPaiRepo;
@Controller
public class MunicipioController {
	
	/*Con esta anotación se crea la instancia del been cada vez que el sistema lo requiera.*/
	/*se instancian los repositorios, en este caso de depatamentoRepo, paiRepo y municipioRepo, para poder hacer uso de las funciones del reositorio, 
	 * las cuales son la iteración entre la información obtenida en el sistema y la base de datos.*/
	@Autowired
	private IMunicipioRepo iMunicipioRepo;
    @Autowired
	private IDepartamentoRepo iDepartamentoRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;

	/*Método que mapea el formulario necesario para hacer el registro de un municipio, el cuál recibe como parametro el modelo a cargar
	 * en la plantilla con el que se va a interactuar.*/
    @GetMapping("/addMunicipio")
    public String showSignUpForm(Model model) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
    	model.addAttribute("municipio", new Municipio());
    	/*Se le agrega a la plantilla un modelo de paises ya que requerimos de ellos para poder hacer una inserción.*/
    	model.addAttribute("paises", iPaiRepo.findAll());
    	/*Se le agrega a la plantilla un modelo de departamentos ya que requerimos de ellos para poder hacer una inserción.*/
    	model.addAttribute("departamentos", iDepartamentoRepo.findAll());
     	/*Se retorna la plantilla o formulario html para el registro del municipio*/
    	return "add-municipio";
    }
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/
    @PostMapping("/add_municipio")
    public String addMunicipio(@Validated Municipio municipio, BindingResult result, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
        	model.addAttribute("departamentos", iDepartamentoRepo.findAll());
            return "add-municipio";
        }
        /*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iMunicipioRepo.save(municipio);
        /* Se cargan todas los municipios existentes en la base de datos al modelo para poder listarlas.*/
        model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "redirect:/listarMunicipios";
    }
    
    /*En este método se recibe del formulario en donde se listan los municipios el id del municipio a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del municipio.*/
    @GetMapping("/editMunicipio/{idMunicipio}")
    public String showUpdateForm(@PathVariable("idMunicipio") int idMunicipio, Model model) {
    	/*En esta parte se crea un bean de tipo municipio y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el municipio arroja el
    	 * mensaje de error.*/
    	Municipio municipio = iMunicipioRepo.findById(idMunicipio).orElseThrow(() -> new IllegalArgumentException("Invalid municipio id:" + idMunicipio));
    	/*Carga en el modelo los datos del municipio buscado y de los paises disponibles para poder hacer la modificación.*/
    	model.addAttribute("municipio", municipio);
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "update-municipio";
    }
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updateMunicipio/{idMunicipio}")
    public String updateMunicipio(@PathVariable("idMunicipio") int idMunicipio, @Validated Municipio municipio, BindingResult result, Model model) {
        
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
        	model.addAttribute("municipio", municipio);
            model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        	municipio.setIdMunicipio(idMunicipio);
            return "update-municipio";
        }
    	/*De lo contrario, guardara los datos con el método .save del repositorio.*/
        iMunicipioRepo.save(municipio);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de municipios*/
        model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "redirect:/listarMunicipios";
    }
    /*En este método se recibe como parametro de la lista de departamentos el id del municipio seleccionado*/
    @GetMapping("/deleteMunicipio/{idMunicipio}")
    public String deleteMunicipio(@PathVariable("idMunicipio") int idMunicipio, Model model) {
    	/*Se instancia un bean tipo municipio y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el municipio dado el id recibido*/
        Municipio municipio = iMunicipioRepo.findById(idMunicipio).orElseThrow(() -> new IllegalArgumentException("Invalid municipio id:" + idMunicipio));
        /* Si encuentra el municipio carga el bean y medianted el método delete del repositorio se envia y se elimina el municipio 
         * buscado anteriormente.*/
        iMunicipioRepo.delete(municipio);
        /* Se carga una lista actualiza de municipios al modelo y se redirige a la página de listar.*/
        model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "redirect:/listarMunicipios";
    }
    /*Método encargado de enviar al modelo o plantilla la lista de municipios existentes en la base de datos.*/
    @GetMapping("/listarMunicipios")
    public String ListarMunicipio(Model model) {
    	/*Se buscan los municipios mediante el método del repositorio findbyid y se cargan en la variable 'municipio' 
    	 * a la plantilla o medelo de la plantilla.*/
    	model.addAttribute("municipio", iMunicipioRepo.findAll());
        return "listarMunicipio";
    }
}



