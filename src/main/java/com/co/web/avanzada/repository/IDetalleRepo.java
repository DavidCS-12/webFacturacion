package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.DetalleFactura;

@Repository
public interface IDetalleRepo extends CrudRepository<DetalleFactura, Integer>{
	
	@Query("SELECT D FROM DetalleFactura D WHERE D.factura.idFactura=?1")
	List<DetalleFactura> detalleFactura(int idFactura);

}
