package com.acdat.padel.Controlador;

public class ReponseInsertar {
	String message;
	boolean insertado;
	int id, code;
	
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
	public boolean isInsertado() {
		return insertado;
	}
	public void setInsertado(boolean insertado) {
		this.insertado = insertado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
