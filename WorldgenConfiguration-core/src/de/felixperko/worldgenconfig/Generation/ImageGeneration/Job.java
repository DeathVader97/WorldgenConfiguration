package de.felixperko.worldgenconfig.Generation.ImageGeneration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Job implements Comparable<Job>{
	WorldgenImage image;
	double priority;
	public double scaling;
	public double shiftX;
	public double shiftY;
	
	int[][][] typeIDs;
	float[][][] intensities;
	int size;
	
	public void setSize(int size){
		this.size = size;
		typeIDs = new int[size][size][];
		intensities = new float[size][size][];
	}
	
	public Job(WorldgenImage image, double priority, double scaling, float x, float y) {
		this.image = image;
		this.priority = priority;
		this.scaling = scaling;
		this.shiftX = x;
		this.shiftY = - y;
	}

	@Override
	public int compareTo(Job o) {
		return Double.compare(priority, o.priority);
	}
	
	public void finish(final Pixmap pixmap){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					((SpriteDrawable)image.getDrawable()).getSprite().getTexture().dispose();
				} catch (NullPointerException e){}
				Texture t = new Texture(pixmap);
				t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				image.setDrawable(new SpriteDrawable(new Sprite(t)));
			}
		});
	}
}
