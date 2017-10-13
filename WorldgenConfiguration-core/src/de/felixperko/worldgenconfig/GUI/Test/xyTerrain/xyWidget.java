package de.felixperko.worldgenconfig.GUI.Test.xyTerrain;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import de.felixperko.worldgen.Generation.Noise.NoiseHelper;
import de.felixperko.worldgen.Generation.Noise.OpenSimplexNoise;
import de.felixperko.worldgen.Generation.Noise.PointData2D;
import de.felixperko.worldgenconfig.GUI.Test.TestManager;
import de.felixperko.worldgenconfig.GUI.Test.TestWidget;
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
	
	int count = 0;
	int last_count = 0;
	long next_time = System.nanoTime()+1000000000;
	
	@Override
	public void tick() {
		generate();
		long t = System.nanoTime();
		if (t >= next_time){
			System.out.println("fps: "+(count-last_count)/(((double)t-(next_time-1000000000))/1000000000.));
			next_time = t+1000000000;
			last_count = count;
		}
		count++;
//		try {
////			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	private void generate() {
		Pixmap pm = getPixmap();
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
		
		Pixmap pm = new Pixmap(w, 256, Pixmap.Format.RGB888);
		for (int x = 0 ; x < w ; x++){
			for (int y = 0 ; y < h ; y++){
				PointData2D data = getData(x, y);
				double angle = (z*5)%360;
				double angle2 = data.getAngle();
				double d = Math.abs(angle2-angle);
				if (d > 180)
					d = 360-d;
				d /= 360;
				float density = (float) (d);
				float brightness = (float) Math.sqrt(data.ddx*data.ddx+data.ddy*data.ddy);
				
				if (brightness > 1)
					brightness = 1;
//				else if (brightness < 0)
//					brightness = 0;
				java.awt.Color color = java.awt.Color.getHSBColor(data.getAngle()/(360f), 1f, 1);
				pm.setColor(new Color(color.getRed()/256f, color.getGreen()/256f, color.getBlue()/256f, color.getAlpha()/256f));
				density = (float) data.value*2-1f;
				pm.setColor(new Color(density,density,density,1f));
				if (data.value > 0.7){
					if (Math.sqrt(data.ddx*data.ddx+data.ddy*data.ddy) > 1)
						pm.setColor(new Color(density,density,density,1f));
					else
						pm.setColor(new Color(0,density,0,1f));
				} else
					pm.setColor(new Color(0,0,0.5f,1f));
				
				if (((int)(data.value*100))%10 == 0)
					pm.setColor(new Color(0,0,0,1f));
				
				pm.drawPixel(x, y);
			}
		}
		return pm;
	}
	
	private PointData2D getData(double x, double y){
		double frequency = 0.005;
		double warp = 0.75;
		PointData2D data = new PointData2D();
//		double value = NoiseHelper.erodedNoise2D(x, y, frequency, 0.8, 0.5, 2, 0.4, 0.35, 0.5, 0.8, warp, noises.length, noises);
		PointData2D value = NoiseHelper.test_swissNoise2D_deriv(x, y, frequency, 0.5, 2, 0.15/frequency, noises.length, noises);
//		double value = NoiseHelper.iqNoise2D(x, y, frequency, 0.5, 2, noises.length, noises);
		
		double d = 0.001;
		
//		double value10 = NoiseHelper.erodedNoise2D(x+d, y, frequency, 0.8, 0.5, 2, 0.4, 0.35, 0.5, 0.8, warp, noises.length, noises);
		PointData2D value10 = NoiseHelper.test_swissNoise2D_deriv(x+d, y, frequency, 0.5, 2, 0.15/frequency, noises.length, noises);
//		double value10 = NoiseHelper.iqNoise2D(x+d, y, frequency, 0.5, 2, noises.length, noises);
		
//		double value01 = NoiseHelper.erodedNoise2D(x, y+d, frequency, 0.8, 0.5, 2, 0.4, 0.35, 0.5, 0.8, warp, noises.length, noises);
		PointData2D value01 = NoiseHelper.test_swissNoise2D_deriv(x, y+d, frequency, 0.5, 2, 0.15/frequency, noises.length, noises);
//		double value01 = NoiseHelper.iqNoise2D(x, y+d, frequency, 0.5, 2, noises.length, noises);

//		if (z%2 == 0){
			data.ddx = (value10.value-value.value)*100/d;
			data.ddy = (value01.value-value.value)*100/d;
			data.value = value.value;
//		} else {
//			data.ddx = value.ddx;
//			data.ddy = value.ddy;
//			data.value = value.value;
//		}
//		else {
//			data.ddx *= 100;
//			data.ddy *= 100;
//		}
//		data.value = value*0.5 + 0.5;
		
//		System.out.println(data.ddx);
		
		return data;
	}

}
