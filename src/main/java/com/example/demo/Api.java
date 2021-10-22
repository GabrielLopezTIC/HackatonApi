package com.example.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") //localhost:8080/api/
public class Api {
	
	
	private static final Logger log = LoggerFactory.getLogger(Api.class);

	
	@Autowired
	BaseDeDatos base;

	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> saluda() {
		log.info("Regresando todos los usuarios");
		return ResponseEntity.status(HttpStatus.OK).body(base.findAll());
	}
	
	@PostMapping("/user")
	public ResponseEntity<String> add(@RequestBody Usuario usr){
		log.info("Añadiendo nuevo usuario");
		base.save(usr);
		log.info("El tamaño es {}",base.findAll().size());
		return ResponseEntity.status(HttpStatus.OK).body("Se genero usuario nuevo");
	}
		

}
