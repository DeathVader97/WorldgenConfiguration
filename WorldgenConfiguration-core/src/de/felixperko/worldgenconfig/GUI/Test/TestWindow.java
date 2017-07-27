package de.felixperko.worldgenconfig.GUI.Test;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisWindow;

import de.felixperko.worldgenconfig.GUI.Test.Towngen.TowngenWidget;
import de.felixperko.worldgenconfig.GUI.Test.xyTerrain.xyWidget;

public class TestWindow extends VisWindow{

	public TestWindow() {
		
		super("test");
		addCloseButton();
		
		TestWidget tw = new TowngenWidget();
		add(tw).center();
		pack();
		setPosition((Gdx.graphics.getWidth()-getWidth())/2, (Gdx.graphics.getHeight()-getHeight())/2);
	}
}
