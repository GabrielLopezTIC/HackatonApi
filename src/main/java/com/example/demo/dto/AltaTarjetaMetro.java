package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AltaTarjetaMetro {
	
	private String user;
    private String numeroTarjeta;
    private String activa;
    private String puntos;

}
