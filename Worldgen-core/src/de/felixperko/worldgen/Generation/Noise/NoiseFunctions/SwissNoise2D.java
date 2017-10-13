package de.felixperko.worldgen.Generation.Noise.NoiseFunctions;

import java.util.Random;

import de.felixperko.worldgen.Generation.Noise.NoiseHelper;
import de.felixperko.worldgen.Generation.Noise.OpenSimplexNoise;
import de.felixperko.worldgen.Generation.Noise.PointData2D;

public class SwissNoise2D extends NoiseFunction2D{

	double frequency = 0.1;
	double persistance = 0.5;
	double lacunarity = 2;
	double warp = 0.1;
	int octaves = 1;
	OpenSimplexNoise[] noise = null;
	
	Random random = null;

	long seed = default_seed;
	
	public SwissNoise2D(){
		
	}
	
	public double getFrequency() {
		return frequency;
	}

	public SwissNoise2D setFrequency(double frequency) {
		this.frequency = frequency;
		return this;
	}

	public double getPersistance() {
		return persistance;
	}

	public SwissNoise2D setPersistance(double persistance) {
		this.persistance = persistance;
		return this;
	}

	public double getLacunarity() {
		return lacunarity;
	}

	public SwissNoise2D setLacunarity(double lacunarity) {
		this.lacunarity = lacunarity;
		return this;
	}

	public int getOctaves() {
		return octaves;
	}

	public SwissNoise2D setOctaves(int octaves) {
		this.octaves = octaves;
		return this;
	}

	public double getWarp() {
		return warp;
	}

	public SwissNoise2D setWarp(double warp) {
		this.warp = warp;
		return this;
	}

	public Random getSeed() {
		return random;
	}

	public SwissNoise2D setSeed(long seed) {
		this.seed = seed;
		this.random = new Random(seed);
		this.noise = null;
		return this;
	}

	@Override
	public double getValue(double x, double y) {
		if (noise == null)
			createNoiseObjects();
		return NoiseHelper.swissNoise2D(x, y, frequency, persistance, lacunarity, warp, octaves, noise);
	}

	@Override
	public PointData2D getValueDeriv(double x, double y) {
		if (noise == null)
			createNoiseObjects();
		return NoiseHelper.swissNoise2D_deriv(x, y, frequency, persistance, lacunarity, warp, octaves, noise);
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
