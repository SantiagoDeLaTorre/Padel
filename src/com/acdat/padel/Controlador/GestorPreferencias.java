package com.acdat.padel.Controlador;

import android.content.Context;
import android.content.SharedPreferences;

public class GestorPreferencias {

	public static void GuardarDatos(Context contexto, String email,
			String pass, String token, int id, String nombre) {
		SharedPreferences sp = contexto.getSharedPreferences("preferencias",
				contexto.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putString("email", email).commit();
		ed.putString("pass", pass).commit();
		ed.putInt("id", id).commit();
		ed.putString("nombre", nombre).commit();
		ed.putString("token", token).commit();
	}

	public static String getToken(Context c) {
		SharedPreferences sp = c.getSharedPreferences("preferencias",c.MODE_PRIVATE);
		return sp.getString("token", "");
	}
	
	public static String getPass(Context c) {
		SharedPreferences sp = c.getSharedPreferences("preferencias",c.MODE_PRIVATE);
		return sp.getString("pass", "");
	}
	
	public static String getEmail(Context c) {
		SharedPreferences sp = c.getSharedPreferences("preferencias",c.MODE_PRIVATE);
		return sp.getString("email", "");
	}
	
	public static int getID(Context c) {
		SharedPreferences sp = c.getSharedPreferences("preferencias",c.MODE_PRIVATE);
		return sp.getInt("id", -1);
	}
	
	public static String getNombre(Context c) {
		SharedPreferences sp = c.getSharedPreferences("preferencias",c.MODE_PRIVATE);
		return sp.getString("nombre", "");
	}

}
