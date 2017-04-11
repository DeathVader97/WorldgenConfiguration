package de.felixperko.worldgenconfig.MainMisc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class JobInstance implements Comparable<JobInstance>{
	WorldgenImage image;
	double priority;
	
	public JobInstance(WorldgenImage image, double priority) {
		this.image = image;
		this.priority = priority;
	}

	@Override
	public int compareTo(JobInstance o) {
		return Double.compare(priority, o.priority);
	}

	
	public void finish(final Pixmap pixmap){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				image.setDrawable(new SpriteDrawable(new Sprite(new Texture(pixmap))));
			}
		});
	}
}
