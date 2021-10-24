package com.example.demo.controllers;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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

import com.example.demo.dto.AltaTarjetaMetro;
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
	public ResponseEntity<Mensaje> login(@RequestBody UsuarioTestLogin usr) {
		Optional<UsuarioTestLogin> usuario = usrRepo.findByUsuarioAndPassword(usr.getUsuario(), usr.getPassword());

		if (usuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Usuario Autenticado"));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Mensaje("Usuario o contraseña incorrectos"));
		}
	}

	@GetMapping("/v1/usuario/{usr}")
	public ResponseEntity<?> getUsuario(@PathVariable("usr") String usr) {
		Optional<UsuarioTestLogin> usuario = usrRepo.findByUsuario(usr);
		if (!usuario.isPresent())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));
		return ResponseEntity.status(HttpStatus.OK).body(usuario.get());
	}

	@GetMapping("/v1/all")
	public ResponseEntity<Iterable<UsuarioTestLogin>> getAll() {
		// usrRepo.
		return ResponseEntity.status(HttpStatus.OK).body(usrRepo.findAll());
	}

	@PostMapping("/v1/tarjeta/user/{user}")
	public ResponseEntity<Mensaje> addTarjeta(@PathVariable("user") String user, @RequestBody Tarjeta tarj) {
		System.out.println("El usuario es : " + user);
		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(user);
		// usrRepo.deleteByUsuario(user);
		if (!usr.isPresent())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));
		List<Tarjeta> tarjetas = usr.get().getTarjetas();
		tarjetas.add(tarj);
		usr.get().setTarjetas(tarjetas);
		System.out.println(usr.get());
		usrRepo.save(usr.get());
		return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Tarjeta Añadida"));
	}

	@PostMapping("/v1/sincroniza/tarjetametro")
	public ResponseEntity<Mensaje> addTarjetaMetro(@RequestBody AltaTarjetaMetro altaMetro) {
		String user = altaMetro.getUser();
		String numeroTarjeta = altaMetro.getNumeroTarjeta();
		String activa = altaMetro.getActiva();
		String puntos = altaMetro.getPuntos();
		
		
		
		Metro metro = new Metro(0,String.valueOf(getRandomString(19)),activa.equals("true"),puntos.equals("true"));

		// se busca el usuario a modificar
		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(user);
		// usrRepo.deleteByUsuario(user);
		if (!usr.isPresent())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje("No existe el usuario"));

		// se obtienen las tarjetas del usuario
		List<Tarjeta> tarjetas = usr.get().getTarjetas();
		Tarjeta tarjetaFind = null;

		// se busca la tarjeta a sincronizar
		for (Tarjeta t : tarjetas) {
			if (t.getNumeroTarjeta().equals(numeroTarjeta)) {
				tarjetaFind = t;
				break;
			}
		}
		// se sincroniza la tarjeta con la nueva tarjeta metro
		tarjetaFind.getTarjetasMetro().add(metro);

		usr.get().setTarjetas(tarjetas);

		usrRepo.save(usr.get());
		return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Tarjeta Añadida"));
	}

	@PostMapping("/pago/usuario/{usuario}/idTarjeta/{idTarjeta}/cant/{cant}")
	public ResponseEntity<?> simulaPago(@PathVariable("usuario") String usuario,
			@PathVariable("idTarjeta") String idTarjeta, @PathVariable("cant") String cant) {

		Optional<UsuarioTestLogin> usr = usrRepo.findByUsuario(usuario);
		List<Tarjeta> tarjetasBanco = usr.get().getTarjetas();
		Tarjeta tarjetaFind = null;
		Metro metroFind = null;
		for (Tarjeta t : tarjetasBanco) {
			List<Metro> metros = t.getTarjetasMetro();
			for (Metro m : metros) {
				if (m.getNumeroIdTarjeta().equals(idTarjeta)) {
					tarjetaFind = t;
					metroFind = m;
					break;
				}
			}
		}
		
		
		Tarjeta tarjetaFindOriginal = tarjetaFind;
		System.out.println("tarjeta find "+tarjetaFind);
		System.out.println("tarjeta metro "+metroFind);
		

		double saldoTarjeta = tarjetaFind.getSaldo();
		double pago = Double.parseDouble(cant);
		double puntos = usr.get().getPuntos();
		double puntosEnPesos = puntos * 0.07;
		double pagoEnPuntos = pago * 14;

		System.out.println("Puntos activos? "+metroFind.isPuntosActivos());
		if (metroFind.isPuntosActivos()) {
			if (pagoEnPuntos > puntos) { // no te alcanzan los puntos
				System.out.println("no te alcanzan los puntos");
				if ((puntosEnPesos + saldoTarjeta) < pago) { // si puntos + saldo te alcanza
					System.out.println("te alzanza puntos + saldos");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new Mensaje("Puntos y Saldo insuficiente"));
				} else {

					usr.get().setPuntos(0);

					tarjetaFind.setSaldo((saldoTarjeta + puntosEnPesos) - (pago));
					usr.get().getTarjetas().remove(tarjetaFindOriginal);
					usr.get().getTarjetas().add(tarjetaFind);
					System.out.println("3" + usr.get());
					usrRepo.save(usr.get());

					return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Pago correcto"));
				}
			} else { // si te alcanzan los puntos
				System.out.println("si te alcanzan los puntos");
				if((saldoTarjeta - pago) < 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje("Saldo insuficiente"));
				}else {
					usr.get().setPuntos((long) (usr.get().getPuntos() - pagoEnPuntos));
					usrRepo.save(usr.get());
					return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Pago correcto"));
				}

				
			}

		} else {
			System.out.println("saldo tarjeta"+ saldoTarjeta);
			System.out.println("pago"+ pago);
			if ((saldoTarjeta - pago) < 0) {
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje("Saldo insuficiente"));
			} else {
				tarjetaFind.setSaldo(saldoTarjeta - pago);
				usr.get().getTarjetas().remove(tarjetaFindOriginal);
				usr.get().getTarjetas().add(tarjetaFind);
				usrRepo.save(usr.get());
				return ResponseEntity.status(HttpStatus.OK).body(new Mensaje("Pago correcto"));
			}
		}

	}

	public String getRandomString(int t) {
		StringBuilder num = new StringBuilder("");
		int pos = 4;
		for(int i=0; i < t;i++) {
			Random claseRandom = new Random(); // Esto crea una instancia de la Clase Random
			if(i== pos) {
				num.append("-");
				pos+=5;
			}else {
				num.append(claseRandom.nextInt(9));
			}
		}
		return num.toString();
	}

}
