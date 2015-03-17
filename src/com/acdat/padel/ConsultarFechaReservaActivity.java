package com.acdat.padel;

import com.acdat.padel.Database.Reservas.ReservaDatasource;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ConsultarFechaReservaActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_consultar_fecha_reserva);
		Cursor c = new ReservaDatasource(this).getFechas();
		String[] from = new String[] { "fecha" };
		int[] to = new int[] { R.id.itemFechaText };
		SimpleCursorAdapter sca = new SimpleCursorAdapter(this,
				R.layout.item_fecha, c, from, to);
		getListView().setAdapter(sca);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor sca = (Cursor) parent.getItemAtPosition(position);
				sca.moveToPosition(position);
				startActivity(new Intent(ConsultarFechaReservaActivity.this,
						ListarReservaActivity.class).putExtra("fecha",
						sca.getString(0)));
			}
		});
		getListView().setBackgroundResource(R.color.amarillo);
	}
}
