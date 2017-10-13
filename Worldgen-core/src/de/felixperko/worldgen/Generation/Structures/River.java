package de.felixperko.worldgen.Generation.Structures;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import de.felixperko.worldgen.Util.Math.Rect4i;
import de.felixperko.worldgen.Util.Math.Vector2i;

public class River {
	Map<Vector2i, RiverChunkData> chunks = Collections.synchronizedMap(new HashMap<>());
	Rect4i chunkBound = null;
	
	RiverChunkData previousChunk = null;
	double widthStepFactor = 0.2;
	double width = 5;
	
	public void addChunk(Vector2i chunkPos){
		RiverChunkData chunkData = new RiverChunkData(chunkPos, previousChunk.chunkPos, width);
		width += widthStepFactor;
		chunks.put(chunkPos, chunkData);
		previousChunk = chunkData;
		int x = chunkPos.getX();
		int y = chunkPos.getY();
		if (chunkBound == null)
			chunkBound = new Rect4i(x, y, x, y);
		else {
			if (x < chunkBound.getMinX())
				chunkBound.setMinX(x);
			else if (x > chunkBound.getMaxX())
				chunkBound.setMaxX(x);
			if (y < chunkBound.getMinY())
				chunkBound.setMinY(y);
			else if (y > chunkBound.getMaxY())
				chunkBound.setMaxY(y);
		}
	}
	
	public RiverChunkData getData(Vector2i chunkPos){
		return chunks.get(chunkPos);
	}
}
