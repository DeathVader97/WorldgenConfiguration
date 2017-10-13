package de.felixperko.worldgen.Generation.GenerationResources;

import de.felixperko.worldgen.Chunk;
import de.felixperko.worldgen.Generation.WorldgenMaterial;
import de.felixperko.worldgen.Util.Math.Vector3i;

public abstract class GroundLayer {
	
	public boolean isConnected(){
		return true; //true -> once it isn't applicable for a column (y down from height to 0) it isn't checked again; false -> always gets checked
	}
	
	public abstract boolean applicable(Vector3i pos, int currentDepth, Chunk c);
	public abstract WorldgenMaterial getMaterial(Vector3i pos, Chunk c);
}
