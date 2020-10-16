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
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IUsuarioRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class UsuarioTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	IUsuarioRepo repository;
	
	@Test
	public void should_find_no_users_if_repository_is_empty() {
	    Iterable<Usuario> users = repository.findAll();
	    
	    for (Usuario user : users) {
			System.out.println("Usuario:     "+user.toString());
		}

	    assertThat(users).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_user() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario user = repository.save(new Usuario(1, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "CLIENTE", recibirAuthority));

	    assertThat(user).hasFieldOrPropertyWithValue("nombre", "Diana");
	    assertThat(user).hasFieldOrPropertyWithValue("email", "@dianaValencia");
	  }
	
	@Test
	public void should_find_all_users() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario usu1   = new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu1);

	    Usuario usu2   = new Usuario(2, "nmbre2","apellido2", "telefono2", "email2", "password2", "direccion 2", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu2);

	    Usuario usu3  = new Usuario(3, "nmbre3","apellido3", "telefono3", "email3", "password3", "direccion 3", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu3);

	    Iterable<Usuario> users = repository.findAll();

	    assertThat(users).hasSize(6).contains(usu1, usu2  , usu3 );
	  }
		
	@Test
	  public void should_find_user_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);

	    Usuario usu1   = new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu1);

	    Usuario usu2   = new Usuario(2, "nmbre2","apellido2", "telefono2", "email2", "password2", "direccion 2", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu2);
 
	    
	    Usuario foundUser = repository.findById(usu2.getDni()).get();

	    assertThat(foundUser).isEqualTo(usu2);
	  }
	
	@Test
	  public void should_update_user_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario usu1   = new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu1);

	    Usuario usu2   = new Usuario(2, "nmbre2","apellido2", "telefono2", "email2", "password2", "direccion 2", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu2);


	    Usuario updatedUsu = new Usuario(2,"nmbre_update","apellido_update", "telefono_update", "email_update", "password_update", "direccion_update", municipio, true, "CLIENTE", recibirAuthority);

	    Usuario usu = repository.findById(usu2.getDni()).get();
	    usu.setNombre(updatedUsu.getNombre());
	    usu.setEmail(updatedUsu.getEmail());
	    repository.save(usu);

	    Usuario checkUsu = repository.findById(usu2.getDni()).get();
	    
	    assertThat(checkUsu.getDni()).isEqualTo(usu2.getDni());
	    assertThat(checkUsu.getNombre()).isEqualTo(updatedUsu.getNombre());
	    assertThat(checkUsu.getEmail()).isEqualTo(updatedUsu.getEmail());

	  }
	@Test
	  public void should_delete_user_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		
	    Usuario usu1   = new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu1);

	    Usuario usu2   = new Usuario(2, "nmbre2","apellido2", "telefono2", "email2", "password2", "direccion 2", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu2);

	    Usuario usu3  = new Usuario(3, "nmbre3","apellido3", "telefono3", "email3", "password3", "direccion 3", municipio, true, "CLIENTE", recibirAuthority);
	    entityManager.persist(usu3);

	    repository.deleteById(usu2.getDni());

	    Iterable<Usuario> users = repository.findAll();

	    assertThat(users).hasSize(5).contains(usu1, usu3 );
	  }

	  @Test
	  public void should_delete_all_users() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);  
	    entityManager.persist(new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "CLIENTE", recibirAuthority));
	    entityManager.persist(new Usuario(2,"nmbre_update","apellido_update", "telefono_update", "email_update", "password_update", "direccion_update", municipio, true, "CLIENTE", recibirAuthority));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }
	  
	  @Test
	  public void shoukd_fine_user_by_role_cliente() {
		  Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(35, "Quindio", pai);
			Municipio municipio =  new Municipio(25, "Armenia", departamento);
			Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE CLIENTE");
			Set<Authority> recibirAuthority = new HashSet<>();
			recibirAuthority.add(authority);

		    Usuario usu1   = new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "CLIENTE", recibirAuthority);
		    entityManager.persist(usu1);

		    Usuario usu2   = new Usuario(2, "nmbre2","apellido2", "telefono2", "email2", "password2", "direccion 2", municipio, true, "CLIENTE", recibirAuthority);
		    entityManager.persist(usu2);
		    
		    Iterable<Usuario> users = repository.findByRoleCliente();

		    assertThat(users).hasSize(2).contains(usu1, usu2);
	  }
	  @Test
	  public void shoukd_fine_user_by_role_vendedor() {
		  Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(35, "Quindio", pai);
			Municipio municipio =  new Municipio(25, "Armenia", departamento);
			Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
			Set<Authority> recibirAuthority = new HashSet<>();
			recibirAuthority.add(authority);

		    Usuario usu1   = new Usuario(1, "nmbre1","apellido1", "telefono1", "email1", "password1", "direccion 1", municipio, true, "VENDEDOR", recibirAuthority);
		    entityManager.persist(usu1);

		    Usuario usu2   = new Usuario(2, "nmbre2","apellido2", "telefono2", "email2", "password2", "direccion 2", municipio, true, "VENDEDOR", recibirAuthority);
		    entityManager.persist(usu2);
		    
		    Iterable<Usuario> users = repository.findByRoleVendedor();

		    assertThat(users).hasSize(3).contains(usu1, usu2);
	  }
	

}
