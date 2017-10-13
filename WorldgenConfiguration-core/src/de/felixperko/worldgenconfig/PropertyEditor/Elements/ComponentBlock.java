package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;

import de.felixperko.worldgen.Generation.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.BlockData;
import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorStage;

public class ComponentBlock extends Block {
	
	/*
	 * ComponentBlocks are UI objects that represent Components in the Editor.
	 */
	
	static HashMap<Class<? extends Component>, Integer> count = new HashMap<>();
	
	public EditorStage stage;
	
	Texture img;
	int border = 4;
	int circleRadius = 2;
	
	public Connector[] inputs = new Connector[2];
	Connector[] connectors;
	public Connector output;
	
	Component component;
	
	String text = "#1";
	
	VisLabel label = null;
	
	public ComponentBlock(EditorStage stage, float x, float y, Class<? extends Component> componentClass) {
		this.stage = stage;
		try {
			this.component = componentClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		inputs = new Connector[this.component.getInputCount()];
		
		setWidth(150);
		setHeight(60);
		setPosition(x-getWidth()/2, y-getHeight()/2);
		
		calculateConnectors();
		drawImage();
		
		text = "#"+incrementCount();
	}
	
	public ComponentBlock(EditorStage stage, float x, float y, Component component) {
		this.stage = stage;
		this.component = component;
		inputs = new Connector[this.component.getInputCount()];
		
		setWidth(150);
		setHeight(60);
		setPosition(x-getWidth()/2, y-getHeight()/2);
		
		calculateConnectors();
		drawImage();
		
		text = "#"+incrementCount();
	}
	
	private int incrementCount() {
		Integer val = count.get(component.getClass());
		if (val == null)
			val = 1;
		else
			val = val+1;
		count.put(component.getClass(), val);
		return val;
	}

	@Override
	public void init() {
		label = new VisLabel(component.getDisplayName()+"\n"+text);
		label.setPosition(getX()+border, getY()+border);
		label.setWidth(getWidth()-border*2);
		label.setHeight(getHeight()-border*2);
		label.setAlignment(Align.center, Align.center);
		stage.addActor(label);
	}
	
	private void calculateConnectors() {
		float spacing = getHeight()/(inputs.length+1);
		float yf = 0;
		for (int i = 0; i < inputs.length; i++) {
			yf += spacing;
			int y = Math.round(yf);
			inputs[i] = new Connector(this, border, y, false);
		}
		output = new Connector(this, (int)getWidth()-border-1, Math.round(getHeight()/2), true);
		connectors = Arrays.copyOf(inputs, inputs.length+1);
		connectors[connectors.length-1] = output;
	}
	
	@Override
	public void drawImage() {
		
		float a = 0.8f;
		
		int w = (int) getWidth();
		int h = (int) getHeight();
		Pixmap pm = new Pixmap(w, h, Format.RGBA8888);

		pm.setColor(new Color(0.1f, 0.1f, 0.1f, a));
		pm.fillRectangle(border, border, w-border*2, h-border*2);
		
		pm.setColor(new Color(1f, 1f, 1f, 1));
		if (selected)
			pm.setColor(new Color(0,1f,0,1));
		pm.drawRectangle(border, border, w-border*2, h-border*2);
		
		for (Connector c : connectors){
			if (c.connection == null)
				pm.setColor(new Color(1f,0,0,1));
			else
				pm.setColor(new Color(1f,1f,1f,1f));
			pm.fillCircle(Math.round(c.pos.x), Math.round(getHeight()-c.pos.y), circleRadius);
		}

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
		return inputs;
	}
	
	@Override
	public void setPosition(float x, float y) {
		if (label != null)
			label.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public boolean remove() {
		if (label != null)
			label.remove();
		return super.remove();
	}
	
	@Override
	protected void doubleclicked() {
		stage.addActor(new ComponentSettingWindow(this));
	}

	public Component getComponent() {
		BlockData data = new BlockData(component);
		data.setEditorName(text);
		data.setEditorX(getX());
		data.setEditorY(getY());
		return component;
	}

	public void updateText(String text) {
		this.text = text;
		if (label != null)
			label.setText(component.getDisplayName()+"\n"+text);
	}

	public void addComponentToList(ArrayList<Component> components) {
		Connector[] inputs = getInputs();
		for (int i = 0 ; i < inputs.length ; i++){
			Component component2 = null;
//			if (inputs[i].connection != null){
				component2 = ((ComponentBlock)inputs[i].getConnectedBlock()).getComponent();
				if (component2 != null)
					component.setInput(i, component2);
//			}
		}
		components.add(component);
		for (int i = 0; i < inputs.length; i++) {
			((ComponentBlock)inputs[i].connection.output.block).addComponentToList(components);
		}
	}
}
