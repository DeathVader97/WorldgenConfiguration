package de.felixperko.worldgen.Generation.Interpolation;

import java.util.ArrayList;

public class CosineInterpolationInterval extends Interval{
	
	double yMin, yMax;
	
	public CosineInterpolationInterval(ArrayList<String> serialized){
		super(Double.parseDouble(serialized.get(0)),Double.parseDouble(serialized.get(1)));
		yMin = Double.parseDouble(serialized.get(2));
		yMax = Double.parseDouble(serialized.get(3));
	}
	
	public CosineInterpolationInterval(double min, double max, double v1, double v2) {
		super(min, max);
		this.yMin = v1;
		this.yMax = v2;
	}
	
	public CosineInterpolationInterval(){}

	@Override
	public double modify(double v) {
		return Interpolation.cosineInterpolation(v, min, max, yMin, yMax);
	}

	@Override
	public void serializeData(StringBuilder s) {
		s.append(yMin).append(",").append(yMax);
	}

	public double getyMin() {
		return yMin;
	}

	public void setyMin(double yMin) {
		this.yMin = yMin;
	}

	public double getyMax() {
		return yMax;
	}

	public void setyMax(double yMax) {
		this.yMax = yMax;
	}

}
