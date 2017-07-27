package de.felixperko.worldgenconfig.Generation.GenPath.Components;

import java.util.Random;

import de.felixperko.worldgenconfig.Generation.GenPath.Misc.DoubleComponentSetting;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.IntComponentSetting;
import de.felixperko.worldgenconfig.Generation.Noise.OpenSimplexNoise;

public class NoiseGeneratorComponent extends Component{
	
	@DoubleComponentSetting (lowest = 0.0)
	public double frequency = 0.05;
	
	@DoubleComponentSetting (lowest = 0.0)
	public double persistance = 0.5;
	
	@DoubleComponentSetting (lowest = 0.0)
	public double lacunarity = 2;
	
	@IntComponentSetting (lowest = 1)
	public int octaves = 1;
	
	@IntComponentSetting
	public int seed = 42;
	
	int prevGenerationSeed = 0;
	
	ThreadLocal<OpenSimplexNoise[]> noises = new ThreadLocal<>();
	
	public NoiseGeneratorComponent(){
		generateID();
	}
	
	public double simplexNoise2D(double x, double y){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double frequency = this.frequency;
		for (int o = 0 ; o < octaves ; o++){
			value += noises.get()[o].eval(x*frequency, y*frequency)*amplitude;
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
	
	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}
	
	public int getSeed(){
		return seed;
	}
	
	public void setSeed(int seed){
		this.seed = seed;
	}

	public void resetOctaves() {
		noises.set(new OpenSimplexNoise[octaves]);
		Random random = new Random(seed);
		prevGenerationSeed = seed;
		for (int i = 0; i < noises.get().length; i++) {
			long noiseseed = random.nextLong();
			noises.get()[i] = new OpenSimplexNoise(noiseseed);
		}
	}

	@Override
	public double getGenerationValue(GenerationParameterSupply supply) {
		if (noises.get() == null || noises.get().length != octaves || prevGenerationSeed != seed)
			resetOctaves();
		return simplexNoise2D(supply.getX(), supply.getY());
	}

	@Override
	public int getInputCount() {
		return 0;
	}
	
	@Override
	public Integer getInputID(int index) {
		System.err.println("Tried to get Input of a NoiseGeneratorComponent which has no inputs");
		return null;
	}

	@Override
	public void setInput(int index, Component component) {
		System.err.println("Tried to set Input for a NoiseGeneratorComponent which has no inputs");
	}

	@Override
	public String getDisplayName() {
		return "Noise";
	}
}
