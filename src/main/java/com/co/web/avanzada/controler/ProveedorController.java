package com.co.web.avanzada.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Proveedor;
import com.co.web.avanzada.repository.IPaiRepo;
import com.co.web.avanzada.repository.IProveedorRepo;


@Controller
public class ProveedorController {
	/*Con esta anotación se crea la instancia del been cada vez que el sistema lo requiera.*/
	/*se instancian los repositorios, en este caso de proveedorRepo y paiRepo, para poder hacer uso de las funciones del reositorio, 
	 * las cuales son la iteración entre la información obtenida en el sistema y la base de datos.*/
	@Autowired
	private  IProveedorRepo iProveedorRepo;
	@Autowired
	private  IPaiRepo iPaiRepo;
	/*Método que mapea el formulario necesario para hacer el registro de un proveedor, el cuál recibe como parametro el modelo a cargar
	 * en la plantilla , con el que se va a interactuar.*/
    @GetMapping("/addproveedor")
    public String addProveedor(Proveedor proveedor, Model model) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
    	Modelos(model);
    	model.addAttribute("proveedor", new Proveedor());
    	/*Se retorna la plantilla o formulario html para el registro del departamento*/
        return "add-proveedor";
    }
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/
    @PostMapping("/add_proveedor")
    public String add_proveedor(@Validated Proveedor proveedor, BindingResult result, Model model) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar proveedor de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de proveedores.*/
    	if (result.hasErrors()) {
    		model.addAttribute("proveedor", proveedor);
    		Modelos(model);
            return "redirect:/admind/addproveedor";
        }
    	/*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iProveedorRepo.save(proveedor);
        /* Se cargan todas los proveedores existentes en la base de datos al modelo para poder listarlas.*/
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "redirect:/admind/listarProveedor";
    }
    
    public void Modelos(Model model) {
        model.addAttribute("paises", iPaiRepo.findAll());
    	model.addAttribute("municipios", new Municipio());
    	model.addAttribute("departamentos", new Departamento());
    }
    /*En este método se recibe del formulario en donde se listan los proveedores el id del proveedor a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del proveedor.*/
    @GetMapping("/editProveedor/{nit}")
    public String showUpdateForm(@PathVariable("nit") int nit, Model model) {
    	/*En esta parte se crea un bean de tipo proveedor y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el proveedor arroja el
    	 * mensaje de error.*/
    	Proveedor proveedor = iProveedorRepo.findById(nit).orElseThrow(() -> new IllegalArgumentException("Invalid proveedor id:" + nit));
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
    	model.addAttribute("proveedor",proveedor);
        /*Se le agrega a la plantilla un modelo de paises ya que requerimos de ellos para poder hacer una inserción.*/
    	model.addAttribute("paises",iPaiRepo.findAll());
        return "update-proveedor";
    }
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updateProveedor/{nit}")
    public String updateProveedor(@PathVariable("nit") int nit, @Validated Proveedor proveedor, BindingResult result, Model model) {
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
    		model.addAttribute("proveedor", proveedor);
    		Modelos(model);
            return "redirect:/admind/updateProveedor/"+nit;
        }
    	/*De lo contrario, guardara los datos con el método .save del repositorio.*/
        iProveedorRepo.save(proveedor);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de proveedores*/
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "redirect:/admind/listarProveedor";
    }
    /*En este método se recibe como parametro de la lista de proveedores el id del proveedor seleccionado*/
    @GetMapping("/deleteProveedor/{nit}")
    public String deleteProveedor(@PathVariable("nit") int nit, Model model) {
    	/*Se instancia un bean tipo proveedor y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el proveedor dado el id recibido*/
        Proveedor proveedor = iProveedorRepo.findById(nit).orElseThrow(() -> new IllegalArgumentException("Invalid usuario id:" + nit));
        /* Si encuentra el proveedor carga el bean y medianted el método delete del repositorio se envia y se elimina el proveedor 
         * buscado anteriormente.*/
        iProveedorRepo.delete(proveedor);
        /* Se carga una lista actualiza de proveedores al modelo y se redirige a la página de listar.*/
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "redirect:/admind/listarProveedor";
    }
    /*Método encargado de enviar al modelo o plantilla la lista de proveedores existentes en la base de datos.*/
    @GetMapping("/listarProveedor")
    public String ListarProveedor(Model model) {
    	/*Se buscan los proveedores mediante el método del repositorio findbyid y se cargan en la variable 'proveedor' 
    	 * a la plantilla o medelo de la plantilla.*/
        model.addAttribute("proveedor", iProveedorRepo.findAll());
        return "listarProveedor";
    }

}
