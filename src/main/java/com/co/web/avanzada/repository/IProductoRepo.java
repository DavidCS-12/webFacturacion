package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.co.web.avanzada.entity.Producto;

@Repository
public interface IProductoRepo extends
JpaRepository<Producto, Integer>{
	@Query("Select P from Producto P, Inventario I where I.producto.codigoProducto= P.codigoProducto and P.categoria.idCategoria=?1")
	List<Producto> findByCategoria(int idCategoria);
	
	@Query("Select P from Producto P , Inventario I where I.producto.codigoProducto= P.codigoProducto and P.proveedor.municipio=?1")
	List<Producto> findByMunicipio(int idMunicipio);
	
	@Query("Select P from Producto P, Inventario I where I.producto.codigoProducto=P.codigoProducto and I.cantidad>0")
	Page<Producto> findByInventario(Pageable pageable);
	
	Page<Producto> findAll(Pageable pageable);
}
