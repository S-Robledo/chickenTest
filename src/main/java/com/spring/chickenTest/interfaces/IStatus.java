package com.spring.chickenTest.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.spring.chickenTest.modelo.Gallina;

public interface IStatus extends CrudRepository<Gallina, Integer> {

}
