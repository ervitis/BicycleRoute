package com.pack.bicycleroute;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class ThreadGrafico extends Thread implements Runnable{
	private Context context;
	private XYPlot xyPlot;
	private XYSeries serie;
	private Vector<String> vector;
	private ArrayList<Recorrido> vectorRecorrido;
	private Recorrido recorrido;
	public Grafico grafico;
	
	public ThreadGrafico(Context c, XYPlot plot){
		this.context = c;
		this.xyPlot = plot;
	}
	
	@Override
	public void run(){
		SQLiteAdapter sqliteAdapter = new SQLiteAdapter(this.context);
		String[] t;
		Number[] serieY, serieX;
		
		vector = new Vector<String>();
		vector = sqliteAdapter.getListaGrafico();
		vectorRecorrido = new ArrayList<Recorrido>();
		sqliteAdapter.close();
		
		if ( vector != null ){
			if ( !vector.isEmpty() ){
				int i = 0;
				serieY = new Number[vector.size()];
				serieX = new Number[vector.size()];
				
				while ( i < vector.size() ){
					t = vector.get(i).split("/");
					recorrido = new Recorrido((int)Integer.parseInt(t[0].toString()), (int)Integer.parseInt(t[2].toString()), (int)Integer.parseInt(t[1].toString()), (int)Integer.parseInt(t[3].toString()), (int)Integer.parseInt(t[5].toString()), (double)Double.parseDouble(t[4].toString()));
					vectorRecorrido.add(recorrido);
					
					i++;
				}
				
				grafico = new Grafico(this.xyPlot, serie);
				
				//Speed
				i = 0;
				for(Recorrido item : vectorRecorrido){
					serieX[i] = item.getDia();
					serieY[i] = item.calculaVelocidad(item.getDistancia(), item.toSegundos(item.calculaDiferenciaTiempo(item.toAMPM(item.getHoraInicio()), item.toAMPM(item.getHoraFin())), item.calculaDiferenciaTiempo(item.getMinInicio(), item.getMinFin())));
					i++;
				}
				
				try{
					grafico.setSeries(serieX, serieY);
				} catch (Exception ex){
					Toast.makeText(this.context, this.context.getResources().getString(R.string.errorseriesxy), Toast.LENGTH_SHORT).show();
				}
								
				this.xyPlot = grafico.creaGrafico();
				this.xyPlot.setVisibility(View.VISIBLE);
			}
			else{
				Toast.makeText(this.context, this.context.getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(this.context, this.context.getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
		}
	}
}
