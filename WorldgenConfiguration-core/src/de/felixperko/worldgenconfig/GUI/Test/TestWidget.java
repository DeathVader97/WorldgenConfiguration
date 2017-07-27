package de.felixperko.worldgenconfig.GUI.Test;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public abstract class TestWidget extends Widget {
	
	public TestManager testManager = null;
	
	@Override
	public float getPrefWidth() {
		return 500;
	}
	
	@Override
	public float getPrefHeight() {
		return 500;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		render(batch);
	}
	
	public abstract void render(Batch batch);
	
	public abstract void tick();
}
