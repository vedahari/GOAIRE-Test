package test;

import java.util.List;

public class SeriesPlot<XType,YType> {
	
	public SeriesPlot(String seriesName, List<XType> xValues, List<YType> yValues) {
		super();
		this.seriesName = seriesName;
		XValues = xValues;
		YValues = yValues;
	}



	public String seriesName;
	public List<XType> XValues;
	public List<YType> YValues;

}
