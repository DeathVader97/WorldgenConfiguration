package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import java.util.ArrayList;
import java.util.Random;

public class TowngenVertex extends TowngenPoint{

	int freeSlots = 4;
	TowngenStreet[] connectedStreets = new TowngenStreet[freeSlots];
	
	ArrayList<String> changes = new ArrayList<>();
	
	boolean[] blockedSlots = new boolean[freeSlots];
	
	public TowngenVertex(int x, int y) {
		super(x, y);
	}
	
	public TowngenAlignment getAvailableAlignment(Random random){
		int r = random.nextInt(freeSlots);
		for (int i = 0 ; i < connectedStreets.length ; i++){
			if (blockedSlots[i])
				continue;
			TowngenStreet street = connectedStreets[i];
			if (street == null){
				if (r == 0)
					return TowngenAlignment.values()[i];
				r--;
			}
		}
		return null;
	}
	
	public void setStreetStart(TowngenStreet street){
		if (street == null)
			throw new IllegalStateException("wanted to add start street with value null to vertex");
		if (connectedStreets[street.alignment.ordinal()] == null){
			freeSlots--;
			changes.add("set street "+street.alignment.ordinal());
		}
		connectedStreets[street.alignment.ordinal()] = street;
	}
	
	public void setStreetEnd(TowngenStreet street){
		if (street == null)
			throw new IllegalStateException("wanted to add end street with value null to vertex");
		if (connectedStreets[street.alignment.opposite().ordinal()] == null){
			if (!blockedSlots[street.alignment.opposite().ordinal()])
				freeSlots--;
			changes.add("set street (end) "+street.alignment.opposite().ordinal());
		}
		connectedStreets[street.alignment.opposite().ordinal()] = street;
	}

	public void removeStartStreet(TowngenStreet street, TowngenStreet other) {
		if (street == null)
			throw new IllegalStateException("wanted to remove start street with value null to vertex");
		if (connectedStreets[street.alignment.ordinal()] != street)
			throw new IllegalStateException("Tried to remove a street from a vertex that wasn't connected to the vertex.");
		freeSlots++;
		changes.add("removed Street "+street.alignment.ordinal());
		connectedStreets[street.alignment.ordinal()] = null;
	}
	
	public void setBlocked(TowngenStreet street){
		int index = street.alignment.ordinal();
		if (connectedStreets[index] == null && blockedSlots[index] == false){
			freeSlots--;
			changes.add("blocked street "+street.alignment.ordinal());
			blockedSlots[index] = true;
		}
	}
	
	public boolean isDirectlyConnected(TowngenStreet street){
		for (int i = 0 ; i < connectedStreets.length ; i++)
			if (connectedStreets[i] == street)
				return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "("+x+"/"+y+")";
	}

	public int getConnectedStreetCount() {
		int c = 0;
		for (TowngenStreet street : connectedStreets)
			if (street != null)
				c++;
		return c;
	}
}
