package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class TowngenObject {
	
	int x,y;
	int width,height;
	
	public TowngenObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public TowngenObject(){
		
	}
	
	public abstract void render(ShapeRenderer sr);
}
