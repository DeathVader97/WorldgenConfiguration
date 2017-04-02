package de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes;

import java.util.Random;

import de.felixperko.worldgenconfig.Generation.GenerationPath.DoubleNodeSetting;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenerationPath.IntNodeSetting;
import de.felixperko.worldgenconfig.Generation.Noise.OpenSimplexNoise;

public class NoiseGenerator implements Node{
	
	@DoubleNodeSetting (lowest = 0.0)
	public double frequency = 1;
	@DoubleNodeSetting (lowest = 0.0)
	public double persistance = 0.5;
	@DoubleNodeSetting (lowest = 0.0)
	public double lacunarity = 2;
	@IntNodeSetting (lowest = 1)
	public int octaves = 1;
	
	OpenSimplexNoise[] noises;
	
	public NoiseGenerator(){
		noises = new OpenSimplexNoise[]{new OpenSimplexNoise(new Random().nextLong())};
	}
	
	public double simplexNoise2D(double x, double y){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noises[o].eval(x*frequency, y*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		return value/maxAmp;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public double getPersistance() {
		return persistance;
	}

	public void setPersistance(double persistance) {
		this.persistance = persistance;
	}

	public double getLacunarity() {
		return lacunarity;
	}

	public void setLacunarity(double lacunarity) {
		this.lacunarity = lacunarity;
	}

	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves, Random octaveSeed) {
		this.octaves = octaves;
		noises = new OpenSimplexNoise[octaves];
		for (int i = 0; i < noises.length; i++) {
			noises[i] = new OpenSimplexNoise(octaveSeed.nextLong());
		}
	}

	@Override
	public double getGenerationValue(GenerationParameterSupply supply) {
		return simplexNoise2D(supply.getX(), supply.getY());
	}

	@Override
	public int getInputCount() {
		return 0;
	}

	@Override
	public void setInput(int index, Node node) {
		System.err.println("Tried to set Input for the GenerationNode NoiseGenerator which has no inputs");
	}

	@Override
	public String getDisplayName() {
		return "Noise";
	}
}
