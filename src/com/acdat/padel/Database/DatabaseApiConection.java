package com.acdat.padel.Database;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.acdat.padel.Controlador.Email;
import com.google.gson.Gson;

public class DatabaseApiConection {
	//public static final String HOST = "192.168.1.132";
	public static final String HOST = "95.85.10.175";
	public static final String LOGIN = "/Padel/login/";
	public static final String DOWN_JUGADORES = "/Padel/listarJugadores";
	public static final String DOWN_RESERVAS = "/Padel/listarReservas";
	public static final String ADD_RESERVA = "/Padel/reserva";
	public static final String EDIT_RESERVA = "/Padel/reserva";
	public static final String DELETE_RESERVA = "/Padel/reserva";
	public static final String EMAIL = "/Padel/email";
	public static final String REGISTRO = "/Padel/registro";

	public InputStreamReader login(String correo, String password) throws ClientProtocolException, IOException{
		String text = "{\"password\":\""+password+"\"}";
		Connection myConnection= new Connection(HOST, LOGIN + correo );
		return myConnection.makePostRequest(text);
	}
	
	public InputStreamReader DescargaUsuarios() throws ClientProtocolException, IOException{
		Connection myConnection= new Connection(HOST, DOWN_JUGADORES);
		return myConnection.makeGetRequest();
	}
	
	public InputStreamReader DescargaReservas() throws ClientProtocolException, IOException{
		Connection myConnection= new Connection(HOST, DOWN_RESERVAS);
		return myConnection.makeGetRequest();
	}
	
	public InputStreamReader InsertarReservas(String dato) throws ClientProtocolException, IOException{
		Connection myConnection= new Connection(HOST, ADD_RESERVA);
		return myConnection.makePostRequest(dato);
	}
	
	public InputStreamReader borrarReserva(String token, String dato) throws ClientProtocolException, IOException{
		Connection myConnection= new Connection(HOST, DELETE_RESERVA + "/" + token + "/" + dato);
		return myConnection.makeDeleteRequest();
	}
	
	public InputStreamReader editarReservas(String dato) throws ClientProtocolException, IOException{
		Log.v("--", dato);
		Connection myConnection= new Connection(HOST, EDIT_RESERVA);
		return myConnection.makePutRequest(dato);
	}
	
	public InputStreamReader registro(String dato) throws ClientProtocolException, IOException{
		Connection myConnection= new Connection(HOST, REGISTRO);
		return myConnection.makePostRequest(dato);
	}
	
	public InputStreamReader emailSite(String dato) throws ClientProtocolException, IOException{
		Connection con = new Connection(HOST, EMAIL);
		return con.makePostRequest(dato);

	}
	
}
