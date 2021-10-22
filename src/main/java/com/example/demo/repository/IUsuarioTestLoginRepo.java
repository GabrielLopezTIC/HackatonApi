package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UsuarioTestLogin;


public interface IUsuarioTestLoginRepo extends JpaRepository<UsuarioTestLogin,Long> {
	
	public Optional<UsuarioTestLogin> findByUsuarioAndPassword(String usuario,String password); 

}
