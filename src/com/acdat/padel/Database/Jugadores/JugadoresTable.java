package com.acdat.padel.Database.Jugadores;

public class JugadoresTable {
	public static String C_NOMBRE = "nombre";
	public static String C_Email = "email";
	
	public static String[] COLUMNS = {C_NOMBRE, C_Email};
	
	public static String Table = "jugadores";
	
	public static String create_table = "create table jugadores(nombre varchar(50), email varchar(50))";
	public static String drop_table = "drop table if exists jugadores";
	public static String Truncate = "delete from jugadores";
}
