package de.felixperko.worldgenconfig.Generation.Interpolation;

public class LinearInterpolationInterval extends Interval {
	
	double m,n;
	
	public LinearInterpolationInterval(double min, double max, double m, double n) {
		super(min, max);
		this.m = m;
		this.n = n;
	}

	@Override
	public double getValue(double v) {
		return v*m+n;
	}

}
