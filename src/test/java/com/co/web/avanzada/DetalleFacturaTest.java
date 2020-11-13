package com.co.web.avanzada;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.co.web.avanzada.entity.Authority;
import com.co.web.avanzada.entity.Categoria;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.DespachoPedido;
import com.co.web.avanzada.entity.DetalleFactura;
import com.co.web.avanzada.entity.Factura;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.entity.Proveedor;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IDetalleFacturaRepo;
import com.co.web.avanzada.repository.IFacturaRepo;
import com.co.web.avanzada.repository.IProductoRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class DetalleFacturaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IDetalleFacturaRepo repository;
	
	@Autowired
	IUsuarioRepo repository2;
	
	@Autowired
	private IFacturaRepo repository3;
	
	@Autowired
	private IProductoRepo repository4;
	
	@Test
	public void should_find_no_detalleFactura_if_repository_is_empty() {
	    Iterable<DetalleFactura> detalleFacturas = repository.findAll();
	    
	    for (DetalleFactura detalleFactura: detalleFacturas) {
			System.out.println("Factura:     "+detalleFactura.toString());
		}

	    assertThat(detalleFacturas).isEmpty();
	}
	
	@Test
	  public void should_store_a_detalleFactura() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	
		Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);	
	    Factura	factura = repository3.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));
	    Producto producto = repository4.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria,provider));
		
	    DetalleFactura	detalleFactura = repository.save(new DetalleFactura(1,2,producto, factura));

		
	    assertThat(detalleFactura).hasFieldOrPropertyWithValue("usuario", "vendedor");
	    assertThat(detalleFactura).hasFieldOrPropertyWithValue("idDespacho", 1);
	  }
	
	@Test
	public void should_find_all_detalleFacturas() {
		
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	
		Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);	
	    Factura	factura = repository3.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));
	    Producto producto = repository4.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria,provider));
		
	    		
	    DetalleFactura detalleFactura1 = new DetalleFactura(2,12,producto, factura);
		entityManager.persist(detalleFactura1);
		DetalleFactura detalleFactura2 = new DetalleFactura(3,22,producto, factura);
		entityManager.persist(detalleFactura2);
		DetalleFactura detalleFactura3 = new DetalleFactura(4,23,producto, factura);
		entityManager.persist(detalleFactura3);
		
	    Iterable<DetalleFactura> detalleFactura = repository.findAll();

	    assertThat(detalleFactura).hasSize(4).contains(detalleFactura1, detalleFactura2  , detalleFactura3);
	  }
	
	@Test
	  public void should_find_detalleFactura_by_id() {
	
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	
		Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);	
	    Factura	factura = repository3.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));
	    Producto producto = repository4.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria,provider));
		
	    		
	    DetalleFactura detalleFactura1 = new DetalleFactura(2,12,producto, factura);
		entityManager.persist(detalleFactura1);
		DetalleFactura detalleFactura2 = new DetalleFactura(3,22,producto, factura);
		entityManager.persist(detalleFactura2);
		DetalleFactura detalleFactura3 = new DetalleFactura(4,23,producto, factura);
		entityManager.persist(detalleFactura3);
		
		
	    
		DetalleFactura foundDetalleFact = repository.findById(detalleFactura2.getIdDetalle()).get();

	    assertThat(foundDetalleFact).isEqualTo(detalleFactura2);
	  }
	
	@Test
	  public void should_update_detalleFactura_by_id() {
		
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	
		Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);	
	    Factura	factura = repository3.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));
	    Producto producto = repository4.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria,provider));
		
		DetalleFactura detalleFactura2 = new DetalleFactura(3,22,producto, factura);
		entityManager.persist(detalleFactura2);
		DetalleFactura detalleFactura3 = new DetalleFactura(4,23,producto, factura);
		entityManager.persist(detalleFactura3);
		
		DetalleFactura detalleUpdate   = new DetalleFactura(3,24,producto,factura);

		DetalleFactura bod = repository.findById(detalleFactura2.getIdDetalle()).get();
	    bod.setCantidad(detalleUpdate.getCantidad());
	    repository.save(bod);

	    DetalleFactura checkMun = repository.findById(bod.getIdDetalle()).get();
	    
	    assertThat(checkMun.getIdDetalle()).isEqualTo(detalleFactura2.getIdDetalle());
	    assertThat(checkMun.getCantidad()).isEqualTo(detalleUpdate.getCantidad());
	  }
	
	@Test
	  public void should_delete_detalleFactura_by_id() {
		
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Categoria categoria = new Categoria(4,"Muebles hogar");
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	
		Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
		Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);	
	    Factura	factura = repository3.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));
	    Producto producto = repository4.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria,provider));
		
	    		
	    DetalleFactura detalleFactura1 = new DetalleFactura(2,12,producto, factura);
		entityManager.persist(detalleFactura1);
		DetalleFactura detalleFactura2 = new DetalleFactura(3,22,producto, factura);
		entityManager.persist(detalleFactura2);
		DetalleFactura detalleFactura3 = new DetalleFactura(4,23,producto, factura);
		entityManager.persist(detalleFactura3);
		
	    repository.deleteById(detalleFactura2.getIdDetalle());

	    Iterable<DetalleFactura> despachoPedidos = repository.findAll();

	    assertThat(despachoPedidos).hasSize(3).contains(detalleFactura1, detalleFactura3);
	  }
	
	 @Test
	  public void should_delete_all_detalleFacturas() {  
		
			Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(35, "Quindio", pai);
			Municipio municipio =  new Municipio(25, "Armenia", departamento);
			Categoria categoria = new Categoria(4,"Muebles hogar");
			
			Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
			Set<Authority> recibirAuthority = new HashSet<>();
			recibirAuthority.add(authority);
		
			Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
			Proveedor provider   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
			DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);	
		    Factura	factura = repository3.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));
		    Producto producto = repository4.save(new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria,provider));
			
					
	    entityManager.persist(new DetalleFactura(2,12,producto, factura));
	    entityManager.persist(new DetalleFactura(4,23,producto, factura));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }
		
}
