package com.co.web.avanzada.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.co.web.avanzada.entity.Inventario;
@Repository
public interface IInventarioRepo extends CrudRepository<Inventario, Integer>{
	
	@Query("Select I from Inventario I where I.bodega.idBodega=?1")
	List<Inventario> findByBodegaId(int idBodega);
	
	@Query("Select I from Inventario I where I.bodega.usuario.dni=?1")
	List<Inventario> findByBodegaVendedor(int dni);
	
	@Query("Select I from Inventario I where I.bodega.usuario.dni=?1 and I.producto.codigoProducto=?2")
	Inventario findByProductoVendedor(int dni, int codigoProducto);
}
