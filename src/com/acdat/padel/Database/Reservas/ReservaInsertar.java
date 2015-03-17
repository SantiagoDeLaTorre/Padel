package com.acdat.padel.Database.Reservas;

public class ReservaInsertar {
	public String fecha, token, hora;
	int duracion, reservado, id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public int getReservado() {
		return reservado;
	}
	public void setReservado(int reservado) {
		this.reservado = reservado;
	}
	public ReservaInsertar(String fecha, String token, String hora,
			int duracion, int reservado) {
		super();
		this.fecha = fecha;
		this.token = token;
		this.hora = hora;
		this.duracion = duracion;
		this.reservado = reservado;
	}
	public ReservaInsertar(int id, String fecha, String token, String hora,
			int duracion, int reservado) {
		super();
		this.fecha = fecha;
		this.token = token;
		this.hora = hora;
		this.duracion = duracion;
		this.reservado = reservado;
		this.id = id;
	}
}
