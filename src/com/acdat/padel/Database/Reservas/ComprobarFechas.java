package com.acdat.padel.Database.Reservas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.NumberPicker;
import android.widget.TimePicker;

public class ComprobarFechas {

	public static String comprobarFecha(Calendar fechaElegida, Calendar fechaHoy) {
		String error = "";
		long faltanMinutos, faltanDias, tiempoRestante;

		if (fechaElegida.getTimeInMillis() < fechaHoy.getTimeInMillis())
			error = " No puedes reservar para antes de ahora";
		else {
			tiempoRestante = fechaElegida.getTimeInMillis()
					- fechaHoy.getTimeInMillis();
			faltanMinutos = tiempoRestante / (1000 * 60);
			if (faltanMinutos < 30)
				error = "se tiene que hacer la reserva como minimo 30 minutos de antelacion";
			else {
				faltanDias = faltanMinutos / (60 * 24);
				if (faltanDias > 30) {
					error = "No puedes hacer una reserva mas alla de 30 dias";
				}
			}
		}
		return error;
	}

	public static  String comprobarHora(TimePicker hora) {
		String error = ""; //
		int hor = hora.getCurrentHour();
		if (hor >= 22)
			error = "No puede reservar a partir de las 22H";
		else if (hor < 9)
			error = "No puede reservar antes de las 9H";
		return error;
	}
	
	public static String comprobarFinReserva(String[] tiempos, NumberPicker duracion, Calendar fechaElegida){
		String error = "";
		if(horaFinal(tiempos, duracion, fechaElegida).get(Calendar.HOUR_OF_DAY) >= 22)
			error = "No se puede hacer una reserva que termine despues de las 22H";
		return error;
	}	
	public static Calendar horaFinal(String[] tiempos, NumberPicker duracion, Calendar fechaElegida){
		int duracionMinutos = Integer.valueOf(tiempos[duracion.getValue()]);
		long finHora = fechaElegida.getTimeInMillis() + (duracionMinutos * 60 * 1000);
		Calendar finReserva = Calendar.getInstance();
		finReserva.setTimeInMillis(finHora);
		return finReserva;
	}

	public static String formatHora(long millis){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date(millis));
	}
}
