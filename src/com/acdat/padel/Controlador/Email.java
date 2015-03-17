package com.acdat.padel.Controlador;

public class Email {

	private String subject, correo, mensaje;



	public Email( String correo, String subject,String mensaje) {
		super();
		this.subject = subject;
		this.correo = correo;
		this.mensaje = mensaje;
	}


	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	

}
