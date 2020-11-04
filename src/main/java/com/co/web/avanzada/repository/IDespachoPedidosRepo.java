package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.DespachoPedido;


@Repository
public interface IDespachoPedidosRepo extends
CrudRepository<DespachoPedido, Integer>{
	
	@Query ("Select d from DespachoPedido d WHERE d.idDespacho=?1")
	List<DespachoPedido> ListarDespacho (int idDespacho);
	
}
