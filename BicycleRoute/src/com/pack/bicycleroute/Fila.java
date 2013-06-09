package com.pack.bicycleroute;

public class Fila extends Recorrido{
	
	public Fila(int horaInicio, int horaFin, int minInicio, int minFin, int dia, double distancia){
		super(horaInicio, horaFin, minInicio, minFin, dia, distancia);
	}
	
	public String getHoraMinuto(int h, int m){
		return h + ":" + m;
	}
}
