package de.felixperko.worldgen.Util.Math;

public class Rect4i {
	int minX, minY, maxX, maxY;

	public Rect4i(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public Rect4i(){
		this.minX = 0;
		this.minY = 0;
		this.maxX = 0;
		this.maxY = 0;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	public boolean touch(Vector2i v){
		return v.x >= minX && v.x <= maxX && v.y >= minY && v.y <= maxY;
	}
	
	public boolean intersect(Vector2i v){
		return v.x > minX && v.x < maxX && v.y > minY && v.y < maxY;
	}
}
