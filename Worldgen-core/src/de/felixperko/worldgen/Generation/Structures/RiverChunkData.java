package de.felixperko.worldgen.Generation.Structures;

import de.felixperko.worldgen.Util.Math.Vector2i;

public class RiverChunkData {
	Vector2i chunkPos;
	
	Vector2i previousChunkPos;
	Vector2i nextChunkPos = null;
	
	double width;
	
	public RiverChunkData(Vector2i chunkPos, Vector2i previousChunkPos, double width){
		this.chunkPos = chunkPos;
		this.previousChunkPos = previousChunkPos;
		this.width = width;
	}
	
	public void setNextChunkPos(Vector2i nextChunk){
		this.nextChunkPos = nextChunk;
	}
}
