package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.DetalleFactura;


@Repository
public interface IDetalleFacturaRepo extends
CrudRepository<DetalleFactura, Integer>{
	
	@Query ("Select d from DetalleFactura d WHERE d.idDetalleFactura=?1")
	List<DetalleFactura> ListarDetalleFactura (int idDetalleFactura);
	
}

