package de.felixperko.worldgen;

import de.felixperko.worldgen.Generation.Misc.GenerationParameterSupply;
import de.felixperko.worldgen.Generation.Misc.Parameters;
import de.felixperko.worldgen.Util.Math.Rect4i;
import de.felixperko.worldgen.Util.Math.Vector2i;

public class DefaultChunkGenerator extends ChunkGenerator{
	
	public DefaultChunkGenerator(ChunkManager chunkManager, Parameters generationParameters) {
		super (chunkManager, generationParameters, new ChunkGenerationStep[4]);

		generationSteps[0] = new ChunkGenerationStep(50) {
			@Override
			public void generate(Chunk chunkData) {
				generateProperties(chunkData);
				chooseSampleBiome(chunkData);
			}
		};
		generationSteps[1] = new ChunkGenerationStep(12) {
			@Override
			public void generate(Chunk chunkData) {
				chooseBiomes(chunkData);
			}
		};
		generationSteps[2] = new ChunkGenerationStep(10) {
			@Override
			public void generate(Chunk chunkData) {
				generateTerrain(chunkData);
			}
		};
		generationSteps[3] = new ChunkGenerationStep(0) {
			@Override
			public void prepare(Chunk chunkData) {
				int x = chunkData.pos.getX();
				int y = chunkData.pos.getY();
				planStructures(new Rect4i(x-radius/2, y-radius/2, x+radius/2, y+radius/2));
			}
			
			@Override
			public void generate(Chunk chunkData) {
				placeStructures(chunkData);
				populateTerrain(chunkData);
			}
		};
	}
	
	@Override
	public void generateChunk(ChunkManager manager, Vector2i chunkPos){
		int chunkX = chunkPos.getX();
		int chunkY = chunkPos.getY();
		int maxR = generationSteps[0].radius;
		for (int x = chunkX-maxR ; x <= chunkX+maxR ; x++){
			for (int y = chunkY-maxR ; y <= chunkY+maxR ; y++){
				Vector2i pos = new Vector2i(x,y);
				Chunk c = chunkManager.getChunkData(pos);
				if (c.currentStep == -1)
					scheduler.addTask(pos, 0);
				for (int i = 1 ; i < generationSteps.length ; i++){
					int radius = generationSteps[i].radius;
					if (x >= chunkX-radius && x <= chunkX+radius && y >= chunkY-radius && y <= chunkY+radius){
						if (c.currentStep < i)
							scheduler.addTask(pos, i);
					} else
						break;
				}
			}
		}
	}

	@Override
	public void generateChunk(ChunkManager chunkManager2, Vector2i chunkPos, int toStep) {
		if (toStep > generationSteps.length-1)
			toStep = generationSteps.length-1;
		int chunkX = chunkPos.getX();
		int chunkY = chunkPos.getY();
		int maxR = generationSteps[0].radius;
		for (int x = chunkX-maxR ; x <= chunkX+maxR ; x++){
			for (int y = chunkY-maxR ; y <= chunkY+maxR ; y++){
				Vector2i pos = new Vector2i(x,y);
				Chunk c = chunkManager.getChunkData(pos);
				if (c.currentStep == -1)
					scheduler.addTask(pos, 0);
				for (int i = 1 ; i < toStep ; i++){
					int radius = generationSteps[i].radius;
					if (x >= chunkX-radius && x <= chunkX+radius && y >= chunkY-radius && y <= chunkY+radius){
						if (c.currentStep < i)
							scheduler.addTask(pos, i);
					} else
						break;
				}
			}
		}
	}
	
	GenerationParameterSupply supply = new GenerationParameterSupply();

	protected void generateProperties(Chunk chunkData) {
		Vector2i chunkPos = chunkData.pos;
		chunkData.properties = new double[generationParameters.propertySize];
		supply.setPos(chunkPos.getX(), chunkPos.getY(), chunkData.properties);
		for (int i = 0 ; i < generationParameters.propertySize ; i++){
			chunkData.properties[i] = generationParameters.getProperty(i, supply);
		}
	}

	protected void chooseSampleBiome(Chunk chunkData) {
		Vector2i chunkPos = chunkData.pos;
		chunkData.sampleBiome = generationParameters.selectionRuleset.getValue(chunkPos.getX(), chunkPos.getY());
	}
	
	protected void planStructures(Rect4i area) {
		int radius = area.getMaxX()-area.getMinX();
		for (int x = area.getMinX() ; x <= area.getMaxX() ; x++){
			for (int y = area.getMinY() ; y <= area.getMaxY() ; y++){
				Chunk c = chunkManager.getChunkData(new Vector2i(x,y));
				if (!c.structuresPlanned){
					planStructures(c, radius);
				}
			}
		}
	}
	
	private void planStructures(Chunk chunk, int maxRadius) {
		
	}

	int smoothRadius = 3;
	
	protected void chooseBiomes(Chunk chunkData) {
		Vector2i pos = chunkData.pos;
		int px = pos.getX();
		int py = pos.getY();
		double[] values = new double[generationParameters.types.length];
		double totalValue = 0;
		//loop through neighboring chunks
		for (int x = px-smoothRadius ; x <= px+smoothRadius ; x++){
			for (int y = py-smoothRadius ; y <= py+smoothRadius ; y++){
				Vector2i pos2 = new Vector2i(x,y);
				Chunk c = chunkManager.getChunkData(pos2);
				if (c.currentStep == -1){
					throw new IllegalStateException("Chunk at "+c.pos.toString()+" is not generated");
				}
				double distance = Math.sqrt(pos.distanceSquared(pos2));
				double factor = 1;
				if (distance != 0){
					factor = 1/distance;
				}
				values[c.sampleBiome] += factor;
				totalValue += factor;
			}
		}
		for (int i = 0 ; i < values.length ; i++){
			double v = values[i];
			if (v == 0)
				continue;
			chunkData.biomes.put(i, v/totalValue);
		}
	}

	protected void generateTerrain(Chunk chunkData) {
		((DefaultTerrainConstructor)getTerrainConstructor()).construct(chunkData);
	}

	protected void placeStructures(Chunk chunkData) {
		
	}

	protected void populateTerrain(Chunk chunkData) {
		chunkData.generationFinished = true;
	}

	@Override
	protected TerrainConstructor createConstructor() {
		return new DefaultTerrainConstructor(this);
	}
	
}
