package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.felixperko.worldgenconfig.PropertyEditor.EditorStage;

import com.badlogic.gdx.graphics.Texture;

public class EndPoint extends DisplayNode {
	
	EditorStage stage;
	Connector connector;
	int circleRadius = 2;
	int border = 4;
	
	Texture img;
	
	public EndPoint(EditorStage stage, int x, int y) {
		this.stage = stage;
		
		setWidth(30);
		setHeight(30);
		setPosition(x-getWidth()/2, y-getHeight()/2);
		
		connector = new Connector(this, border, (int) (getHeight()/2), false);
		
		drawImage();
	}

	@Override
	public void drawImage() {
		int x = (int)getWidth();
		int y = (int)getHeight();
		Pixmap pm = new Pixmap(x, y, Format.RGBA8888);
		
		pm.setColor(Color.BLACK);
		pm.fillCircle(x/2, y/2, (x/2)-border);
		
		pm.setColor(Color.WHITE);
		if (selected)
			pm.setColor(Color.GREEN);
		pm.drawCircle(x/2, y/2, (x/2)-border);
		
		if (connector.connection == null)
			pm.setColor(Color.RED);
		pm.fillCircle((int)connector.pos.x, (int)connector.pos.y, circleRadius);
		
		if (img != null)
			img.dispose();
		img = new Texture(pm);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(new Color(1,1,1,1f));
		batch.draw(img, getX(), getY());
	}
	
	@Override
	public Connector[] getInputs() {
		return new Connector[]{connector};
	}
}
