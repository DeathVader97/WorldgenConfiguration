package de.felixperko.worldgen.Generation.Noise.NoiseFunctions;

import java.util.Random;

import de.felixperko.worldgen.Generation.Noise.NoiseHelper;
import de.felixperko.worldgen.Generation.Noise.OpenSimplexNoise;
import de.felixperko.worldgen.Generation.Noise.PointData2D;

public class SimplexNoise2D extends NoiseFunction2D {
	
	double frequency = 0.1;
	double persistance = 0.5;
	double lacunarity = 2;
	int octaves = 1;
	OpenSimplexNoise[] noise = null;
	
	Random random = null;

	long seed = default_seed;
	
	public SimplexNoise2D(){
		
	}
	
	public double getFrequency() {
		return frequency;
	}

	public SimplexNoise2D setFrequency(double frequency) {
		this.frequency = frequency;
		return this;
	}

	public double getPersistance() {
		return persistance;
	}

	public SimplexNoise2D setPersistance(double persistance) {
		this.persistance = persistance;
		return this;
	}

	public double getLacunarity() {
		return lacunarity;
	}

	public SimplexNoise2D setLacunarity(double lacunarity) {
		this.lacunarity = lacunarity;
		return this;
	}

	public int getOctaves() {
		return octaves;
	}

	public SimplexNoise2D setOctaves(int octaves) {
		this.octaves = octaves;
		return this;
	}

	public Random getSeed() {
		return random;
	}

	public SimplexNoise2D setSeed(long seed) {
		this.seed = seed;
		this.random = new Random(seed);
		this.noise = null;
		return this;
	}

	@Override
	public double getValue(double x, double y) {
		if (noise == null)
			createNoiseObjects();
		return NoiseHelper.simplexNoise2D(x, y, frequency, persistance, lacunarity, octaves, noise);
	}

	@Override
	public PointData2D getValueDeriv(double x, double y) {
		if (noise == null)
			createNoiseObjects();
		return NoiseHelper.simplexNoise2D_deriv(x, y, frequency, persistance, lacunarity, octaves, noise);
	}
	
	public void createNoiseObjects(){
		if (random == null)
			random = new Random(seed);
		noise = new OpenSimplexNoise[octaves];
		for (int i = 0 ; i < octaves ; i++){
			noise[i] = new OpenSimplexNoise(random.nextLong());
		}
	}
}
