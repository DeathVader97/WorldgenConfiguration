package de.felixperko.worldgen;

public abstract class TerrainConstructor {
	ChunkGenerator generator;
	
	public TerrainConstructor(ChunkGenerator generator) {
		this.generator = generator;
	}
}
