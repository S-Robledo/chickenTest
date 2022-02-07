package com.spring.chickenTest.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.chickenTest.modelo.Gallina;

@Repository
public interface IGallina extends CrudRepository<Gallina, Integer>{

}
