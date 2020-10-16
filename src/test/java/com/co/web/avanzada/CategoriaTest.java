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
import com.co.web.avanzada.entity.Categoria;
import com.co.web.avanzada.repository.ICategoriaRepo;



@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class CategoriaTest {

	@Autowired
	private TestEntityManager entityManager;

	
	@Autowired
	private ICategoriaRepo repository;

	
	public void should_find_no_categoria_if_repository_is_empty() {
	    Iterable<Categoria> categorias = repository.findAll();
	    
	    for (Categoria categoria: categorias) {
			System.out.println("Categoria:     "+categoria.toString());
		}

	    assertThat(categorias).isEmpty();
	}

	@Test
	  public void should_store_a_categoria() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    Categoria categoria = repository.save(new Categoria(1, "nombrePrueba"));
	    assertThat(categoria).hasFieldOrPropertyWithValue("nombre", "nombrePrueba");
	    assertThat(categoria).hasFieldOrPropertyWithValue("idCategoria", 1);
	  }
	
	@Test
	public void should_find_all_categorias() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	   
		Categoria categoria1 = new Categoria(1, "nombrePrueba1");
		entityManager.persist(categoria1);
        Categoria categoria2 = new Categoria(2, "nombrePrueba2");
		entityManager.persist(categoria2);
        Categoria categoria3 = new Categoria(3, "nombrePrueba3");
		entityManager.persist(categoria3);
		
	    Iterable<Categoria> categorias = repository.findAll();

	    assertThat(categorias).hasSize(4).contains(categoria1,categoria2,categoria3);
	  }
	
	@Test
	  public void should_find_categoria_by_id() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    

		Categoria categoria1 = new Categoria(1, "nombrePrueba1");
		entityManager.persist(categoria1);
		Categoria categoria2 = new Categoria(2, "nombrePrueba2");
		entityManager.persist(categoria2);
		Categoria categoria3 = new Categoria(3, "nombrePrueba3");
		entityManager.persist(categoria3);
		
		Categoria foundCategoria = repository.findById(categoria1.getIdCategoria()).get();

	    assertThat(foundCategoria).isEqualTo(categoria1);
	  }
	
	
	@Test
	  public void should_update_categoria_by_id() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    
		
		Categoria categoria1 = new Categoria(1, "nombrePrueba1");
		entityManager.persist(categoria1);
		Categoria categoria2 = new Categoria(2, "nombrePrueba2");
		entityManager.persist(categoria2);
		
	    Categoria categoriaUpdate   = new Categoria(1, "nombreUpdate");

	    Categoria cate = repository.findById(categoria1.getIdCategoria()).get();
	    cate.setNombre(categoriaUpdate.getNombre());
	    repository.save(cate);

	    Categoria checkCat = repository.findById(cate.getIdCategoria()).get();
	    
	    assertThat(checkCat.getIdCategoria()).isEqualTo(categoria1.getIdCategoria());
	    assertThat(checkCat.getNombre()).isEqualTo(categoriaUpdate.getNombre());
	  }
	
	@Test
	  public void should_delete_categoria_by_id() {
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
	    		
		Categoria categoria1 = new Categoria(1, "nombrePrueba1");
		entityManager.persist(categoria1);
		Categoria categoria2 = new Categoria(2, "nombrePrueba2");
		entityManager.persist(categoria2);
		Categoria categoria3 = new Categoria(3, "nombrePrueba3");
		entityManager.persist(categoria3);
		
	    repository.deleteById(categoria2.getIdCategoria());

	    Iterable<Categoria> categorias = repository.findAll();

	    assertThat(categorias).hasSize(3).contains(categoria1,categoria3);
	  }
	
	  @Test
	  public void should_delete_all_categorias() {  
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
				
	    entityManager.persist(new Categoria(2, "nombrePrueba"));
	    entityManager.persist(new Categoria(3, "nombrePrueba2"));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }
	
	
	
}
