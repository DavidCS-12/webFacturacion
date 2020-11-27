package com.co.web.avanzada.controler;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.ObjectUtils;
import com.co.web.avanzada.config.CloudinaryConfig;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.repository.ICategoriaRepo;
import com.co.web.avanzada.repository.IProductoRepo;
import com.co.web.avanzada.repository.IProveedorRepo;

@Controller
public class ProductoController {
	/*Con esta anotación se crea la instancia del been cada vez que el sistema lo requiera.*/
	/*se instancian los repositorios, en este caso de productoRepo, proveedorRepo y categoriaRepo, para poder hacer uso de las funciones del repositorio, 
	 * las cuales son la iteración entre la información obtenida en el sistema y la base de datos.*/
	
	@Autowired
	private  IProductoRepo iProductoRepo;
	@Autowired
	private  IProveedorRepo iProveedorRepo;
	@Autowired
	private  ICategoriaRepo iCategoriaRepo;
	@Autowired
	private CloudinaryConfig cloudc;
	
	
	
	 /*Método que mapea el formulario necesario para hacer el registro de un producto, el cuál recibe como parametro el modelo a cargar
		 * en la plantilla con el que se va a interactuar.*/
    @GetMapping("/addproducto")
    public String showSignUpForm(Model model) {
    	/*Se le agrega a la plantilla un modelo vacio del been al que vamos a hacer una inserción*/
        model.addAttribute("producto",new Producto());
        /*Se le agrega a la plantilla un modelo de proveedores ya que requerimos de ellos para poder hacer una inserción.*/
    	model.addAttribute("proveedor", iProveedorRepo.findAll());
    	 /*Se le agrega a la plantilla un modelo de categorias ya que requerimos de ellos para poder hacer una inserción.*/
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        /*Se retorna la plantilla o formulario html para el registro del producto*/
    	return "add-producto";
    }
    
    
    /*Se reciben y se validan todos los datos del formulario mediante las anotaciones postMapping y validated, con el blinding result se manejan los resultados
     * de la inserción de los datos.*/    
    @PostMapping("/add_producto")
    public String addProducto(@Validated Producto producto, BindingResult result, Model model,@RequestParam("file") MultipartFile file) {
    	/* Si el result de blinding result encuentra algun error a la hora de insertar los datos va  
    	 * a retornar al formulario de agregar productos, de lo contrario hace la inserción de los datos 
    	 * en la base de datos y retorna a la lista de productos.*/
        if (result.hasErrors()) {
        	model.addAttribute("proveedor", iProveedorRepo.findAll());
            model.addAttribute("categorias", iCategoriaRepo.findAll());
            return "add-producto";
        }
        try {
        	Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            System.out.println(uploadResult.get("url").toString());
            producto.setPrecioVenta((producto.getPrecioCompra()*0.19)+producto.getPrecioCompra());
            producto.setUrlFoto(uploadResult.get("url").toString());	
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    	/*Mediante el método .save del repositorio se guardan los datos despues de pasar todas las validaciones.*/
        iProductoRepo.save(producto);
        /* Se cargan todas los productos existentes en la base de datos al modelo para poder listarlas.*/
        model.addAttribute("productos", iProductoRepo.findAll());
        return "redirect:/listarProducto";
    }
    
    
    /*En este método se recibe del formulario en donde se listan los productos el id del producto a editar, este id se
     * se busca en la base de datos y se carga su información en el modelo o formulario de modificación del producto.*/
    @GetMapping("/editProducto/{codigoProducto}")
    public String showUpdateForm(@PathVariable("codigoProducto") int codigoProducto, Model model) {
    	/*En esta parte se crea un bean de tipo producto y se le asigna el bean de la busqueda realizada a la base de datos
    	 * mediante el método del repositorio findbyid. Si no encuentra el producto arroja el
    	 * mensaje de error.*/
    	Producto producto = iProductoRepo.findById(codigoProducto).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + codigoProducto));
    	/*Carga en el modelo los datos del producto buscado,los proveedores y categorias disponibles para poder hacer la modificación.*/
    	model.addAttribute("producto", producto);
    	model.addAttribute("proveedores", iProveedorRepo.findAll());
        model.addAttribute("categorias", iCategoriaRepo.findAll());
        return "update-producto";
    }
    
    /*Recibe los nuevos datos ingresados , valida que no falte ningún atributo y que todos sean los necesarios y realiza la modificación*/
    @PostMapping("/updateProducto/{codigoProducto}")
    public String updateProducto(@PathVariable("codigoProducto") int codigoProducto, @Validated Producto producto, BindingResult result, Model model,@RequestParam("file") MultipartFile file, @RequestParam("cambioUrl") boolean cambioUrl){
    	/* Si se encuentra algún error a la hora de hacer la inserción de los nuevos datos va a retornar al formulario 
         * de modificación.*/
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
	            producto.setPrecioVenta((producto.getPrecioCompra()*0.19)+producto.getPrecioCompra());
	            producto.setUrlFoto(uploadResult.get("url").toString());	
	        } catch (Exception e) {
	        	System.out.println(e.getMessage());
	        }
		}
        /* Si se cumple o o la condición se modifica los datos cambiados en dicho producto. */
        iProductoRepo.save(producto);
        /*Cargara los nuevos datos al modelo para que estos puedan aparecer en la lista de productos*/
        model.addAttribute("productos", iProductoRepo.findAll());
        return "redirect:/listarProducto";
    }
    
    /*En este método se recibe como parametro de la lista de productos el id del producto seleccionado*/
    @GetMapping("/deleteProducto/{codigoProducto}")
    public String deleteProducto(@PathVariable("codigoProducto") int codigoProducto, Model model) {
    	/*Se instancia un bean tipo producto y se le asigna los valores obtenidos por el método del repositorio
    	 * findById el cual va a buscar el producto dado el id recibido*/
    	Producto producto = iProductoRepo.findById(codigoProducto).orElseThrow(() -> new IllegalArgumentException("Invalid departamento id:" + codigoProducto));
    	  /* Si encuentra el producto carga el bean y medianted el método delete del repositorio se envia y se elimina el producto 
         * buscado anteriormente.*/
    	iProductoRepo.delete(producto);
    	  /* Se carga una lista actualiza de productos al modelo y se redirige a la página de listar.*/
        model.addAttribute("productos", iProductoRepo.findAll());
        return "redirect:/listarProducto";
    }
    
    /*Método encargado de enviar al modelo o plantilla la lista de productos existentes en la base de datos.*/
    @GetMapping("/listarProducto")
    public String ListarProductos(@RequestParam Map<String, Object> params,Model model) {
    	
    	int page = params.get("page") != null ? Integer.valueOf(params.get("page").toString())-1 : 0;
    	PageRequest pageRequest = PageRequest.of(page, 5);
    	Page<Producto> pageProducto = iProductoRepo.findAll(pageRequest);
    	int totalPage = pageProducto.getTotalPages();
    	List<Integer> pages = null;
    	if(totalPage>0) {
    		pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
    	}
    	/*Se buscan los productos mediante el método del repositorio findbyid y se cargan en la variable 'productos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	model.addAttribute("productos", pageProducto.getContent());
    	model.addAttribute("pages", pages);
    	//model.addAttribute("productos", iProductoRepo.findAll());
        return "listarProducto";
    }
    /*Método encargado de enviar al modelo o plantilla la lista de productos existentes en la base de datos.*/
    @GetMapping("/ajax/listarProductoMunicipio")
    public String ListarProductosMunicipio(Model model , @RequestParam("idMunicipio") String idCategoria) {
    	/*Se buscan los productos mediante el método del repositorio findbyid y se cargan en la variable 'productos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	int id = Integer.parseInt(idCategoria);
		model.addAttribute("productos", iProductoRepo.findByMunicipio(id));
		return "index :: categorias";
    }
    @GetMapping("/ajax/listarProductoCategoria")
    public String ListarProductosCategoría(Model model, @RequestParam("idCategoria") String idCategoria) {
    	/*Se buscan los productos mediante el método del repositorio findbyid y se cargan en la variable 'productos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	int id = Integer.parseInt(idCategoria);
    	System.out.println(id);
		model.addAttribute("productos", iProductoRepo.findByCategoria(id));
		return "index :: categorias";
    }
    
    @GetMapping("/ajax/listarProductoCategoriaInventario")
    public String ListarProductosCategoríaInventario(Model model, @RequestParam("idCategoria") String idCategoria) {
    	/*Se buscan los productos mediante el método del repositorio findbyid y se cargan en la variable 'productos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	int id = Integer.parseInt(idCategoria);
    	System.out.println(id);
		model.addAttribute("productos", iProductoRepo.findByCategoriaInventario(id));
		return "index :: categorias";
    }
    
    @GetMapping("/listarProductosBodega")
    public String ListarProductosBodega(Model model) {
    	/*Se buscan los productos mediante el método del repositorio findbyid y se cargan en la variable 'productos' 
    	 * a la plantilla o medelo de la plantilla.*/
    	model.addAttribute("productos", iProductoRepo.findAll());
        return "listarProducto";
    }
    
}
