package com.co.web.avanzada.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Categoria;
@Repository
public interface ICategoriaRepo extends
CrudRepository<Categoria, Integer>{
	
	
	@Query("SELECT C FROM Categoria C WHERE C.nombre=?1")
	List<Categoria> BuscarCategoriaNombre(String nombre);
}
