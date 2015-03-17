package com.acdat.padel.Controlador;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.acdat.padel.R;
import com.acdat.padel.Database.Reservas.Reserva;
import com.acdat.padel.R.id;
import com.acdat.padel.R.layout;

public class ReservaAdapter extends ArrayAdapter<Reserva> {

	public ReservaAdapter(Context context, List<Reserva> objects) {
		super(context, R.layout.item_reserva, objects);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Holder holder;
		Reserva j = getItem(position);
		if (view == null) {
			holder = new Holder();
			LayoutInflater infla = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			view = infla.inflate(R.layout.item_reserva, null);
			holder.duracion = (TextView)view.findViewById(R.id.ItemTxtDuracion);
			holder.horaIni = (TextView)view.findViewById(R.id.ItemTxtHorarioInicio);
			holder.horaFin = (TextView)view.findViewById(R.id.ItemTxtHorarioFin);
			holder.fecha = (TextView)view.findViewById(R.id.ItemTxtFecha);
			view.setTag(holder);
		}else
			holder = (Holder)view.getTag();
		holder.duracion.setText(String.valueOf(j.getDuracion())+ "'");
		holder.horaIni.setText(j.getHoraInicio());
		holder.horaFin.setText(j.getHoraFin());
		holder.fecha.setText(j.getFecha());
		return view;
	}
	
	public void borrar(int pos){
		remove(getItem(pos));
		notifyDataSetChanged();
	}
	
	class Holder{
		TextView horaIni;
		TextView horaFin;
		TextView duracion;
		TextView fecha;
	}

}
