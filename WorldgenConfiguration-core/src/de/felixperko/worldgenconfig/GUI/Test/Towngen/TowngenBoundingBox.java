package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TowngenBoundingBox extends TowngenObject{
	
	int minX;
	int maxX;
	int minY;
	int maxY;
	
	Color color;
	
	public TowngenBoundingBox(int minX, int maxX, int minY, int maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public boolean overlap(TowngenStreet street) {
//		return (overlap(street.minX, minX, maxX) || overlap(street.maxX, minX, maxX) ||
//				overlap(minX, street.minX, street.maxX) || overlap(maxX, street.minX, street.maxX)) &&
//				(overlap(street.minY, minY, maxY) || overlap(street.maxY, minY, maxY) ||
//				overlap(minY, street.minY, street.maxY) || overlap(maxY, street.minY, street.maxY));
		return !(street.maxX <= minX || street.minX >= maxX) &&
				!(street.maxY <= minY || street.minY >= maxY);
	}
	
	private boolean overlap(int value, int lower, int higher){
		return value > lower && value < higher;
	}
	
	public void setColor(Color color){
		this.color = color;
	}

	@Override
	public void render(ShapeRenderer sr) {
		sr.setColor(color);
		sr.rect(minX, minY, maxX-minX, maxY-minY);
	}
}
