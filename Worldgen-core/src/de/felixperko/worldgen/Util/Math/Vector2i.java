package de.felixperko.worldgen.Util.Math;

public class Vector2i {
	
	int x,y;

	public Vector2i(int x, int y) {
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
	
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(Vector2i vector){
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}
	
	public Vector2i sub(Vector2i vector){
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}
	
	public Vector2i mult(Vector2i vector){
		this.x *= vector.x;
		this.y *= vector.y;
		return this;
	}
	
	public Vector2i div(Vector2i vector){
		this.x /= vector.x;
		this.y /= vector.y;
		return this;
	}
	
	public Vector2i differenceTo(Vector2i other){
		return other.copy().sub(this);
	}
	
	public int lenSq(){
		return x*x+y*y;
	}
	
	public double len(){
		return Math.sqrt(x*x + y*y);
	}
	
	public double angle(){
		return Math.atan2(y, x);
	}
	
	public Vector2i copy(){
		return new Vector2i(x, y);
	}
	
	private int elegant(int x, int y) {
	    return x < y ? y * y + x : x * x + x + y;
	}
	
	@Override
	public int hashCode() {
	    if (x < 0) {
	        if (y < 0)
	            return 3 + 4 * elegant(-x - 1, -y - 1);
	        return 2 + 4 * elegant(-x - 1, y);
	    }
	    if (y < 0)
	        return 1 + 4 * elegant(x, -y - 1);
	    return 4 * elegant(x, y);
	}

	public double distanceSquared(Vector2i other) {
		return Math.abs(x-other.x) + Math.abs(y-other.y);
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Vector2i) && ((Vector2i)o).x == x && ((Vector2i)o).y == y;
	}
}
