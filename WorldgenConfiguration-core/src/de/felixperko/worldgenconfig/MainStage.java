package de.felixperko.worldgenconfig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import de.felixperko.worldgenconfig.Generation.Noise.NoiseHelper;

public class MainStage extends Stage{
	
//	SpriteBatch batch;
	
	public MainStage(Viewport vp, SpriteBatch b) {
		super(vp, b);
	}
	
	VisTable mainTable;
	VisTable leftTable;
	
	Image textureImage;
	
	VisSlider slider = new VisSlider(0, 1, 0.025f, false);
	VisLabel label = new VisLabel(slider.getValue()+"");
	int w = Gdx.graphics.getWidth()-300;
	int h = Gdx.graphics.getHeight();
	
	{
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				label.setText(slider.getValue()+"");
				long t1 = System.nanoTime();
				textureImage.setDrawable(new SpriteDrawable(new Sprite(new Texture(generateTestPixmap(w, h, slider.getValue())))));
				long t2 = System.nanoTime();
				System.out.println(t2-t1);
			}
		});
	}
	
	public void init(){
//		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(this);
		
		mainTable = new VisTable(true);
		leftTable = new VisTable(true);
		VisTable collapsibleTable = new VisTable(true);
		
		addActor(mainTable);
		
		textureImage = new Image(new Texture(generateTestPixmap(w, h, slider.getValue())));
		textureImage.setWidth(w);
		textureImage.setHeight(h);
		
		final CollapsibleWidget widget = new CollapsibleWidget(collapsibleTable);
		widget.setCollapsed(true);
		
		VisCheckBox expand = new VisCheckBox("Properties");

		VisScrollPane scrollPane = new VisScrollPane(null);
		scrollPane.setFillParent(true);
		
		mainTable.align(Align.top);
		mainTable.setFillParent(true);
		mainTable.add(textureImage).expand().fill();
		mainTable.add(scrollPane).fillY().width(300);
		
		leftTable.add(expand).align(Align.left).row();
		leftTable.add(widget).row();
//		leftTable.add(widget);
		collapsibleTable.align(Align.top | Align.left);
		for (int i = 0 ; i < 50 ; i++)
			collapsibleTable.add(new VisTextButton("Button "+i)).align(Align.left).row();
		collapsibleTable.add(slider);
		collapsibleTable.add(label);
		
		
		scrollPane.setWidget(leftTable);
		leftTable.align(Align.top | Align.left);
		leftTable.pack();
		
		expand.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				widget.setCollapsed(!widget.isCollapsed(), false);
			}
		});
		long t1 = System.nanoTime();
	}
	
	Float[][] array = null;
	
	private Pixmap generateTestPixmap(int w, int h, float border) {
		long t1 = System.nanoTime();
		long tt1 = 0;
		long tt2 = 0;
		if (array == null)
			array = new Float[w][h];
		Pixmap pm = new Pixmap(w, h, Format.RGB888);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				tt1 += System.nanoTime();
				float v = getNoiseValue(x, y);
				tt2 += System.nanoTime();
				v = Math.abs(v);
				Color color;
				if (v < border){
					color = new Color(0, 0, 1, 1);
				}else{
//					v = v*0.5f+0.5f;
					color = new Color(v, v, v, 1);
				}
				pm.setColor(color);
				pm.drawPixel(x,y);
			}
		}
		long t2 = System.nanoTime();
		System.out.println(tt2-tt1);
		System.out.println(t2-t1);
		return pm;
	}
	
	public Float getNoiseValue(int x, int y){
		Float res = array[x][y];
		if (res != null)
			return res;
		res = (float) NoiseHelper.simplexNoise2D(x, y, 0.025f, 0.5f, 2, 64);
		array[x][y] = res;
		return res;
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
