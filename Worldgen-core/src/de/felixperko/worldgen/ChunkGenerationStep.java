package de.felixperko.worldgen;

public abstract class ChunkGenerationStep {
	
	int radius;
	
	public ChunkGenerationStep(int radius) {
		this.radius = radius;
	}
	
	/**
	 * is handled after all previous steps are finished to do stuff like planning
	 * @param chunkData
	 */
	public void prepare(Chunk chunkData){}
	
	/**
	 * the work that is done at this step. executed on a random helper thread.
	 * @param chunkData
	 */
	public abstract void generate(Chunk chunkData);
}