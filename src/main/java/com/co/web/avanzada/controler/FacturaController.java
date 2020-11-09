package com.co.web.avanzada.controler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.co.web.avanzada.entity.DespachoPedido;
import com.co.web.avanzada.entity.Factura;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IDespachoPedidosRepo;
import com.co.web.avanzada.repository.IDetalleRepo;
import com.co.web.avanzada.repository.IFacturaRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;

@Controller
public class FacturaController {
	
	@Autowired
	private IUsuarioRepo iUsuarioRepo;
	
	@Autowired
	private IFacturaRepo iFacturaRepo;
	
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
	public String addProducto(@Validated Factura factura, BindingResult result, Model model) {
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
	@GetMapping("/listarFacturas/{email}")
	public String ListarFacturas(Model model, @PathVariable("email") String email) {
		Usuario vendedor = iUsuarioRepo.findByEmail(email).get(); 
		model.addAttribute("facturas", iFacturaRepo.findByVendedor(vendedor.getDni()));
		return "listar-facturas";
	}
	
}
