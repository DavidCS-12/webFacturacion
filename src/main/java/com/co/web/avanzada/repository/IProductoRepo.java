package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.co.web.avanzada.entity.Producto;

@Repository
public interface IProductoRepo extends
CrudRepository<Producto, Integer>{
	@Query("Select P from Producto P where P.categoria.idCategoria=?1")
	List<Producto> findByCategoria(int idCategoria);
	
	@Query("Select P from Producto P where P.proveedor.municipio=?1")
	List<Producto> findByMunicipio(int idMunicipio);
	
}
