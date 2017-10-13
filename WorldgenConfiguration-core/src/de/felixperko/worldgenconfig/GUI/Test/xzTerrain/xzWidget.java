package de.felixperko.worldgenconfig.GUI.Test.xzTerrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import de.felixperko.worldgen.Chunk;
import de.felixperko.worldgen.Generation.Misc.Parameters;
import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;
import de.felixperko.worldgen.Generation.Misc.SelectionRuleset;
import de.felixperko.worldgen.Generation.Misc.TerrainType;
import de.felixperko.worldgen.Util.Math.Vector2i;
import de.felixperko.worldgenconfig.GUI.Test.TestManager;
import de.felixperko.worldgenconfig.GUI.Test.TestWidget;
import de.felixperko.worldgenconfig.Generation.GenMisc.WorldConfiguration;
import de.felixperko.worldgenconfig.MainMisc.Main;

public class xzWidget extends TestWidget {
	
	Image image;
	Texture texture;
	
	int w = 512;
	int h = 512;
	
	@Override
	public float getPrefWidth() {
		return w;
	}
	
	@Override
	public float getPrefHeight() {
		return h;
	}
	
	public xzWidget() {
		Main.tickThread.setTestManager(new TestManager(this));
	}
	
	@Override
	public void render(Batch batch) {
		if (image != null)
			image.draw(batch, 1f);
	}
	
	Chunk chunk;
	
	boolean generated = false;
	boolean redraw = false;
	
	int y = 64;
	Vector2i chunkPos = new Vector2i(0,0);
	
	public void addDrawY(int add){
		this.y += add;
		if (y < 0)
			y = 128;
		redraw = true;
	}
	
	public void addChunkPos(int x, int y){
		chunkPos = new Vector2i(chunkPos.getX()+x, chunkPos.getY()+y);
		chunk = null;
		generated = false;
		redraw = true;
	}
	
	int[] stepCounter = new int[4];
	
	@Override
	public void tick() {
//		System.out.println("tick...");
		if (generated){
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
//			y--;
//			redraw = true;
			
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			if (redraw){
				try {
					generatePixmap();
					redraw = false;
				} catch (Exception e){
					System.out.println("exception at redraw (xzWidget): "+e.getClass().getSimpleName()+" '"+e.getMessage()+"'");
//					e.printStackTrace();
				}
				System.out.println(y);
			}
			return;
		}
		if (chunk == null){
			WorldConfiguration wc = Main.main.currentWorldConfig;
			Main.main.worldgenAPI.setGenerationParameters(
					new Parameters(new SelectionRuleset() {
						
						@Override
						protected int selectValue() {
							double minDifference = Double.MAX_VALUE;
							TerrainType currentType = null;
							double[] features = new double[parameters.propertySize];
							for (int i = 0; i < features.length; i++) {
								features[i] = getProperty(i);
							}
							for (TerrainType type : parameters.types){
								double d = type.selector.getDifference(features);
								if (d < minDifference){
									minDifference = d;
									currentType = type;
								}
							}
							if (currentType != null)
								return currentType.id;
							return 0;
						}
					},
							wc.properties.toArray(new PropertyDefinition[wc.properties.size()]),
							wc.types.toArray(new TerrainType[wc.types.size()])));
			System.out.println("gave types: "+wc.types.toString());
			System.out.println("generate chunk: "+chunkPos.toString());
			chunk = Main.main.worldgenAPI.generateChunk(chunkPos);
			return;
		}
		if (!chunk.generationFinished){
//			System.out.println("wait for chunk generation...");
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			return;
		}
		System.out.println("generating pixmap...");
		generatePixmap();
		System.out.println("generated pixmap");
		generated = true;
	}


	private void generatePixmap() {
		Pixmap pm = getPixmap();
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				if (texture != null)
					texture.dispose();
				texture = new Texture(pm);
				image = new Image(texture);
				image.setSize(w, h);
			}
		});
		
	}

	private Pixmap getPixmap() {
		Pixmap pm = new Pixmap(16,16,Format.RGBA8888);
		
		pm.setColor(Color.WHITE);
		for (int x = 0 ; x < pm.getWidth() ; x++){
			for (int z = 0 ; z < pm.getHeight() ; z++){
				pm.drawPixel(x, z);
			}
		}

		float alpha = 0;
		for (int y = this.y-5 ; y <= this.y ; y++){
			alpha += 0.05f;
			if (y < 0)
				continue;
			if (y == this.y)
				alpha = 1;
			for (int x = 0 ; x < pm.getWidth() ; x++){
				for (int z = 0 ; z < pm.getHeight() ; z++){
					pm.setColor(getColor(x,y,z,alpha));
					pm.drawPixel(x, z);
				}
			}
		}
		return pm;
	}

	private Color getColor(int x, int y, int z, float alpha) {
		int matID = chunk.getMaterialID(x,y,z);
		Color c = getMaterialColor(matID, alpha);
		int matAbove = chunk.getMaterialID(x,y+1,z);
		if (matAbove != 0 && matAbove != 2)
			c = new Color(c.r/2,c.g/2,c.b/2,c.a);
		return c;
	}

	private Color getMaterialColor(int matID, float alpha) {

		switch (matID){
		case 0:
			return new Color(1,1,1,0);
		case 1:
			return new Color(.5f,.5f,.5f,alpha);
		case 2:
			return new Color(0,0,1,alpha/2);
		case 3:
			return new Color(.55f,.27f,.08f,alpha);
		case 4:
			return new Color(0,0.75f,0,alpha);
		}
		return Color.RED;
	}

}
