package com.acdat.padel;

import java.util.ArrayList;

import com.acdat.padel.Controlador.ReservaAdapter;
import com.acdat.padel.Database.Jugadores.Jugador;
import com.acdat.padel.Database.Jugadores.JugadoresDatasource;
import com.acdat.padel.Database.Reservas.Reserva;
import com.acdat.padel.Database.Reservas.ReservaDatasource;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

public class ListarReservaActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String fecha = getIntent().getStringExtra("fecha");
		Cursor c = new ReservaDatasource(this).getReservaFecha(fecha);
		ArrayList<Reserva> lista = getReservas(c);
		ReservaAdapter adapter = new ReservaAdapter(this, lista);
		getListView().setAdapter(adapter);
		getActionBar().setTitle(fecha);
		getListView().setBackgroundResource(R.color.amarillo);
	}
	
	private ArrayList<Reserva> getReservas(Cursor c){
		ArrayList<Reserva> lis = new ArrayList<Reserva>();
		if (c != null) {
		    while(c.moveToNext()) {
		    	Reserva r = new Reserva(c.getInt(0), c.getString(1),c.getString(2), c.getString(3), c.getInt(4), c.getInt(5));
		        lis.add(r);
		    }
		    c.close();
		}
		return lis;		
	}
}
