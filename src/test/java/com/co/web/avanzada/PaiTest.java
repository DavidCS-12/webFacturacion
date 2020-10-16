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
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.repository.IPaiRepo;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class PaiTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IPaiRepo repository;
	
	@Test
	public void should_find_no_pai_if_repository_is_empty() {
	    Iterable<Pai> paises = repository.findAll();
	    
	    for (Pai pai: paises) {
			System.out.println("Pai:     "+pai.toString());
		}

	    assertThat(paises).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_pai() {
		
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
	    Pai pai = repository.save(new Pai(1, "nombrePrueba"));

	    assertThat(pai).hasFieldOrPropertyWithValue("nombre", "nombrePrueba");
	    assertThat(pai).hasFieldOrPropertyWithValue("idPais", 1);
	  }
	
	@Test
	public void should_find_all_categorias() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	   
		Pai pai1 = new Pai(1, "nombrePrueba1");
		entityManager.persist(pai1);
		Pai pai2 = new Pai(2, "nombrePrueba2");
		entityManager.persist(pai2);
		Pai pai3 = new Pai(3, "nombrePrueba3");
		entityManager.persist(pai3);
		
	    Iterable<Pai> pai = repository.findAll();

	    assertThat(pai).hasSize(4).contains(pai1,pai2,pai3);
	  }
	
	@Test
	  public void should_find_pai_by_id() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    

		Pai pai1 = new Pai(1, "nombrePrueba1");
		entityManager.persist(pai1);
		Pai pai2 = new Pai(2, "nombrePrueba2");
		entityManager.persist(pai2);
		Pai pai3 = new Pai(3, "nombrePrueba3");
		entityManager.persist(pai3);
		
		Pai foundPai = repository.findById(pai1.getIdPais()).get();

	    assertThat(foundPai).isEqualTo(pai1);
	  }
	
	
	@Test
	  public void should_update_pai_by_id() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
		
		Pai pai1 = new Pai(1, "nombrePrueba1");
		entityManager.persist(pai1);
		Pai pai2 = new Pai(2, "nombrePrueba2");
		entityManager.persist(pai2);
		
		
	    Pai paiUpdate   = new Pai(1, "nombreUpdate");

	    Pai pais = repository.findById(pai1.getIdPais()).get();
	    pais.setNombre(paiUpdate.getNombre());
	    repository.save(pais);

	    Pai checkPai = repository.findById(pais.getIdPais()).get();
	    
	    assertThat(checkPai.getIdPais()).isEqualTo(pai1.getIdPais());
	    assertThat(checkPai.getNombre()).isEqualTo(paiUpdate.getNombre());
	  }
	
	@Test
	  public void should_delete_categoria_by_id() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    		
		Pai pai1 = new Pai(1, "nombrePrueba1");
		entityManager.persist(pai1);
		Pai pai2 = new Pai(2, "nombrePrueba2");
		entityManager.persist(pai2);
		Pai pai3 = new Pai(3, "nombrePrueba3");
		entityManager.persist(pai3);
		
	    repository.deleteById(pai2.getIdPais());

	    Iterable<Pai> paises = repository.findAll();

	    assertThat(paises).hasSize(3).contains(pai1,pai3);
	  }
	
	  @Test
	  public void should_delete_all_pai() {  
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
				
	    entityManager.persist(new Pai(2, "nombrePrueba"));
	    entityManager.persist(new Pai(3, "nombrePrueba2"));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }
	
	
	
}
