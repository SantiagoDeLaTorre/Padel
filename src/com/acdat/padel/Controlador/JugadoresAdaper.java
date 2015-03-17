package com.acdat.padel.Controlador;

import java.util.List;

import com.acdat.padel.R;
import com.acdat.padel.Database.Jugadores.Jugador;
import com.acdat.padel.R.id;
import com.acdat.padel.R.layout;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class JugadoresAdaper extends ArrayAdapter<Jugador> {
	
	CheckBox ch;

	public JugadoresAdaper(Context context, List<Jugador> objects) {
		super(context, R.layout.item_jugadores, objects);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Holder holder;
		Jugador j = getItem(position);
		if (view == null) {
			holder = new Holder();
			LayoutInflater infla = (LayoutInflater) getContext()
					.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			view = infla.inflate(R.layout.item_jugadores, null);
			holder.check = (CheckBox)view.findViewById(R.id.invitarCheck);
			holder.check.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox ch = (CheckBox)v;
					Jugador j = (Jugador)ch.getTag();
					j.setCheck(ch.isChecked());
				}
			});
			view.setTag(holder);
		}else{
			holder = (Holder)view.getTag();
			
		}
		holder.check.setChecked(j.getCheck());
		holder.check.setTag(j);
		holder.check.setText(j.getNombre());
		return view;
	}
	
	class Holder{
		CheckBox check;
	}
}
