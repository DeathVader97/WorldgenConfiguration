package de.felixperko.worldgen;

import java.util.HashMap;

import de.felixperko.worldgen.Util.Math.Vector2i;

public class Chunk {
	
	Vector2i pos;
	
	int currentStep = -1;
	
	double[] properties;
	int sampleBiome = -1;
	
	HashMap<Integer,Double> biomes = new HashMap<>();
	
	int[] heights;
	int[] materials;
	
	public boolean structuresPlanned = false;
	public boolean generationFinished;
	
	public Chunk(Vector2i chunkPos) {
		this.pos = chunkPos;
	}
	
	public int getHeightAt(int x, int z){
		return heights[index2D(x,z)];
	}

	public int getMaterialID(int x, int y, int z) {
		return materials[index3D(x,y,z)];
	}
	
	private int index2D(int x, int z){
		return x + (z << 4);
	}
	
	private int index3D(int x, int y, int z){
		return x + (z << 4) + (y << 8);
	}
}
