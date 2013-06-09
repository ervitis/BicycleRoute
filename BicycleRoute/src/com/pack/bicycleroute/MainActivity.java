package com.pack.bicycleroute;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.androidplot.xy.XYPlot;

public class MainActivity extends LicenseCheckActivity {
	private static final int REQUEST_MAP = 1;
	private static final int REQUEST_HISTORY = 2;
	ThreadGrafico threadGrafico;
	private XYPlot xyPlot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		Toast.makeText(this, getResources().getString(R.string.checking_license), Toast.LENGTH_SHORT).show();
		checkLicense();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
		return true;
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		xyPlot = (XYPlot) findViewById(R.id.grafica);
		threadGrafico = new ThreadGrafico(this, xyPlot);
		threadGrafico.run();
	}
	
	@Override
	protected void onRestart(){
		super.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		switch( menuItem.getItemId() ){
			case R.id.menu_salir:
				onDestroy();
		}
		return super.onOptionsItemSelected(menuItem);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data ){
		switch( requestCode ){
			case REQUEST_MAP:
				break;
			case REQUEST_HISTORY:
				break;
		}
	}
	
	public void onEmpezar(View view){
		Intent i = new Intent(this, GoogleActivity.class);
		startActivityForResult(i, REQUEST_MAP);
	}
	
	public void onHistorial(View view){
		Intent i = new Intent(this, HistorialActivity.class);
		startActivityForResult(i, REQUEST_HISTORY);
	}

}
