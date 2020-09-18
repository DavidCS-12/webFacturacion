package com.co.web.avanzada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Municipio;
@Repository
public interface IMunicipioRepo extends
CrudRepository<Municipio, Integer>{
	
	@Query("SELECT M FROM Municipio M WHERE M.departamento.idDepartamento=?1")
	List<Municipio> ListarMunicipioDeartamento(int idDepartamento);
	
	@Query("SELECT M FROM Municipio M WHERE M.nombre=?1")
	List<Municipio> BuscarMunicipiosNombre(String nombre);
}
