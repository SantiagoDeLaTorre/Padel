package com.acdat.padel.Database.Jugadores;

public class Jugador {
	
	String nombre, email;
	boolean check;

	public String getNombre() {
		return nombre;
	}

	public String getEmail() {
		return email;
	}
	

	public Jugador(String nombre, String email) {
		super();
		this.nombre = nombre;
		this.email = email;
	}

	public boolean getCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}
}
