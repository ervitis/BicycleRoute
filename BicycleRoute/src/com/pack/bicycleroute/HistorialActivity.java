package com.pack.bicycleroute;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class HistorialActivity extends Activity {
	TableLayout tableContent;
	TableLayout tableCabecera;
	TableRow.LayoutParams layoutFila, layoutInicio, layoutFin, layoutDia, layoutDistancia;
	ThreadTabla threadTabla;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historial);
		
		tableContent = (TableLayout) findViewById(R.id.tabla);
		tableCabecera = (TableLayout) findViewById(R.id.cabecera);
				
		setLayoutParams();
		setCabecera();
		setContenido();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		finish();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	void setLayoutParams(){
		layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		layoutInicio = new TableRow.LayoutParams(91, TableRow.LayoutParams.WRAP_CONTENT);
		layoutFin = new TableRow.LayoutParams(91, TableRow.LayoutParams.WRAP_CONTENT);
		layoutDia = new TableRow.LayoutParams(91, TableRow.LayoutParams.WRAP_CONTENT);
		layoutDistancia = new TableRow.LayoutParams(150, TableRow.LayoutParams.WRAP_CONTENT);
	}
	
	void setCabecera(){
		TableRow tableRow;
		TextView textInicio = new TextView(this);
		TextView textFin = new TextView(this);
		TextView textDia = new TextView(this);
		TextView textDistancia = new TextView(this);
		
		tableRow = new TableRow(this);
		tableRow.setLayoutParams(layoutFila);
				
		textInicio.setText(this.getResources().getString(R.string.tabla_inicio));
		textFin.setText(this.getResources().getString(R.string.tabla_fin));
		textDia.setText(this.getResources().getString(R.string.tabla_dia));
		textDistancia.setText(this.getResources().getString(R.string.tabla_distancia));
		
		setProperties(textInicio, layoutInicio, true);
		setProperties(textFin, layoutFin, true);
		setProperties(textDia, layoutDia, true);
		setProperties(textDistancia, layoutDistancia, true);
		
		tableRow.addView(textDia);
		tableRow.addView(textInicio);
		tableRow.addView(textFin);
		tableRow.addView(textDistancia);
		
		tableCabecera.addView(tableRow);
	}
	
	void setContenido(){
		String dia, inicio, fin, distancia;
		ArrayList<Fila> filas = new ArrayList<Fila>();
				
		ThreadTabla thread = new ThreadTabla(this);
		thread.execute();
		
		try{
			if ( (filas = thread.get()) != null ){
				for(Fila item: filas){
					TableRow tableRow = new TableRow(this);
					tableRow.setLayoutParams(layoutFila);
					
					TextView textInicio = new TextView(this);
					TextView textFin = new TextView(this);
					TextView textDia = new TextView(this);
					TextView textDistancia = new TextView(this);
					
					dia = String.valueOf(item.getDia());
					inicio = item.getHoraMinuto(item.getHoraInicio(), item.getMinInicio());
					fin = item.getHoraMinuto(item.getHoraFin(), item.getMinFin());
					distancia = String.valueOf(item.getDistancia());
					
					textDia.setText(dia);
					textInicio.setText(inicio);
					textFin.setText(fin);
					textDistancia.setText(distancia);
					
					setProperties(textInicio, layoutInicio, false);
					setProperties(textFin, layoutFin, false);
					setProperties(textDia, layoutDia, false);
					setProperties(textDistancia, layoutDistancia, false);
					
					tableRow.addView(textDia);
					tableRow.addView(textInicio);
					tableRow.addView(textFin);
					tableRow.addView(textDistancia);
						
					tableContent.addView(tableRow);
				}
			}
			else{
				Toast.makeText(this, this.getResources().getString(R.string.nodata), Toast.LENGTH_SHORT).show();
			}
		}catch(Exception ex){
			Toast.makeText(this, "Error hilo", Toast.LENGTH_SHORT).show();
			ex.printStackTrace();
		}
			
		
	}
	
	void setProperties(TextView txt, TableRow.LayoutParams lp, boolean cab){
		txt.setGravity(Gravity.CENTER_HORIZONTAL);
		txt.setTextAppearance(this, R.style.etiqueta);
		txt.setLayoutParams(lp);
		
		if ( cab ){
			txt.setBackgroundResource(R.drawable.tabla_cabecera);
		}
		else{
			txt.setBackgroundResource(R.drawable.tabla_celda);
		}
	}
}
