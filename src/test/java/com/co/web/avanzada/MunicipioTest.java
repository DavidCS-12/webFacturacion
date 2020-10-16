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
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.repository.IMunicipioRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class MunicipioTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IMunicipioRepo repository;
	
	@Test
	public void should_find_no_municipio_if_repository_is_empty() {
	    Iterable<Municipio> municipios = repository.findAll();
	    
	    for (Municipio municipio: municipios) {
			System.out.println("Municipio:     "+municipio.toString());
		}

	    assertThat(municipios).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_municipio() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
	    Municipio municipio = repository.save(new Municipio(1, "nombrePrueba", departamento));

	    assertThat(municipio).hasFieldOrPropertyWithValue("direccion", "direccionPrueba");
	    assertThat(municipio).hasFieldOrPropertyWithValue("idBodega", 1);
	  }
	
	@Test
	public void should_find_all_bodegas() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	   
	    Municipio muni1 = new Municipio(2, "nombrePrueba", departamento);
		entityManager.persist(muni1);
		Municipio muni2 = new Municipio(3, "nombrePrueba", departamento);
		entityManager.persist(muni2);
		Municipio muni3 = new Municipio(4, "nombrePrueba", departamento);
		entityManager.persist(muni3);
		
	    Iterable<Municipio> municipio = repository.findAll();

	    assertThat(municipio).hasSize(4).contains(muni1, muni2  , muni3);
	  }
		
	@Test
	  public void should_find_municpio_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	   
		
		  Municipio muni1 = new Municipio(2, "nombrePrueba", departamento);
		  entityManager.persist(muni1);
		  Municipio muni2 = new Municipio(3, "nombrePrueba", departamento);
		  entityManager.persist(muni2);
		  Municipio muni3 = new Municipio(4, "nombrePrueba", departamento);
		  entityManager.persist(muni3);
	    
	    Municipio foundMunicipio = repository.findById(muni2.getIdMunicipio()).get();

	    assertThat(foundMunicipio).isEqualTo(muni2);
	  }
	
	@Test
	  public void should_update_municipio_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	   
		
		  Municipio muni1 = new Municipio(2, "nombrePrueba", departamento);
		  entityManager.persist(muni1);
		  Municipio muni2 = new Municipio(3, "nombrePrueba", departamento);
		  entityManager.persist(muni2);
		
	    Municipio municipioUpdate   = new Municipio(2, "nombreUpdate", departamento);

	    Municipio bod = repository.findById(muni2.getIdMunicipio()).get();
	    bod.setNombre(municipioUpdate.getNombre());
	    repository.save(bod);

	    Municipio checkMun = repository.findById(bod.getIdMunicipio()).get();
	    
	    assertThat(checkMun.getIdMunicipio()).isEqualTo(muni2.getIdMunicipio());
	    assertThat(checkMun.getNombre()).isEqualTo(municipioUpdate.getNombre());
	  }
	
	@Test
	  public void should_delete_municipio_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    		
		  Municipio muni1 = new Municipio(2, "nombrePrueba", departamento);
		  entityManager.persist(muni1);
		  Municipio muni2 = new Municipio(3, "nombrePrueba", departamento);
		  entityManager.persist(muni2);
		  Municipio muni3 = new Municipio(4, "nombrePrueba", departamento);
		  entityManager.persist(muni3);
		
	    repository.deleteById(muni2.getIdMunicipio());

	    Iterable<Municipio> municipios = repository.findAll();

	    assertThat(municipios).hasSize(3).contains(muni1, muni3);
	  }

	  @Test
	  public void should_delete_all_municpios() {  
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(42, "Antioquia", pai);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		   
	    entityManager.persist(new Municipio(2, "nombrePrueba",departamento));
	    entityManager.persist(new Municipio(3, "nombrePrueba", departamento));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }

	
	}