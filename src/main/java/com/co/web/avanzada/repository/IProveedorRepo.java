package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Departamento;
import com.co.web.avanzada.entity.Proveedor;

@Repository
public interface IProveedorRepo extends CrudRepository<Proveedor, Integer>{
	@Query("SELECT P FROM Proveedor P WHERE P.municipio.idMunicipio=?1")
	List<Departamento> ListarProveedoresMunicipio(int idMunicipio);
}
