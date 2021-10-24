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
import com.example.demo.entity.Metro;
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
	public ResponseEntity<Iterable<UsuarioTestLogin>> getAll(){
		//usrRepo.
		return ResponseEntity.status(HttpStatus.OK).body(usrRepo.findAll());
	}
	
	@PostMapping("/v1/tarjeta/user/{user}")
	public ResponseEntity<Mensaje> addTarjeta(@PathVariable("user") String user, @RequestBody Tarjeta tarj){
		System.out.println("El usuario es : "+user);
		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(user);
		//usrRepo.deleteByUsuario(user);
		if(!usr.isPresent())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));
		List<Tarjeta> tarjetas = usr.get().getTarjetas();
		tarjetas.add(tarj);
		usr.get().setTarjetas(tarjetas);
		System.out.println(usr.get());
		usrRepo.save(usr.get());		
		return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Tarjeta Añadida"));
	}
	
//	@PostMapping("/v1/metro/user/{user}/{numeroTarjeta}")
//	public ResponseEntity<Mensaje> addTarjeta(@PathVariable("user") String user,
//			@PathVariable("numeroTarjeta") String numeroTarjeta, @RequestBody Metro metro){
//		
//		
//		// se busca el usuario a modificar
//		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(user);
//		
//		
//		
//		//usrRepo.deleteByUsuario(user);
//		if(!usr.isPresent())
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));
//		
//		//se obtienen las tarjetas del usuario
//		List<Tarjeta> tarjetas = usr.get().getTarjetas();
//		Tarjeta tarjetaFind = null;
//		
//		//se busca la tarjeta a sincronizar
//		for(Tarjeta t: tarjetas) {
//			if(t.getNumeroTarjeta().equals(numeroTarjeta)) {
//				tarjetaFind = t;
//				break;
//			}
//		}
//		//se sincroniza la tarjeta con la nueva tarjeta metro
//		tarjetaFind.getTarjetasMetro().add(metro);
//		
//		//se
//		usr.get().getTarjetas()
//		
//		
//		usr.get().setTarjetas(tarjetas);
//		System.out.println(usr.get());
//		usrRepo.save(usr.get());		
//		return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Tarjeta Añadida"));
//	}
	
	
	
	@PostMapping("/pago/usuario/{usuario}/idTarjeta/{idTarjeta}/cant/{cant}")
	public ResponseEntity<?> simulaPago(@PathVariable("usuario") String usuario,
			@PathVariable("idTarjeta") String idTarjeta,@PathVariable("cant") String cant) {
		
		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(usuario);
		List<Tarjeta> tarjetasBanco = usr.get().getTarjetas();
		Tarjeta tarjetaFind = null;
		Metro metroFind = null;
		for(Tarjeta t :tarjetasBanco){
			List<Metro> metros = t.getTarjetasMetro();
			for(Metro m : metros) {
				if(m.getNumeroIdTarjeta().equals(idTarjeta)) {
					tarjetaFind = t;
					metroFind = m;
					break;
				}
			}
		}
		
		double saldoTarjeta =tarjetaFind.getSaldo() ;
		double pago = Double.parseDouble(cant);
		double puntos = usr.get().getPuntos();
		double puntosEnPesos = puntos * 0.7;
		double pagoEnPuntos = pago * 14;
	
	
		if(metroFind.isPuntosActivos()) {
			if(pagoEnPuntos > puntos) { // no te alcanzan los puntos
				if((pagoEnPuntos + saldoTarjeta) < pago  ) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje("Puntos y Saldo insuficiente"));
				}else {
					usr.get().setPuntos(0);
					tarjetaFind.setSaldo((saldoTarjeta + puntosEnPesos ) - (pago));
					usr.get().getTarjetas().add(tarjetaFind);
					usrRepo.save(usr.get());
					return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Pago correcto"));
				}
			}else { // si te alcanzan los puntos
				usr.get().setPuntos((long)(usr.get().getPuntos() - pagoEnPuntos));
				usrRepo.save(usr.get());
				return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Pago correcto"));
			}
			
		}else {
			if(saldoTarjeta - pago < 0){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje("Saldo insuficiente"));
			}else {
				tarjetaFind.setSaldo(saldoTarjeta - pago);
				usr.get().getTarjetas().add(tarjetaFind);
				usrRepo.save(usr.get());
				return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Pago correcto"));
			}
		}
	
		
		
		
		
		
	}

	
	
	
	
	
}
