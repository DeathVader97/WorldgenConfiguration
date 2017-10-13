package de.felixperko.worldgen;

import java.util.HashMap;

import de.felixperko.worldgen.Util.Math.Vector2i;

public class ChunkManager {
	
	HashMap<Vector2i, Chunk> cachedChunks = new HashMap<>();
	
	public Chunk getChunkData(Vector2i chunkPos) {
		Chunk chunk = cachedChunks.get(chunkPos);
		if (chunk != null){
			return chunk;
		}
		chunk = new Chunk(chunkPos);
		cachedChunks.put(chunkPos, chunk);
		return chunk;
	}

}
