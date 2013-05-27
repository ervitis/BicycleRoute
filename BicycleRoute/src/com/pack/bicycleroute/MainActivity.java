package com.pack.bicycleroute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private static final int REQUEST_MAP = 1;
	private static final int REQUEST_HISTORY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onEmpezar(View view){
		Intent i = new Intent(this, GoogleActivity.class);
		startActivityForResult(i, REQUEST_MAP);
	}
	
	public void onHistorial(View view){
		Intent i = new Intent(this, Historial.class);
		startActivityForResult(i, REQUEST_HISTORY);
	}

}
