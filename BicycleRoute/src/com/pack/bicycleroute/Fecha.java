package com.pack.bicycleroute;

import java.util.Calendar;

public class Fecha {
	private int dia;
	private int mes;
	private int anho;
	private int hora;
	private int min;
	
	public Fecha(){
		Calendar c = Calendar.getInstance();
		
		this.anho = c.get(Calendar.YEAR);
		this.dia = c.get(Calendar.DAY_OF_MONTH);
		this.mes = c.get(Calendar.MONTH) + 1;
		this.hora = c.get(Calendar.HOUR_OF_DAY);
		this.min = c.get(Calendar.MINUTE);
	}
	
	public int getHora(){
		return this.hora;
	}
	
	public int getMinuto(){
		return this.min;
	}
	
	public int getAnho(){
		return this.anho;
	}
	
	public int getMes(){
		return this.mes;
	}
	
	public int getDia(){
		return this.dia;
	}
}
