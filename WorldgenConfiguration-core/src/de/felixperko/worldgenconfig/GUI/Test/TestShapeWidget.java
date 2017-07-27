package de.felixperko.worldgenconfig.GUI.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public abstract class TestShapeWidget extends TestWidget {
	
	ShapeRenderer shapeRenderer;
	ShapeType shapeType;
	
	public TestShapeWidget(ShapeType shapeType) {
		shapeRenderer = new ShapeRenderer();
		this.shapeType = shapeType;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		renderBefore(batch);
		batch.end();
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.begin(shapeType);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderShapes(shapeRenderer);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		batch.begin();
		render(batch);
	}
	
	protected abstract void renderShapes(ShapeRenderer sr);
	
	protected void renderBefore(Batch batch) {}	//To be overridden when needed
	
	@Override
	public void render(Batch batch) {}
	
}
