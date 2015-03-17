package com.acdat.padel.Database.Reservas;

public class ReservaTable {
	public static String C_ID = "_id";
	public static String C_Fecha = "fecha";
	public static String C_HoraInicio = "horaInicio";
	public static String C_HoraFin = "horaFin";
	public static String C_Duracion = "duracion";
	public static String C_Reservado = "reservado";
	
	public static String[] COLUMNS = {C_ID, C_Fecha,C_HoraInicio, C_HoraFin, C_Duracion, C_Reservado};
	
	public static String Table = "reservas";
	
	public static String create_table = "create table reservas(	_id integer primary key autoincrement, fecha text,"
			+ "horaInicio text, horaFin text, duracion integer, reservado integer, foreign key (reservado) references jugadores(id))";
	public static String drop_table = "drop table if exists reservas";
	public static String Truncate = "delete from reservas";
}
