package com.pack.bicycleroute;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class GoogleActivity extends FragmentActivity implements LocationListener {
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Location location, lastLocation;
	private static final int TIEMPO_CAMBIO = 0;
	private static final int DISTANCIA_CAMBIO = 0;
	private MarkerOptions markerOptionsInicio, markerOptionsFin;
	private Marker markerInicio, markerFin;
	private PolylineOptions polylineOptions;
	private int estado;
	private String p;
	private Button bDetener;
	private Fecha fechaInicio, fechaFin;
	SQLiteAdapter sqliteAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google);
		bDetener = (Button) findViewById(R.id.detener);
		bDetener.setClickable(false);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		sqliteAdapter = new SQLiteAdapter(this);
		estado = 0;
		
		setPolylineOptions();
		p = setGoogleMaps();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		if ( p != null ){
			setGPS(p);
		}
	}
	
	@Override
	synchronized
	protected void onDestroy(){
		super.onDestroy();
		sqliteAdapter.close();
		cleanVariables();
		locationManager.removeUpdates(this);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		finish();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	/**
	 * Set the polyline options for the map
	 */
	void setPolylineOptions(){
		polylineOptions = new PolylineOptions();
		polylineOptions.color(Color.RED);
		polylineOptions.width(4);
		polylineOptions.visible(true);
	}
	
	/**
	 * onDetener event
	 * @param view		the button view
	 */
	public void onDetener(View view){
		Button boton = (Button) view;
		
		switch ( estado ){
			case 0:
				//Begin
				//Create the begin marker
				//Set the button text
				if ( locationManager == null ){
					locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					
					if ( p != null ){
						if ( locationManager.isProviderEnabled(p) ){
							setGPS(p);
						}
					}
				}
				
				setPolylineOptions();
				markerOptionsInicio = new MarkerOptions();
				markerOptionsFin = new MarkerOptions();
				setMarker(true);
				boton.setText("Detener");
				setInicio();
				estado = 1;
				break;
			case 1:
				//Stop
				//Create the final marker
				//Set the button text
				setMarker(false);
				boton.setText("Borrar marcas");
				setFin();
				estado = 2;
				guardaDatos();
				break;
			case 2:
				//Delete markers
				//Set the button text
				cleanVariables();				
				boton.setText("Comenzar");
				estado = 0;
				break;
		}
	}
	
	/**
	 * Clean the main variables
	 */
	void cleanVariables(){
		googleMap.clear();
		markerInicio = null;
		markerFin = null;
		markerOptionsInicio = null;
		markerOptionsFin = null;
		polylineOptions = null;
	}
	
	/**
	 * Set the marker for the beginning and the finishing
	 * @param start		<code>boolean</code> <code>true</code> if it's the start or <code>false</code> if it isn't
	 */
	void setMarker(boolean start){	
		LatLng latLng;
		double longitud, latitud;
		
		if ( p != null){
			if ( start ){
				markerOptionsInicio.draggable(false);
				latLng = new LatLng(location.getLatitude(), location.getLongitude());
				markerOptionsInicio.position(latLng);
				latitud = latLng.latitude;
				longitud = latLng.longitude;
				Log.i("setMarker true", "" + latitud + "," + longitud);
				markerInicio = googleMap.addMarker(markerOptionsInicio);
				polylineOptions.add(markerInicio.getPosition());
				Toast.makeText(this, "Funcionando", Toast.LENGTH_SHORT).show();
			}
			else{
				markerOptionsFin.draggable(false);
				latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
				markerOptionsFin.position(latLng);
				latitud = latLng.latitude;
				longitud = latLng.longitude;
				Log.i("setMarker false", "" + latitud + "," + longitud);
				markerFin = googleMap.addMarker(markerOptionsFin);
				polylineOptions.add(markerFin.getPosition());
				Toast.makeText(this, "Parando", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * Set the google maps service
	 * @return		the provider
	 */
	String setGoogleMaps(){
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		if ( status != ConnectionResult.SUCCESS ){
			int requestCode = 10;
			Dialog dialog;
			
			dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}
		else{
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			supportMapFragment.getView().setClickable(false);
			supportMapFragment.getView().setLongClickable(false);
			supportMapFragment.getView().setScrollContainer(false);
			supportMapFragment.getView().setFocusable(false);
			supportMapFragment.getView().setFocusableInTouchMode(false);
			
			googleMap = supportMapFragment.getMap();
			
			if ( googleMap != null ){
				googleMap.setMyLocationEnabled(true);
				googleMap.setOnMapClickListener(null);
				googleMap.setOnMapLongClickListener(null);
				googleMap.setOnMarkerClickListener(null);
				googleMap.setOnMarkerDragListener(null);
				googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.getUiSettings().setMyLocationButtonEnabled(false);
				googleMap.getUiSettings().setZoomControlsEnabled(false);
				googleMap.getUiSettings().setZoomGesturesEnabled(false);
				googleMap.getUiSettings().setRotateGesturesEnabled(false);
				googleMap.getUiSettings().setTiltGesturesEnabled(false);
				
				Criteria criteria = new Criteria();
				String provider = locationManager.getBestProvider(criteria, true);
				
				if ( provider != null ){
					//set gps
					return provider;
				}
			}
		}
		Toast.makeText(getBaseContext(), "Enable GPS", Toast.LENGTH_LONG).show();
		return null;
	}
	
	/**
	 * Set the GPS from the provider
	 * @param provider		the provider GPS
	 */
	void setGPS(String provider){
		location = locationManager.getLastKnownLocation(provider);
					
		if ( location != null ){
			locationManager.requestLocationUpdates(provider, TIEMPO_CAMBIO, DISTANCIA_CAMBIO, this);
			bDetener.setClickable(true);
			onLocationChanged(location);
		}
		else{
			Toast.makeText(this, "Wait for the GPS signal", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Set the start date
	 */
	void setInicio(){
		fechaInicio = new Fecha();
	}
	
	/**
	 * Set the final date
	 */
	void setFin(){
		fechaFin = new Fecha();
	}
	
	/**
	 * Save the data on the database
	 */
	void guardaDatos(){
		int r = -1;
		double d = 0.0;
		
		//new Thread
		try{
			Task t = new Task();
			t.execute(markerInicio.getPosition().latitude, markerInicio.getPosition().longitude, markerFin.getPosition().latitude, markerFin.getPosition().longitude);
			
			d = (double) t.get();
		} catch (Exception ex){
			Log.e("Task getDistance", ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		
		if ( d != 0.0 ){
			r = sqliteAdapter.guardaRecorrido(fechaInicio.getHora(), fechaInicio.getMinuto(), fechaFin.getHora(), fechaFin.getMinuto(), d);
			
			if ( r != -1 ){
				r = sqliteAdapter.guardaFecha(r, fechaFin.getDia(), fechaFin.getMes(), fechaFin.getAnho());
				if ( r == -1 ){
					Toast.makeText(this, "Fallo al guardar la fecha", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(this, "Fallo al guardar el recorrido", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(this, "Tienes que andar un poco más", Toast.LENGTH_SHORT).show();
		}
		
		sqliteAdapter.close();
	}

	@Override
	public void onLocationChanged(Location location) {
		switch ( estado ){
			case 1:
				if ( lastLocation == null ){
					lastLocation = location;
				}
				LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
				LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
				
				Log.d("Location->", "" + location.getLatitude() + "," + location.getLongitude());
				
				googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
				
				if ( polylineOptions != null ){
					polylineOptions.add(lastLatLng);
					polylineOptions.add(latLng);
					googleMap.addPolyline(polylineOptions);
				}
					
				//googleMap.addPolyline(new PolylineOptions().add(latLng).width(3).color(Color.RED).visible(true));
					
				lastLocation = location;
				break;
			case 2:
				break;
			default:
				googleMap.moveCamera(CameraUpdateFactory.zoomTo(1));	
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("GPS->", "Provider disabled " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("GPS->", "Provider enabled " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch( status ){
			case LocationProvider.AVAILABLE:
				Log.d("Gps->", "Status avaliable");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				Log.d("Gps->", "Status out of service");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.d("Gps->", "Status temporarily unavailable");
				break;
		}
	}

	/**
	 * Class for the task.<br>
	 * <ul>
	 * <li>First parameter the type of execute parameters
	 * <li>Second parameter the type of progress unit
	 * <li>Third the result type
	 * </ul>
	 * @author victor
	 *
	 */
	class Task extends AsyncTask<Double, Void, Double>{

		@Override
		protected Double doInBackground(Double... params) {
			Distancia distancia = new Distancia();
			double d = distancia.getDistancia((double)params[0], (double)params[1], (double)params[2], (double)params[3]);
			
			if ( d < 1000000000 ){
				return d;
			}
			return 0.0;
		}
		
		@Override
		protected void onPostExecute(Double result){
			super.onPostExecute(result);
			
		}
		
		@Override
		protected void onCancelled(){
			super.onCancelled();
		}

	}
}
