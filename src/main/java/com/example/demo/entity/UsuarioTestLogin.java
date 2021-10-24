package com.example.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "UsuarioTestLogin")
@ToString
public class UsuarioTestLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String usuario;
	private String password;
	private String numeroCuenta;
	private long puntos;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Tarjeta> tarjetas;
}
