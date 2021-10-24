package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Metro")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Metro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String numeroIdTarjeta;
	private String urlImage;
	private boolean encendido;
	private boolean puntosActivos;

}
