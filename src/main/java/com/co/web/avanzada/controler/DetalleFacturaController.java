package com.co.web.avanzada.controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.DetalleFactura;
import com.co.web.avanzada.entity.Factura;
import com.co.web.avanzada.entity.Inventario;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.repository.IDetalleFacturaRepo;
import com.co.web.avanzada.repository.IFacturaRepo;
import com.co.web.avanzada.repository.IInventarioRepo;

@Controller
public class DetalleFacturaController {

	@Autowired
	private IDetalleFacturaRepo iDetalleFacturaRepo;
	@Autowired
	private IFacturaRepo iFacturaRepo;
	@Autowired
	private IInventarioRepo iInventarioRepo;
	
	
	@GetMapping("/addDetalle/{idFactura}")
	public String showSignUpForm(Model model, @PathVariable("idFactura")int idFactura) {
		Factura factura = iFacturaRepo.findById(idFactura).get();
		
		/*
		 * Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una
		 * inserción
		 */
		model.addAttribute("detalle", new DetalleFactura());
		/*
		 * Se le agrega a la plantilla un modelo de detalle factura ya que requerimos de
		 * ellos para poder hacer una inserción.
		 */
		model.addAttribute("factura", factura);
		
		model.addAttribute("productos", iInventarioRepo.findByBodegaVendedor(factura.getDespachoPedido().getVendedor().getDni()));
		/*
		 * Se retorna la plantilla o formulario html para el registro del detalle
		 * factura
		 */
		return "add-detalle";
	}

	/*
	 * Se reciben y se validan todos los datos del formulario mediante las
	 * anotaciones postMapping y validated, con el blinding result se manejan los
	 * resultados de la inserción de los datos.
	 */
	@PostMapping("/add_detalle")
	public String addDetalleFactura(@Validated DetalleFactura detalleFactura, BindingResult result, Model model) {
		/*
		 * Si el result de blinding result encuentra algun error a la hora de insertar
		 * los datos va a retornar al formulario de agregar un detalle factura, de lo
		 * contrario hace la inserción de los datos en la base de datos y retorna a la
		 * lista de detalle facturas.
		 */
		Factura factura = detalleFactura.getFactura();
		boolean respuesta = false;
		List<Inventario> inventarios = iInventarioRepo.findByBodegaVendedor(factura.getDespachoPedido().getVendedor().getDni());
		int cantidad=0;
		for(int i=0;i<inventarios.size();i++) {
			if(inventarios.get(i).getProducto()==detalleFactura.getProducto() && detalleFactura.getCantidad()<=inventarios.get(i).getCantidad()) {
				cantidad = inventarios.get(i).getCantidad()-detalleFactura.getCantidad();
				inventarios.get(i).setCantidad(cantidad);
				iInventarioRepo.save(inventarios.get(i));
				respuesta = true;
				break;
			}
		}
		
		if (result.hasErrors()) {
			model.addAttribute("detalle", new DetalleFactura());
			model.addAttribute("factura", factura.getIdFactura());
			model.addAttribute("productos", inventarios);
			return "redirect:/addDetalle/"+factura.getIdFactura();
		}
		if(respuesta==false) {
			model.addAttribute("detalle", new DetalleFactura());
			model.addAttribute("factura", factura.getIdFactura());
			model.addAttribute("productos", inventarios);
			return "redirect:/addDetalle/"+factura.getIdFactura();
		}
		
		/*
		 * Mediante el método .save del repositorio se guardan los datos despues de
		 * pasar todas las validaciones.
		 */
		iDetalleFacturaRepo.save(detalleFactura);
		double precioSinIva=detalleFactura.getCantidad()*detalleFactura.getProducto().getPrecioCompra();
		double precioIva=detalleFactura.getCantidad()*detalleFactura.getProducto().getPrecioVenta();
		double valorSinIva = factura.getValorCompra()+precioSinIva; 
		double valorIva = factura.getValorCompraIva()+precioIva;
		factura.setValorCompra(valorSinIva);
		factura.setValorCompraIva(valorIva);
		iFacturaRepo.save(factura);
		
		return "redirect:/editFactura/"+detalleFactura.getFactura().getIdFactura();
	}

	/*
	 * En este método se recibe del formulario en donde se listan los productos el
	 * id del producto a editar, este id se se busca en la base de datos y se carga
	 * su información en el modelo o formulario de modificación del producto.
	 */
	@GetMapping("/editDetalle/{idDetalle}")
	public String showUpdateForm(@PathVariable("idDetalle") int idDetalleFactura, Model model) {
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
		model.addAttribute("detalle", detalleFactura);
		return "update-detalle";
	}

