package com.co.web.avanzada.controler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.co.web.avanzada.entity.DespachoPedido;
import com.co.web.avanzada.entity.DetalleFactura;
import com.co.web.avanzada.entity.Factura;
import com.co.web.avanzada.entity.Inventario;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IDespachoPedidosRepo;
import com.co.web.avanzada.repository.IDetalleRepo;
import com.co.web.avanzada.repository.IFacturaRepo;
import com.co.web.avanzada.repository.IInventarioRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;

@Controller
public class FacturaController {
	
	@Autowired
	private IUsuarioRepo iUsuarioRepo;
	
	@Autowired
	private IFacturaRepo iFacturaRepo;
	@Autowired
	private IInventarioRepo iInventarioRepo;
	@Autowired
	private IDespachoPedidosRepo iDespachoPedidosRepo;
	
	@Autowired IDetalleRepo iDetalleRepo;
	
	@GetMapping("/addFactura/{idDespacho}")
	public String addFactura(Model model, @PathVariable("idDespacho")int idDespacho) {
		model.addAttribute("despacho", iDespachoPedidosRepo.findById(idDespacho).get());
		model.addAttribute("factura" , new Factura());
		return "add-factura";
	} 
	@PostMapping("/add_factura")
	public String addFactura(@Validated Factura factura, BindingResult result, Model model) {
		/*
		 * Si el result de blinding result encuentra algun error a la hora de insertar
		 * los datos va a retornar al formulario de agregar una bodega, de lo contrario
		 * hace la inserción de los datos en la base de datos y retorna a la lista de
		 * productos.
		 */
		if (result.hasErrors()) {
			model.addAttribute("despacho", new DespachoPedido());
			model.addAttribute("factura" , new Factura());
			return "add-factura";
		}
		java.util.Date fecha = new Date();
		System.out.println (fecha);
		factura.setFechaVenta(fecha);
		factura.getDespachoPedido().setEstado(false);
		factura.setValorCompra(calcularValorSinIva(factura.getIdFactura()));
		factura.setValorCompra(calcularValorIva(factura.getIdFactura()));
		iDespachoPedidosRepo.save(factura.getDespachoPedido());
		iFacturaRepo.save(factura);
		
		model.addAttribute("facturas", iFacturaRepo.findByVendedor(factura.getDespachoPedido().getVendedor().getDni()));
		return "redirect:/listarFacturas/"+factura.getDespachoPedido().getVendedor().getEmail();
	}
	private double calcularValorIva(int id) {
		int valor = 0;
		if(!iDetalleRepo.detalleFactura(id).isEmpty()) {
			for(int i=0; i<iDetalleRepo.detalleFactura(id).size();i++) {
				valor += iDetalleRepo.detalleFactura(id).get(i).getProducto().getPrecioVenta()*iDetalleRepo.detalleFactura(id).get(i).getCantidad();
			}
			return valor;
		}
		return valor;
	}
	private double calcularValorSinIva(int id) {
		int valor = 0;
		if(!iDetalleRepo.detalleFactura(id).isEmpty()) {
			for(int i=0; i<iDetalleRepo.detalleFactura(id).size();i++) {
				valor += iDetalleRepo.detalleFactura(id).get(i).getProducto().getPrecioCompra()*iDetalleRepo.detalleFactura(id).get(i).getCantidad();
			}
			return valor;
		}
		return valor;
	}
	
	@GetMapping("/editFactura/{idFactura}")
	public String editFactura(@PathVariable("idFactura")int idFactura, Model model){
		model.addAttribute("factura", iFacturaRepo.findById(idFactura).get());
		model.addAttribute("detalles", iDetalleRepo.detalleFactura(idFactura));
		return "mostrar-factura";
	}
	
	@GetMapping("/deleteFactura/{idFactura}")
	public String deleteFactura(@PathVariable("idFactura")int idFactura, Model model) {
		Factura factura = iFacturaRepo.findById(idFactura).get();
		List<DetalleFactura> productosFactura = iDetalleRepo.detalleFactura(idFactura);
		Usuario vendedor = factura.getDespachoPedido().getVendedor();
		Usuario cliente = factura.getDespachoPedido().getCliente();
		List<Inventario> inventarios = new ArrayList<>();
		int nuevacantidad=0;
		if(!factura.getDespachoPedido().isEstado()) {	
			if(vendedor.getRol().equals("ADMIN")){
				inventarios = (List<Inventario>) iInventarioRepo.findAll();
			}
			if(vendedor.getRol().equals("VENDEDOR")) {
				inventarios = iInventarioRepo.findByBodegaVendedor(vendedor.getDni());
			}
			
			if(!productosFactura.isEmpty()) {
				for(int i=0; i< productosFactura.size();i++) {
					for(int j=0;j<inventarios.size();j++) {
						if(inventarios.get(j).getProducto()==productosFactura.get(i).getProducto()) {
							nuevacantidad = inventarios.get(j).getCantidad()+productosFactura.get(i).getCantidad();
							inventarios.get(j).setCantidad(nuevacantidad);
							iInventarioRepo.save(inventarios.get(j));
							break;
						}
					}
				}
			}
			
		}
		
		iDespachoPedidosRepo.delete(factura.getDespachoPedido());
		String email = "";
		if(vendedor.getRol().equals("ADMIN")) {
			email = cliente.getEmail();
		}else {
			email = vendedor.getEmail();
		}
		return "redirect:/listarFacturas/"+email;
		
	}
	
	@GetMapping("/listarFacturas/{email}")
	public String ListarFacturas(Model model, @PathVariable("email") String email) {
		Usuario usuario = iUsuarioRepo.findByEmail(email).get();
		if(usuario.getRol().equals("VENDEDOR")) {
			model.addAttribute("facturas", iFacturaRepo.findByVendedor(usuario.getDni()));
		}
		if(usuario.getRol().equals("CLIENTE")) {
			model.addAttribute("facturas", iFacturaRepo.findByCliente(usuario.getDni()));
		}
		return "listar-facturas";
	}
	
	@GetMapping("/terminarCompra/{idFactura}")
	public String imprimirFactura(@PathVariable("idFactura")int idFactura, Model model) {
		Factura factura = iFacturaRepo.findById(idFactura).get();
		DespachoPedido despacho = factura.getDespachoPedido();
		despacho.setEstado(true);
		iDespachoPedidosRepo.save(despacho);
		return "redirect:/editFactura/"+idFactura;
	}
}
