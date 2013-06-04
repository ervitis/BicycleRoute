package com.pack.bicycleroute;

public class Recorrido {
	private int hI, mI;
	private int hF, mF;
	private double d;
	private int dia;
	private double v;
	
	public Recorrido(int hInicio, int hFin, int mInicio, int mFin, int dia, double d){
		this.d = d;
		this.hF = hFin;
		this.hI = hInicio;
		this.mI = mInicio;
		this.mF = mFin;
		this.dia = dia;
	}
	
	public double getDistancia(){
		return this.d;
	}
	
	public int getHoraFin(){
		return this.hF;
	}
	
	public int getHoraInicio(){
		return this.hI;
	}
	
	public int getMinFin(){
		return this.mF;
	}
	
	public int getMinInicio(){
		return this.mI;
	}
	
	public int getDia(){
		return this.dia;
	}
	
	public void setVelocidad(double v){
		this.v = v;
	}
	
	public double getVelocidad(){
		return this.v;
	}
	
	public double calculaVelocidad(double d, int dif){
		if ( dif != 0){
			return (d / dif);
		}
		else{
			return 0;
		}
	}
	
	public int calculaDiferenciaTiempo(int inicio, int fin){
		return (Math.abs(fin - inicio));
	}
	
	public int toSegundos(int hora, int minuto){
		hora *= 3600;
		minuto *= 60;
		
		return (hora + minuto);
	}
	
	public int toAMPM(int hora){
		return ((hora > 12) ? (hora - 12) : hora);
	}
}
