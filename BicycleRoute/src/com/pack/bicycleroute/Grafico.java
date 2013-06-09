package com.pack.bicycleroute;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Color;

import com.androidplot.ui.AnchorPosition;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XLayoutStyle;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.androidplot.xy.YLayoutStyle;

/**
 * Set the graphic.
 * @author victor
 *
 */
public class Grafico {
	private XYPlot xyPlot;
	private XYSeries eje;
	private Context c;
	
	/**
	 * Constructor for Graph class
	 * @param idPlot	findViewById
	 * @param serie		the axis initialized
	 */
	public Grafico(Context context, XYPlot idPlot, XYSeries serie){
		this.eje = serie;
		this.xyPlot = idPlot;
		this.c = context;
	}
	
	/**
	 * Set the axis series
	 * @param serie			the serial numbers array
	 * @throws Exception 	if it's not the x or y value
	 */
	public void setSeries(Number[] seriex, Number[] seriey) throws Exception{
		this.eje = new SimpleXYSeries(Arrays.asList(seriex), Arrays.asList(seriey), "velocidad");
	}
	
	/**
	 * Returns the axis series
	 * @return		the axis XYSeries or null if error
	 */
	public XYSeries getSeries(){
		return this.eje;
	}
	
	/**
	 * Returns a graphic
	 * @param n		the number of elements
	 * @return 		the XYPlot element
	 */
	public XYPlot creaGrafico(){	
		this.xyPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
		
		//add series to the plot
		this.xyPlot.addSeries(this.eje, new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 100, 0), null, new PointLabelFormatter(Color.WHITE)));
		this.xyPlot.getLayoutManager().remove(this.xyPlot.getLegendWidget());
		this.xyPlot.getLayoutManager().remove(this.xyPlot.getDomainLabelWidget());
		this.xyPlot.getLayoutManager().remove(this.xyPlot.getRangeLabelWidget());
		this.xyPlot.setTitle(this.c.getResources().getString(R.string.nombregrafico));
		this.xyPlot.setMarkupEnabled(false);
		this.xyPlot.position(this.xyPlot.getGraphWidget(), 0, XLayoutStyle.ABSOLUTE_FROM_LEFT, 0, YLayoutStyle.RELATIVE_TO_CENTER, AnchorPosition.LEFT_MIDDLE);
		
		return this.xyPlot;
	}
}
