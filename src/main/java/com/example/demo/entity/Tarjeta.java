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

@Entity
@Table(name = "Tarjeta")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Tarjeta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nombre;
	private double saldo;
	private String numeroTarjeta;
	private String idTarjeta;
	private String fechaExp;
	private String tipo;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Metro> tarjetasMetro;
	
}
