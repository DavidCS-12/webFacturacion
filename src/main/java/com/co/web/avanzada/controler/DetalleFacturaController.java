package com.co.web.avanzada.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.co.web.avanzada.entity.DetalleFactura;
import com.co.web.avanzada.repository.IDespachoPedidosRepo;
import com.co.web.avanzada.repository.IDetalleFacturaRepo;

@Controller
public class DetalleFacturaController {

	@Autowired
	private IDetalleFacturaRepo iDetalleFacturaRepo;
	@Autowired
	private IDespachoPedidosRepo iDespachoPedidosRepo;

	@GetMapping("/addDetalleFactura/")
	public String showSignUpForm(Model model) {
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("detalle_factura", new DetalleFactura());
		/*
		 * Se le agrega a la plantilla un modelo de detalle factura ya que requerimos de
		 * ellos para poder hacer una inserción.
		 */
		model.addAttribute("despacho_Pedido", iDespachoPedidosRepo.findAll());
		/*
		 * Se retorna la plantilla o formulario html para el registro del detalle
		 * factura
		 */
		return "add-detalleFactura";
	}

	/*
	 * Se reciben y se validan todos los datos del formulario mediante las
	 * anotaciones postMapping y validated, con el blinding result se manejan los
	 * resultados de la inserción de los datos.
	 */
	@PostMapping("/add_detalleFactura")
	public String addDetalleFactura(@Validated DetalleFactura detalleFactura, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file) {
		/*
		 * Si el result de blinding result encuentra algun error a la hora de insertar
		 * los datos va a retornar al formulario de agregar un detalle factura, de lo
		 * contrario hace la inserción de los datos en la base de datos y retorna a la
		 * lista de detalle facturas.
		 */
		if (result.hasErrors()) {
			model.addAttribute("detalle_facura", iDetalleFacturaRepo.findAll());
			model.addAttribute("despacho_Pedido", iDespachoPedidosRepo.findAll());
			return "add-detalleFactura";
		}
		/*
		 * Mediante el método .save del repositorio se guardan los datos despues de
		 * pasar todas las validaciones.
		 */
		iDetalleFacturaRepo.save(detalleFactura);
		/*
		 * Se cargan todas los productos existentes en la base de datos al modelo para
		 * poder listarlas.
		 */
		model.addAttribute("detalle_factura", iDetalleFacturaRepo.findAll());
		return "redirect:/listarDetalleFactura";
	}

	/*
	 * En este método se recibe del formulario en donde se listan los productos el
	 * id del producto a editar, este id se se busca en la base de datos y se carga
	 * su información en el modelo o formulario de modificación del producto.
	 */
	@GetMapping("/editDetalleFactura/{idDetalleFactura}")
	public String showUpdateForm(@PathVariable("idDetalleFactura") int idDetalleFactura, Model model) {
		/*
		 * En esta parte se crea un bean de tipo producto y se le asigna el bean de la
		 * busqueda realizada a la base de datos mediante el método del repositorio
		 * findbyid. Si no encuentra el producto arroja el mensaje de error.
		 */
		DetalleFactura detalleFactura = iDetalleFacturaRepo.findById(idDetalleFactura)
				.orElseThrow(() -> new IllegalArgumentException("Invalid detalle_factura id:" + idDetalleFactura));
		/*
		 * Carga en el modelo los datos del producto buscado,los proveedores y
		 * categorias disponibles para poder hacer la modificación.
		 */
		model.addAttribute("detalle_factura", detalleFactura);
		model.addAttribute("despacho_Pedido", iDespachoPedidosRepo.findAll());
		return "update-detalleFactura";
	}

	/*
	 * Recibe los nuevos datos ingresados , valida que no falte ningún atributo y
	 * que todos sean los necesarios y realiza la modificación
	 */
	@PostMapping("/updateDetalleFactura/{idDetalleFactura}")
	public String updateDetalleFactura(@PathVariable("idDetalleFactura") int idDetalleFactura,
			@Validated DetalleFactura detalleFactura, BindingResult result, Model model,
			@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl) {
		/*
		 * Si se encuentra algún error a la hora de hacer la inserción de los nuevos
		 * datos va a retornar al formulario de modificación.
		 */
		if (result.hasErrors()) {
			model.addAttribute("detalle_factura", iDetalleFacturaRepo.findAll());
			model.addAttribute("despacho_Pedido", iDespachoPedidosRepo.findAll());
			return "update-detalleFactura";
		}

		/*
		 * Si se cumple o o la condición se modifica los datos cambiados en dicho
		 * producto.
		 */
		iDetalleFacturaRepo.save(detalleFactura);
		/*
		 * Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista
		 * de productos
		 */
		model.addAttribute("detalle_factura", iDetalleFacturaRepo.findAll());
		return "redirect:/listarDetalleFactura";
	}

	/*
	 * En este método se recibe como parametro de la lista de productos el id del
	 * producto seleccionado
	 */
	@GetMapping("/deleteDetalleFactura/{idDetalleFactura}")
	public String deleteDetalleFactura(@PathVariable("idDetalleFactura") int idDetalleFactura, Model model) {
		/*
		 * Se instancia un bean tipo producto y se le asigna los valores obtenidos por
		 * el método del repositorio findById el cual va a buscar el producto dado el id
		 * recibido
		 */
		DetalleFactura detalleFactura = iDetalleFacturaRepo.findById(idDetalleFactura)
				.orElseThrow(() -> new IllegalArgumentException("Invalid detalle_factura id:" + idDetalleFactura));
		/*
		 * Si encuentra el producto carga el bean y medianted el método delete del
		 * repositorio se envia y se elimina el producto buscado anteriormente.
		 */
		iDetalleFacturaRepo.delete(detalleFactura);
		/*
		 * Se carga una lista actualiza de productos al modelo y se redirige a la página
		 * de listar.
		 */
		model.addAttribute("detalle_factura", iDetalleFacturaRepo.findAll());
		return "redirect:/listarDetalleFactura";
	}

	/*
	 * Método encargado de enviar al modelo o plantilla la lista de productos
	 * existentes en la base de datos.
	 */
	@GetMapping("/listarDetalleFactura")
	public String ListarDetalleFactura(Model model) {
		/*
		 * Se buscan los productos mediante el método del repositorio findbyid y se
		 * cargan en la variable 'productos' a la plantilla o medelo de la plantilla.
		 */
		model.addAttribute("detalle_factura", iDetalleFacturaRepo.findAll());
		return "listarDetalleFactura";
	}

}