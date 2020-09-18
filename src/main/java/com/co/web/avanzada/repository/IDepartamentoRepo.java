package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Departamento;
@Repository
public interface IDepartamentoRepo extends
CrudRepository<Departamento, Integer>{
	
	@Query("SELECT D FROM Departamento D WHERE D.pai.idPais=?1")
	List<Departamento> ListarDeartamentosPais(int idPais);
	
	@Query("SELECT D FROM Departamento D WHERE D.nombre=?1")
	List<Departamento> BuscarDepartamentoNombre(String nombre);
}
