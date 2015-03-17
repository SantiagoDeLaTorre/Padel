package com.acdat.padel.Database.Reservas;

import java.util.ArrayList;

public class ResponseReservas {
	int code;
	public String message;
	ArrayList<Reserva> lista;

	public ArrayList<Reserva> getLista() {
		return lista;
	}

	public void setLista(ArrayList<Reserva> lista) {
		this.lista = lista;
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
