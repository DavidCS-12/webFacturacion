package com.co.web.avanzada.controler;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.Bodega;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IBodegaRepo;
import com.co.web.avanzada.repository.IPaiRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;

@Controller

public class BodegaController {
	/*
	 * Con esta anotación se crea la instancia del been cada vez que el sistema lo
	 * requiera.
	 */
	/*
	 * se instancian los repositorios, en este caso de municipioRepo,
	 * departamentoRepo, paiRepo, usuarioRepo y bodegaRepo, para poder hacer uso de
	 * las funciones del repositorio, las cuales son la iteración entre la
	 * información obtenida en el sistema y la base de datos.
	 */

	@Autowired
	private IPaiRepo iPaiRepo;
	@Autowired
	private IUsuarioRepo iUsuarioRepo;
	@Autowired
	private IBodegaRepo iBodegaRepo;

	/*
	 * Método que mapea el formulario necesario para hacer el registro de una
	 * bodega, el cuál recibe como parametro el modelo a cargar en la plantilla con
	 * el que se va a interactuar.
	 */
	@GetMapping("/addbodega")
	public String showSignUpForm(Model model) {
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("bodega", new Bodega());
		/*
		 * Se le agrega a la plantilla un modelo de rol usuario vendedor ya que
		 * requerimos de el para poder hacer una inserción.
		 */
		model.addAttribute("usuarios", iUsuarioRepo.findByRoleVendedor());
		/*
		 * Se le agrega a la plantilla un modelo de paises ya que requerimos de ellos
		 * para poder hacer una inserción.
		 */
		model.addAttribute("paises", iPaiRepo.findAll());
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("municipios", new Municipio());
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("departamentos", new Departamento());
		/* Se retorna la plantilla o formulario html para el registro de una bodega */
		return "add-bodega";
	}
	@GetMapping("/addbodega/{email}")
	public String agregarBodegaVendedor(Model model, @PathVariable("email")String email) {
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		Bodega bodega = new Bodega();
		bodega.setUsuario(iUsuarioRepo.findByEmail(email).get());
		model.addAttribute("bodega", bodega);
		
		/*
		 * Se le agrega a la plantilla un modelo de paises ya que requerimos de ellos
		 * para poder hacer una inserción.
		 */
		model.addAttribute("paises", iPaiRepo.findAll());
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("municipios", new Municipio());
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("departamentos", new Departamento());
		/* Se retorna la plantilla o formulario html para el registro de una bodega */
		return "add-bodega";
	}

	/*
	 * Se reciben y se validan todos los datos del formulario mediante las
	 * anotaciones postMapping y validated, con el blinding result se manejan los
	 * resultados de la inserción de los datos.
	 */
	@PostMapping("/add_bodega")
	public String addProducto(@Validated Bodega bodega, BindingResult result, Model model) {
		/*
		 * Si el result de blinding result encuentra algun error a la hora de insertar
		 * los datos va a retornar al formulario de agregar una bodega, de lo contrario
		 * hace la inserción de los datos en la base de datos y retorna a la lista de
		 * productos.
		 */
		if (result.hasErrors()) {
			model.addAttribute("bodega", new Bodega());
			model.addAttribute("paises", iPaiRepo.findAll());
			model.addAttribute("municipios", new Municipio());
			model.addAttribute("departamentos", new Departamento());
			return "add-bodega";
		}
		/*
		 * Mediante el método .save del repositorio se guardan los datos despues de
		 * pasar todas las validaciones.
		 */
		iBodegaRepo.save(bodega);
		/*
		 * Se cargan todas las bodegas existentes en la base de datos al modelo para
		 * poder listarlas.
		 */
		model.addAttribute("bodegas", iBodegaRepo.findAll());
		return "redirect:/listarBodega";
	}

