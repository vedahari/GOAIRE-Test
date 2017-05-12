package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.BasicStroke; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart extends ApplicationFrame {	
	private static final long serialVersionUID = 1L;
	
	List<Color> colorCodes = new ArrayList<>(Arrays.asList(Color.RED,Color.GREEN,Color.BLUE,Color.ORANGE,Color.YELLOW,Color.MAGENTA,Color.BLACK));
	
	
	public XYLineChart(String applicationTitle, String chartTitle, String XTitle, String YTitle,  List<SeriesPlot<Double,Double>>seriesPlotMap) {		
		super(applicationTitle);
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				chartTitle ,
				XTitle,
				YTitle ,
				createDataset(seriesPlotMap) ,
				PlotOrientation.VERTICAL ,
				true,true,false);

		ChartPanel chartPanel = new ChartPanel( xylineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		final XYPlot plot = xylineChart.getXYPlot( );
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );		
		for (int i=0;i<seriesPlotMap.size();i++){
			renderer.setSeriesPaint(i, colorCodes.get(i));
			renderer.setSeriesStroke(i, new BasicStroke(i+2));			
		}	
		plot.setRenderer( renderer ); 
		setContentPane( chartPanel ); 
	}
	
	private XYDataset createDataset(List<SeriesPlot<Double,Double>>seriesPlotList) {		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i=0;i<seriesPlotList.size();i++){
			XYSeries series = new XYSeries(seriesPlotList.get(i).seriesName);
			for (int j=0;j<seriesPlotList.get(i).XValues.size();j++){
				series.add(seriesPlotList.get(i).XValues.get(j),seriesPlotList.get(i).YValues.get(j));
			}
			dataset.addSeries(series);
		}
		return dataset;	
	}
	

	public XYLineChart(String applicationTitle, String chartTitle, String XTitle, String YTitle,  HashMap<String,HashMap<Double,Double>>seriesPlotMap) {		
		super(applicationTitle);
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				chartTitle ,
				XTitle,
				YTitle ,
				createDataset(seriesPlotMap) ,
				PlotOrientation.VERTICAL ,
				true , true , false);

		ChartPanel chartPanel = new ChartPanel( xylineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		final XYPlot plot = xylineChart.getXYPlot( );
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );		
		for (int i=0;i<seriesPlotMap.size();i++){
			renderer.setSeriesPaint(i, colorCodes.get(i));
			renderer.setSeriesStroke(i, new BasicStroke(2));			
		}	
		plot.setRenderer( renderer ); 
		setContentPane( chartPanel ); 
	}

	private XYDataset createDataset(HashMap<String,HashMap<Double,Double>>seriesPlotMap ) {		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for(Entry<String,HashMap<Double,Double>> seriesPlotEntry:seriesPlotMap.entrySet()){
			XYSeries series = new XYSeries( seriesPlotEntry.getKey());
			HashMap<Double,Double> plotMap = new HashMap<Double,Double>(seriesPlotEntry.getValue());
			for(Map.Entry<Double,Double> entry: plotMap.entrySet())
			{
				System.out.println("Key :: "+entry.getKey()+"   Value "+entry.getValue());
				series.add(entry.getKey(),entry.getValue());
			}
			dataset.addSeries(series);
		}		
		return dataset;
	}   
}

