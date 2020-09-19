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
/*Se hace referencia a la anotación requestMapping para asignar la url padre de todos y cada uno de los métodos
 * del controlador*/
@RequestMapping("/admind")
public class DepartamentoController {
	
	/*Con esta anotación se crea la instancia del been cada vez que el sistema lo requiera.*/
	/*se instancian los repositorios, en este caso de depatamentoRepo, paiRepo y municipioRepo, para poder hacer uso de las funciones del reositorio, 
	 * las cuales son la iteración entre la información obtenida en el sistema y la base de datos.*/
	@Autowired
	private  IDepartamentoRepo iDepartamentoRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;
	@Autowired
	private  IMunicipioRepo iMunicipioRepo;  
	
	/*Método que mapea el formulario necesario para hacer el registro de un departamento, el cuál recibe como parametro el modelo a cargar
	 * en la plantilla , con el que se va a interactuar.*/
    @GetMapping("/addDepartamento")
    public String showSignUpForm(Model model) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
        model.addAttribute("departamento",new Departamento());
        /*Se le agrega a la plantilla un modelo de paises ya que requerimos de ellos para poder hacer una inserción.*/
    	model.addAttribute("paises", iPaiRepo.findAll());
     	/*Se retorna la plantilla o formulario html para el registro del departamento*/
    	return "add-departamento";
    }
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/
    @PostMapping("/add_departamento")
    public String addDepartamento(@Validated Departamento departamento, BindingResult result, Model model) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar departamentos de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de departamentos.*/
    	if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
            model.addAttribute("municipios", iMunicipioRepo.findAll());
           return "redirect:/admind/addDepartamento";
        }
    	/*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iDepartamentoRepo.save(departamento);
        /* Se cargan todas los departamentos existentes en la base de datos al modelo para poder listarlas.*/
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "redirect:/admind/listarDepartamentos";
    }
    
    /*En este método se recibe del formulario en donde se listan los departamentos el id del departamento a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del departamento.*/
    @GetMapping("/editDepartamento/{idDepartamento}")
    public String showUpdateForm(@PathVariable("idDepartamento") int idDepartamento, Model model) {
    	/*En esta parte se crea un bean de tipo departamento y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el departamento arroja el
    	 * mensaje de error.*/
    	Departamento departamento = iDepartamentoRepo.findById(idDepartamento).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + idDepartamento));
    	/*Carga en el modelo los datos del departamento buscado y de los paises disponibles para poder hacer la modificación.*/
    	model.addAttribute("departamento", departamento);
    	model.addAttribute("paises", iPaiRepo.findAll());
        return "update-departamento";
    }
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updateDepartamento/{idDepartamento}")
    public String updateDepartamento(@PathVariable("idDepartamento") int idDepartamento, @Validated Departamento departamento, BindingResult result, Model model) {
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
        	model.addAttribute("paises", iPaiRepo.findAll());
            model.addAttribute("municipios", iMunicipioRepo.findAll());
        	model.addAttribute("departamentos", departamento);
        	departamento.setIdDepartamento(idDepartamento); 
            return "redirect:/admind/editDepartamento";
        }
    	/*De lo contrario, guardara los datos con el método .save del repositorio.*/
        iDepartamentoRepo.save(departamento);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de departamentos*/
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "redirect:/admind/listarDepartamentos";
    }
    /*En este método se recibe como parametro de la lista de departamentos el id del departamento seleccionado*/
    @GetMapping("/deleteDepartamento/{idDepartamento}")
    public String deleteDepartamento(@PathVariable("idDepartamento") int idDepartamento, Model model) {
    	/*Se instancia un bean tipo departamento y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el departamento dado el id recibido*/
        Departamento departamento = iDepartamentoRepo.findById(idDepartamento).orElseThrow(() -> new IllegalArgumentException("Invalid categoria id:" + idDepartamento));
        /* Si encuentra el departamento carga el bean y medianted el método delete del repositorio se envia y se elimina el departamento 
         * buscado anteriormente.*/
        iDepartamentoRepo.delete(departamento);
        /* Se carga una lista actualiza de departamentos al modelo y se redirige a la página de listar.*/
        model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "redirect:/admind/listarDepartamentos";
    }
    /*Método encargado de enviar al modelo o plantilla la lista de departamentos existentes en la base de datos.*/
    @GetMapping("/listarDepartamentos")
    public String ListarDepa(Model model) {
    	/*Se buscan los departamentos mediante el método del repositorio findbyid y se cargan en la variable 'departamentos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	model.addAttribute("departamentos", iDepartamentoRepo.findAll());
        return "listarDepartamento";
    }
  
}
