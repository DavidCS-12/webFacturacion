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

import com.co.web.avanzada.entity.Categoria;
import com.co.web.avanzada.repository.ICategoriaRepo;

@Controller
/*Se hace referencia a la anotación requestMapping para asignar la url padre de todos y cada uno de los métodos
 * del controlador*/
@RequestMapping("/admind")
public class CategoriaController {
	/*Con esta anotación se crea la instancia del been cada vez que el sistema lo requiera.*/
	@Autowired
	/*se instancia el repositorio en este caso de la categoríaRepo para poder hacer uso de las funciones del reositorio, 
	 * las cuales son la iteración entre la información obtenida en el sistema y la base de datos.*/
	private ICategoriaRepo iCategoriaRepo;	
	
	/*Método que mapea el formulario necesario para hacer el registro de una categoría, el cuál recibe como parametro el modelo a cargar
	 * en la plantilla , con el que se va a interactuar.*/ 
    @GetMapping("/addCategoria")
    public String showSignUpForm(Model model) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
     	model.addAttribute("categoria", new Categoria());
     	/*Se retorna la plantilla o formulario html para el registro de la categoría*/
        return "add-categoria";
    }
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/
    @PostMapping("/add_categoria")
    public String addCategoria(@Validated Categoria categoria, BindingResult result, Model model) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar categoría de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de categorías.*/
        if (result.hasErrors()) {
        	 model.addAttribute("categorias", iCategoriaRepo.findAll());
            return "redirect:/admind/addCategoria";
        }
        /*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iCategoriaRepo.save(categoria);
        /* Se cargan todas las categorias existentes en la base de datos al modelo para poder listarlas.*/
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "redirect:/admind/listarcategorias";
    }
    
    /*En este método se recibe del formulario en donde se listan las categorías el id de la categoría a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación de la categoría.*/
    @GetMapping("/editCategoria/{idCategoria}")
    public String showUpdateForm(@PathVariable("idCategoria") int idCategoria, Model model) {
    	/*En esta parte se crea un bean de tipo categoría y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra la categoría arroja el
    	 * mensaje de error.*/
    	Categoria categoria = iCategoriaRepo.findById(idCategoria).orElseThrow(() -> new IllegalArgumentException("Invalid categoria id:" + idCategoria));
        /*Carga en el modelo los datos de la categoría buscada.*/
    	model.addAttribute("categorias", categoria);
        return "update-categoria";
    }
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updateCategoria/{idCategoria}")
    public String updateCategoria(@PathVariable("idCategoria") int idCategoria, @Validated Categoria categoria, BindingResult result, Model model) {
        /* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
        	model.addAttribute("categorias", iCategoriaRepo.findAll());
        	categoria.setIdCategoria(idCategoria);
            return "redirect:/admind/editCategoria";
        }
        /*De lo contrario, guardara los datos con el método .save del repositorio.*/
        iCategoriaRepo.save(categoria);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de categorías*/
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "redirect:/admind/listarcategorias";
    }
    /*En este método se recibe como parametro de la lista de categorías el id de la categoría seleccionada*/
    @GetMapping("/deleteCategoria/{idCategoria}")
    public String deleteCategoria(@PathVariable("idCategoria") int idCategoria, Model model) {
    	/*Se instancia un bean tipo categoría y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar la categoría dado el id recibido*/
        Categoria categoria = iCategoriaRepo.findById(idCategoria).orElseThrow(() -> new IllegalArgumentException("Invalid categoria id:" + idCategoria));
        /* Si encuentra la categoría carga el bean y medianted el método delete del repositorio se envia y se elimina la categoría 
         * buscada anteriormente.*/
        iCategoriaRepo.delete(categoria);
        /* Se carga una lista actualiza de categorías al modelo y se redirige a la página de listar.*/
    	model.addAttribute("categorias", iCategoriaRepo.findAll());
    	return "redirect:/admind/listarcategorias";
    }
    /*Método encargado de enviar al modelo o plantilla la lista de categoría existentes en la base de datos.*/
    @GetMapping("/listarcategorias")
    public String ListarCate(Model model) {
    	/*Se buscan las categorías mediante el método del repositorio findbyid y se cargan en la variable 'categorias' 
    	 * a la plantilla o medelo de la plantilla.*/
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "listarcategoria";
    }
}