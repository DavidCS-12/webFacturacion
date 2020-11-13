/**
 * 
 */
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
import com.co.web.avanzada.repository.IDespachoPedidosRepo;
import com.co.web.avanzada.repository.IFacturaRepo;
import com.co.web.avanzada.repository.IUsuarioRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class DespachoPedidoTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IDespachoPedidosRepo repository;
	
	@Autowired
	IUsuarioRepo repository2;
	
	@Autowired
	private IFacturaRepo repository3;
	

	@Test
	public void should_find_no_despachoPedido_if_repository_is_empty() {
	    Iterable<DespachoPedido> despachoPedidos = repository.findAll();
	    
	    for (DespachoPedido despachoPedido: despachoPedidos) {
			System.out.println("Factura:     "+despachoPedido.toString());
		}

	    assertThat(despachoPedidos).isEmpty();
	}
	
	@Test
	  public void should_store_a_despachoPedido() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
	
		DespachoPedido	despachoPedido = repository.save(new DespachoPedido(1,null,vendedor, true));

	    assertThat(despachoPedido).hasFieldOrPropertyWithValue("usuario", "vendedor");
	    assertThat(despachoPedido).hasFieldOrPropertyWithValue("idDespacho", 1);
	  }
	
	@Test
	public void should_find_all_despachoPedidos() {
		
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));
	   
	    DespachoPedido despachoPedido1 = new DespachoPedido(2,null,vendedor, true);
		entityManager.persist(despachoPedido1);
		DespachoPedido despachoPedido2 = new DespachoPedido(3,null,vendedor, true);
		entityManager.persist(despachoPedido2);
		DespachoPedido despachoPedido3 = new DespachoPedido(4,null,vendedor, true);
		entityManager.persist(despachoPedido3);
		
	    Iterable<DespachoPedido> despachoPedido = repository.findAll();

	    assertThat(despachoPedido).hasSize(4).contains(despachoPedido1, despachoPedido2  , despachoPedido3);
	  }
		
	@Test
	  public void should_find_despachoPedido_by_id() {
	
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

		DespachoPedido despachoPedido1 = new DespachoPedido(2,null,vendedor, true);
		entityManager.persist(despachoPedido1);
		DespachoPedido despachoPedido2 = new DespachoPedido(3,null,vendedor, true);
		entityManager.persist(despachoPedido2);
		DespachoPedido despachoPedido3 = new DespachoPedido(4,null,vendedor, true);
		entityManager.persist(despachoPedido3);
		
	    
		DespachoPedido foundDespacho = repository.findById(despachoPedido2.getIdDespacho()).get();

	    assertThat(foundDespacho).isEqualTo(despachoPedido2);
	  }
	
	@Test
	  public void should_update_despachoPedido_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

	    DespachoPedido despachoPedido2 = new DespachoPedido(3,null,vendedor, true);
		entityManager.persist(despachoPedido2);
		DespachoPedido despachoPedido3 = new DespachoPedido(4,null,vendedor, true);
		entityManager.persist(despachoPedido3);
		
		DespachoPedido despachoUpdate   = new DespachoPedido(3,null,vendedor, false);

		DespachoPedido bod = repository.findById(despachoPedido2.getIdDespacho()).get();
	    bod.setEstado(despachoUpdate.isEstado());
	    repository.save(bod);

	    DespachoPedido checkMun = repository.findById(bod.getIdDespacho()).get();
	    
	    assertThat(checkMun.getIdDespacho()).isEqualTo(despachoPedido2.getIdDespacho());
	    assertThat(checkMun.isEstado()).isEqualTo(despachoUpdate.isEstado());
	  }
	
	@Test
	  public void should_delete_despachoPedido_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario vendedor = repository2.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority));

	    DespachoPedido despachoPedido1 = new DespachoPedido(2,null,vendedor, true);
		entityManager.persist(despachoPedido1);
		DespachoPedido despachoPedido2 = new DespachoPedido(3,null,vendedor, true);
		entityManager.persist(despachoPedido2);
		DespachoPedido despachoPedido3 = new DespachoPedido(4,null,vendedor, true);
		entityManager.persist(despachoPedido3);
		
	    repository.deleteById(despachoPedido2.getIdDespacho());

	    Iterable<DespachoPedido> despachoPedidos = repository.findAll();

	    assertThat(despachoPedidos).hasSize(3).contains(despachoPedido1, despachoPedido3);
	  }
	
	 @Test
	  public void should_delete_all_despachoPedidos() {  
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
