package de.felixperko.worldgenconfig.Generation.ImageGeneration;

import java.util.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WorldgenImage extends Image{
	
	Vector2 pos;
	ImageManager manager;
	double remainingResolutionFactor = 0;
	
	public WorldgenImage(ImageManager manager, Vector2 pos) {
		super();
		this.pos = pos;
		setPosition(pos.x, pos.y);
		this.manager = manager;
	}
	
	boolean isAtPos(int x, int y) {
		return pos.x == x && pos.y == y;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(pos.x, pos.y);
	}
	
	@Override
	public boolean isVisible(){
		return manager.visible(this);
	}
}
