package de.felixperko.worldgen.Generation.Interpolation;

public abstract class Interval {
	double min;
	double max;
	
	public Interval(){}
	
	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
		System.out.println("set min to "+min);
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
		System.out.println("set max to "+max);
	}

	public Interval(double min, double max){
		this.min = min;
		this.max = max;
	}
	
	public int inInterval(double v){
		if (v < min)
			return -1;
		if (v > max)
			return 1;
		return 0;
	}
	
	public abstract double modify(double v);
	
	public abstract void serializeData(StringBuilder s);
	
	public void serialize(StringBuilder s) {
		s.append(getClass().getSimpleName()).append(",").append(min).append(",").append(max).append(",");
		serializeData(s);
	}
	
}
