package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Bodega;
@Repository
public interface IBodegaRepo extends
CrudRepository<Bodega, Integer>{
	
	@Query("Select B from Bodega B where B.usuario.dni=?1")
	List<Bodega> findByVendedor(int dni);
	
}
