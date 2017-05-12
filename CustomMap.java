package test;

import java.util.HashMap;

public class CustomMap extends HashMap<Double,Double> {	
	public CustomMap(CustomMap value) {
		super(value);		
	}

	private static final long serialVersionUID = 1L;

}
