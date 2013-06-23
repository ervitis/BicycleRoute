package com.pack.bicycleroute;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget extends AppWidgetProvider{
	DataWidget dataWidget = null;
	Tempo tempo;
	
	/**
	 * Called when the widget is updated, see the updatePeriodMillis
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		Log.i("Widget->", "Update");
		
		//get the speed and the distance
		if ( Data.CONTEXT_WIDGET == null ){
			Data.CONTEXT_WIDGET = context;
		}
		
		Timer timer = new Timer();
		
		try{
			tempo = new Tempo();
			timer.scheduleAtFixedRate(tempo, Data.DELAY, Data.PERIOD);
		}catch(Exception ex){
			Log.e("onEnabled", ex.getLocalizedMessage());
		}
	}
	
	/**
	 * Called when it has been layed out at a new size
	 * Bundle contains some data, see the appwidgets web page
	 * Not used
	 */
	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions){
		
	}
	
	/**
	 * Called when the instance has been deleted
	 * Not used
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds){
		super.onDeleted(context, appWidgetIds);
		Log.i("Widget->", "Delete");
	}
	
	/**
	 * When the widget is instantiated
	 * Used to call the db to get the data
	 */
	@Override
	public void onEnabled(Context context){
		Log.i("Widget->", "Enable");
		Data.CONTEXT_WIDGET = context;
		Log.d("DataContext", Data.CONTEXT_WIDGET.toString());
	}
	
	/**
	 * When the last appwidget instance is deleted
	 * Not used
	 */
	@Override
	public void onDisabled(Context context){
		super.onDisabled(context);
		tempo = null;
	}
	
	public class Tempo extends TimerTask{
		
		@Override
		public void run() {
			try{
				SQLiteAdapter sqliteAdapter = new SQLiteAdapter(Data.CONTEXT_WIDGET);
				Vector<String> vector = new Vector<String>();
						
				vector = sqliteAdapter.getLastRecord();
				sqliteAdapter.close();
						
				if ( vector != null ){
					if ( !vector.isEmpty() ){
						String[] data = vector.toString().split("/");
						dataWidget = new DataWidget((double)Double.parseDouble(data[4]), Math.abs((double)Double.parseDouble(data[0]) - (double)Double.parseDouble(data[2])), Math.abs((double)Double.parseDouble(data[1]) - (double)Double.parseDouble(data[3])));
								
						if ( dataWidget.setVelocidad(dataWidget.getDistanciaWidget(), dataWidget.getHoraWidget(), dataWidget.getMinWidget()) != 0 ){
							Toast.makeText(Data.CONTEXT_WIDGET, "Error", Toast.LENGTH_SHORT).show();
						}
					}
				}
						
				RemoteViews remoteViews = new RemoteViews(Data.CONTEXT_WIDGET.getPackageName(), R.layout.widget_layout);
				ComponentName componentName = new ComponentName(Data.CONTEXT_WIDGET, Widget.class);
						
				if ( dataWidget != null ){
					Toast.makeText(Data.CONTEXT_WIDGET, dataWidget.getHoraMinToString(), Toast.LENGTH_SHORT).show();
											
					remoteViews.setTextViewText(R.id.widgDistancia, String.valueOf(dataWidget.getDistanciaWidget()));
					remoteViews.setTextViewText(R.id.widgVelocidad, dataWidget.getVelocidadToString());
				}
				else{
					remoteViews.setTextViewText(R.id.widgDistancia, Data.CONTEXT_WIDGET.getText(R.string.widget_no_distance));
					remoteViews.setTextViewText(R.id.widgVelocidad, Data.CONTEXT_WIDGET.getText(R.string.widget_no_speed));
				}
						
				AppWidgetManager.getInstance(Data.CONTEXT_WIDGET).updateAppWidget(componentName, remoteViews);
			}catch(Exception ex){
				Toast.makeText(Data.CONTEXT_WIDGET, "Error al obtener datos", Toast.LENGTH_SHORT).show();
				Log.e("TimerTask", ex.getLocalizedMessage());
			}			
		}
	}
}
