package de.felixperko.worldgen.Generation.GenerationResources;

import de.felixperko.worldgen.Chunk;
import de.felixperko.worldgen.Generation.WorldgenMaterial;
import de.felixperko.worldgen.Util.Math.Vector3i;

public class DepthGroundLayer extends GroundLayer{
	
	int minDepth, maxDepth;
	WorldgenMaterial material;

	public DepthGroundLayer(int minDepth, int maxDepth, WorldgenMaterial material) {
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.material = material;
	}

	@Override
	public boolean applicable(Vector3i pos, int currentDepth, Chunk c) {
		return (currentDepth <= maxDepth && currentDepth >= minDepth);
	}

	@Override
	public WorldgenMaterial getMaterial(Vector3i pos, Chunk c) {
		return material;
	}
}
