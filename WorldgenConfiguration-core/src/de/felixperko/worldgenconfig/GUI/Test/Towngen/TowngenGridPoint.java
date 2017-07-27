package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import java.util.ArrayList;
import java.util.List;

import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

public class TowngenGridPoint {
	int x,y;
	TowngenGrid grid;
	List<TowngenStreet> streets = new ArrayList<>();
	
	public TowngenGridPoint(TowngenGrid grid, int x, int y){
		this.x = x;
		this.y = y;
		this.grid = grid;
	}
	
	public List<TowngenStreet> getStreets(){
		return streets;
	}
	
	public void add(TowngenStreet street){
		if (!streets.contains(street))
			streets.add(street);
	}
	
	public void remove(TowngenStreet street){
		streets.remove(street);
	}
	
	@Override
	public String toString() {
		return x+","+y;
	}
	
	@Override
	public int hashCode() {
		return x*31+y;
	}
	
	public TowngenBoundingBox getRange(){
		int minX = Math.round(grid.size*(x-0.5f));
		int minY = Math.round(grid.size*(y-0.5f));
		int maxX = Math.round(grid.size*(x+0.5f));
		int maxY = Math.round(grid.size*(y+0.5f));
		return new TowngenBoundingBox(minX, maxX, minY, maxY);
	}

	public double distanceSquared(Pair<Double, Double> gridPos) {
		double dx = x-gridPos.getFirst();
		double dy = y-gridPos.getSecond();
		return dx*dx+dy*dy;
	}
}
