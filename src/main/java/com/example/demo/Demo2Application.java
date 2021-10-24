package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Metro;
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

			List<Metro> metros1 = new ArrayList<>();
			metros1.add(new Metro(1,"2144-1234-7654-3289","",true,true));
			List<Tarjeta> tarjetas1 = new ArrayList<>();
			tarjetas1.add(new Tarjeta(1,"Premium",100, "1111-2222-3333-4567","4567" ,"22/10/21", "Visa",null));
			repo.save(new UsuarioTestLogin(1,"Usuario","Usuario","3333",1000,tarjetas1));
	
		}
	

}
