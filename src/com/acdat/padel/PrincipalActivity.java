package com.acdat.padel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Base64DataException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acdat.padel.Controlador.GestorPreferencias;
import com.acdat.padel.Controlador.ResponseLogin;
import com.acdat.padel.Database.Connection;
import com.acdat.padel.Database.DatabaseApiConection;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class PrincipalActivity extends Activity {

	EditText user, pass;
	Button boton;
	TextView registro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		user = (EditText) findViewById(R.id.logEdtUser);
		pass = (EditText) findViewById(R.id.logEdtPass);
		boton = (Button) findViewById(R.id.logBtn);
		registro = (TextView)findViewById(R.id.txtReg);
		registro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(PrincipalActivity.this, RegistroActivity.class));				
			}
		});
		boton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user.getText().toString().isEmpty())
					mostrarToast("User no puede estar vacio");
				if (pass.getText().toString().isEmpty())
					mostrarToast("User no puede estar vacio");
				String sha1;
				sha1 = toSHA1(pass.getText().toString().getBytes());
				Log.v("---", sha1);
				new ConexionAsincrona(PrincipalActivity.this)
						.execute(new String[] { user.getText().toString(), sha1 });

			}

			public String toSHA1(byte[] hex) {
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("SHA-1");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				return byteArrayToHexString(md.digest(hex));
			}

			public String byteArrayToHexString(byte[] b) {
				String result = "";
				for (int i = 0; i < b.length; i++) {
					result += Integer.toString((b[i] & 0xff) + 0x100, 16)
							.substring(1);
				}
				return result;
			}
		}

		);
		if ( GestorPreferencias.getToken(this) != "") {
			new ConexionAsincrona(PrincipalActivity.this).execute(new String[] {
					GestorPreferencias.getEmail(this), GestorPreferencias.getPass(this) });
		}
	}

	private void mostrarToast(String msg) {
		Toast.makeText(PrincipalActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private class ConexionAsincrona extends
			AsyncTask<String, String, ResponseLogin> {
		private ProgressDialog dialogo;
		private Context contexto;
		private String email, pass;

		public ConexionAsincrona(Context c) {
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
		protected ResponseLogin doInBackground(String... params) {
			Gson gson = new Gson();
			ResponseLogin response = new ResponseLogin();
			DatabaseApiConection myDb;
			InputStreamReader in = null;
			email = params[0];
			pass = params[1];

			myDb = new DatabaseApiConection();
			publishProgress("Logeando . . . ");

			try {
				in = myDb.login(email, pass);
				if (in != null) {
					response = gson.fromJson(in, ResponseLogin.class);
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
		protected void onPostExecute(ResponseLogin result) {
			super.onPostExecute(result);

			dialogo.dismiss();
			if (result != null) {
				if (result.getCode() >= Connection.NETWORK_ERROR)
					mostrarToast(result.getCode() + ":" + result.getMessage());
				else {
					if (result.getCode() == Connection.NETWORK_OK) {
						if (result.getToken().equals("no"))
							mostrarToast("Datos invalidos");
						else {
							GestorPreferencias.GuardarDatos(contexto, email, pass, result.getToken(), result.getId(), result.getNombre());
							Intent i = new Intent(PrincipalActivity.this,
									MenuActivity.class);
							startActivity(i);
						}
					}
				}
				Log.v("---", String.valueOf(Connection.NETWORK_OK));
			} else
				mostrarToast("Error al añadir el sitio");
		}
	}
}
