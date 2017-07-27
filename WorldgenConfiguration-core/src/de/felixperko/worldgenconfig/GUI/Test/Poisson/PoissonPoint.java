package de.felixperko.worldgenconfig.GUI.Test.Poisson;

import com.badlogic.gdx.graphics.Color;

public class PoissonPoint {
	
	int nextMinRange = 5;
	int nextMaxRange = 200;
	
	Color color = Color.WHITE;
	int x,y;

	public PoissonPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean tooClose(PoissonPoint p2) {
		return dist(p2) < nextMinRange;
	}
	
	public double dist(PoissonPoint p2){
		int dx = x - p2.x;
		int dy = y - p2.y;
		return Math.sqrt(dx*dx+dy*dy);
	}
	
}
