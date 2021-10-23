package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Mensaje;
import com.example.demo.entity.Tarjeta;
import com.example.demo.entity.UsuarioTestLogin;
import com.example.demo.repository.IUsuarioTestLoginRepo;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UsuarioTestLoginController {

	@Autowired
	private IUsuarioTestLoginRepo usrRepo;
	
	
	@PostMapping("/v1/login")
	public ResponseEntity<Mensaje> login(@RequestBody UsuarioTestLogin usr){
		Optional<UsuarioTestLogin> usuario = usrRepo.findByUsuarioAndPassword(usr.getUsuario(),usr.getPassword());
		
		if(usuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Usuario Autenticado"));
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Mensaje("Usuario o contraseña incorrectos"));
		}
	}
	
	@GetMapping("/v1/usuario/{usr}")
	public ResponseEntity<?> getUsuario(@PathVariable("usr") String usr){
		Optional<UsuarioTestLogin> usuario = usrRepo.findByUsuario(usr);
		if(!usuario.isPresent())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));
		return ResponseEntity.status(HttpStatus.OK).body(usuario.get());
	}
	
	@GetMapping("/v1/all")
	public ResponseEntity<List<UsuarioTestLogin>> getAll(){
		//usrRepo.
		return ResponseEntity.status(HttpStatus.OK).body(usrRepo.findAll());
	}
	
//	@PostMapping("/v1/tarjeta/user/{user}")
//	public ResponseEntity<Mensaje> addTarjeta(@PathVariable("user") String user, @RequestBody Tarjeta tarj){
//		System.out.println("El usuario es : "+user);
//		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(user);
//		usrRepo.deleteByUsuario(user);
//		if(!usr.isPresent())
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));
//		List<Tarjeta> tarjetas = usr.get().getTarjetas();
//		tarjetas.add(tarj);
//		usr.get().setTarjetas(tarjetas);
//		System.out.println(usr.get());
//		usrRepo.save(usr.get());		
//		return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Tarjeta Añadida"));
//	}
	
	
}
