package de.felixperko.worldgenconfig.MainMisc;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Job {
	
	String busyMsg;
	double scale;
	
	TreeSet<JobInstance> openImages = (TreeSet<JobInstance>) Collections.synchronizedSet(new TreeSet<JobInstance>());
	
	public Job(String busyMsg, double scale) {
		this.busyMsg = busyMsg;
		this.scale = scale;
	}
	
	public JobInstance getNext(){
		return openImages.pollFirst();
	}
}
