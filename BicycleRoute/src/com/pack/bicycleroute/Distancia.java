package com.pack.bicycleroute;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Distancia {
	public double getDistancia(double inicioLat, double inicioLong, double finLat, double finLong){
		String distancia = "";
		String estado = "";
		
		try{
			JSONObject jsonObject = JsonParser.getJSONfromURL("http://maps.googleapis.com/maps/api/directions/json?origin="+ inicioLat +","+ inicioLong +"&destination="+ finLat +","+ finLong +"&sensor=false");
			estado = jsonObject.getString("status");
			
			if ( estado.equalsIgnoreCase("OK") ){
				JSONArray ruta = jsonObject.getJSONArray("routes");
				JSONObject nulo = ruta.getJSONObject(0);
				JSONArray pies = nulo.getJSONArray("legs");
				JSONObject nulo2 = pies.getJSONObject(0);
				JSONObject d = nulo2.getJSONObject("distance");
				distancia = d.getString("value");
				
				//minimal 20 meters
				if ( (double)Double.parseDouble(distancia) < 20.0 ){
					distancia = "100000000";
				}
			}
			else{
				distancia = "100000000";
			}
		} catch (Exception ex){
			Log.e("getDistancia", ex.getMessage());
			return -1;
		}
		
		return ((double) (Double.parseDouble(distancia)));
	}
}
