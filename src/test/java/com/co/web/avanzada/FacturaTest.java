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
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.DespachoPedido;
import com.co.web.avanzada.entity.Factura;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IFacturaRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class FacturaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IFacturaRepo repository;
	
	@Autowired
	IUsuarioRepo repository2;
	
	
	
	@Test
	public void should_find_no_factura_if_repository_is_empty() {
	    Iterable<Factura> facturas = repository.findAll();
	    
	    for (Factura factura: facturas) {
			System.out.println("Factura:     "+factura.toString());
		}

	    assertThat(facturas).isEmpty();
	}
	
	@Test
	  public void should_store_a_factura() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);
				
	    Factura	factura = repository.save(new Factura(1,new Date(),3.000,3.600, despachoPedido));

	    assertThat(factura).hasFieldOrPropertyWithValue("fechaVenta", new Date());
	    assertThat(factura).hasFieldOrPropertyWithValue("idFactura", 1);
	  }
	
	@Test
	public void should_find_all_facturas() {
		
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);
				

	    Factura factura1 = new Factura(2,new Date(),7.000,7.600, despachoPedido);
		entityManager.persist(factura1);
		Factura factura2 = new Factura(3,new Date(),8.000,8.600, despachoPedido);
		entityManager.persist(factura2);
		Factura factura3 = new Factura(4,new Date(),6.000,6.600, despachoPedido);
		entityManager.persist(factura3);
		
	    Iterable<Factura> factura = repository.findAll();

	    assertThat(factura).hasSize(4).contains(factura1, factura2  , factura3);
	  }
		
	@Test
	  public void should_find_factura_by_id() {
	
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);
				
		Factura factura1 = new Factura(2,new Date(),7.000,7.600, despachoPedido);
		entityManager.persist(factura1);
		Factura factura2 = new Factura(3,new Date(),8.000,8.600, despachoPedido);
		entityManager.persist(factura2);
		Factura factura3 = new Factura(4,new Date(),6.000,6.600, despachoPedido);
		entityManager.persist(factura3);
		
	    
	    Factura foundFactura = repository.findById(factura2.getIdFactura()).get();

	    assertThat(foundFactura).isEqualTo(factura2 );
	  }
	
	@Test
	  public void should_update_factura_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);
		
		Factura factura1 = new Factura(2,new Date(),7.000,7.600, despachoPedido);
		entityManager.persist(factura1);
		Factura factura2 = new Factura(3,new Date(),8.000,8.600, despachoPedido);
		entityManager.persist(factura2);
		
	    Factura facturaUpdate   = new Factura(2,new Date(),6.000,7.600, despachoPedido);

	    Factura bod = repository.findById(factura2.getIdFactura()).get();
	    bod.setValorCompra(facturaUpdate.getValorCompra());
	    repository.save(bod);

	    Factura checkMun = repository.findById(bod.getIdFactura()).get();
	    
	    assertThat(checkMun.getIdFactura()).isEqualTo(factura2.getIdFactura());
	    assertThat(checkMun.getValorCompra()).isEqualTo(facturaUpdate.getValorCompra());
	  }
	
	@Test
	  public void should_delete_factura_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

		DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);
				
		Factura factura1 = new Factura(2,new Date(),7.000,7.600, despachoPedido);
		entityManager.persist(factura1);
		Factura factura2 = new Factura(3,new Date(),8.000,8.600, despachoPedido);
		entityManager.persist(factura2);
		Factura factura3 = new Factura(4,new Date(),6.000,6.600, despachoPedido);
		entityManager.persist(factura3);
		
	    repository.deleteById(factura2.getIdFactura());

	    Iterable<Factura> facturas = repository.findAll();

	    assertThat(facturas).hasSize(3).contains(factura1, factura3);
	  }
	
	 @Test
	  public void should_delete_all_facturas() {  
		 Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(35, "Quindio", pai);
			Municipio municipio =  new Municipio(25, "Armenia", departamento);
			Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
			Set<Authority> recibirAuthority = new HashSet<>();
			recibirAuthority.add(authority);
			
		    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

			DespachoPedido despachoPedido= new DespachoPedido(1,vendedor,null,true);
					
	    entityManager.persist(new Factura(2,new Date(),7.000,7.600, despachoPedido));
	    entityManager.persist(new Factura(3,new Date(),8.000,8.600, despachoPedido));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }


}
