package com.acdat.padel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;


import com.acdat.padel.Controlador.GestorPreferencias;
import com.acdat.padel.Controlador.ReponseInsertar;
import com.acdat.padel.Database.Connection;
import com.acdat.padel.Database.DatabaseApiConection;
import com.acdat.padel.Database.Reservas.ComprobarFechas;
import com.acdat.padel.Database.Reservas.Reserva;
import com.acdat.padel.Database.Reservas.ReservaDatasource;
import com.acdat.padel.Database.Reservas.ReservaInsertar;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class EditarReservaActivity extends Activity {

	Calendar fechaHoy;
	Calendar fechaElegida;
	NumberPicker duracion;
	TimePicker hora;
	DatePicker fecha;
	Button boton;
	String[] tiempos = new String[] { "30", "45", "60", "75", "90", "105","120" };
	
	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editar_reserva);
		id = getIntent().getIntExtra("id", -1);
		Log.v("---", String.valueOf(id));
		duracion = (NumberPicker) findViewById(R.id.addNPDuracion);
		hora = (TimePicker) findViewById(R.id.addTime);
		fecha = (DatePicker) findViewById(R.id.addDate);
		boton = (Button) findViewById(R.id.addBTN);

		duracion.setValue(4);
		duracion.setDisplayedValues(tiempos);
		duracion.setMaxValue(tiempos.length - 1);
		duracion.setMinValue(0);
		
		fechaElegida = Calendar.getInstance();
		fechaHoy = Calendar.getInstance();
		fechaHoy.setTimeInMillis(System.currentTimeMillis());

		hora.setIs24HourView(true);

		boton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mensajeError = "";
				fechaElegida.set(fecha.getYear(), fecha.getMonth(),
						fecha.getDayOfMonth(), hora.getCurrentHour(),
						hora.getCurrentMinute());
				
				if(!(mensajeError = ComprobarFechas.comprobarFecha(fechaElegida, fechaHoy)).isEmpty())
					mostrarToast(mensajeError);
				else if(!(mensajeError = ComprobarFechas.comprobarHora(hora)).isEmpty())
					mostrarToast(mensajeError);
				else if(!(mensajeError = ComprobarFechas.comprobarFinReserva(tiempos, duracion, fechaElegida)).isEmpty())
					mostrarToast(mensajeError);
				else{
					int d = fechaElegida.get(Calendar.DAY_OF_MONTH);
					int m = fechaElegida.get(Calendar.MONTH) + 1;
					String dia = (d < 10)?  String.valueOf("0" + d): String.valueOf(d);
					String mes = (m < 10)?  String.valueOf("0" + m): String.valueOf(m);
					String fecha = fechaElegida.get(Calendar.YEAR) + "-" + mes + "-" + dia;
					
					int duracionMinutos = Integer.valueOf(tiempos[duracion.getValue()]);
					String min = String.valueOf(hora.getCurrentMinute());
					String minutos = (min.length() < 2)? "0" + min : min;
					String hor = (hora.getCurrentHour() == 9)? "09" : String.valueOf(hora.getCurrentHour());
					String horaReserva = String.valueOf( hor  +":"+ minutos);
					ReservaInsertar reservaInsertar = new ReservaInsertar(id, fecha, 
														GestorPreferencias.getToken(EditarReservaActivity.this),
														horaReserva,
														duracionMinutos,
														GestorPreferencias.getID(EditarReservaActivity.this));
					String finHoraReserva = ComprobarFechas.formatHora(ComprobarFechas.horaFinal(tiempos, duracion, fechaElegida).getTimeInMillis());
					
					Reserva reserva = new Reserva(id, fecha, horaReserva, finHoraReserva,duracionMinutos,
													GestorPreferencias.getID(EditarReservaActivity.this));
					new ConexionAsincronaInsertarReserva(
							EditarReservaActivity.this, reservaInsertar, reserva).execute();
				}
			}
		});
	}

	private void mostrarToast(String msg) {
		Toast.makeText(EditarReservaActivity.this, msg, Toast.LENGTH_LONG)
				.show();
	}

	private class ConexionAsincronaInsertarReserva extends
			AsyncTask<String, String, ReponseInsertar> {
		private ProgressDialog dialogo;
		private Context contexto;
		private ReservaInsertar res;
		private Reserva reserva;

		public ConexionAsincronaInsertarReserva(Context c, ReservaInsertar r, Reserva reser) {
			this.contexto = c;
			res = r;
			reserva = reser;
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
		protected ReponseInsertar doInBackground(String... params) {

			Gson gson = new Gson();
			ReponseInsertar response = new ReponseInsertar();
			DatabaseApiConection myDb;
			InputStreamReader in = null;

			myDb = new DatabaseApiConection();
			publishProgress("Editando...");
			String dato = new Gson().toJson(res);
			try {
				in = myDb.editarReservas(dato);
				if (in != null) {
					response = gson.fromJson(in, ReponseInsertar.class);
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
		protected void onPostExecute(ReponseInsertar result) {
			super.onPostExecute(result);

			dialogo.dismiss();
			if (result != null) {
				if (result.getCode() >= Connection.NETWORK_ERROR)
					mostrarToast(result.getCode() + ":" + result.getMessage());
				else {
					if (result.getCode() == Connection.NETWORK_OK) {
						if(result.isInsertado()){
							ReservaDatasource rd = new ReservaDatasource(contexto);
							reserva.setId(result.getId());
							rd.borrarReservaID(id);
							rd.insertarReserva(reserva);
							finish();
						}
						mostrarToast(result.getMessage());
					}
				}
			}

		}
	}

}
