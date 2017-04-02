package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public abstract class DisplayNode extends Widget{
	
	boolean selected = false;
	
	protected abstract void drawImage();

	public abstract Connector[] getInputs();
	
	public boolean isComplete(){
		for (Connector c : getInputs())
			if (c.connection == null || !c.connection.isComplete())
				return false;
		return true;
	}
	
	public void init(){}
	protected void doubleclicked(){}
	
	public void setSelected(boolean selected){
		this.selected = selected;
		drawImage();
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	long lastClick;
	
	public void clicked() {
		long t = System.nanoTime();
		if (t - lastClick < 200000000)
			doubleclicked();
		lastClick = t;
	}
}
