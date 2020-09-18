package com.co.web.avanzada.controler;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import com.co.web.avanzada.config.CloudinaryConfig;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.repository.ICategoriaRepo;
import com.co.web.avanzada.repository.IProductoRepo;
import com.co.web.avanzada.repository.IProveedorRepo;

@Controller
@RequestMapping("/admind")
public class ProductoController {
	@Autowired
	private  IProductoRepo iProductoRepo;
	@Autowired
	private  IProveedorRepo iProveedorRepo;
	@Autowired
	private  ICategoriaRepo iCategoriaRepo;
	@Autowired
	private CloudinaryConfig cloudc;
	
    @GetMapping("/addproducto")
    public String showSignUpForm(Model model) {
        model.addAttribute("producto",new Producto());
    	model.addAttribute("proveedor", iProveedorRepo.findAll());
        model.addAttribute("categorias", iCategoriaRepo.findAll());
    	return "add-producto";
    }
    
    @PostMapping("/add_producto")
    public String addProducto(@Validated Producto producto, BindingResult result, Model model,@RequestParam("file") MultipartFile file) {
        if (result.hasErrors()) {
        	model.addAttribute("proveedor", iProveedorRepo.findAll());
            model.addAttribute("categorias", iCategoriaRepo.findAll());
            return "add-producto";
        }
        try {
        	Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            System.out.println(uploadResult.get("url").toString());
            producto.setUrlFoto(uploadResult.get("url").toString());	
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        iProductoRepo.save(producto);
        model.addAttribute("productos", iProductoRepo.findAll());
        return "redirect:/admind/listarProducto";
    }
    
    @GetMapping("/editProducto/{codigoProducto}")
    public String showUpdateForm(@PathVariable("codigoProducto") int codigoProducto, Model model) {
    	Producto producto = iProductoRepo.findById(codigoProducto).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + codigoProducto));
    	model.addAttribute("producto", producto);
    	model.addAttribute("proveedores", iProveedorRepo.findAll());
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "update-producto";
    }
    
    @PostMapping("/updateProducto/{codigoProducto}")
    public String updateProducto(@PathVariable("codigoProducto") int codigoProducto, @Validated Producto producto, BindingResult result, Model model,@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl){
        if (result.hasErrors()) {
            model.addAttribute("proveedores", iProveedorRepo.findAll());
            model.addAttribute("categorias", iCategoriaRepo.findAll());
        	model.addAttribute("producto", producto); 
            return "update-producto";
        }
        /* Si en la vista principal de actualización se cambia la imagen del producto, se setea al objeto producto la nueva url y se imprime el resultado. */
        if (cambioUrl) { 
			try {
	            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
	            System.out.println(uploadResult.get("url").toString());
	            producto.setUrlFoto(uploadResult.get("url").toString());	
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	        }
		}
        /* Si se cumple o o la condición se modifica los datos cambiados en dicho producto. */
        iProductoRepo.save(producto);
        model.addAttribute("productos", iProductoRepo.findAll());
        return "redirect:/admind/listarProducto";
    }
    
    @GetMapping("/deleteProducto/{codigoProducto}")
    public String deleteProducto(@PathVariable("codigoProducto") int codigoProducto, Model model) {
    	Producto producto = iProductoRepo.findById(codigoProducto).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + codigoProducto));
        iProductoRepo.delete(producto);
        model.addAttribute("productos", iProductoRepo.findAll());
        return "redirect:/admind/listarProducto";
    }
    
    @GetMapping("/listarProducto")
    public String ListarDepa(Model model) {
    	model.addAttribute("productos", iProductoRepo.findAll());
        return "listarProducto";
    }
    
}
