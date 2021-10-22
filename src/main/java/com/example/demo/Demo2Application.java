package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		System.out.println(repo.count());
		if(repo.count() == 0) {
			repo.save(new UsuarioTestLogin(1,"Rodrigo","qwert")); 
		}
	}

}
