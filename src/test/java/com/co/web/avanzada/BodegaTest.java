package com.co.web.avanzada;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.co.web.avanzada.entity.Bodega;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IBodegaRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class BodegaTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IBodegaRepo repository;
	
	@Test
	public void should_find_no_bodega_if_repository_is_empty() {
	    Iterable<Bodega> bodegas = repository.findAll();
	    
	    for (Bodega bodega: bodegas) {
			System.out.println("Bodega:     "+bodega.toString());
		}

	    assertThat(bodegas).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_bodega() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
		
	    Bodega bodega = repository.save(new Bodega(1, "direccionPrueba", municipio, user));

	    assertThat(bodega).hasFieldOrPropertyWithValue("direccion", "direccionPrueba");
	    assertThat(bodega).hasFieldOrPropertyWithValue("idBodega", 1);
	  }
	
	@Test
	public void should_find_all_bodegas() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
		
		
	    Bodega bodega1 = new Bodega(2, "direccionPrueba", municipio, user);
		entityManager.persist(bodega1);
		Bodega bodega2 = new Bodega(3, "direccionPrueba2", municipio, user);
		entityManager.persist(bodega2);
		Bodega bodega3 = new Bodega(4, "direccionPrueba3", municipio, user);
		entityManager.persist(bodega3);
		
	    Iterable<Bodega> bodegas = repository.findAll();

	    assertThat(bodegas).hasSize(4).contains(bodega1, bodega2  , bodega3);
	  }
		
	@Test
	  public void should_find_bodega_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
		
		
	    Bodega bodega1 = new Bodega(2, "direccionPrueba", municipio, user);
		entityManager.persist(bodega1);
		Bodega bodega2 = new Bodega(3, "direccionPrueba2", municipio, user);
		entityManager.persist(bodega2);
		Bodega bodega3 = new Bodega(4, "direccionPrueba3", municipio, user);
		entityManager.persist(bodega3);
	    
	    Bodega foundBodega = repository.findById(bodega2.getIdBodega()).get();

	    assertThat(foundBodega).isEqualTo(bodega2);
	  }
	
	@Test
	  public void should_update_bodega_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
		
		
	    Bodega bodega1 = new Bodega(2, "direccionPrueba", municipio, user);
		entityManager.persist(bodega1);
		Bodega bodega2 = new Bodega(3, "direccionPrueba2", municipio, user);
		entityManager.persist(bodega2);
		
	    Bodega bodegaUpdate   = new Bodega(2, "direccionUpdate", municipio, user);

	    Bodega bod = repository.findById(bodega2.getIdBodega()).get();
	    bod.setDireccion(bodegaUpdate.getDireccion());
	    repository.save(bod);

	    Bodega checkBod = repository.findById(bod.getIdBodega()).get();
	    
	    assertThat(checkBod.getIdBodega()).isEqualTo(bodega2.getIdBodega());
	    assertThat(checkBod.getDireccion()).isEqualTo(bodegaUpdate.getDireccion());
	  }
	
	@Test
	  public void should_delete_bodega_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Municipio municipio =  new Municipio(26, "Medellin", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
		
		
	    Bodega bodega1 = new Bodega(2, "direccionPrueba", municipio, user);
		entityManager.persist(bodega1);
		Bodega bodega2 = new Bodega(3, "direccionPrueba2", municipio, user);
		entityManager.persist(bodega2);
		Bodega bodega3 = new Bodega(4, "direccionPrueba3", municipio, user);
		entityManager.persist(bodega3);
		
	    repository.deleteById(bodega2.getIdBodega());

	    Iterable<Bodega> bodegas = repository.findAll();

	    assertThat(bodegas).hasSize(3).contains(bodega1, bodega3);
	  }

	  @Test
	  public void should_delete_all_bodegas() {  
		  Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(42, "Antioquia", pai);
			Municipio municipio =  new Municipio(26, "Medellin", departamento);
			Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
			Set<Authority> recibirAuthority = new HashSet<>();
			recibirAuthority.add(authority);
		    Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
			
		
	    entityManager.persist(new Bodega(2, "direccionPrueba", municipio, user));
	    entityManager.persist(new Bodega(3, "direccionPrueba2", municipio, user));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }

	
	}
