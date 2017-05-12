package test;

import java.util.ArrayList;

public class MathHelper {	
	public static double calculateMean(ArrayList<Integer>input){
		double mean=0.0;
		for (int i=0;i<input.size();i++){
			mean += input.get(i).doubleValue();			
		}
		mean = mean/input.size();
		return mean;		
	}
	
	public static double calculateSD(ArrayList<Integer> input){
		double sd=0.0;
		double mean=calculateMean(input);
		
		double deviationSum = 0.0;
		for(int i=0;i<input.size();i++){
			deviationSum += Math.pow(input.get(i).doubleValue()-mean,2);
		}
		sd = Math.sqrt(deviationSum/input.size());		
		return sd;	
	}
	
	public static void testSD(){
		int[] arr= {9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4};
		ArrayList <Integer> a = new ArrayList<Integer>();
		for(int i=0;i<arr.length;i++){
			a.add(arr[i]);
		}
		System.out.println("Calculated SD is "+ calculateSD(a));
	}
	

}
