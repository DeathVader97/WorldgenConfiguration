package de.felixperko.worldgenconfig.GUI.Test.Towngen;

public enum TowngenAlignment {
	
	UP(true), LEFT(false), DOWN(true), RIGHT(false);
	
	private boolean vertical;
	private boolean horizontal;
	
	private TowngenAlignment(boolean vertical) {
		this.vertical = vertical;
		this.horizontal = !vertical;
	}
	
	public boolean isVertical() {
		return vertical;
	}

	public boolean isHorizontal() {
		return horizontal;
	}
	
	public boolean isParallel(TowngenAlignment other){
		return isVertical() == other.isVertical();
	}
	
	public boolean isPerpendicular(TowngenAlignment other){
		return isVertical() != other.isVertical();
	}

	public TowngenAlignment opposite() {
		switch (this){
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		}
		return null;
	}
}
