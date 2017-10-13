package de.felixperko.worldgen.Generation.Noise.NoiseFunctions;

import de.felixperko.worldgen.Generation.Noise.PointData2D;

public abstract class NoiseFunction2D {
	
	static long default_seed = 42;
	
	public abstract double getValue(double x, double y);
	
	public abstract PointData2D getValueDeriv(double x, double y);
	
}
