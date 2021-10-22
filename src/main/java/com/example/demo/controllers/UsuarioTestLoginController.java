package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.UsuarioTestLogin;
import com.example.demo.repository.IUsuarioTestLoginRepo;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UsuarioTestLoginController {

	@Autowired
	private IUsuarioTestLoginRepo usrRepo;
	
	
	@PostMapping("/v1/login")
	public ResponseEntity<String> login(@RequestBody UsuarioTestLogin usr){
		Optional<UsuarioTestLogin> usuario = usrRepo.findByUsuarioAndPassword(usr.getUsuario(),usr.getPassword());
		
		if(usuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body("Usuario Autenticado");
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
		}
	}
	
	@GetMapping("/v1/all")
	public ResponseEntity<List<UsuarioTestLogin>> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(usrRepo.findAll());
	}
}
