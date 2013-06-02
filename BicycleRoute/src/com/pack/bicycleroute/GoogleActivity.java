package com.pack.bicycleroute;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class GoogleActivity extends FragmentActivity implements LocationListener {
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Location location, lastLocation;
	private static final int TIEMPO_CAMBIO = 0;
	private static final int DISTANCIA_CAMBIO = 0;
	private MarkerOptions markerOptions;
	private Marker[] marker;
	private Polyline polyline;
	private PolylineOptions polylineOptions;
	private boolean isGpsSetup = false;
	private int estado;
	SQLiteAdapter sqliteAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		sqliteAdapter = new SQLiteAdapter(this);
		marker = new Marker[2];
		estado = 0;
		
		polylineOptions = new PolylineOptions();
		polylineOptions.color(Color.RED);
		polylineOptions.width(2);
		polylineOptions.visible(true);

		setGoogleMaps();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
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
	
	public void onDetener(View view){
		Button boton = (Button) view;
		
		if ( isGpsSetup ){
			switch ( estado ){
				case 0:
					//Begin
					//Create the begin marker
					//Set the button text
					markerOptions = new MarkerOptions();
					setMarker(true);
					boton.setText("Detener");
					estado = 1;
					break;
				case 1:
					//Stop
					//Create the final marker
					//Set the button text
					setMarker(false);
					boton.setText("Borrar marcas");
					estado = 2;
					break;
				case 2:
					//Delete markers
					//Set the button text
					marker[0].setVisible(false);
					marker[1].setVisible(false);
					marker[0].remove();
					marker[1].remove();
					markerOptions = null;
					polyline.remove();
					polyline = null;
					boton.setText("Comenzar");
					estado = 0;
					break;
			}
		}
	}
	
	synchronized
	void setMarker(boolean start){		
		if ( start ){
			markerOptions.draggable(false);
			markerOptions.title("Inicio");
			markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
			marker[0] = googleMap.addMarker(markerOptions);
			Toast.makeText(this, "Funcionando", Toast.LENGTH_SHORT).show();
		}
		else{
			markerOptions.draggable(false);
			markerOptions.title("Fin");
			markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
			marker[1] = googleMap.addMarker(markerOptions);
			Toast.makeText(this, "Parando", Toast.LENGTH_SHORT).show();
		}
	}
	
	void setGoogleMaps(){
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		
		if ( status != ConnectionResult.SUCCESS ){
			int requestCode = 10;
			Dialog dialog;
			
			dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}
		else{
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			
			googleMap = supportMapFragment.getMap();
			
			if ( googleMap != null ){
				googleMap.setMyLocationEnabled(true);
				googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				googleMap.getUiSettings().setCompassEnabled(true);
				googleMap.getUiSettings().setMyLocationButtonEnabled(false);
				googleMap.getUiSettings().setZoomControlsEnabled(false);
				googleMap.getUiSettings().setZoomGesturesEnabled(false);
				googleMap.getUiSettings().setRotateGesturesEnabled(false);
				googleMap.getUiSettings().setTiltGesturesEnabled(false);
				
				Criteria criteria = new Criteria();
				String provider = locationManager.getBestProvider(criteria, true);
				
				if ( provider == null ){
					//set gps
					Toast.makeText(getBaseContext(), "Enable GPS", Toast.LENGTH_SHORT).show();
				}
				else{
					location = locationManager.getLastKnownLocation(provider);
					isGpsSetup = true;
					
					if ( location != null ){
						onLocationChanged(location);
					}
					
					locationManager.requestLocationUpdates(provider, TIEMPO_CAMBIO, DISTANCIA_CAMBIO, this);
				}
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if ( isGpsSetup ){
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
					
					polylineOptions.add(lastLatLng);
					polylineOptions.add(latLng);
					polyline = googleMap.addPolyline(polylineOptions);
					
					//googleMap.addPolyline(new PolylineOptions().add(latLng).width(3).color(Color.RED).visible(true));
					
					lastLocation = location;
					break;
				case 2:
					break;
				default:
					googleMap.moveCamera(CameraUpdateFactory.zoomTo(1));	
			}
			
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("GPS->", "Provider disabled " + provider);
		Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
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

}
