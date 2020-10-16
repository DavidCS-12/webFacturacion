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
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.repository.IDepartamentoRepo;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class DepartamentoTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IDepartamentoRepo repository;
	
	@Test
	public void should_find_no_departamento_if_repository_is_empty() {
	    Iterable<Departamento> departamentos = repository.findAll();
	    
	    for (Departamento departamento: departamentos) {
			System.out.println("Departamento:     "+departamento.toString());
		}

	    assertThat(departamentos).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_departamento() {
		Pai pai= new Pai(41, "Colombia");
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	   
		Departamento departamento = repository.save(new Departamento(1, "nombrePrueba", pai));

	    assertThat(departamento).hasFieldOrPropertyWithValue("nombre", "nombrePrueba");
	    assertThat(departamento).hasFieldOrPropertyWithValue("idPais", 1);
	  }
	
	@Test
	public void should_find_all_departamentos() {
		Pai pai= new Pai(41, "Colombia");
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	 
	    Departamento depar1 = new Departamento(2, "nombrePrueba", pai);
		entityManager.persist(depar1);
		Departamento depar2 = new Departamento(3, "nombrePrueba2", pai);
		entityManager.persist(depar2);
		Departamento depar3 = new Departamento(4, "nombrePrueba3", pai);
		entityManager.persist(depar3);
		
	    Iterable<Departamento> departamento = repository.findAll();

	    assertThat(departamento).hasSize(4).contains(depar1,depar2,depar3);
	  }
		
	@Test
	  public void should_find_departamento_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
		Departamento depar1 = new Departamento(2, "nombrePrueba", pai);
		entityManager.persist(depar1);
		Departamento depar2 = new Departamento(3, "nombrePrueba2", pai);
		entityManager.persist(depar2);
		Departamento depar3 = new Departamento(4, "nombrePrueba3", pai);
		entityManager.persist(depar3);
	    
	    Departamento foundDepar = repository.findById(depar2.getIdDepartamento()).get();

	    assertThat(foundDepar).isEqualTo(depar2);
	  }
	
	@Test
	  public void should_update_departamento_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
		Departamento depar1 = new Departamento(2, "nombrePrueba", pai);
		entityManager.persist(depar1);
		Departamento depar2 = new Departamento(3, "nombrePrueba2", pai);
		entityManager.persist(depar2);
		
	    Departamento deparUpdate   = new Departamento(2, "direccionUpdate",pai);

	    Departamento dept = repository.findById(depar2.getIdDepartamento()).get();
	    dept.setNombre(deparUpdate.getNombre());
	    repository.save(dept);

	    Departamento checkDep = repository.findById(dept.getIdDepartamento()).get();
	    
	    assertThat(checkDep.getIdDepartamento()).isEqualTo(depar2.getIdDepartamento());
	    assertThat(checkDep.getNombre()).isEqualTo(deparUpdate.getNombre());
	  }
	
	@Test
	  public void should_delete_departamento_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
		Departamento depar1 = new Departamento(2, "nombrePrueba", pai);
		entityManager.persist(depar1);
		Departamento depar2 = new Departamento(3, "nombrePrueba2", pai);
		entityManager.persist(depar2);
		Departamento depar3 = new Departamento(4, "nombrePrueba3", pai);
		entityManager.persist(depar3);
		
	    repository.deleteById(depar2.getIdDepartamento());

	    Iterable<Departamento> departamentos = repository.findAll();

	    assertThat(departamentos).hasSize(3).contains(depar1, depar3);
	  }

	  @Test
	  public void should_delete_all_departamentos() {  
		Pai pai= new Pai(41, "Colombia");
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	
		entityManager.persist(new Departamento(2, "direccionPrueba", pai));
	    entityManager.persist(new Departamento(3, "direccionPrueba2", pai));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }

	
	}