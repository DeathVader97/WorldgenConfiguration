package de.felixperko.worldgen;

import de.felixperko.worldgen.Generation.Misc.Parameters;
import de.felixperko.worldgen.Util.Math.Vector2i;

public abstract class ChunkGenerator {
	
	ChunkGenerationStep[] generationSteps;
	
	GenerationScheduler scheduler;
	
	Parameters generationParameters;
	
	ChunkManager chunkManager;
	
	ThreadLocal<TerrainConstructor> terrainConstructors = new ThreadLocal<>();
	
	public ChunkGenerator(ChunkManager chunkManager, Parameters generationParamaters, ChunkGenerationStep[] steps){
		this.generationSteps = steps;
		this.chunkManager = chunkManager;
		this.generationParameters = generationParamaters;
		this.scheduler = new GenerationScheduler(chunkManager, this, steps.length);
	}

	public GenerationScheduler getScheduler() {
		return scheduler;
	}

	public Parameters getGenerationParameters() {
		return generationParameters;
	}

	public void setGenerationParameters(Parameters generationParameters) {
		this.generationParameters = generationParameters;
	}
	
	/**
	 * schedules necessary work to fully generate a chunk, including prerequisites.
	 * @param manager
	 * @param chunkPos
	 */
	public abstract void generateChunk(ChunkManager manager, Vector2i chunkPos);
	
	/**
	 * schedules necessary work to generate a chunk up to the specified step, including prerequisites.
	 * @param manager
	 * @param chunkPos
	 */
	public abstract void generateChunk(ChunkManager chunkManager2, Vector2i chunkPos, int toStep);
	
	protected TerrainConstructor getTerrainConstructor(){
		TerrainConstructor constructor = terrainConstructors.get();
		if (constructor == null){
			constructor = createConstructor();
			terrainConstructors.set(constructor);
		}
		return constructor;
	}

	protected abstract TerrainConstructor createConstructor();

}
