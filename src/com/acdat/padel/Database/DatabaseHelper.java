package com.acdat.padel.Database;

import com.acdat.padel.Database.Jugadores.JugadoresTable;
import com.acdat.padel.Database.Reservas.ReservaTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String database = "padel.sqlite";
	public static final int version = 5;
	private static DatabaseHelper singleton;

	private DatabaseHelper(Context context) {
		super(context, database, null, version);
	}

	public synchronized static DatabaseHelper getInstance(Context con) {
		if (singleton == null)
			singleton = new DatabaseHelper(con);
		return singleton;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(JugadoresTable.create_table);
		db.execSQL(ReservaTable.create_table);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(JugadoresTable.drop_table);
		db.execSQL(ReservaTable.drop_table);
		onCreate(db);
	}

}