	/*
	 * En este método se recibe del formulario en donde se listan las bodegas el
	 * id de la bodega a editar, este id se se busca en la base de datos y se carga
	 * su información en el modelo o formulario de modificación de la bodega.
	 */
	@GetMapping("/editBodega/{idBodega}")
	public String showUpdateForm(@PathVariable("idBodega") int idBodega, Model model) {
		/*
		 * En esta parte se crea un bean de tipo bodega y se le asigna el bean de la
		 * busqueda realizada a la base de datos mediante el método del repositorio
		 * findbyid. Si no encuentra la bodega arroja el mensaje de error.
		 */
		Bodega bodega = iBodegaRepo.findById(idBodega).orElseThrow(() -> new IllegalArgumentException("Invalid bodega id:" + idBodega));
		/*Carga en el modelo los datos de la bodega buscada,los vendedores, paise, municipios y departamentos disponibles para poder hacer la modificación.*/
    	model.addAttribute("bodega", bodega);
		model.addAttribute("usuario", iUsuarioRepo.findById(bodega.getUsuario().getDni()));
		model.addAttribute("usuarios", iUsuarioRepo.findByRoleVendedor());
		model.addAttribute("paises", iPaiRepo.findAll());
		model.addAttribute("municipios", new Municipio());
		model.addAttribute("departamentos", new Departamento());
		return "update-bodega";
	}

	
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
	@PostMapping("/updateBodega/{idBodega}")
	public String updateBodega(@PathVariable("idBodega") int idBodega, @Validated Bodega bodega, BindingResult result ,Model model) {
		/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
		if (result.hasErrors()) {
			model.addAttribute("bodega", bodega);
			model.addAttribute("usuario", iUsuarioRepo.findById(bodega.getUsuario().getDni()));
			model.addAttribute("usuarios", iUsuarioRepo.findByRoleVendedor());
			model.addAttribute("paises", iPaiRepo.findAll());
			model.addAttribute("municipios", new Municipio());
			model.addAttribute("departamentos", new Departamento());
			return "update-bodega";
		}
        /* Si se cumple o o la condición se modifica los datos cambiados en dicha bodega. */
		iBodegaRepo.save(bodega);
		/*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de las bodegas*/
		model.addAttribute("bodegas", iBodegaRepo.findAll());
		return "redirect:/listarBodega";
	}

	/*En este método se recibe como parametro de la lista de bodegas el id de la bodega seleccionada*/
	@GetMapping("/deleteBodega/{idBodega}")
	public String deleteProducto(@PathVariable("idBodega") int idBodega, Model model) {
		/*Se instancia un bean tipo bodega y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar la bodega dado el id recibido*/
		Bodega bodega = iBodegaRepo.findById(idBodega).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + idBodega));
		  /* Si encuentra la bodega carga el bean y medianted el método delete del repositorio se envia y se elimina la bodega 
         * buscada anteriormente.*/
		iBodegaRepo.delete(bodega);
		 /* Se carga una lista actualiza de bodegas al modelo y se redirige a la página de listar.*/
		model.addAttribute("bodega", iBodegaRepo.findAll());
		return "redirect:/listarBodega";
	}

	 /*Método encargado de enviar al modelo o plantilla la lista de bodegas existentes en la base de datos.*/
	@GetMapping("/listarBodega")
	public String ListarBodega(Model model) {
		/*Se buscan las bodegas mediante el método del repositorio findbyid y se cargan en la variable 'bodegas' 
    	 * a la plantilla o medelo de la plantilla.*/
		model.addAttribute("bodegas", iBodegaRepo.findAll());
		return "listarBodega";
	}
	@GetMapping("/listarBodegasVendedor/{email}")
	public String ListarBodegasVendedor(Model model, @PathVariable("email") String email) {
		/*Se buscan las bodegas mediante el método del repositorio findbyid y se cargan en la variable 'bodegas' 
    	 * a la plantilla o medelo de la plantilla.*/
		Optional<Usuario> usuario = iUsuarioRepo.findByEmail(email);
		model.addAttribute("bodegas", iBodegaRepo.findByVendedor(usuario.get().getDni()));
		return "listarBodega";
	}

}