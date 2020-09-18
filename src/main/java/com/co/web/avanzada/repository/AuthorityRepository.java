package com.co.web.avanzada.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.co.web.avanzada.entity.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long>  {
    public Authority findByAuthority(String authority);
}