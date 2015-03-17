package com.acdat.padel.Database.Jugadores;

import java.util.ArrayList;

public class ResponseJugadores {
	
	ArrayList<Jugador> lista;
	int code;
	String message;

	public ArrayList<Jugador> getLista() {
		return lista;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
