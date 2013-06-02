package com.pack.bicycleroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	private static final int REQUEST_MAP = 1;
	private static final int REQUEST_HISTORY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
