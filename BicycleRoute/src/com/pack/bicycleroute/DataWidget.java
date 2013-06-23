package com.pack.bicycleroute;

public class DataWidget{
	private double distanciaTotal;
	private double horaTotal;
	private double minTotal;
	private double velocidadTotal;
	
	public DataWidget(double distancia, double horaTotal, double minTotal){
		this.distanciaTotal = distancia;
		this.horaTotal = horaTotal;
		this.minTotal = minTotal;
	}
	
	public double getDistanciaWidget(){
		return this.distanciaTotal;
	}
	
	public double getHoraWidget(){
		return this.horaTotal;
	}
	
	public double getMinWidget(){
		return this.minTotal;
	}
	
	public double getVelocidadWidget(){
		return this.velocidadTotal;
	}
	
	public void setDistanciaWidget(double d){
		this.distanciaTotal = d;
	}
	
	public void setHoraWidget(double h){
		this.horaTotal = h;
	}
	
	public void setMinWidget(double m){
		this.minTotal = m;
	}
	
	public void setVelocidadWidget(double v){
		this.velocidadTotal = v;
	}
	
	public String getVelocidadToString(){
		return "" + this.velocidadTotal;
	}
	
	public String getHoraMinToString(){
		return this.horaTotal + ":" + this.minTotal;
	}
	
	public int setVelocidad(double d, double h, double m){
		try{
			this.velocidadTotal = d / (h + (m/60));
		}catch(Exception ex){
			return -1;
		}
		return 0;
	}
}
