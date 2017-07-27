package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

public class TowngenGrid {
	
	float size;
	
	HashMap<Pair<Integer,Integer>, TowngenGridPoint> gridPoints = new HashMap<>();
	
	public TowngenGrid(int maxStreetLength){
		this.size = maxStreetLength;
	}
	
	public Set<TowngenGridPoint> getLoopGridPoints(int minX, int minY, int maxX, int maxY, boolean includeEmpty){
		Set<TowngenGridPoint> set = new HashSet<>();
		Pair<Integer, Integer> lower = getGridPos(minX, minY);
		Pair<Integer, Integer> upper = getGridPos(maxX, maxY);
		for (int x = lower.getFirst() ; x <= upper.getFirst() ; x++){
			for (int y = lower.getSecond() ; y <= upper.getSecond() ; y++){
				TowngenGridPoint gridPoint = getGridPointByGridCoordinates(new Pair<Integer, Integer>(x,y));
				if (includeEmpty || !gridPoint.streets.isEmpty())
					set.add(gridPoint);
			}
		}
		return set;
	}

	public TowngenGridPoint getMainStreetPoint(TowngenStreet street){
		return getGridPoint((street.maxX+street.minX)/2, (street.maxY+street.minY)/2);
	}
	
	private Pair<Integer, Integer> getGridPos(int x, int y){
		return new Pair<>(toGridPos(x), toGridPos(y));
	}
	
	private int toGridPos(int v) {
		return Math.round(v/size);
	}

	private TowngenGridPoint getGridPoint(int x, int y){
		return getGridPointByGridCoordinates(getGridPos(x,y));
	}
	
	private TowngenGridPoint getGridPointByGridCoordinates(Pair<Integer, Integer> gridCoords) {
		TowngenGridPoint point = gridPoints.get(gridCoords);
		if (point == null){
			point = new TowngenGridPoint(this, gridCoords.getFirst(), gridCoords.getSecond());
			gridPoints.put(gridCoords, point);
		}
		return point;
	}

	public Pair<Double, Double> getUnroundedGridPos(double x, double y) {
		return new Pair<Double, Double>(x/size, y/size);
	}
}
