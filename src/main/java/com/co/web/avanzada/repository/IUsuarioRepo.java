package com.co.web.avanzada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.co.web.avanzada.entity.Usuario;

@Repository
public interface IUsuarioRepo extends
CrudRepository<Usuario, Integer>{
	public Optional<Usuario> findByEmail(String email);

	@Query("Select U from Usuario U where U.rol='CLIENTE'")
	List<Usuario> findByRoleCliente();
	
	@Query("Select U from Usuario U where U.rol='VENDEDOR'")
	List<Usuario> findByRoleVendedor();
}
