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

public class SDNeighborsLineChart extends ApplicationFrame {	
	private static final long serialVersionUID = 1L;
	HashMap<String,HashMap<Double,Double>> citySDCountMap = null;	
	List<Color> colorCodes = new ArrayList<>(Arrays.asList(Color.RED,Color.BLUE,Color.GREEN,Color.ORANGE,Color.YELLOW));

	public void initializeDataSet(HashMap<String,ArrayList<Double>>citySDListMap){
		citySDCountMap = new HashMap<String,HashMap<Double,Double>> ();
		for(Map.Entry<String, ArrayList<Double>>citySDListEntry:citySDListMap.entrySet()){
			HashMap<Double,Double>sdCountMap = new HashMap<Double,Double>();
			ArrayList <Double> sdList = citySDListEntry.getValue();
			for (int i=0;i<sdList.size();i++){
				double key = (sdList.get(i).intValue()/5)*5;
				Double val = sdCountMap.get(key);
				if (val==null){
					sdCountMap.put(key, 1.0);
				}
				else{
					sdCountMap.put(key,val+1.0);
				}
			}
			for(Entry<Double, Double> entry: sdCountMap.entrySet())
			{
				System.out.println("Key :: "+entry.getKey()+"   Value "+entry.getValue());
				sdCountMap.put(entry.getKey(), entry.getValue()/sdList.size());
			}
			
			citySDCountMap.put(citySDListEntry.getKey(), new HashMap<Double,Double>(sdCountMap));			
		}
	}

	public SDNeighborsLineChart(String applicationTitle, String chartTitle, HashMap<String,ArrayList<Double>>citySDListMap) {		
		super(applicationTitle);		
		initializeDataSet(citySDListMap);

		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				chartTitle ,
				"Standard Deviation in seconds" ,
				"Probability" ,
				createDataset() ,
				PlotOrientation.VERTICAL ,
				true , true , false);

		ChartPanel chartPanel = new ChartPanel( xylineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		final XYPlot plot = xylineChart.getXYPlot( );
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );		
		for (int i=0;i<citySDCountMap.size();i++){
			renderer.setSeriesPaint(i, colorCodes.get(i));
			renderer.setSeriesStroke(i, new BasicStroke(i+2));			
		}	
		plot.setRenderer( renderer ); 
		setContentPane( chartPanel ); 
	}

	private XYDataset createDataset( ) {		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for(Entry<String, HashMap<Double,Double>> citySDCountEntry:citySDCountMap.entrySet()){
			XYSeries series = new XYSeries( citySDCountEntry.getKey());
			HashMap<Double,Double>sdCountMap = new HashMap<Double,Double>(citySDCountEntry.getValue());
			for(Map.Entry<Double, Double> entry: sdCountMap.entrySet())
			{
				System.out.println("Key :: "+entry.getKey()+"   Value "+entry.getValue());
				series.add(entry.getKey(),entry.getValue());
			}
			dataset.addSeries(series);
		}		
		return dataset;
	}   
}
