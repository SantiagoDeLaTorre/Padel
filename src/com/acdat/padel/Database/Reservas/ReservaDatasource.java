package com.acdat.padel.Database.Reservas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.acdat.padel.Database.DatabaseHelper;
import com.acdat.padel.Database.Jugadores.JugadoresTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReservaDatasource {
	SQLiteDatabase db;
	DatabaseHelper dbHelper;

	public ReservaDatasource(Context context) {
		dbHelper = DatabaseHelper.getInstance(context);
		db = dbHelper.getWritableDatabase();
	}

	public void insertarListaReservas(ArrayList<Reserva> lista) {
		db.execSQL(ReservaTable.drop_table);
		db.execSQL(ReservaTable.create_table);
		for (Reserva item : lista) {
			Log.v("--InsertarReservas--", "Fecha: " + item.getFecha());
			insertar(item);
		}
	}

	public void insertarReserva(Reserva res) {
		insertar(res);
	}
	
	public void borrarReserva(Reserva res) {
		String where = ReservaTable.C_ID + " = " + res.getId();
		db.delete(ReservaTable.Table, where, null);
	}
	
	public void borrarReservaID(int id) {
		String where = ReservaTable.C_ID + " = " + id;
		db.delete(ReservaTable.Table, where, null);
	}

	public Cursor listarReservasMias() {
		return db.query(ReservaTable.Table, ReservaTable.COLUMNS, null, null,
				null, null, null);
	}

	public Cursor getFechas() {
		String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String where = "date(" + ReservaTable.C_Fecha + ") >= date('" + fechaHoy + "')";
		Log.v("---", where);
		return db.query(ReservaTable.Table, new String[] {
				ReservaTable.C_Fecha, ReservaTable.C_ID}, where , null,
				ReservaTable.C_Fecha, null, null, null);
		
	}
	
	public Cursor getReservaID(int id) {
		String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String condicionFecha = "date(" + ReservaTable.C_Fecha + ") >= date('" + fechaHoy + "')";
		String where = ReservaTable.C_Reservado + " = " + id + " and " + condicionFecha;
		return db.query(ReservaTable.Table, ReservaTable.COLUMNS,	where , null, null, null, ReservaTable.C_Fecha, null);
	}

	public Cursor getReservaFecha(String fecha) {
		return db.query(true, ReservaTable.Table, ReservaTable.COLUMNS,
				"fecha = '" + fecha + "'", null, null, null,
				ReservaTable.C_HoraInicio, null);
	}

	private void insertar(Reserva item) {
		ContentValues values = new ContentValues();
		values.put(ReservaTable.C_ID, item.getId());
		values.put(ReservaTable.C_Duracion, item.getDuracion());
		values.put(ReservaTable.C_Fecha, item.getFecha());
		values.put(ReservaTable.C_HoraFin, item.getHoraFin().split(":")[0]
				+ ":" + item.getHoraFin().split(":")[1]);
		values.put(ReservaTable.C_HoraInicio,
				item.getHoraInicio().split(":")[0] + ":"
						+ item.getHoraInicio().split(":")[1]);
		values.put(ReservaTable.C_Reservado, item.getReservado());
		long id = db.insert(ReservaTable.Table, null, values);
		Log.v("--", String.valueOf(id + " / " + values.toString()));
		
	}
}
