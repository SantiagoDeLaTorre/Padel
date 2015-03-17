package com.acdat.padel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.acdat.padel.Controlador.Email;
import com.acdat.padel.Controlador.JugadoresAdaper;
import com.acdat.padel.Controlador.ReponseInsertar;
import com.acdat.padel.Controlador.Response;
import com.acdat.padel.Database.DatabaseApiConection;
import com.acdat.padel.Database.Jugadores.Jugador;
import com.acdat.padel.Database.Jugadores.JugadoresDatasource;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InvitarActivity extends ListActivity {

	ArrayList<Jugador> lista;
	Button boton;
	EditText titulo, mensaje, amigo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitar);
		lista = getArrayJugadores();
		boton = (Button) findViewById(R.id.btnEmail);
		titulo = (EditText) findViewById(R.id.InvEDTAsunto);
		mensaje = (EditText) findViewById(R.id.InvEdtMensaje);
		amigo = (EditText) findViewById(R.id.InvAmigo);
		boton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titulo.getText().toString().isEmpty()) {
					mostrarToast("Debe de poner un titulo");
					return;
				}
				if (mensaje.getText().toString().isEmpty()) {
					mostrarToast("Debe de poner un titulo");
					return;
				}
				for (Jugador item : lista) {
					if (item.getCheck()) {
						Email email = new Email(item.getEmail(), titulo
								.getText().toString(), mensaje.getText()
								.toString());
						new ConexionAsincrona(InvitarActivity.this)
								.execute(email);
					}
				}
				if (!amigo.getText().toString().isEmpty()) {
					Email email = new Email(amigo.getText().toString(), titulo
							.getText().toString(), mensaje.getText().toString());
					new ConexionAsincrona(InvitarActivity.this).execute(email);
				}
			}

		});
		JugadoresAdaper adapter = new JugadoresAdaper(this, lista);
		getListView().setAdapter(adapter);
	}

	public ArrayList<Jugador> getArrayJugadores() {
		ArrayList<Jugador> lis = new ArrayList<Jugador>();
		JugadoresDatasource jd = new JugadoresDatasource(this);
		Cursor c = jd.listarClientes(this);
		if (c != null) {
			while (c.moveToNext()) {
				Jugador j = new Jugador(c.getString(0), c.getString(1));
				lis.add(j);
			}
			c.close();
		}
		return lis;
	}

	private void mostrarToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	private class ConexionAsincrona extends AsyncTask<Email, String, ReponseInsertar> {
		private ProgressDialog dialogo;
		private Context contexto;
		private String ema;

		public ConexionAsincrona(Context c) {
			// poner el contexto de la actividad desde la que se crea
			this.contexto = c;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogo = new ProgressDialog(contexto);
			dialogo.setIndeterminate(true);
			dialogo.setTitle("Conectando . . . ");
			dialogo.setMessage("Preparando . . . ");
			// dialogo.setMessage(contexto.getResources().getString(R.string.Mensaje));
			dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialogo.setCancelable(true);
			dialogo.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					ConexionAsincrona.this.cancel(true);
				}
			});
			dialogo.show();
		}

		@Override
		protected void onCancelled() {
			dialogo.dismiss();
			// mostrar cancelación
			Toast.makeText(contexto, "Cancelado", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected ReponseInsertar doInBackground(Email... params) {
			Gson gson = new Gson();
			ReponseInsertar response = new ReponseInsertar();
			DatabaseApiConection myDb;

			myDb = new DatabaseApiConection();
			InputStreamReader in = null;
			ema = params[0].getCorreo();
			publishProgress("Enviando email . . . ");
			// Descargar los sitios del servidor de base de datos MySQL
			try {
				String dato = new Gson().toJson(params[0]);
				Log.v("email", dato);
				in = myDb.emailSite(dato);
				if (in != null)
					response = gson.fromJson(in, ReponseInsertar.class);

			} catch (JsonSyntaxException e) {
				Log.e("----Error", e.getMessage());
			} catch (JsonIOException ex) {
				Log.e("---Error", ex.getMessage());
				response = null;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			dialogo.setMessage(progress[0]);
		}

		@Override
		protected void onPostExecute(ReponseInsertar result) {
			super.onPostExecute(result);

			dialogo.dismiss();

			Log.v("-", result.getMessage());
			if (result != null)
				Toast.makeText(contexto,ema + ": "+  result.getMessage(),
						Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(contexto, "Error en el envío del email",
						Toast.LENGTH_SHORT).show();
		}
	}
}
