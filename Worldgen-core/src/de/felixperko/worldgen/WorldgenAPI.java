package de.felixperko.worldgen;

import de.felixperko.worldgen.Generation.Misc.Parameters;
import de.felixperko.worldgen.Util.Math.Vector2i;

public class WorldgenAPI {
	
	ChunkGenerator chunkGenerator;
	ChunkManager chunkManager;
	
	public WorldgenAPI(){
		chunkManager = new ChunkManager();
		chunkGenerator = new DefaultChunkGenerator(chunkManager, null);
	}
	
	public void setGenerationParameters(Parameters parameters){
		chunkGenerator.generationParameters = parameters;
	}
	
	public Chunk generateChunk(Vector2i chunkPos){
		if (chunkGenerator.generationParameters == null)
			throw new NullPointerException("The WorldgenAPI doesn't have parameters.");
		chunkGenerator.generateChunk(chunkManager, chunkPos);
		return getChunk(chunkPos);
	}
	
	public Chunk generateChunk(Vector2i chunkPos, int toStep){
		if (chunkGenerator.generationParameters == null)
			throw new NullPointerException("The WorldgenAPI doesn't have parameters.");
		chunkGenerator.generateChunk(chunkManager, chunkPos, toStep);
		return getChunk(chunkPos);
	}
	
	public Chunk getChunk(Vector2i chunkPos){
		return chunkManager.getChunkData(chunkPos);
	}
}
