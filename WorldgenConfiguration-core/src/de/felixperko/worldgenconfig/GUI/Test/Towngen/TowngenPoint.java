package de.felixperko.worldgenconfig.GUI.Test.Towngen;

public class TowngenPoint {
	
	int x,y;

	public TowngenPoint(int x, int y) {
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

	public int distance(TowngenPoint other) {
		int dx = x-other.x;
		int dy = y-other.y;
		return (int) Math.round(Math.sqrt(dx*dx+dy*dy));
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TowngenPoint))
			return false;
		TowngenPoint p = (TowngenPoint)obj;
		return p.x == x && p.y == y;
	}
}
