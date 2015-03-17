package com.acdat.padel.Database.Reservas;


public class Reserva {
	private int id, duracion, reservado;
	private String fecha, horaInicio, horaFin;
	public Reserva(int id, String fecha, String horaInicio, String horaFin, int duracion, int reservado) {
		super();
		this.id = id;
		this.duracion = duracion;
		this.reservado = reservado;
		this.fecha = fecha;
		this.horaFin = horaFin;
		this.horaInicio = horaInicio;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public String getHoraFin() {
		return horaFin;
	}
	public int getId() {
		return id;
	}
	public void setId(int i) {
		this.id = i;
	}
	public int getDuracion() {
		return duracion;
	}
	public int getReservado() {
		return reservado;
	}
	public String getFecha() {
		return fecha;
	}
}
