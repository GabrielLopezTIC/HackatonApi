package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Tarjeta;
import com.example.demo.entity.UsuarioTestLogin;
import com.example.demo.repository.IUsuarioTestLoginRepo;

@SpringBootApplication
public class Demo2Application implements CommandLineRunner {

	@Autowired
	private IUsuarioTestLoginRepo repo;
	
	public static void main(String[] args) {
		SpringApplication.run(Demo2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		if(repo.count() == 0) {
			List<Tarjeta> tarjetas = new ArrayList<>();
			tarjetas.add(new Tarjeta(1,"Premium",100, "1111-2222-3333-4567","4567" ,"22/10/21", "Visa"));
			tarjetas.add(new Tarjeta(1,"Basica",100,"1111-2222-3333-8967","4567","22/10/21", "MasterCard"));
			tarjetas.add(new Tarjeta(1,"Platinum",100,"1111-2222-3333-2341","4567","22/10/21", "Visa"));
			//repo.save(new UsuarioTestLogin(1,"Rodrigo","qwert","2222",tarjetas)); 
			repo.save(new UsuarioTestLogin(1,"Gabriel","Gabriel","3333",tarjetas));
			repo.save(new UsuarioTestLogin(1,"Felipe","Felipe","4444",tarjetas));
			repo.save(new UsuarioTestLogin(1,"Rodrigo","Rodrigo","5555",tarjetas));
			repo.save(new UsuarioTestLogin(1,"Jesus","Jesus","6666",tarjetas)); 
		}
	}

}
