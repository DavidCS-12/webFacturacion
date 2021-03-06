package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.DespachoPedido;


@Repository
public interface IDespachoPedidosRepo extends
CrudRepository<DespachoPedido, Integer>{
	
	@Query ("Select d from DespachoPedido d WHERE d.idDespacho=?1")
	List<DespachoPedido> ListarDespacho (int idDespacho);
	
	@Query ("Select d from DespachoPedido d")
	List<DespachoPedido> Listar ();
	
	@Query ("Select d from DespachoPedido d where d.cliente.email=?1 and d.vendedor.rol='ADMIN' and d.estado='false'")
	DespachoPedido BuscarCliente (String email,PageRequest pageRequest);
	
	@Query ("Select d from DespachoPedido d where d.cliente.email=?1 and d.vendedor.email=?2 and d.estado='false'")
	DespachoPedido findAdmindCliente(String email, String email2, PageRequest pageRequest);
	
}
