package de.felixperko.worldgenconfig.GUI.Test.xyTerrain;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import de.felixperko.worldgenconfig.GUI.Test.TestManager;
import de.felixperko.worldgenconfig.GUI.Test.TestWidget;
import de.felixperko.worldgenconfig.Generation.Noise.NoiseHelper;
import de.felixperko.worldgenconfig.Generation.Noise.OpenSimplexNoise;
import de.felixperko.worldgenconfig.MainMisc.Main;

public class xyWidget extends TestWidget {
	
	Image image;
	Texture texture;
	
	int w = 512;
	int h = 512;
	
	
	public xyWidget() {
		Main.tickThread.setTestManager(new TestManager(this));
	}

	@Override
	public void render(Batch batch) {
		if (image != null)
			image.draw(batch, 1f);
	}

	@Override
	public void tick() {
		generate();
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	private void generate() {
		Pixmap pm = getPixmap();
		xyWidget widget = this;
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				if (texture != null)
					texture.dispose();
				texture = new Texture(pm);
				image = new Image(texture);
			}
		});
	}
	
	@Override
	public float getPrefWidth() {
		return w;
	}
	
	@Override
	public float getPrefHeight() {
		return h;
	}
	
	Random r = new Random(42);
	OpenSimplexNoise[] noises = new OpenSimplexNoise[4];
	double[] noiseMap = new double[w];
	int z = 0;
	
	private Pixmap getPixmap() {
		r = new Random(42);
		noises = new OpenSimplexNoise[8];
		z++;
		for (int i = 0 ; i < noises.length ; i++){
			noises[i] = new OpenSimplexNoise(r.nextLong());
		}
		for (int i = 0 ; i < noiseMap.length ; i++){
			noiseMap[i] = NoiseHelper.simplexNoise2D(i, z, 0.02, 0.5, 2, noises.length, noises);
		}
		Pixmap pm = new Pixmap(w, 256, Pixmap.Format.RGB888);
		for (int x = 0 ; x < w ; x++){
			for (int y = 0 ; y < h ; y++){
				double density = getDensity(x,y,z);
				if (density > 0.0)
					pm.setColor(Color.BROWN);
				else
					pm.setColor(Color.WHITE);
				pm.drawPixel(x, y);
			}
		}
		return pm;
	}

	private double getDensity(int x, int y, int z) {
		double d = (-0.25 + y/(double)h + 0.04 * noiseMap[x]);
		if (d < 0)
			return d;
		double ridged = Math.abs(NoiseHelper.simplexNoise3D(x, y, z, 0.02, 0.5, 2, 3, noises));
		double ridged2 = Math.abs(NoiseHelper.simplexNoise3D(x, y-1000, z, 0.02, 0.5, 2, 3, noises));
		double ridgedValue = ((Math.sqrt(ridged*ridged + ridged2*ridged2)-0.05));
////		System.out.println((Math.sqrt(ridged*ridged + ridged2*ridged2)));
		return d * ((Math.sqrt(ridged*ridged + ridged2*ridged2)-0.05));
		
//		double d = (-0.25 + y/(double)h);
//		if (d > 0)
//			d *= Math.sqrt(ridgedValue);
//		if (d < -0.08 || d > 0.08)
//			return d;
//		if (NoiseHelper.simplexNoise3DSelector(x, y, z, 0.025, 0.5, 2, 4, noises, -d/0.08, true, false))
//			return 0;
//		return d;
//		return d + 0.08 * NoiseHelper.simplexNoise3D(x, y, z, 0.025, 0.5, 2, 4, noises);
	}

}
