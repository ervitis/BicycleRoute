package com.pack.bicycleroute;

import java.util.Arrays;

import android.graphics.Color;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

/**
 * Set the graphic.
 * @author victor
 *
 */
public class Grafico {
	private XYPlot xyPlot;
	private XYSeries ejeX, ejeY;
	
	/**
	 * Constructor for Grafico class
	 * @param idPlot	findViewById
	 * @param serieX	the X axis initialized
	 * @param serieY	the Y axis initialized
	 */
	public Grafico(XYPlot idPlot, XYSeries serieX, XYSeries serieY){
		this.ejeX = serieX;
		this.ejeY = serieY;
		this.xyPlot = idPlot;
	}
	
	/**
	 * Set the axis series
	 * @param serie			the serial numbers array
	 * @param eje			char:<code>x</code> value or <code>y</code> value.
	 * @throws Exception 	if it's not the x or y value
	 */
	public void setSeries(Number[] serie, char eje) throws Exception{
		switch(eje){
			case 'x':
				this.ejeX = new SimpleXYSeries(Arrays.asList(serie), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "tiempo");
				break;
			case 'y':
				this.ejeY = new SimpleXYSeries(Arrays.asList(serie), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "velocidad");
				break;
			default:
				throw new Exception();
		}
	}
	
	/**
	 * Returns the axis series
	 * @param eje	char:<code>x</code> value or <code>y</code> value.
	 * @return		the axis XYSeries or null if error
	 */
	public XYSeries getSeries(char eje){
		switch(eje){
			case 'x':
				return this.ejeX;
			case 'y':
				return this.ejeY;
			default:
				return null;					
		}
	}
	
	/**
	 * Returns a graphic
	 * @return the XYPlot element
	 */
	public XYPlot creaGrafico(){		
		//add series to the plot
		xyPlot.addSeries(ejeX, new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 100, 0), null, new PointLabelFormatter(Color.WHITE)));
		xyPlot.addSeries(ejeY, new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), null, new PointLabelFormatter(Color.WHITE)));
		
		return xyPlot;
	}
}
