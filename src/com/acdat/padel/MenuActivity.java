package com.acdat.padel;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.acdat.padel.Controlador.GestorPreferencias;
import com.acdat.padel.Database.Connection;
import com.acdat.padel.Database.DatabaseApiConection;
import com.acdat.padel.Database.Jugadores.JugadoresDatasource;
import com.acdat.padel.Database.Jugadores.ResponseJugadores;
import com.acdat.padel.Database.Reservas.ReservaDatasource;
import com.acdat.padel.Database.Reservas.ResponseReservas;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class MenuActivity extends Activity {

	Button invitarJugador, anadirReserva, consultarFecha, editar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		invitarJugador = (Button) findViewById(R.id.menBoToNInvitar);
		anadirReserva = (Button) findViewById(R.id.menBoToNAnadir);
		consultarFecha = (Button) findViewById(R.id.menBoToNConsultar);
		editar = (Button) findViewById(R.id.menBoToNEdit);
		editar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this, EditarActivity.class));				
			}
		});
		consultarFecha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this, ConsultarFechaReservaActivity.class));
			}
		});
		anadirReserva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this,
						AnadirReservaActivity.class));
			}
		});
		invitarJugador.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MenuActivity.this,
						InvitarActivity.class));
			}
		});
		getActionBar().setTitle("Bienvenido, " + GestorPreferencias.getNombre(this));
		new ConexionAsincronaJugadores(this).execute();
		
	}

	private void mostrarToast(String msg) {
		Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private class ConexionAsincronaJugadores extends
			AsyncTask<String, String, ResponseJugadores> {
		private ProgressDialog dialogo;
		private Context contexto;

		public ConexionAsincronaJugadores(Context c) {
			this.contexto = c;
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
		protected ResponseJugadores doInBackground(String... params) {

			Gson gson = new Gson();
			ResponseJugadores response = new ResponseJugadores();
			DatabaseApiConection myDb;
			InputStreamReader in = null;

			myDb = new DatabaseApiConection();
			publishProgress("Actualizando Base de Datos Usuarios...");

			try {
				in = myDb.DescargaUsuarios();
				if (in != null) {
					response = gson.fromJson(in, ResponseJugadores.class);
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
		protected void onPostExecute(ResponseJugadores result) {
			super.onPostExecute(result);

			dialogo.dismiss();
			if (result != null) {
				if (result.getCode() >= Connection.NETWORK_ERROR)
					mostrarToast(result.getCode() + ":" + result.getMessage());
				else {
					if (result.getCode() == Connection.NETWORK_OK) {
						new JugadoresDatasource(contexto)
								.insertarListaClientes(result.getLista());
					}
				}
			} else
				mostrarToast("Hubo un error en la descarga de usuarios");
			new ConexionAsincronaReservas(contexto).execute();
		}
	}

	private class ConexionAsincronaReservas extends
			AsyncTask<String, String, ResponseReservas> {
		private ProgressDialog dialogo;
		private Context contexto;

		public ConexionAsincronaReservas(Context c) {
			this.contexto = c;
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
		protected ResponseReservas doInBackground(String... params) {

			Gson gson = new Gson();
			ResponseReservas response = new ResponseReservas();
			DatabaseApiConection myDb;
			InputStreamReader in = null;

			myDb = new DatabaseApiConection();
			publishProgress("Actualizando Base de Datos Reservas...");

			try {
				in = myDb.DescargaReservas();
				if (in != null) {
					response = gson.fromJson(in, ResponseReservas.class);
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
		protected void onPostExecute(ResponseReservas result) {
			super.onPostExecute(result);

			dialogo.dismiss();
			if (result != null) {
				if (result.getCode() >= Connection.NETWORK_ERROR)
					mostrarToast(result.getCode() + ":" + result.getMessage());
				else {
					if (result.getCode() == Connection.NETWORK_OK) {
						ReservaDatasource rd = new ReservaDatasource(contexto);
						rd.insertarListaReservas(result.getLista());
					}
				}
			} else
				mostrarToast("Hubo un error en la descarga de reservas");
		}
	}
}
