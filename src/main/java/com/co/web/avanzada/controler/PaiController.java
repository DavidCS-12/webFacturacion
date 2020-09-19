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
/*Se hace referencia a la anotación requestMapping para asignar la url padre de todos y cada uno de los métodos
 * del controlador*/
@RequestMapping("/admind")
public class PaiController {
	private final IPaiRepo iPaiRepo;
	
    @Autowired
    public PaiController(IPaiRepo iPaiRepo) {
        this.iPaiRepo = iPaiRepo;
    }
    
    /*Método que mapea el formulario necesario para hacer el registro de un país, el cuál recibe como parametro el modelo a cargar
	 * en la plantilla con el que se va a interactuar.*/
    @GetMapping("/addpai")
    public String showSignUpForm(Model model) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
    	model.addAttribute("pai", new Pai());
     	/*Se retorna la plantilla o formulario html para el registro del país*/
    	return "add-pai";
    }
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/
    @PostMapping("/add_pai")
    public String addPais(@Validated Pai pai, BindingResult result, Model model) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar país de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de paises.*/
        if (result.hasErrors()) {
            return "redirect:/admind/addpai";
        }
        /*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iPaiRepo.save(pai);
        /* Se cargan todas los paises existentes en la base de datos al modelo para poder listarlas.*/
        model.addAttribute("pai", iPaiRepo.findAll());
        return "redirect:/admind/listarPaises";
    }
    
    /*En este método se recibe del formulario en donde se listan los paises el id del país a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del país*/
    @GetMapping("/editPai/{idPais}")
    public String showUpdateForm(@PathVariable("idPais") int idPais, Model model) {
    	/*En esta parte se crea un bean de tipo pai y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el país arroja el
    	 * mensaje de error.*/
    	Pai pai = iPaiRepo.findById(idPais).orElseThrow(() -> new IllegalArgumentException("Invalid pais id:" + idPais));
    	/*Carga en el modelo los datos del país buscado.*/
    	model.addAttribute("pai", pai);
        return "update-pai";
    }
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updatePai/{idPais}")
    public String updatePais(@PathVariable("idPais") int idPais, @Validated Pai pai, BindingResult result, Model model) {
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
        	pai.setIdPais(idPais);
            return "redirect:/admind/updatePai";
        }
    	/*De lo contrario, guardara los datos con el método .save del repositorio.*/
        iPaiRepo.save(pai);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de pasises*/
        model.addAttribute("pai", iPaiRepo.findAll());
        return "redirect:/admind/listarPaises";
    }
    /*En este método se recibe como parametro de la lista de paises el id del país seleccionado*/
    @GetMapping("/deletePai/{idPais}")
    public String deletePais(@PathVariable("idPais") int idPais, Model model) {
    	/*Se instancia un bean tipo país y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el país dado el id recibido*/
        Pai pai = iPaiRepo.findById(idPais).orElseThrow(() -> new IllegalArgumentException("Invalid pais id:" + idPais));
        /* Si encuentra el país carga el bean y medianted el método delete del repositorio se envia y se elimina el país 
         * buscado anteriormente.*/
        iPaiRepo.delete(pai);
        /* Se carga una lista actualiza de paises al modelo y se redirige a la página de listar.*/
        model.addAttribute("pai", iPaiRepo.findAll());
        return "redirect:/admind/listarPaises";
    }
    /*Método encargado de enviar al modelo o plantilla la lista de paises existentes en la base de datos.*/
    @GetMapping("/listarPaises")
    public String ListarPais(Model model) {
    	/*Se buscan los paises mediante el método del repositorio findbyid y se cargan en la variable 'pai' 
    	 * a la plantilla o medelo de la plantilla.*/
        model.addAttribute("pai", iPaiRepo.findAll());
		return "listarPai";
    }

}







