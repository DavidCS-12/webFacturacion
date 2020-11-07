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
import com.co.web.avanzada.entity.Categoria;
import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Inventario;
import com.co.web.avanzada.entity.Municipio;
import com.co.web.avanzada.entity.Pai;
import com.co.web.avanzada.entity.Producto;
import com.co.web.avanzada.entity.Proveedor;
import com.co.web.avanzada.entity.Usuario;
import com.co.web.avanzada.repository.IInventarioRepo;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class InventarioTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private IInventarioRepo repository;
	
	@Test
	public void should_find_no_inventario_if_repository_is_empty() {
	    Iterable<Inventario> inventario = repository.findAll();
	    
	    for (Inventario inventarios : inventario) {
			System.out.println("Inventario:     "+inventarios.toString());
		}

	    assertThat(inventario).isEmpty();
	}
	
	@Test
	public void should_store_a_inventario() {
		//Fase bodegas
		Pai pai= new Pai(41, "Colombia");
		Departamento departamento = new Departamento(35, "Quindio", pai);
		Municipio municipio =  new Municipio(25, "Armenia", departamento);
		Authority authority = new Authority(Integer.toUnsignedLong(1), "ROLE VENDEDOR");
		Set<Authority> recibirAuthority = new HashSet<>();
		recibirAuthority.add(authority);
		Usuario user = new Usuario(123, "Diana", "Valencia", "31232421", "@dianaValencia", "12345", "Centro de armenia", municipio, true, "VENDEDOR", recibirAuthority);
		Bodega bodega = new Bodega(1, "direccionPrueba", municipio, user);
	    //Fase productos
		Categoria categoria = new Categoria(4,"Muebles hogar");
	    Proveedor proveedor   = new Proveedor(1234, "roa@empresa.com", "carrera16", "Arroz roa", "312320845", municipio);
	    Producto producto = new Producto(1, "descripcionprueba", "nombreprueba", 0000 , 0001, "urlprueba", categoria, proveedor);

	    Inventario inventario = repository.save(new Inventario(0, 20, producto, bodega));

	    assertThat(inventario).hasFieldOrPropertyWithValue("cantidad", 20);
	    assertThat(inventario).hasFieldOrPropertyWithValue("idInventario", 0);
	}

}
