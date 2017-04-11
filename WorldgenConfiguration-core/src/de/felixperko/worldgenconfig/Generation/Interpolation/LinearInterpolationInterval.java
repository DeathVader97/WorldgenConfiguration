package de.felixperko.worldgenconfig.Generation.Interpolation;

import java.util.ArrayList;

public class LinearInterpolationInterval extends Interval {
	
	double scalingFactor,shift;
	
	public LinearInterpolationInterval(ArrayList<String> serialized){
		super(Double.parseDouble(serialized.get(0)),Double.parseDouble(serialized.get(1)));
		scalingFactor = Double.parseDouble(serialized.get(2));
		shift = Double.parseDouble(serialized.get(3));
	}
	
	public LinearInterpolationInterval(double min, double max, double m, double n) {
		super(min, max);
		this.scalingFactor = m;
		this.shift = n;
	}
	
	public LinearInterpolationInterval(){}

	@Override
	public double modify(double v) {
		return v*scalingFactor+shift;
	}

	@Override
	public void serializeData(StringBuilder s) {
		s.append(scalingFactor).append(",").append(shift);
	}

	public double getScalingFactor() {
		return scalingFactor;
	}

	public void setScalingFactor(double scalingFactor) {
		this.scalingFactor = scalingFactor;
	}

	public double getShift() {
		return shift;
	}

	public void setShift(double shift) {
		this.shift = shift;
	}
}
