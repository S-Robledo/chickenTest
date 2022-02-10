package com.spring.chickenTest.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.spring.chickenTest.modelo.Cuenta;

public interface ICuenta extends CrudRepository<Cuenta, Integer> {

}
