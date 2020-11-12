package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Factura;

@Repository
public interface IFacturaRepo extends CrudRepository<Factura, Integer>{

	@Query("SELECT F FROM Factura F WHERE F.despachoPedido.vendedor.dni=?1")
	List<Factura> findByVendedor(int dni);

	@Query("SELECT F FROM Factura F WHERE F.despachoPedido.idDespacho=?1")
	Factura findByDespacho(int idDespacho);
	
}
