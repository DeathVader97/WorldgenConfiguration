package de.felixperko.worldgenconfig.MainMisc.Utilities.Events;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.Generation.GenMisc.TerrainType;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.WorldgenEvent;

public class TypesChangedEvent extends WorldgenEvent {
	ArrayList<TerrainType> oldTypes;
	ArrayList<TerrainType> newTypes;
	ArrayList<TerrainType> addedTypes = new ArrayList<>();
	ArrayList<TerrainType> removedTypes = new ArrayList<>();
	
	public TypesChangedEvent(ArrayList<TerrainType> oldTypes, ArrayList<TerrainType> newTypes) {
		this.oldTypes = oldTypes;
		this.newTypes = newTypes;
		for (TerrainType type : newTypes){
			if (!oldTypes.contains(type))
				addedTypes.add(type);
		}
		for (TerrainType type : oldTypes){
			if (!newTypes.contains(type))
				removedTypes.add(type);
		}
	}

	public ArrayList<TerrainType> getOldTypes() {
		return oldTypes;
	}

	public ArrayList<TerrainType> getNewTypes() {
		return newTypes;
	}

	public ArrayList<TerrainType> getAddedTypes() {
		return addedTypes;
	}

	public ArrayList<TerrainType> getRemovedTypes() {
		return removedTypes;
	}

}
