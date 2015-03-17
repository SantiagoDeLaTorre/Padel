package com.acdat.padel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.acdat.padel.Controlador.GestorPreferencias;
import com.acdat.padel.Controlador.ReponseBorrar;
import com.acdat.padel.Controlador.ReservaAdapter;
import com.acdat.padel.Database.Connection;
import com.acdat.padel.Database.DatabaseApiConection;
import com.acdat.padel.Database.Reservas.Reserva;
import com.acdat.padel.Database.Reservas.ReservaDatasource;
import com.acdat.padel.Database.Reservas.ReservaInsertar;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class EditarActivity extends ListActivity {

	ReservaAdapter adapter;
	int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar);
		getListView().setBackgroundResource(R.color.amarillo);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				EditarActivity.this.pos = position;
				AlertDialog.Builder ab = new AlertDialog.Builder(EditarActivity.this);
				ab.setTitle("Borrado");
				ab.setMessage("¿Esta seguro de que desea Borrar la reserva?");
				ab.setPositiveButton("Aceptar", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ReservaAdapter ra = (ReservaAdapter) getListView().getAdapter();
						Reserva res = ra.getItem(EditarActivity.this.pos);
						new ConexionAsincronaBorrarReserva(EditarActivity.this,	EditarActivity.this.pos).execute(res);
						dialog.dismiss();
					}
				});
				ab.setNegativeButton("Cancelar", null);
				ab.show();
				return true;
			}
		});
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ReservaAdapter ra = (ReservaAdapter) getListView().getAdapter();
				Reserva res = ra.getItem(position);
				startActivity(new Intent(EditarActivity.this,
						EditarReservaActivity.class).putExtra("id", res.getId()));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Cursor c = new ReservaDatasource(this).getReservaID(GestorPreferencias
				.getID(this));
		ArrayList<Reserva> lista = getReservas(c);
		adapter = new ReservaAdapter(this, lista);
		getListView().setAdapter(adapter);
	}
	
	public int getPostion(){
		return this.pos;
	}

	private void mostrarToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private ArrayList<Reserva> getReservas(Cursor c) {
		ArrayList<Reserva> lis = new ArrayList<Reserva>();
		if (c != null) {
			while (c.moveToNext()) {
				Reserva r = new Reserva(c.getInt(0), c.getString(1),
						c.getString(2), c.getString(3), c.getInt(4),
						c.getInt(5));
				lis.add(r);
			}
			c.close();
		}
		return lis;
	}

	private class ConexionAsincronaBorrarReserva extends
			AsyncTask<Reserva, String, ReponseBorrar> {
		private ProgressDialog dialogo;
		private Context contexto;
		private Reserva reserva;
		private int posicion;

		public ConexionAsincronaBorrarReserva(Context c, int pos) {
			this.contexto = c;
			posicion = pos;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogo = new ProgressDialog(contexto);
			dialogo.setIndeterminate(true);
			dialogo.setTitle("Conectando . . . ");
			dialogo.setMessage("Preparando . . . ");
			dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialogo.show();
		}

		@Override
		protected ReponseBorrar doInBackground(Reserva... params) {

			Gson gson = new Gson();
			ReponseBorrar response = new ReponseBorrar();
			DatabaseApiConection myDb;
			InputStreamReader in = null;
			reserva = params[0];
			myDb = new DatabaseApiConection();
			publishProgress("Borrando...");
			try {
				in = myDb.borrarReserva(GestorPreferencias.getToken(contexto),
						String.valueOf(reserva.getId()));
				if (in != null) {
					response = gson.fromJson(in, ReponseBorrar.class);
					response.setCode(Connection.NETWORK_OK);
				} else {
					response.setCode(Connection.NETWORK_NOT_FOUND);
					response.setMessage("Error de comunicación con el servidor");
				}
			} catch (JsonSyntaxException e) {
				Log.e("Error", e.getMessage());
				response.setMessage("Error de sintaxis JSON\n" + e.getMessage());
			} catch (JsonIOException ex) {
				Log.e("Error", ex.getMessage());
				response.setMessage("Error de I/O JSON\n" + ex.getMessage());
			} catch (ClientProtocolException excepti) {
				Log.e("Error", excepti.getMessage());
				response.setMessage("Error de Protocolo\n"
						+ excepti.getMessage());
			} catch (IOException exception) {
				Log.e("Error", exception.getMessage());
				response.setMessage("Error de I/O\n" + exception.getMessage());
			}
			return response;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			dialogo.setMessage(progress[0]);
		}

		@Override
		protected void onPostExecute(ReponseBorrar result) {
			super.onPostExecute(result);

			dialogo.dismiss();
			if (result != null) {
				if (result.getCode() >= Connection.NETWORK_ERROR)
					mostrarToast(result.getCode() + ":" + result.getMessage());
				else {
					if (result.getCode() == Connection.NETWORK_OK) {
						if (result.isHecho()) {
							ReservaAdapter ra = (ReservaAdapter) getListView()
									.getAdapter();
							ra.borrar(posicion);
							new ReservaDatasource(contexto).borrarReserva(reserva);
						}
						mostrarToast(result.getMessage());
					}
				}
			}

		}
	}

}