	/*
	 * Recibe los nuevos datos ingresados , valida que no falte ningún atributo y
	 * que todos sean los necesarios y realiza la modificación
	 */
	@PostMapping("/updateDetalle/{idDetalle}")
	public String updateDetalleFactura(@PathVariable("idDetalle")int idDetalle,@Validated DetalleFactura detalleFactura, BindingResult result, Model model) {
		
		/*
		 * Si se encuentra algún error a la hora de hacer la inserción de los nuevos
		 * datos va a retornar al formulario de modificación.
		 */
		if (result.hasErrors()) {
			model.addAttribute("detalle", detalleFactura);
			model.addAttribute("productos", iInventarioRepo.findByBodegaVendedor(idDetalle));
			return "update-detalle";
		}
		
		Factura factura = detalleFactura.getFactura();
		Producto producto = detalleFactura.getProducto();
		List<Inventario> inventarios = iInventarioRepo.findByBodegaVendedor(factura.getDespachoPedido().getVendedor().getDni());
		System.out.println(detalleFactura.getIdDetalle());
		DetalleFactura detalleAnterior = iDetalleFacturaRepo.findById(idDetalle).get();
		int nuevaCantidad = 0;
		//Se actualizan las cantidades en el inventario de donde se estan solicitando los productos
		for(int i=0; i<inventarios.size();i++) {
			if(producto == inventarios.get(i).getProducto()) {
				if(detalleAnterior.getCantidad()>detalleFactura.getCantidad()) {
					nuevaCantidad=inventarios.get(i).getCantidad()+(detalleAnterior.getCantidad()-detalleFactura.getCantidad());
					inventarios.get(i).setCantidad(nuevaCantidad);
				}
				if(detalleAnterior.getCantidad()<detalleFactura.getCantidad()) {
					nuevaCantidad = inventarios.get(i).getCantidad()-(detalleFactura.getCantidad()-detalleAnterior.getCantidad());
					inventarios.get(i).setCantidad(nuevaCantidad);
				}
				iInventarioRepo.save(inventarios.get(i));
				break;
			}
		}
		
		
		/*
		 * Si se cumple o o la condición se modifica los datos cambiados en dicho
		 * producto.
		 */
		
		//Se actualiza los totales en la factura actual
		System.out.println(factura.getValorCompra()+"    "+factura.getValorCompraIva()+"  "+detalleAnterior.getCantidad());
		double valorSinIva = (factura.getValorCompra()-(detalleAnterior.getCantidad()*detalleAnterior.getProducto().getPrecioCompra()))+(detalleFactura.getCantidad()*detalleFactura.getProducto().getPrecioCompra());
		double valorIva = (factura.getValorCompraIva()-(detalleAnterior.getCantidad()*detalleAnterior.getProducto().getPrecioVenta()))+(detalleFactura.getCantidad()*detalleFactura.getProducto().getPrecioVenta());
		System.out.println(valorSinIva+"   "+valorIva+"  "+detalleFactura.getCantidad());
		factura.setValorCompra(valorSinIva);
		factura.setValorCompraIva(valorIva);
		iFacturaRepo.save(factura);
		iDetalleFacturaRepo.save(detalleFactura);
		/*
		 * Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista
		 * de productos
		 */
		model.addAttribute("detalle", detalleFactura);
		model.addAttribute("productos", iInventarioRepo.findByBodegaVendedor(detalleFactura.getFactura().getDespachoPedido().getVendedor().getDni()));
		return "redirect:/editFactura/"+detalleFactura.getFactura().getIdFactura();
	}

	/*
	 * En este método se recibe como parametro de la lista de productos el id del
	 * producto seleccionado
	 */
	@GetMapping("/deleteDetalle/{idDetalle}")
	public String deleteDetalleFactura(@PathVariable("idDetalle") int idDetalleFactura, Model model) {
		/*
		 * Se instancia un bean tipo producto y se le asigna los valores obtenidos por
		 * el método del repositorio findById el cual va a buscar el producto dado el id
		 * recibido
		 */
		DetalleFactura detalleFactura = iDetalleFacturaRepo.findById(idDetalleFactura)
				.orElseThrow(() -> new IllegalArgumentException("Invalid detalle_factura id:" + idDetalleFactura));
		
		Factura factura = iFacturaRepo.findById(detalleFactura.getFactura().getIdFactura()).get();
		int cantidad = detalleFactura.getCantidad();
		List<Inventario> inventarios = iInventarioRepo.findByBodegaVendedor(detalleFactura.getFactura().getDespachoPedido().getVendedor().getDni());
		for(int i=0; i<inventarios.size();i++) {
			if(detalleFactura.getProducto()==inventarios.get(i).getProducto()) {
				//Se cambia el valor de la compra en la factura
				double valorRestar = factura.getValorCompra()-(detalleFactura.getProducto().getPrecioCompra()*detalleFactura.getCantidad());
				double valorRestarIva = factura.getValorCompraIva()-(detalleFactura.getProducto().getPrecioVenta()*detalleFactura.getCantidad());
				factura.setValorCompra(valorRestar);
				factura.setValorCompraIva(valorRestarIva);
				iFacturaRepo.save(factura);
				//Se cambia la cantidad de productos en el inventario
				int cantidadInventario = inventarios.get(i).getCantidad()+cantidad;
				inventarios.get(i).setCantidad(cantidadInventario);
				iInventarioRepo.save(inventarios.get(i));
				break;
			}
		}
		/*
		 * Si encuentra el producto carga el bean y medianted el método delete del
		 * repositorio se envia y se elimina el producto buscado anteriormente.
		 */
		iDetalleFacturaRepo.delete(detalleFactura);
		/*
		 * Se carga una lista actualiza de productos al modelo y se redirige a la página
		 * de listar.
		 */
		model.addAttribute("detalle", detalleFactura);
		model.addAttribute("productos", iInventarioRepo.findByBodegaVendedor(detalleFactura.getFactura().getDespachoPedido().getVendedor().getDni()));
		return "redirect:/editFactura/"+detalleFactura.getFactura().getIdFactura();
	}

	/*
	 * Método encargado de enviar al modelo o plantilla la lista de productos
	 * existentes en la base de datos.
	 */
	@GetMapping("/listarDetalles")
	public String ListarDetalleFactura(Model model) {
		/*
		 * Se buscan los productos mediante el método del repositorio findbyid y se
		 * cargan en la variable 'productos' a la plantilla o medelo de la plantilla.
		 */
		model.addAttribute("detalle_factura", iDetalleFacturaRepo.findAll());
		return "listarDetalleFactura";
	}

}