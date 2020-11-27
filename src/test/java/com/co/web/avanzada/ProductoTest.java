package com.co.web.avanzada;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.co.web.avanzada.entity.Categoria;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.entity.Proveedor;
import com.co.web.avanzada.repository.IProductoRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ProductoTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IProductoRepo repository;
	
	@Test
	public void should_find_no_products_if_repository_is_empty() {
	    Iterable<Producto> products = repository.findAll();
	    
	    for (Producto producto: products) {
			System.out.println("Producto:     "+producto.toString());
		}

	    assertThat(products).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_product() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		
	    Producto producto = repository.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, provider));

	    assertThat(producto).hasFieldOrPropertyWithValue("descripcion", "descripcionprueba");
	    assertThat(producto).hasFieldOrPropertyWithValue("nombre", "nombreprueba");
	  }
	
	@Test
	public void should_find_all_products() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		
		
		Producto product1 = new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, provider);
		entityManager.persist(product1);
		Producto product2 = new Producto(2, "descripcionprueba2", "nombreprueba2", 0000 , 0002, "urlprueba2", categoria, provider);
		entityManager.persist(product2);
		Producto product3 = new Producto(3, "descripcionprueba3", "nombreprueba3", 0000 , 0003, "urlprueba2", categoria, provider);
		entityManager.persist(product3);
		
	    Iterable<Producto> products = repository.findAll();

	    assertThat(products).hasSize(4).contains(product1, product2  , product3);
	  }
		
	@Test
	  public void should_find_product_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		
		
		Producto product1 = new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, provider);
		entityManager.persist(product1);
		Producto product2 = new Producto(2, "descripcionprueba2", "nombreprueba2", 0000 , 0002, "urlprueba2", categoria, provider);
		entityManager.persist(product2);
		Producto product3 = new Producto(3, "descripcionprueba3", "nombreprueba3", 0000 , 0003, "urlprueba2", categoria, provider);
		entityManager.persist(product3);
	    
	    Producto foundProduct = repository.findById(Long.parseLong(Integer.toString(product2.getCodigoProducto()))).get();

	    assertThat(foundProduct).isEqualTo(product2);
	  }
	
	@Test
	  public void should_update_product_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		
		
		
		Producto product1 = new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, provider);
		entityManager.persist(product1);
		Producto product2 = new Producto(2, "descripcionprueba2", "nombreprueba2", 0000 , 0002, "urlprueba2", categoria, provider);
		entityManager.persist(product2);

	    Producto productoUpdate   = new Producto(2, "descripcionupdate", "nombreupdate", 0001,0003, "url_update",categoria, provider);

	    Producto prov = repository.findById(Long.parseLong(Integer.toString(product2.getCodigoProducto()))).get();
	    prov.setNombre(productoUpdate.getNombre());
	    prov.setDescripcion(productoUpdate.getDescripcion());
	    repository.save(prov);

	    Producto checkProv = repository.findById(Long.parseLong(Integer.toString(prov.getCodigoProducto()))).get();
	    
	    assertThat(checkProv.getCodigoProducto()).isEqualTo(product2.getCodigoProducto());
	    assertThat(checkProv.getNombre()).isEqualTo(productoUpdate.getNombre());
	    assertThat(checkProv.getDescripcion()).isEqualTo(productoUpdate.getDescripcion());
	  }
	
	@Test
	  public void should_delete_producto_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		
		
		
		Producto product1 = new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, provider);
		entityManager.persist(product1);
		Producto product2 = new Producto(2, "descripcionprueba2", "nombreprueba2", 0000 , 0002, "urlprueba2", categoria, provider);
		entityManager.persist(product2);
		Producto product3 = new Producto(3, "descripcionprueba3", "nombreprueba3", 0000 , 0003, "urlprueba2", categoria, provider);
		entityManager.persist(product3);
		
	    repository.deleteById(Long.parseLong(Integer.toString(product2.getCodigoProducto())));

	    Iterable<Producto> products = repository.findAll();

	    assertThat(products).hasSize(3).contains(product1, product3);
	  }

	  @Test
	  public void should_delete_all_products() {  
		  Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(42, "Antioquia", pai);
			Municipio municipio =  new Municipio(26, "Medellin", departamento);
			Categoria categoria = new Categoria(4,"Muebles hogar");
			Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
			
		
	    entityManager.persist(new Producto(2, "descripcionprueba2", "nombreprueba2", 0000 , 0002, "urlprueba2", categoria, provider));
	    entityManager.persist(new Producto(3, "descripcionprueba3", "nombreprueba3", 0000 , 0003, "urlprueba2", categoria, provider));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }
	  @Test
	  public void should_find_all_products_for_categoria() {
			Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(42, "Antioquia", pai);
			Municipio municipio =  new Municipio(26, "Medellin", departamento);
			Categoria categoria = new Categoria(4,"Muebles hogar");
			Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
			
			
			Producto product1 = new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, provider);
			entityManager.persist(product1);
			Producto product2 = new Producto(2, "descripcionprueba2", "nombreprueba2", 0000 , 0002, "urlprueba2", categoria, provider);
			entityManager.persist(product2);
			Producto product3 = new Producto(3, "descripcionprueba3", "nombreprueba3", 0000 , 0003, "urlprueba2", categoria, provider);
			entityManager.persist(product3);
			
		    Iterable<Producto> products = repository.findByCategoria(categoria.getIdCategoria());

		    assertThat(products).hasSize(4).contains(product1, product2  , product3);
	  }


}
