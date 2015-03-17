package com.acdat.padel;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;

import com.acdat.padel.Controlador.GestorPreferencias;
import com.acdat.padel.Controlador.Registro;
import com.acdat.padel.Controlador.ReponseInsertar;
import com.acdat.padel.Controlador.ResponseLogin;
import com.acdat.padel.Database.Connection;
import com.acdat.padel.Database.DatabaseApiConection;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends Activity {

	EditText nombre, password, passRep, email;
	Button boton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		nombre = (EditText)findViewById(R.id.regedtNombre);
		password = (EditText)findViewById(R.id.regEdtPass);
		passRep = (EditText)findViewById(R.id.regEdtPassREP);
		email = (EditText)findViewById(R.id.regedtEmail);
		boton = (Button)findViewById(R.id.btnRegistrar);
		boton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(nombre.getText().toString().isEmpty())
					mostrarToast("El nombre no puede estar vacio");
				else if(email.getText().toString().isEmpty())
					mostrarToast("El email no puede estar vacio");
				else if(password.getText().toString().isEmpty())
					mostrarToast("El password no puede estar vacio");
				else if(passRep.getText().toString().isEmpty())
					mostrarToast("El password repetido no puede estar vacio");
				else if(!password.getText().toString().equals(passRep.getText().toString())){
					mostrarToast("Los passwords no coinciden");
					Log.v("---", password.getText().toString() + " : " + passRep.getText().toString());
			}else{
					Registro reg = new Registro(nombre.getText().toString(), email.getText().toString(), password.getText().toString());
					new ConexionAsincrona(RegistroActivity.this, reg).execute();
				}
			}
		});
	}

	private void mostrarToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private class ConexionAsincrona extends
			AsyncTask<String, String, ReponseInsertar> {
		private ProgressDialog dialogo;
		private Context contexto;
		private Registro registro;

		public ConexionAsincrona(Context c, Registro reg) {
			this.contexto = c;
			registro = reg;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialogo = new ProgressDialog(contexto);
			dialogo.setIndeterminate(true);
			dialogo.setTitle("Conectando . . . ");
			dialogo.setMessage("Preparando . . . ");
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
			Toast.makeText(contexto, "Login Cancelado", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected ReponseInsertar doInBackground(String... params) {
			Gson gson = new Gson();
			ReponseInsertar response = new ReponseInsertar();
			DatabaseApiConection myDb;
			InputStreamReader in = null;

			myDb = new DatabaseApiConection();
			publishProgress("Registrando . . . ");

			try {
				String dato = new Gson().toJson(registro);
				Log.v("", dato);
				in = myDb.registro(dato);
				if (in != null) {
					response = gson.fromJson(in, ReponseInsertar.class);
					response.setCode(Connection.NETWORK_OK);
				} else {
					response.setCode(Connection.NETWORK_NOT_FOUND);
					response.setMessage("Error de comunicación con el servidor");
				}
			} catch (JsonSyntaxException e) {
				Log.e("----Error", e.getMessage());
				response.setMessage("Error de sintaxis JSON\n" + e.getMessage());
			} catch (JsonIOException ex) {
				Log.e("---Error", ex.getMessage());
				response.setMessage("Error de I/O JSON\n" + ex.getMessage());
			} catch (ClientProtocolException excepti) {
				Log.e("--Error", excepti.getMessage());
				response.setMessage("Error de Protocolo\n"
						+ excepti.getMessage());
			} catch (IOException exception) {
				Log.e("-Error", exception.getMessage());
				response.setMessage("Error de I/O\n" + exception.getMessage());
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
			if (result != null) {
				if (result.getCode() >= Connection.NETWORK_ERROR)
					mostrarToast(result.getMessage());
				else {
					if (result.getCode() == Connection.NETWORK_OK) {
						if(result.getId() == -3)
						mostrarToast(result.getMessage());
						else{
						mostrarToast(result.getMessage());
						finish();
						}
					}
				}
			}
		}
	}

}
