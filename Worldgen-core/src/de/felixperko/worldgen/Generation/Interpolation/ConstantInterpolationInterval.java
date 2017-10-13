package de.felixperko.worldgen.Generation.Interpolation;

import java.util.ArrayList;

public class ConstantInterpolationInterval extends Interval{
	
	double value;
	
	public ConstantInterpolationInterval(ArrayList<String> serialized){
		super(Double.parseDouble(serialized.get(0)),Double.parseDouble(serialized.get(1)));
		value = Double.parseDouble(serialized.get(2));
	}
	
	public ConstantInterpolationInterval(double min, double max, double v) {
		super(min, max);
		this.value = v;
	}
	
	public ConstantInterpolationInterval() {}
	
	public double getValue() {
		return value;
	}

	public void setValue(double v) {
		this.value = v;
	}

	@Override
	public double modify(double v) {
		return this.value;
	}

	@Override
	public void serializeData(StringBuilder s) {
		s.append(value);
	}

}
