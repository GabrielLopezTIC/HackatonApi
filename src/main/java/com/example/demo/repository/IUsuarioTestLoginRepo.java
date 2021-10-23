package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.UsuarioTestLogin;


public interface IUsuarioTestLoginRepo extends CrudRepository<UsuarioTestLogin,Long> {
	
	public Optional<UsuarioTestLogin> findByUsuarioAndPassword(String usuario,String password); 
	public Optional<UsuarioTestLogin> findByUsuario(String usuario);
	public void deleteByUsuario(String usuario);
	
}
