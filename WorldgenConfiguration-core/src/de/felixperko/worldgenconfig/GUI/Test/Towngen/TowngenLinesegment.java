package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TowngenLinesegment extends TowngenObject {
	
	TowngenPoint p1,p2;
	Color color;
	int thickness = 2;

	public TowngenLinesegment(TowngenPoint p1, TowngenPoint p2, Color color) {
		super(p1.x <= p2.x ? p1.x : p2.x, p1.y <= p2.y ? p1.y : p2.y, Math.abs(p2.x-p1.x), Math.abs(p2.y-p1.y));
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
	}

	@Override
	public void render(ShapeRenderer sr) {
		sr.setColor(color);
		sr.rectLine(p1.x, p1.y, p2.x, p2.y, thickness);
	}

}
