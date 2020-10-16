package com.co.web.avanzada;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.entity.Proveedor;
import com.co.web.avanzada.repository.IProveedorRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ProveedorTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private IProveedorRepo repository;
	
	@Test
	public void should_find_no_providers_if_repository_is_empty() {
	    Iterable<Proveedor> providers = repository.findAll();
	    
	    for (Proveedor provedor: providers) {
			System.out.println("Proveedor:     "+provedor.toString());
		}

	    assertThat(providers).isEmpty();
	}
	
	
	@Test
	  public void should_store_a_provider() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		
	    Proveedor provider = repository.save(new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio));

	    assertThat(provider).hasFieldOrPropertyWithValue("nombreProveedor", "nombreprueba");
	    assertThat(provider).hasFieldOrPropertyWithValue("correo", "@correoprueba");
	  }
	
	@Test
	public void should_find_all_providers() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		
	    Proveedor provider1   = new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio);
	    entityManager.persist(provider1);

	    Proveedor provider2   = new Proveedor(2, "@correoprueba2", "direccionprueba2", "nombreprueba2", "telefonoprueba2", municipio);
	    entityManager.persist(provider2);

	    Proveedor provider3   = new Proveedor(3, "@correoprueba3", "direccionprueba3", "nombreprueba3", "telefonoprueba3", municipio);
	    entityManager.persist(provider3);

	    Iterable<Proveedor> providers = repository.findAll();

	    assertThat(providers).hasSize(5).contains(provider1, provider2  , provider3);
	  }
		
	@Test
	  public void should_find_provider_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		
	    Proveedor provider1   = new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio);
	    entityManager.persist(provider1);

	    Proveedor provider2   = new Proveedor(2, "@correoprueba2", "direccionprueba2", "nombreprueba2", "telefonoprueba2", municipio);
	    entityManager.persist(provider2);

	    Proveedor provider3   = new Proveedor(3, "@correoprueba3", "direccionprueba3", "nombreprueba3", "telefonoprueba3", municipio);
	    entityManager.persist(provider3); 
	    
	    Proveedor foundProvider = repository.findById(provider2.getNit()).get();

	    assertThat(foundProvider).isEqualTo(provider2);
	  }
	
	@Test
	  public void should_update_provider_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		
	    Proveedor provider1   = new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio);
	    entityManager.persist(provider1);

	    Proveedor provider2   = new Proveedor(2, "@correoprueba2", "direccionprueba2", "nombreprueba2", "telefonoprueba2", municipio);
	    entityManager.persist(provider2);

	    Proveedor providerUpdate   = new Proveedor(2, "@correoupdate", "direccionupdate", "nombreupdate", "telefonoupdate", municipio);

	    Proveedor prov = repository.findById(provider2.getNit()).get();
	    prov.setNombreProveedor(providerUpdate.getNombreProveedor());
	    prov.setCorreo(providerUpdate.getCorreo());
	    repository.save(prov);

	    Proveedor checkProv = repository.findById(prov.getNit()).get();
	    
	    assertThat(checkProv.getNit()).isEqualTo(provider2.getNit());
	    assertThat(checkProv.getNombreProveedor()).isEqualTo(providerUpdate.getNombreProveedor());
	    assertThat(checkProv.getCorreo()).isEqualTo(providerUpdate.getCorreo());
	  }
	
	@Test
	  public void should_delete_provider_by_id() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		
	    Proveedor provider1   = new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio);
	    entityManager.persist(provider1);

	    Proveedor provider2   = new Proveedor(2, "@correoprueba2", "direccionprueba2", "nombreprueba2", "telefonoprueba2", municipio);
	    entityManager.persist(provider2);

	    Proveedor provider3   = new Proveedor(3, "@correoprueba3", "direccionprueba3", "nombreprueba3", "telefonoprueba3", municipio);
	    entityManager.persist(provider3); 

	    repository.deleteById(provider2.getNit());

	    Iterable<Proveedor> providers = repository.findAll();

	    assertThat(providers).hasSize(4).contains(provider1, provider3);
	  }

	  @Test
	  public void should_delete_all_users() {
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		  
	    entityManager.persist(new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio));
	    entityManager.persist(new Proveedor(2, "@correoprueba2", "direccionprueba2", "nombreprueba2", "telefonoprueba2", municipio));

	    repository.deleteAll();

	    assertThat(repository.findAll()).isEmpty();
	  }
	  @Test
	  public void should_find_provider_by_municipio() {
		  Pai pai= new Pai(41, "Colombia");
			Departamento departamento = new Departamento(35, "Quindio", pai);
			Municipio municipio =  new Municipio(25, "Armenia", departamento);
			Municipio municipio2 =  new Municipio(24, "Pereira", departamento);
			
		    Proveedor provider1   = new Proveedor(1, "@correoprueba", "direccionprueba", "nombreprueba", "telefonoprueba", municipio);
		    entityManager.persist(provider1);

		    Proveedor provider2   = new Proveedor(2, "@correoprueba2", "direccionprueba2", "nombreprueba2", "telefonoprueba2", municipio2);
		    entityManager.persist(provider2);

		    Proveedor provider3   = new Proveedor(3, "@correoprueba3", "direccionprueba3", "nombreprueba3", "telefonoprueba3", municipio);
		    entityManager.persist(provider3); 
		    
		    List<Proveedor> providers = repository.ListarProveedoresMunicipio(municipio2.getIdMunicipio());

		    assertThat(providers).hasSize(2).contains(provider2);

	  }

}
