package test;

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.ui.RefineryUtilities;

import OnAireServlets.Constants;

public class MainTest {

	private static RequestManagerClient reqManClient=null;
	private static ArrayList<String> cityList = null;

	private static void init(ArrayList<String> c){
		reqManClient = new RequestManagerClient();
		reqManClient.initialize();
		cityList = new ArrayList<String>(c);
	}

	public static void main(String[] args) {
		mainTest();
	}	

	private static void mainTest() {		
		ArrayList<String> cities = new ArrayList<String> ();
		cities.add(Constants.AT);
		cities.add(Constants.BB);
		init(cities);
		//testMeanOfAllStops();
		testSDOfAllStops();
		//testMeanSDOfNeighbors();
	}

	private static void testMeanSDOfNeighbors() {
		HashMap<String,ArrayList<Double>> citySDListMap = new HashMap<String,ArrayList<Double>>();
		for (int i=0;i<cityList.size();i++){
			ArrayList<Double> sdList = reqManClient.getMeanSDOfNearestNeighbors(cityList.get(i));
			citySDListMap.put(cityList.get(i), sdList);
		}
		plotSDChart(citySDListMap);
	}

	private static void plotSDChart(HashMap<String,ArrayList<Double>> sdMap) {
		SDNeighborsLineChart chart = new SDNeighborsLineChart("Standard Deviation Plot",
				"Distribution of SD with Neighbors within 100m approx.",sdMap);
		chart.pack( );          
		RefineryUtilities.centerFrameOnScreen( chart );          
		chart.setVisible( true );
	}

	private static void testMeanOfAllStops() {
		
		for(int i=0;i<cityList.size();i++){
			System.out.println("Mean of All Stops in "+cityList.get(i)+" is "+reqManClient.getMeanOfAllStops(cityList.get(i)));
		}

	}

	private static void testSDOfAllStops() {
		
		for(int i=0;i<cityList.size();i++){
			System.out.println("SD of All Stops in "+cityList.get(i)+" is "+reqManClient.getSDOfAllStops(cityList.get(i)));
		}
	}

}
