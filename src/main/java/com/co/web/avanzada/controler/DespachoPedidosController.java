package com.co.web.avanzada.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.DespachoPedido;
import com.co.web.avanzada.repository.IDespachoPedidosRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;


@Controller
public class DespachoPedidosController {

	@Autowired
	private IUsuarioRepo iUsuarioRepo;
	@Autowired
	private IDespachoPedidosRepo iDespachoPedidosRepo;
	
    @GetMapping("/addDespacho/{email}")
    public String showSignUpForm(Model model, @PathVariable("email")String email) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
        model.addAttribute("despacho",new DespachoPedido());
        /*Se le agrega a la plantilla un modelo de proveedores ya que requerimos de ellos para poder hacer una inserción.*/
    	model.addAttribute("cliente", iUsuarioRepo.findByRoleCliente());
    	 /*Se le agrega a la plantilla un modelo de categorias ya que requerimos de ellos para poder hacer una inserción.*/
        model.addAttribute("vendedor", iUsuarioRepo.findByEmail(email).get());
        /*Se retorna la plantilla o formulario html para el registro del producto*/
    	return "add-despacho";
    }
    
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/    
    @PostMapping("/add_despacho")
    public String addDespachoPedido(@Validated DespachoPedido despachoPedido, BindingResult result, Model model) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar productos, de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de productos.*/
    	
        if(result.hasErrors()) {
        	model.addAttribute("despacho", new DespachoPedido());
    		model.addAttribute("cliente", iUsuarioRepo.findByRoleCliente());
    		model.addAttribute("vendedor", iUsuarioRepo.findByEmail(despachoPedido.getVendedor().getEmail()).get());
        	return "redirect:/addDespacho/"+despachoPedido.getIdDespacho();
        
        }
        
        despachoPedido.setEstado(false);
        iDespachoPedidosRepo.save(despachoPedido);
        int idDespacho = 0;
        for(int i = 0 ; i<iDespachoPedidosRepo.Listar().size();i++) {
        	if(iDespachoPedidosRepo.Listar().get(i).getCliente().equals(despachoPedido.getCliente()) 
        			&& iDespachoPedidosRepo.Listar().get(i).getVendedor().equals(despachoPedido.getVendedor()) 
        			&& !iDespachoPedidosRepo.Listar().get(i).isEstado()) {
        		idDespacho = iDespachoPedidosRepo.Listar().get(i).getIdDespacho();
        	}
        }
        return "redirect:/addFactura/"+idDespacho;    
    	/*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        
    }
    
    
    /*En este método se recibe del formulario en donde se listan los productos el id del producto a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del producto.*/
    @GetMapping("/editDespacho/{idDespacho}")
    public String showUpdateForm(@PathVariable("idDespacho") int idDespacho, Model model) {
    	/*En esta parte se crea un bean de tipo producto y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el producto arroja el
    	 * mensaje de error.*/
    	DespachoPedido despachoPedido = iDespachoPedidosRepo.findById(idDespacho).orElseThrow(() -> new IllegalArgumentException("Invalid despacho_pedido id:" + idDespacho));
    	/*Carga en el modelo los datos del producto buscado,los proveedores y categorias disponibles para poder hacer la modificación.*/
    	model.addAttribute("despacho", despachoPedido);
    	model.addAttribute("vendedor", despachoPedido.getVendedor());
        model.addAttribute("cliente", iUsuarioRepo.findByRoleCliente());
        return "update-despacho";
    }
    
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updateDespacho/{idDespacho}")
    public String updateDespachoPedido(@PathVariable("idDespacho") int idDespacho, @Validated DespachoPedido despachoPedido, BindingResult result, Model model){
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
    	if (result.hasErrors()) {
            model.addAttribute("despacho", iDespachoPedidosRepo.findById(idDespacho));
            model.addAttribute("vendedor", despachoPedido.getVendedor());
            model.addAttribute("cliente", iUsuarioRepo.findByRoleCliente());
            return "redirect:/editDespacho/"+idDespacho;
        }
       
        /* Si se cumple o o la condición se modifica los datos cambiados en dicho producto. */
    	iDespachoPedidosRepo.save(despachoPedido);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de productos*/
        model.addAttribute("despacho_pedido", iDespachoPedidosRepo.findAll());
        return "redirect:/listarFacturas/"+despachoPedido.getVendedor().getEmail();
    }
    
    /*En este método se recibe como parametro de la lista de productos el id del producto seleccionado*/
    @GetMapping("/deleteDespacho/{idDespacho}")
    public String deleteDespachoPedido(@PathVariable("idDespacho") int idDespacho, Model model) {
    	/*Se instancia un bean tipo producto y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el producto dado el id recibido*/
    	DespachoPedido despachoPedido = iDespachoPedidosRepo.findById(idDespacho).orElseThrow(() -> new IllegalArgumentException("Invalid despacho_pedido id:" + idDespacho));
    	  /* Si encuentra el producto carga el bean y medianted el método delete del repositorio se envia y se elimina el producto 
         * buscado anteriormente.*/
    	iDespachoPedidosRepo.delete(despachoPedido);
    	  /* Se carga una lista actualiza de productos al modelo y se redirige a la página de listar.*/
        model.addAttribute("despacho_Pedido", iDespachoPedidosRepo.findAll());
        return "redirect:/listarFacturas/"+despachoPedido.getVendedor().getEmail();
    }
    
    /*Método encargado de enviar al modelo o plantilla la lista de productos existentes en la base de datos.*/
    @GetMapping("/listarDespachos")
    public String ListarDespacho(Model model) {
    	/*Se buscan los productos mediante el método del repositorio findbyid y se cargan en la variable 'productos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	model.addAttribute("despachos", iDespachoPedidosRepo.findAll());
        return "listarDespacho";
    }
  

   }