package de.felixperko.worldgenconfig.GUI.Test.Towngen;

public class TowngenLine {
	
	int coordinate;
	boolean horizontal;
	
	public TowngenLine(int coordinate, boolean xAxis) {
		this.coordinate = coordinate;
		this.horizontal = xAxis;
	}

	public int distance(TowngenStreet street) {
		if (street.alignment.isHorizontal() == horizontal){
			if (horizontal){
				return Math.abs(coordinate - street.minY);
			}
			return Math.abs(coordinate - street.minX);
		} else {
			if (horizontal){
				if (street.maxY <= coordinate)
					return coordinate - street.maxY;
				if (street.minY >= coordinate)
					return street.minY - coordinate;
				return 0;
//				if (collide(coordinate, street.minY, street.maxY))
//					return 0;
//				if (street.minY > coordinate)
//					return street.minY - coordinate;
//				else
//					return coordinate - street.maxY;
			} else {
				if (street.maxX <= coordinate)
					return coordinate - street.maxX;
				if (street.minX >= coordinate)
					return street.minX - coordinate;
				return 0;
//				if (collide(coordinate, street.minX, street.maxX))
//					return 0;
//				if (street.minX > coordinate)
//					return street.minX - coordinate;
//				else
//					return coordinate - street.maxX;
			}
		}
	}
	
	private boolean collide(int value, int lower, int higher){
		return value >= lower && value <= higher;
	}
	
}
