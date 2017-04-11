package de.felixperko.worldgenconfig.MainMisc;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WorldgenImage extends Image{
	
	static int imgSize = 50;
	static ArrayList<WorldgenImage> images = new ArrayList<>();
	
	public static WorldgenImage getAtPos(Vector2 pos){
		int x = (int) pos.x;
		int y = (int) pos.y;
		for (WorldgenImage img : images){
			if (img.isAtPos(x,y))
				return img;
		}
		WorldgenImage res = new WorldgenImage(pos);
		images.add(res);
		return res;
	}
	
	Vector2 pos;
	
	public WorldgenImage(Vector2 pos) {
		super();
		this.pos = pos;
	}
	
	private boolean isAtPos(int x, int y) {
		return pos.x == x && pos.y == y;
	}
}
