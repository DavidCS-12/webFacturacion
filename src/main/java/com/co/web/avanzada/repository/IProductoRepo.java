package com.co.web.avanzada.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.co.web.avanzada.entity.Producto;

@Repository
public interface IProductoRepo extends
CrudRepository<Producto, Integer>{

}
