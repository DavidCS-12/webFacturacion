package com.co.web.avanzada.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.co.web.avanzada.entity.Producto;

public interface ProductoServiceAPI {
	Page<Producto> findByInventario(Pageable pageable);
}
