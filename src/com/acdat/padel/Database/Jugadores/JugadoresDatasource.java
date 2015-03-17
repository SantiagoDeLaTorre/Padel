package com.acdat.padel.Database.Jugadores;

import java.util.ArrayList;

import com.acdat.padel.Controlador.GestorPreferencias;
import com.acdat.padel.Database.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JugadoresDatasource {
	SQLiteDatabase db;
	DatabaseHelper dbHelper;
	
	public JugadoresDatasource(Context context){
		dbHelper = DatabaseHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public void insertarListaClientes(ArrayList<Jugador> lista){
		db.execSQL(JugadoresTable.Truncate);
		for (Jugador item : lista) {
			Log.v("--InsertarJugadores--", "Nombre: " + item.getNombre());
			ContentValues values = new ContentValues();
			values.put(JugadoresTable.C_Email, item.getEmail());
			values.put(JugadoresTable.C_NOMBRE, item.getNombre());
			db.insert(JugadoresTable.Table, null, values);
		}
	}
	
	public Cursor listarClientes(Context con){
		String where = "'" + GestorPreferencias.getEmail(con) + "' != " + JugadoresTable.C_Email;
		return db.query(JugadoresTable.Table, JugadoresTable.COLUMNS, where, null, null, null, null);
	}
}
