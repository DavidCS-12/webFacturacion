package com.co.web.avanzada.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Bodega;
@Repository
public interface IBodegaRepo extends
CrudRepository<Bodega, Integer>{
}
