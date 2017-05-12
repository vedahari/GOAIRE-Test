package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


import OnAireServlets.Constants;
import OnAireServlets.IdlingTimeResponse;
import OnAireServlets.Point;
import OnAireServlets.RequestManager;

public class RequestManagerClient {
	RequestManager reqMan;
	
	public RequestManagerClient(){
		reqMan = new RequestManager();
	}
	
	public void initialize(){
		reqMan.initialize();
	}

	public double getMeanOfAllStops(String city) {		
		
		String fileName = Constants.outputStopPointFilePrefix+city+".txt";
		double res = 0.0;
		ArrayList<Integer> stopTimeList = new ArrayList<Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName ))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	Scanner sc = new Scanner(line);		    	
		    	sc.nextDouble();
		    	sc.nextDouble();
		    	int stopTime = sc.nextInt();
		    	stopTimeList.add(stopTime);
		    	sc.close();
		    }
		    System.out.println("Printing all the stoptimes \n"+stopTimeList.toString());
		    res = MathHelper.calculateMean(stopTimeList);
		} catch (IOException e) {			
			e.printStackTrace();
		}	
		return res;	
	}
	
	public double getSDOfAllStops(String city) {		
		
		String fileName = Constants.outputStopPointFilePrefix+city+".txt";
		double res = 0.0;
		ArrayList<Integer> stopTimeList = new ArrayList<Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName ))) {
		    String line;
		    while ((line = br.readLine()) != null) {		       
		    	Scanner sc = new Scanner(line);		    	
		    	sc.nextDouble();
		    	sc.nextDouble();
		    	int stopTime = sc.nextInt();
		    	stopTimeList.add(stopTime);
		    	sc.close();
		    }
		    res = MathHelper.calculateSD(stopTimeList);
		} catch (IOException e) {			
			e.printStackTrace();
		}	
		return res;	
	}
	
	
	public ArrayList<Double> getMeanSDOfNearestNeighbors(String city){		

		String fileName = Constants.outputStopPointFilePrefix+city+".txt";
		double res = 0.0;
		ArrayList<Point> StpPts = new ArrayList<Point>();
		ArrayList<Double> sdList = new ArrayList<Double>();
		int stopCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName ))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	++stopCount;
		    	Scanner sc = new Scanner(line);		    	
		    	double lat = sc.nextDouble();
		    	double lon = sc.nextDouble();
		    	sc.nextInt();		    	
		    	StpPts =(reqMan.getNearestStops(lat, lon));
		    	ArrayList<Integer>StpLengths = new ArrayList<Integer>();  
		    	for (int k=0;k<StpPts.size();k++){
		    		StpLengths.add(StpPts.get(k).getStopTime());		    		
		    	}
		    	double sd =MathHelper.calculateSD(StpLengths);
		    	sdList.add(sd);
		    	//System.out.println("The value of sd is "+sd);
		    	res += sd;
		    	sc.close();
		    }
		    res = res/stopCount;
		    System.out.println("Mean SD of all stops is "+res);
		} catch (IOException e) {			
			e.printStackTrace();
		}	
		return sdList;		
	}
	
	
	public HashMap<Double,Double> getExpectedOnlineCostForAllStops(String city, int breakEven){	

		String fileName = Constants.outputStopPointFilePrefix+city+".txt";		
		HashMap<Double,Double> stopLengthOnlineCostMap = new HashMap<Double,Double>();
		HashMap<Double,Double> stopLengthFreqMap = new HashMap<Double,Double>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName ))) {
		    String line;
		    while ((line = br.readLine()) != null) {		    	
		    	Scanner sc = new Scanner(line);		    	
		    	double lat = sc.nextDouble();
		    	double lon = sc.nextDouble();
		    	int stopTime = sc.nextInt();
		    	IdlingTimeResponse resp =reqMan.getAdvisedIdlingTime(lat,lon,breakEven);
		    	
		    	if (stopLengthOnlineCostMap.get(stopTime)==null){
		    		stopLengthOnlineCostMap.put((double) stopTime, resp.getCost());
		    		stopLengthFreqMap.put((double)stopTime, 1.0);		    		
		    	}
		    	else{
		    		double prevCount = stopLengthFreqMap.get(stopTime);
		    		double currAvg = stopLengthOnlineCostMap.get(stopTime);
		    		//double newAvg = ((prevCount*currAvg)+resp.getCost())/(prevCount+1.0);
		    		stopLengthOnlineCostMap.put((double)stopTime,Math.min(currAvg, resp.getCost()));
		    		stopLengthFreqMap.put((double)stopTime,prevCount+1.0);
		    	}
		    	
		    	sc.close();
		    }		    
		} catch (IOException e) {			
			e.printStackTrace();
		}	
		return stopLengthOnlineCostMap;		
	}
	
	
	public HashMap<Double,Double> getExpectedCompetitiveRatioForAllStops(String city, int breakEven, double[] final_avg){
		String fileName = Constants.outputStopPointFilePrefix+city+".txt";		
		HashMap<Double,Double> stopLengthOnlineCostMap = new HashMap<Double,Double>();
		HashMap<Double,Double> stopLengthFreqMap = new HashMap<Double,Double>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName ))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	
		    	Scanner sc = new Scanner(line);		    	
		    	double lat = sc.nextDouble();
		    	double lon = sc.nextDouble();
		    	int stopTime = sc.nextInt();
		    	IdlingTimeResponse resp =reqMan.getAdvisedIdlingTime(lat,lon,breakEven);
		    	double offLineCost = resp.getuB()+(resp.getqB()*breakEven);
		    	if (stopLengthOnlineCostMap.get(stopTime)==null){
		    		stopLengthOnlineCostMap.put((double) stopTime, resp.getCost()/offLineCost);
		    		stopLengthFreqMap.put((double)stopTime, 1.0);		    		
		    	}
		    	else{
		    		double prevCount = stopLengthFreqMap.get(stopTime);
		    		double currAvg = stopLengthOnlineCostMap.get(stopTime);
		    		double newAvg = ((prevCount*currAvg)+(resp.getCost()/offLineCost))/(prevCount+1.0);
		    		
		    		//Best case:
		    		//stopLengthOnlineCostMap.put((double)stopTime,Math.min(currAvg, resp.getCost()/offLineCost));
		    		
		    		//Average Case
		    		stopLengthOnlineCostMap.put((double)stopTime,newAvg);
		    		
		    		//Worst Case
		    		//stopLengthOnlineCostMap.put((double)stopTime,Math.max(currAvg, resp.getCost()/offLineCost));
		    		
		    		stopLengthFreqMap.put((double)stopTime,prevCount+1.0);
		    	}
		    	
		    	sc.close();
		    }
		    
		    //calculating the net average
		    double total_elements = 0;
		    for(HashMap.Entry<Double,Double>entry:stopLengthOnlineCostMap.entrySet()){
		    	double freq = stopLengthFreqMap.get(entry.getKey());
		    	final_avg[0] += freq*entry.getValue();
		    	total_elements+=freq;
		    }
		    final_avg[0]=final_avg[0]/total_elements;		    
		    
		} catch (IOException e) {			
			e.printStackTrace();
		}	
		return stopLengthOnlineCostMap;		
	}
	
	
	
}
