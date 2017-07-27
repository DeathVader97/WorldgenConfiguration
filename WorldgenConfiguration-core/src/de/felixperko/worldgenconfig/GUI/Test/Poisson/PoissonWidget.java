package de.felixperko.worldgenconfig.GUI.Test.Poisson;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

import de.felixperko.worldgenconfig.GUI.Test.TestWidget;

public class PoissonWidget extends TestWidget {
	
	ArrayList<PoissonPoint> points = new ArrayList<>();
	
	public PoissonWidget() {
	}
	
	ShapeRenderer sr = new ShapeRenderer();
	
	@Override
	public void render(Batch batch) {
		batch.end();
		sr.setTransformMatrix(batch.getTransformMatrix());
		sr.begin(ShapeType.Line);
		
		sr.setColor(Color.GRAY);
		sr.rect(1, 1, 500, 500);
		
		for (PoissonPoint p : points){
			sr.setColor(p.getColor());
			sr.circle(p.x, p.y, 1);
//			sr.setColor(Color.GRAY);
//			sr.circle(p.x, p.y, p.nextMinRange);
//			sr.setColor(0,1f,0,0.2f);
//			sr.circle(p.x, p.y, p.nextMaxRange);
		}
		
		sr.end();
		batch.begin();
	}
	
	Random random;
	
	int amount = 1;
	
	public PoissonPoint genPoint(){
		if (points.isEmpty()){
			PoissonPoint p = new PoissonPoint(random.nextInt((int)getWidth()), random.nextInt((int)getHeight()));
			return p;
		}
		outer : for (int i = 0 ; i < 100 ; i++){
			PoissonPoint rp = points.get(random.nextInt(points.size()));
			double abstand = rp.nextMinRange+random.nextDouble()*(rp.nextMaxRange-rp.nextMinRange);
			double winkel = random.nextDouble()*2*Math.PI;
			int dx = (int)(Math.cos(winkel)*abstand);
			int dy = (int)(Math.sin(winkel)*abstand);
			PoissonPoint p = new PoissonPoint(rp.x-dx, rp.y-dy);
			if (p.x < 0 || p.y < 0 || p.x > getWidth() || p.y > getHeight())
				continue;
			for (PoissonPoint p2 : points){
				if (p.tooClose(p2))
					continue outer;
			}
			return p;
		}
		return null;
	}

	@Override
	public void tick() {
		random = new Random(46);
		points.clear();
//		points.add(new Point(250,250));
		
		for (int i = 0 ; i < amount ; i++){
			PoissonPoint p = genPoint();
			if (p != null)
				points.add(p);
		}
		amount += 10;
		if (amount > 3000)
			amount = 1;
	}
}
