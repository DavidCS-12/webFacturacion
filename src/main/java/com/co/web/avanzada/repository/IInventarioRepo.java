package com.co.web.avanzada.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.co.web.avanzada.entity.Bodega;
import com.co.web.avanzada.entity.Inventario;

public interface IInventarioRepo extends CrudRepository<Inventario, Integer>{
	
	@Query("Select * from Inventario I where I.bodega.idBodega=?1")
	Optional<Bodega> findByBodegaId(int idBodega);

}
