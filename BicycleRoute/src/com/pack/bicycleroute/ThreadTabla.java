package com.pack.bicycleroute;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.os.AsyncTask;

public class ThreadTabla extends AsyncTask<Void, Void, ArrayList<Fila>>{
	private Context context;
	private Fila fila;
	private Vector<String> vector;
	
	public ThreadTabla(Context c){
		this.context = c;
	}
	
	@Override
	protected ArrayList<Fila> doInBackground(Void... arg0) {
		SQLiteAdapter sqliteAdapter = new SQLiteAdapter(this.context);
		vector = new Vector<String>();
		ArrayList<Fila> filas = new ArrayList<Fila>();
		String [] t;
		
		vector = sqliteAdapter.getListaGrafico();
		sqliteAdapter.close();
		
		if ( vector != null ){
			if ( !vector.isEmpty() ){
				int i = 0;
				
				while ( i < vector.size() ){
					t = vector.get(i).split("/");
					fila = new Fila((int)Integer.parseInt(t[0].toString()), (int)Integer.parseInt(t[2].toString()), (int)Integer.parseInt(t[1].toString()), (int)Integer.parseInt(t[3].toString()), (int)Integer.parseInt(t[5].toString()), (double)Double.parseDouble(t[4].toString()));
					filas.add(fila);
					i++;
				}
				
				return filas;
			}
			else{
				return null;
				
			}
		}
		else{
			return null;
		}
	}
}
